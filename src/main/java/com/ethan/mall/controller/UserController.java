package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
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
            @RequestParam("username") String username,
            @RequestParam("password") String password) throws EthanMailException {
        //对用户名和密码做空校验
        if(StringUtils.isEmpty(username)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8位
        if(password.length()<8){
            return ApiRestResponse.error(EthanMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(username, password);
        return ApiRestResponse.success();
    }
}
