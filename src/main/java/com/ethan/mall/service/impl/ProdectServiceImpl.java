package com.ethan.mall.service.impl;

import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.dao.ProductMapper;
import com.ethan.mall.model.pojo.Category;
import com.ethan.mall.model.pojo.Product;
import com.ethan.mall.model.query.ProductListQuery;
import com.ethan.mall.model.request.AddProductReq;
import com.ethan.mall.model.request.ProductListReq;
import com.ethan.mall.model.request.UpdateProductReq;
import com.ethan.mall.model.vo.CategoryVO;
import com.ethan.mall.service.CategoryService;
import com.ethan.mall.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：商品服务实现类
 */
@Service
public class ProdectServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private CategoryService categoryService;

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

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus){
        productMapper.batchUpdateSellStatus(ids,sellStatus);
    }

    @Override
    public PageInfo listProductForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    @Override
    public Product detail(@RequestParam Integer id){
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq){
        //构建Query对象
        ProductListQuery productListQuery = new ProductListQuery();

        //搜索处理
        if(!StringUtils.isEmpty(productListReq.getKeyword())){
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            productListQuery.setKeyword(keyword);
        }

        //目录处理，若查某个目录下的商品，不仅查出该目录下的，还要把所有子目录的所有商品都查出来，所以要拿到一个目录id的List
        if(productListReq.getCategoryId() != null){
            //使用CategoryService中的接口提取id集合
            List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList,categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }

        //排序，为安全性，提前设置好可以排序什么
        String orderBy = productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            //传的排序方式支持排序
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize(),orderBy);
        }else{
            //传的排序方式不支持排序，按默认输出
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize());
        }
        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }
    //categoryVOList树状结构下面的所有id要拿到
    private void getCategoryIds(List<CategoryVO> categoryVOList,ArrayList<Integer> categoryIds){
        for(int i = 0;i < categoryVOList.size() ; i++){
            CategoryVO categoryVO = categoryVOList.get(i);
            if(categoryVO != null){
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(),categoryIds);
            }
        }
    }

}
