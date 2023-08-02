package com.ethan.mall.exception;

import com.ethan.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException:",e);
        return handleBindingResult(e.getBindingResult());
    }

    public ApiRestResponse handleBindingResult(BindingResult result){
        //把异常处理为对外暴露的提示
        List<String> list = new ArrayList<>();
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for(ObjectError objectError:allErrors){
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }
        if (list.size() == 0){
            return ApiRestResponse.error(EthanMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(EthanMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),list.toString());
    }

}
