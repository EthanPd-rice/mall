package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.pojo.Product;
import com.ethan.mall.model.request.AddProductReq;
import com.ethan.mall.model.request.UpdateCategoryReq;
import com.ethan.mall.model.request.UpdateProductReq;
import com.ethan.mall.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;

@Controller
public class ProductAdminController {

    @Resource
    private ProductService productService;

    @PostMapping("admin/product/add")
    @ResponseBody
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq){
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    @PostMapping("admin/upload/file")
    @ResponseBody
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file")MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        if(!fileDirectory.exists()){
            if(!fileDirectory.mkdir()){
                throw new EthanMailException(EthanMallExceptionEnum.MKDIR_FAILED);
            }
        }
        //
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //StringBuffer+""变String格式
        try {
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL()+""))+"/images/"+newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(EthanMallExceptionEnum.UPLOAD_FAILED);
        }
    }

    private URI getHost(URI uri){
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(),uri.getUserInfo(),uri.getHost(),uri.getPort(),null,null,null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    @PostMapping("/admin/product/update")
    @ResponseBody
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq,product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/product/delete")
    @ResponseBody
    public ApiRestResponse deleteProduct(@RequestParam("id") Integer id){
        productService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量上下架接口")
    @PostMapping("/admin/product/batchUpdateSellStatus")
    @ResponseBody
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,@RequestParam Integer sellStatus){
        productService.batchUpdateSellStatus(ids,sellStatus);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品列表")
    @GetMapping("/admin/product/list")
    @ResponseBody
    public ApiRestResponse listProductForAdmin(@RequestParam  Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = productService.listProductForAdmin(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }
}
