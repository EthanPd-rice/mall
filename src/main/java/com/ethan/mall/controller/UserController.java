package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.pojo.User;
import com.ethan.mall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("/test")
    @ResponseBody
    public User personalPage(@RequestParam Integer id){
        return userService.getUser(id);
    }
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password) throws EthanMailException {
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
        userService.register(userName, password);
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

    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session,@RequestParam String signature) throws EthanMailException {
        User currentUser = (User)session.getAttribute(Constant.ETHAN_MALL_USER);
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
}
