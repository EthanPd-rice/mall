package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.filter.UserFilter;
import com.ethan.mall.model.vo.CartVO;
import com.ethan.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class CartController {

    @Resource
    private CartService cartService;

    @ApiOperation("购物车列表")
    @GetMapping("/cart/list")
    @ResponseBody
    public ApiRestResponse list(){
        List<CartVO> cartVOList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartVOList);
    }


    @ApiOperation("添加商品到购物车")
    @PostMapping("/cart/add")
    @ResponseBody
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count){
        cartService.add(UserFilter.currentUser.getId(),productId,count);
        return ApiRestResponse.success();
    }
}
