package com.ystmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.ystmall.common.Const;
import com.ystmall.common.ServerResponse;
import com.ystmall.pojo.User;
import com.ystmall.util.CookieUtil;
import com.ystmall.util.JsonUtil;
import com.ystmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * 统一权限拦截器
 * @author Shengtong Yuan
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {

    /**
     * controller执行之前
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

        //请求中Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod)handler;

        //解析HandlerMethod
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数，具体key和value
        StringBuffer requestParambuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String)entry.getKey();

            String mapValue = StringUtils.EMPTY;

            //httpServletRequest这个参数的map中的value返回的是一个String[]
            Object obj = entry.getValue();
            if(obj instanceof String[]){
                String[] strs = (String[])obj;
                mapValue = Arrays.toString(strs);
            }

            requestParambuffer.append(mapKey).append("=").append(mapValue);
        }

        //解决无法登录问题solution2
        if(StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName, "login")){
            log.info("权限拦截器拦截到请求，className:{}, methodName:{}", className, methodName);
            //如果拦截到登录请求，不打印参数，因为防止日志泄漏密码等
            return true;
        }

        log.info("权限拦截器拦截到请求，className:{}, methodName:{}, param:{}", className, methodName, requestParambuffer.toString());

        User user = null;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr, User.class);
        }

        if(user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            //返回false，不会调用controller中的方法

            //重写的话一定要重置response
            httpServletResponse.reset();
            //设置编码
            httpServletResponse.setCharacterEncoding("UTF-8");
            //设置返回值类型
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter out = httpServletResponse.getWriter();

            if(user == null){
                //特殊处理富文本上传，因为返回对象不同
                if(StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                }else{
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            } else{
                //特殊处理富文本上传，因为返回对象不同
                if(StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                }else{
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
                }
            }
            out.flush();
            out.close();

            //不需要进入controller
            return false;
        }
        //进入controller
        return true;
    }

    /**
     * controller执行之后
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 完全完成后
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {

    }
}
