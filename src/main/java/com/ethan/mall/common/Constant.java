package com.ethan.mall.common;

import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component//要让spring识别Value注解，还需要加@Component注解
public class Constant {
    public static final String ETHAN_MALL_USER = "ethan_mall_user";
    //盐值不能太简单，复杂点
    public static final String SALT = "SJsdsSK.SAD/.S28371][S]./";

    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc","price asc");
    }

    public interface CartChecked{
        int UN_CHECKED = 0;//g
        int CHECKED = 1;
    }

    public enum OrderStatusEnum{
        CANCELED(0,"用户已取消")
        ,NOT_PAID(10,"未付款")
        ,PAID(20,"已付款")
        ,DELIVERED(30,"已发货")
        ,FINISHED(40,"交易完成");


        private int code;
        private String value;

        OrderStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum:values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new EthanMailException(EthanMallExceptionEnum.NO_EMUM);
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
