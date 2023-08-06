package com.ethan.mall.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    public static void generateQRCodeImage(String text,int width,int height,String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //比特矩阵
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,width,height);
        //图片存放路径,使用FileSystems.getDefault().getPath去获取路径可以根据当前操作系统来处理路径的分隔符，不用担心跨平台的兼容问题。
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix,"PNG",path);
    }

    public static void main(String[] args) {
        try {
            generateQRCodeImage("Hello World",350,350,"D:/Java/code/Ethan-mall-image/QRTest.png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
