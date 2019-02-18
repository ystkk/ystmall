package com.ystmall.controller.common;

import com.ystmall.common.Const;
import com.ystmall.pojo.User;
import com.ystmall.util.CookieUtil;
import com.ystmall.util.JsonUtil;
import com.ystmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * redis重置session时间过滤器
 * @Shengtong Yuan
 */
public class SessionExpireFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 对以.do结尾的过滤
     * @param servletRequest
     * @param servletResponse
     * @param filterchain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterchain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        //判断logintoken是否为空或者""
        if(StringUtils.isNotEmpty(loginToken)){
            //如果不为空，符合条件，继续拿user信息
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr, User.class);
            if(user != null){
                //如果user不为空，则重置session的时间，即调用expire命令
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }

        filterchain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
