package com.ethan.mall.model.dao;

import com.ethan.mall.model.pojo.Cart;
import com.ethan.mall.model.vo.CartVO;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserProductId(Integer userId,Integer productId);

    List<CartVO> selectCartVOById(Integer userId);

    Integer selectOrNot(Integer userId,Integer productId,Integer selected);
}