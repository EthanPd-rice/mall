package com.ethan.mall.exception;

/**
 * 描述：      异常枚举
 */
public enum EthanMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空")
    ,NEED_PASSWORD(10002,"密码不能为空")
    ,PASSWORD_TOO_SHORT(10003,"密码位数不能小于8")
    ,NAME_EXISTED(10004,"不允许重名，注册失败")
    ,INSERT_FAILED(10005,"插入失败，请重试");
    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    EthanMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
