package com.ethan.mall.service.impl;

import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.dao.ProductMapper;
import com.ethan.mall.model.pojo.Product;
import com.ethan.mall.model.request.AddProductReq;
import com.ethan.mall.model.request.UpdateProductReq;
import com.ethan.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：商品服务实现类
 */
@Service
public class ProdectServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public void add(AddProductReq addProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq,product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if(productOld != null){
            throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if(count == 0){
            throw new EthanMailException(EthanMallExceptionEnum.CREATE_FAILED);
        }
    }

    /**
     * 更新商品信息
     * @param updateProduct 要更新的Product对象
     */
    @Override
    public void update(Product updateProduct){
        Product productOld = productMapper.selectByName(updateProduct.getName());
        if(productOld != null && !updateProduct.getId().equals(productOld.getId())){
            throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if(count == 0 ){
            throw new EthanMailException(EthanMallExceptionEnum.UPDATE_FAILED);
        }
    }


    /**
     * 删除商品信息
     * @param id 要删除的商品id
     */
    @Override
    public void delete(Integer id){
        Product productOld = productMapper.selectByPrimaryKey(id);
        if(productOld == null){
            throw new EthanMailException(EthanMallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if(count == 0){
            throw new EthanMailException(EthanMallExceptionEnum.DELETE_FAILED);
        }

    }
}
