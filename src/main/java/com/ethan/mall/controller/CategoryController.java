package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.pojo.User;
import com.ethan.mall.model.request.AddCategoryReq;
import com.ethan.mall.service.CategoryService;
import com.ethan.mall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class CategoryController {
    @Resource
    private UserService userService;
    @Resource
    private CategoryService categoryService;

    @PostMapping("/admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @RequestBody AddCategoryReq addCategoryReq){
        //若有参数是空的，则提示参数不能为空
        if(addCategoryReq.getName() == null || addCategoryReq.getOrderNum() == null
                || addCategoryReq.getParentId() == null || addCategoryReq.getOrderNum() == null){
            return ApiRestResponse.error(EthanMallExceptionEnum.PARA_NOT_NULL);
        }
        User currentUser = (User) session.getAttribute(Constant.ETHAN_MALL_USER);
        //判断用户是否登录状态
        if(currentUser == null){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_LOGIN);
        }
        //判断用户是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if(adminRole){
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        }else{
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_ADMIN);
        }
    }
}
