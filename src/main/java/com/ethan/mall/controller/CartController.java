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
        //避免横向越权,从UserFilter.currentUser.getId()取到用户id
        List<CartVO> cartVOList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartVOList);
    }


    @ApiOperation("添加商品到购物车")
    @PostMapping("/cart/add")
    @ResponseBody
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count){
        return ApiRestResponse.success(cartService.add(UserFilter.currentUser.getId(),productId,count));
    }

    @ApiOperation("更新购物车某商品数量")
    @PostMapping("/cart/update")
    @ResponseBody
    public ApiRestResponse update(@RequestParam Integer productId,@RequestParam Integer count){
        //用户id和订单id不坐参数从控制台传进去
        List<CartVO> cartVOS = cartService.update(UserFilter.currentUser.getId(),productId,count);
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("删除购物车的某个商品")
    @PostMapping("/cart/delete")
    @ResponseBody
    public ApiRestResponse delete(@RequestParam Integer productId){
        List<CartVO> cartVOS = cartService.delete(UserFilter.currentUser.getId(),productId);
        return  ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("选中/不选中购物车的某个商品")
    @PostMapping("/cart/select")
    @ResponseBody
    public ApiRestResponse select(Integer productId,Integer selected){
        List<CartVO> cartVOS = cartService.select(UserFilter.currentUser.getId(),productId,selected);
        return  ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("全选/全不选购物车的某个商品")
    @PostMapping("/cart/selectAll")
    @ResponseBody
    public ApiRestResponse select(Integer selected){
        List<CartVO> cartVOS = cartService.selectAll(UserFilter.currentUser.getId(),selected);
        return  ApiRestResponse.success(cartVOS);
    }

}
