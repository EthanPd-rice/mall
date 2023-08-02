package com.ethan.mall.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}
