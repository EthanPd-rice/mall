package com.ethan.mall.service;


import com.ethan.mall.model.vo.CartVO;

import java.util.List;

/**
 * 购物车Service
 */
public interface CartService {

    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer userId, Integer productId, Integer count);

    List<CartVO> update(Integer userId, Integer productId, Integer count);

    List<CartVO> delete(Integer userId, Integer productId);

    List<CartVO> select(Integer userId, Integer productId, Integer selected);

    List<CartVO> selectAll(Integer userId, Integer selected);
}
