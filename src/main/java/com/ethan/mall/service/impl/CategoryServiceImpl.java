package com.ethan.mall.service.impl;

import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.dao.CategoryMapper;
import com.ethan.mall.model.pojo.Category;
import com.ethan.mall.model.request.AddCategoryReq;
import com.ethan.mall.model.vo.CategoryVO;
import com.ethan.mall.service.CategoryService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void update(Category updatecCategory){
        if(updatecCategory.getName() != null){
            Category categoryOld = categoryMapper.selectByName(updatecCategory.getName());
            if(categoryOld != null && !categoryOld.getId().equals(updatecCategory.getId())){
                throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
            }
        }

        int count = categoryMapper.updateByPrimaryKeySelective(updatecCategory);
        if(count == 0 ){
            throw new EthanMailException(EthanMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id){
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        if(categoryOld == null){
            throw new EthanMailException(EthanMallExceptionEnum.DELETE_FAILED);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if(count == 0){
            throw new EthanMailException(EthanMallExceptionEnum.DELETE_FAILED);
        }
    }

    //pageInfo包括页号，页空间等信息
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize,"type,order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "listCategoryForCustomer")//import org.springframework.cache.annotation.Cacheable;
    public List<CategoryVO> listCategoryForCustomer(){
        ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
        recursivelyFindCategories(categoryVOList,0);
        return categoryVOList;

    }

    public void recursivelyFindCategories(List<CategoryVO> categoryVOList,Integer parentId){
        //递归获取所有子类别，并组合成为一个“目录树“
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        if(!CollectionUtils.isEmpty(categoryList)){
            for(int i = 0; i < categoryList.size();i++){
                Category category = categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category,categoryVO);
                categoryVOList.add(categoryVO);
                recursivelyFindCategories(categoryVO.getChildCategory(),categoryVO.getId());
            }
        }
    }


}
