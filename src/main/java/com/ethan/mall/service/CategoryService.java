package com.ethan.mall.service;

import com.ethan.mall.model.pojo.Category;
import com.ethan.mall.model.request.AddCategoryReq;
import com.ethan.mall.model.vo.CategoryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 目录Service
 */
public interface CategoryService {
    void add(AddCategoryReq addCategoryReq);

    void update(Category updatecCategory);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer();
}
