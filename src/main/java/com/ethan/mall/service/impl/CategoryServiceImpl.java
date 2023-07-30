package com.ethan.mall.service.impl;

import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.dao.CategoryMapper;
import com.ethan.mall.model.pojo.Category;
import com.ethan.mall.model.request.AddCategoryReq;
import com.ethan.mall.service.CategoryService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public void add(AddCategoryReq addCategoryReq){
        Category category = new Category();
        //BeanUtils.copyProperties(addCategoryReq,category)
        //把addCategoryReq和category对象的相同参数拷贝过去，无需频繁的get与set操作
        BeanUtils.copyProperties(addCategoryReq,category);
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        //检查要新增的目录名是否已有相同名字
        if(categoryOld != null){
            throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        //若更新条数为0，抛出创建失败的错误
        if(count == 0){
            throw new EthanMailException(EthanMallExceptionEnum.CREATE_FAILED);
        }
    }
}
