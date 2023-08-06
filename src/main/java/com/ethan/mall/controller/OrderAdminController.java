package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.service.OrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class OrderAdminController {


    @Resource
    private OrderService orderService;

    @GetMapping("admin/order/list")
    @ApiOperation("后台订单列表")
    @ResponseBody
    public ApiRestResponse list(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 发货。订单状态流程：0用户已取消，10未付款，20已付款，30已发货，40交易完成
     */
    @PostMapping("/admin/order/delivered")
    @ApiOperation("订单发货")
    @ResponseBody
    public ApiRestResponse delivered(@RequestParam String orderNo){
        orderService.delivered(orderNo);
        return ApiRestResponse.success();
    }

    /**
     *管理员和用户都可以调用完结订单接口
     */
    @PostMapping("/order/finish")
    @ApiOperation("订单完结")
    @ResponseBody
    public ApiRestResponse finish(@RequestParam String orderNo){
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }

}
