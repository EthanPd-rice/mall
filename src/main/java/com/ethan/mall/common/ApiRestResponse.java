package com.ethan.mall.common;

import com.ethan.mall.exception.EthanMallExceptionEnum;

public class ApiRestResponse<T> {
    private Integer status;
    private String msg;
    //使用泛型，data返回可能是用户对象,也可能是购物车对象
    private T data;
    //定义常量
    private static final int OK_CODE = 10000;
    private static final String OK_MSG = "SUCCESS";

    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    //请求成功，使用默认构造方法
    public ApiRestResponse() {
        this(OK_CODE,OK_MSG);
    }

    //返回通用响应对象-成功时
    public static <T> ApiRestResponse<T> success(){
        //建立一个成功的返回值，含OK_CODE = 10000，OK_MSG = "SUCCESS"
        return new ApiRestResponse<>();
    }
    //请求成功时，带上一个返回值
    public static <T> ApiRestResponse<T> success(T result){
        ApiRestResponse<T> response = new ApiRestResponse<>();
        //response不仅携带了OK_CODE与OK_MSG，还携带data
        response.setData(result);
        return response;
    }

    //返回通用响应对象-失败时
    //自己写error不利于维护，新建枚举类，把常见错误收拢在一起，之后找到枚举值，一调用就可以
    public static <T> ApiRestResponse<T> error(Integer code,String msg){
        return new ApiRestResponse<>(code,msg);
    }
    public static <T> ApiRestResponse<T> error(EthanMallExceptionEnum ex){
        return new ApiRestResponse<>(ex.getCode(),ex.getMsg());
    }


    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static int getOkCode() {
        return OK_CODE;
    }

    public static String getOkMsg() {
        return OK_MSG;
    }
}
