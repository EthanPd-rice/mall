package com.ethan.mall.exception;

/**
 * 描述：      异常枚举
 */
public enum EthanMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空")
    ,NEED_PASSWORD(10002,"密码不能为空")
    ,PASSWORD_TOO_SHORT(10003,"密码位数不能小于8")
    ,NAME_EXISTED(10004,"不允许重名")
    ,INSERT_FAILED(10005,"插入失败，请重试")
    ,WRONG_PASSWORD(10006,"密码错误")
    ,NEED_LOGIN(10007,"用户未登录")
    ,UPDATE_FAILED(10008,"更新失败")
    ,NEED_ADMIN(10009,"无管理员权限")
    ,PARA_NOT_NULL(10010,"参数不能为空")
    ,CREATE_FAILED(10011,"新增失败")
    ,REQUEST_PARAM_ERROR(10012,"参数错误")
    ,DELETE_FAILED(10013,"删除失败")
    ,MKDIR_FAILED(10014,"文件夹创建失败")
    ,UPLOAD_FAILED(10015,"图片上传失败")
    ,CART_NOT_SELL(10016,"商品不可售")
    ,CART_NOT_STOCK(10017,"商品库存不够")
    ,SELECT_FAILED(10018,"勾选失败")
    ,CART_CHECK_NULL(10019,"购物车已勾选的商品为空")
    ,NO_EMUM(10020,"未找到对应枚举")
    ,NO_ORDER(10021,"订单不存在")
    ,NO_YOU_ORDER(10022,"不是你的订单")
    ,WRONG_ORDER_STATUS(10023,"订单状态不符")
    ,WRONG_EMAIL(10024,"非法的邮件地址")
    ,EMAIL_ALREADY_BEEN_REGISTERED(10025,"email地址已被注册")
    ,EMAIL_ALREADY_BEEN_SEND(10026,"email已发送，若无法收到，请稍后再试")
    ,NEED_EMAIL(10027,"邮箱不能为空")
    ,NEED_VERIFY(10028,"验证码不能为空")
    ,WRONG_VERIFY(10029,"验证码错误")
    ,TOKEN_EXPIRED(10029,"token过期")
    ,TOKEN_WRONG(10030,"token解析失败")
    ,SYSTEM_ERROR(20000,"系统异常");
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
