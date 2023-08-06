package com.ethan.mall.model.dao;

import com.ethan.mall.model.pojo.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByOrderNo(String OrderNo);

    List<Order> selectByUserId(Integer userId);

    List<Order> selectAllForAdmin();
}