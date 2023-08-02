package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.pojo.Category;
import com.ethan.mall.model.pojo.User;
import com.ethan.mall.model.request.AddCategoryReq;
import com.ethan.mall.model.request.UpdateCategoryReq;
import com.ethan.mall.model.vo.CategoryVO;
import com.ethan.mall.service.CategoryService;
import com.ethan.mall.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class CategoryController {
    @Resource
    private UserService userService;
    @Resource
    private CategoryService categoryService;

    @ApiOperation("后台添加目录")
    @PostMapping("/admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq){
        //若有参数是空的，则提示参数不能为空
//        if(addCategoryReq.getName() == null || addCategoryReq.getOrderNum() == null
//                || addCategoryReq.getParentId() == null || addCategoryReq.getOrderNum() == null){
//            return ApiRestResponse.error(EthanMallExceptionEnum.PARA_NOT_NULL);
//        }
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


    @ApiOperation("后台更新目录")
    @PostMapping("/admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq,HttpSession session){
        User currentUser = (User) session.getAttribute(Constant.ETHAN_MALL_USER);
        //判断用户是否登录状态
        if(currentUser == null){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_LOGIN);
        }
        //判断用户是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if(adminRole){
            Category category = new Category();
            BeanUtils.copyProperties(updateCategoryReq,category);
            categoryService.update(category);
            return ApiRestResponse.success();
        }else{
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("后台删除目录")
    @PostMapping("/admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id){
        categoryService.delete(id);
        return  ApiRestResponse.success();
    }

    @ApiOperation("后台目录列表")
    @PostMapping("/admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("前台目录列表")
    @PostMapping("/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer(){
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }


}
