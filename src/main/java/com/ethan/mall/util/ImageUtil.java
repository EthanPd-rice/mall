package com.ethan.mall.util;

import com.ethan.mall.common.Constant;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.poi.hpsf.Thumbnail;

import java.io.IOException;

public class ImageUtil {

    public static void main(String[] args) throws IOException {
        String path = "D:/Java/code/Ethan-mall-image/";

        //切割
        Thumbnails.of(path+"YYDMW.jpg").sourceRegion(Positions.BOTTOM_RIGHT,500,500).size(200, 200).toFile(path+"crop.jpg");

        //缩放
        Thumbnails.of(path+"YYDMW.jpg").scale(0.7).toFile(path+"scale1.jpg");
        Thumbnails.of(path+"YYDMW.jpg").scale(1.5).toFile(path+"scale1.jpg");
        Thumbnails.of(path+"YYDMW.jpg").size(500,500).keepAspectRatio(false).toFile(path+"size1.jpg");
        Thumbnails.of(path+"YYDMW.jpg").size(500,500).keepAspectRatio(true).toFile(path+"size2.jpg");

        //旋转
        Thumbnails.of(path+"YYDMW.jpg").size(500, 500).keepAspectRatio(true).rotate(90).toFile(path+"rotate1.jpg");
        Thumbnails.of(path+"YYDMW.jpg").size(500, 500).keepAspectRatio(true).rotate(180).toFile(path+"rotate2.jpg");


    }

}
