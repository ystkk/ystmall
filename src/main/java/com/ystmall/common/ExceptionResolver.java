package com.ystmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 * @author Shengtong Yuan
 */
@Slf4j
//组件，注入到spring容器中
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                         Object o, Exception e) {

        log.error("{} Exception",httpServletRequest.getRequestURI(), e);
        ModelAndView modelAndView = new ModelAndView(new MappingJacksonJsonView());

        //当使用是jackson2.x时候使用MappingJackson2JsonView
        modelAndView.addObject("status",ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg","接口异常，详情查看服务端日志");
        modelAndView.addObject("data",e.toString());

        return modelAndView;
    }
}
