package com.ethan.mall.service;

import com.ethan.mall.model.pojo.Product;
import com.ethan.mall.model.request.AddProductReq;
import com.ethan.mall.model.request.ProductListReq;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品Service
 */

public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listProductForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize);

    Product detail(@RequestParam Integer id);

    PageInfo list(ProductListReq productListReq);
}
