package com.most.core.pub.exception;

import com.most.core.pub.data.ServiceResponse;
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
            ServiceResponse serviceResponse = new ServiceResponse();
            if(ex instanceof  InvocationTargetException) {
                Throwable throwable = ex.getCause();
                if(throwable instanceof  GenericException) {
                    GenericException genericException = (GenericException)throwable;
                    serviceResponse.setError(genericException.getCode(),genericException.getDesc());
                    response.getWriter().write(serviceResponse.toJsonString());
                } else {
                    serviceResponse.setError("-1",throwable.getMessage());
                    response.getWriter().write(serviceResponse.toJsonString());
                }
            } else if(ex instanceof  GenericException){
                GenericException genericException = (GenericException)ex;
                serviceResponse.setError(genericException.getCode(),genericException.getDesc());
                response.getWriter().write(serviceResponse.toJsonString());
            } else {
                serviceResponse.setError("-1",ex.getMessage() != null ? ex.getMessage() : "系统异常");
                response.getWriter().write(serviceResponse.toJsonString());
            }
            logger.error("异常:", ex);
        } catch (IOException e) {
            logger.error("与客户端通讯异常\n", e);
        }

        ModelAndView modelAndView=new ModelAndView();

        return modelAndView;
    }
}
