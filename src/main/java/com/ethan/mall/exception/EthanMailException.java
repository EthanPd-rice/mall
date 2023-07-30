package com.ethan.mall.exception;

public class EthanMailException extends RuntimeException {
    private final Integer code;
    private final String message;

    public EthanMailException(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public EthanMailException(EthanMallExceptionEnum exceptionEnum){
        this(exceptionEnum.code,exceptionEnum.msg);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}