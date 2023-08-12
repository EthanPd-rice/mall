package com.ethan.mall.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.filter.UserFilter;
import com.ethan.mall.model.pojo.User;
import com.ethan.mall.service.EmailService;
import com.ethan.mall.service.UserService;
import com.ethan.mall.util.EmailUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private EmailService emailService;
    @GetMapping("/test")
    @ResponseBody
    public User personalPage(@RequestParam Integer id){
        return userService.getUser(id);
    }
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(
            @RequestParam("userName") String userName
            ,@RequestParam("password") String password
            ,@RequestParam("emailAddress") String emailAddress
            ,@RequestParam("verificationCode") String verificationCode) throws EthanMailException {
        //对用户名和密码做空校验
        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8位
        if(password.length()<8){
            return ApiRestResponse.error(EthanMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        if(StringUtils.isEmpty(emailAddress)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_EMAIL);
        }
        if(StringUtils.isEmpty(verificationCode)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_VERIFY);
        }
        //如果邮箱已注册，则不允许再次注册
        boolean emailPassed = userService.checkEmailRegistered(emailAddress);
        if(!emailPassed){
            return ApiRestResponse.error(EthanMallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
        }
        //检验邮箱和验证码是否匹配
        Boolean passEmailAndCode = emailService.checkEmailAndCode(emailAddress, verificationCode);
        if(!passEmailAndCode){
            return ApiRestResponse.error(EthanMallExceptionEnum.WRONG_VERIFY);
        }
        userService.register(userName, password,emailAddress);
        return ApiRestResponse.success();
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(
            @RequestParam("userName") String userName
            , @RequestParam("password") String password
            , HttpSession session) throws EthanMailException {
        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName,password);
        //安全考虑，不返回用户密码
        user.setPassword(null);
        //在Constant类设置一个Key,把user对象放到key为ETHAN_MALL_USER常量里去
        session.setAttribute(Constant.ETHAN_MALL_USER,user);
        return ApiRestResponse.success(user);
    }

    @PostMapping("/loginWithJwt")
    @ResponseBody
    public ApiRestResponse loginWithJwt(
            @RequestParam("userName") String userName
            , @RequestParam("password") String password) {
        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName,password);
        //安全考虑，不返回用户密码
        user.setPassword(null);
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        String token = JWT.create()
                .withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_ROLE, user.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                .sign(algorithm);
        return ApiRestResponse.success(token);
    }

    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session,@RequestParam String signature) throws EthanMailException {
//        User currentUser = (User)session.getAttribute(Constant.ETHAN_MALL_USER);
        User currentUser = UserFilter.currentUser;
        if(currentUser == null){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.ETHAN_MALL_USER);
        return ApiRestResponse.success();
    }

    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(
            @RequestParam("userName") String userName
            , @RequestParam("password") String password
            , HttpSession session) throws EthanMailException {
        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName,password);
        //相比普通用户登录，管理员登录多了一层校验，user表中role为2是管理员，1是普通用户
        if (userService.checkAdminRole(user)) {
            //安全考虑，不返回用户密码
            user.setPassword(null);
            //在Constant类设置一个Key,把user对象放到key为ETHAN_MALL_USER常量里去
            session.setAttribute(Constant.ETHAN_MALL_USER,user);
            return ApiRestResponse.success(user);
        }else{
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_ADMIN);
        }

    }


    @PostMapping("/adminLoginWithJwt")
    @ResponseBody
    public ApiRestResponse adminLoginWithJwt(
            @RequestParam("userName") String userName
            , @RequestParam("password") String password) throws EthanMailException {
        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName,password);
        //相比普通用户登录，管理员登录多了一层校验，user表中role为2是管理员，1是普通用户
        if (userService.checkAdminRole(user)) {
            //安全考虑，不返回用户密码
            user.setPassword(null);
            Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
            String token = JWT.create()
                    .withClaim(Constant.USER_NAME, user.getUsername())
                    .withClaim(Constant.USER_ID, user.getId())
                    .withClaim(Constant.USER_ROLE, user.getRole())
                    .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                    .sign(algorithm);
            //在Constant类设置一个Key,把user对象放到key为ETHAN_MALL_USER常量里去
            return ApiRestResponse.success(token);
        }else{
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_ADMIN);
        }

    }

    @ApiOperation("发送邮件")
    @PostMapping("user/sendEmail")
    @ResponseBody
    public ApiRestResponse sendEmail(@RequestParam String emailAddress ){
        boolean validEmailAddress = EmailUtil.isValidEmailAddress(emailAddress);
        if(validEmailAddress){
            //检验通过后，看邮件是否被注册
            boolean emailPassed = userService.checkEmailRegistered(emailAddress);
            if(!emailPassed){
                return ApiRestResponse.error(EthanMallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
            } else {
                String verificationCode = EmailUtil.genVerificationCode();
                //验证码信息比较适合放在redis里面
                Boolean saveEmailToRedis = emailService.saveEmailToRedis(emailAddress, verificationCode);
                if(saveEmailToRedis){
                    emailService.sendSimpleMessage(emailAddress,Constant.EMAIL_SUBJECT,"欢迎注册，您的验证码是："+verificationCode);
                    return ApiRestResponse.success();
                }else{
                    return ApiRestResponse.error(EthanMallExceptionEnum.EMAIL_ALREADY_BEEN_SEND);
                }
            }
        }else{
            return ApiRestResponse.error(EthanMallExceptionEnum.WRONG_EMAIL);
        }
    }



}
