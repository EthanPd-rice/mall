package com.ethan.mall.service;


import com.ethan.mall.model.vo.CartVO;

import java.util.List;

/**
 * 购物车Service
 */
public interface CartService {

    List<CartVO> list(Integer userId);

    void add(Integer userId, Integer productId, Integer count);
}
