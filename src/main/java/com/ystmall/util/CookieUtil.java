package com.ystmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = ".ystmall.top";

    private final static String COOKIE_NAME = "ystmall_login_token";

    /**
     * 读login cookie
     * @param request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for(Cookie ck : cks){
                log.info("read cookieName: {},cookieValue: {}",ck.getName(),ck.getValue());
                if(StringUtils.equals(ck.getName(), COOKIE_NAME)){
                    log.info("read cookieName: {},cookieValue: {}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 写login cookie
     * @param response
     * @param token
     */
    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        //设置在根目录
        ck.setPath("/");
        //防止脚本攻击带来的信息泄漏风险，不许通过脚本访问cookie
        //ck.setHttpOnly(true);
        /**
         * 设置cookie有效期
         * -1 -> 永久
         * 0 -> 删除此cookie
         * 单位是秒
         * 如果maxage不设置的话，cookie就不会写入硬盘，而是写在内存。只在当前页面有效。
         */
        ck.setMaxAge(60 * 60 * 24 * 365);

        log.info("write cookieName: {}, cookieValue: {}", ck.getName(),ck.getValue());

        response.addCookie(ck);

    }

    /**
     * 删除cookie
     * @param request
     * @param response
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for(Cookie ck : cks){
                if(StringUtils.equals(ck.getName(), COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    //设置为0 代表删除此cookie
                    ck.setMaxAge(0);
                    log.info("write cookieName: {}, cookieValue: {}", ck.getName(),ck.getValue());
                    response.addCookie(ck);
                }
            }
        }
    }
}
