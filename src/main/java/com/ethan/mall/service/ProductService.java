package com.ethan.mall.service;

import com.ethan.mall.model.pojo.Product;
import com.ethan.mall.model.request.AddProductReq;

/**
 * 商品Service
 */

public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);
}
