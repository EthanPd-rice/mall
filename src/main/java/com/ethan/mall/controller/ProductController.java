package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.model.pojo.Product;
import com.ethan.mall.model.request.ProductListReq;
import com.ethan.mall.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
public class ProductController {

    @Resource
    private ProductService productService;

    @ApiOperation("商品详情")
    @GetMapping("/product/detail")
    @ResponseBody
    public ApiRestResponse detail(@RequestParam Integer id){
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("前台商品列表")
    @GetMapping("product/list")
    @ResponseBody
    public ApiRestResponse list(ProductListReq productListReq){
        PageInfo pageInfo = productService.list(productListReq);
        return ApiRestResponse.success(pageInfo);
    }


}
