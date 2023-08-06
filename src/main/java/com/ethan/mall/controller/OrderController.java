package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.model.request.CreateOrderReq;
import com.ethan.mall.model.vo.OrderVO;
import com.ethan.mall.service.OrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class OrderController {

    @Resource
    private OrderService orderService;


    @PostMapping("order/create")
    @ApiOperation("创建订单")
    @ResponseBody
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    @GetMapping("order/detail")
    @ApiOperation("前台订单详情")
    @ResponseBody
    public ApiRestResponse detail(@RequestParam String orderNo){
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    @GetMapping("order/list")
    @ApiOperation("前台订单列表")
    @ResponseBody
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @PostMapping("order/cancel")
    @ApiOperation("前台取消订单")
    @ResponseBody
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    @PostMapping("order/qrcode")
    @ApiOperation("生成支付二维码")
    @ResponseBody
    public ApiRestResponse qrcode(@RequestParam String orderNo){
        String pngAddress = orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }

    @GetMapping("/pay")
    @ApiOperation("支付接口")
    @ResponseBody
    public ApiRestResponse pay(String orderNo){
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }
}
