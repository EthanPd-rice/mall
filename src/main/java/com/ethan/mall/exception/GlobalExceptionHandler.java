package com.ethan.mall.exception;

import com.ethan.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
//@ControllerAdvice 是一个注解，用于标记一个类是全局控制器异常处理器。
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger("GlobalExceptionHandler.class");
    //Spring将根据 @ExceptionHandler 注解来捕获并处理异常，从而提供一个统一的异常处理机制。
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e){
        log.error("Default Exception:",e);
        return ApiRestResponse.error(EthanMallExceptionEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(EthanMailException.class)
    @ResponseBody
    public Object handleEthanMailException(EthanMailException e){
        log.error("EthanMailException:",e);
        return ApiRestResponse.error(e.getCode(),e.getMessage());
    }

}
