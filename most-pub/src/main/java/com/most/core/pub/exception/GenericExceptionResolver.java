package com.most.core.pub.exception;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class GenericExceptionResolver implements HandlerExceptionResolver {

    private static Logger logger = LoggerFactory.getLogger(GenericExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setStatus(HttpStatus.OK.value()); //设置状态码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType
        response.setCharacterEncoding("UTF-8"); //避免乱码
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        try {
            JSONObject jsonError = new JSONObject();
            if(ex instanceof  InvocationTargetException) {
                Throwable throwable = ex.getCause();
                if(throwable instanceof  GenericException) {
                    GenericException genericException = (GenericException)throwable;
                    jsonError.put("RESULT_CODE", genericException.getCode());
                    jsonError.put("RESULT_INFO", genericException.getDesc());
                    response.getWriter().write(jsonError.toJSONString());
                } else {
                    jsonError.put("RESULT_CODE", "-1");
                    jsonError.put("RESULT_INFO", throwable.getMessage());
                    response.getWriter().write(jsonError.toJSONString());
                }
            } else if(ex instanceof  GenericException){
                GenericException genericException = (GenericException)ex;
                jsonError.put("RESULT_CODE", genericException.getCode());
                jsonError.put("RESULT_INFO", genericException.getDesc());
                response.getWriter().write(jsonError.toJSONString());
            } else {
                jsonError.put("RESULT_CODE", "-1");
                jsonError.put("RESULT_INFO", ex.getMessage());
                response.getWriter().write(jsonError.toJSONString());
            }
        } catch (IOException e) {
            logger.error("与客户端通讯异常\n", e);
        }

        return null;
    }
}
