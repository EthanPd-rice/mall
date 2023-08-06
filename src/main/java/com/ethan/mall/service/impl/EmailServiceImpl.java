package com.ethan.mall.service.impl;

import com.ethan.mall.common.Constant;
import com.ethan.mall.service.EmailService;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

    //用于发邮件
    @Resource
    private JavaMailSender mailSender ;

    @Resource
    private EmailService emailService;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(Constant.EMAIL_FROM);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        mailSender.send(simpleMailMessage);
    }


    @Override
    public Boolean saveEmailToRedis(String emailAddress,String verificationCode){
        RedissonClient client = Redisson.create();
        RBucket<String> bucket =  client.getBucket(emailAddress);
        //bucket.isExists()看是否存在值
        boolean exists = bucket.isExists();
        if(!exists){
            bucket.set(verificationCode,60, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }
}
