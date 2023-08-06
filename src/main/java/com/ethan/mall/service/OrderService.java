package com.ethan.mall.service;


import com.ethan.mall.model.request.CreateOrderReq;
import com.ethan.mall.model.vo.CartVO;
import com.ethan.mall.model.vo.OrderVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 购物车Service
 */
public interface OrderService {

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    String qrcode(String orderNo);

    void pay(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void delivered(String orderNo);

    void finish(String orderNo);
}
