package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.common.Constant;
import com.ethan.mall.common.ValidList;
import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.pojo.Product;
import com.ethan.mall.model.request.AddProductReq;
import com.ethan.mall.model.request.UpdateCategoryReq;
import com.ethan.mall.model.request.UpdateProductReq;
import com.ethan.mall.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Validated
public class ProductAdminController {

    @Resource
    private ProductService productService;
    @Value("${file.upload.uri}")
    String uri;

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
        createFile(file, fileDirectory, destFile, fileDirectory.mkdir());
        //StringBuffer+""变String格式

        //修改ip
        String address = uri;
        return ApiRestResponse.success("http://"+address+"/images/"+newFileName);
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


    @ApiOperation("后台批量上传商品")
    @PostMapping("/admin/upload/product")
    @ResponseBody
    public ApiRestResponse uploadProduct(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        createFile(multipartFile, fileDirectory, destFile, fileDirectory.mkdirs());
        productService.addProductByExcel(destFile);
        return ApiRestResponse.success();
    }

    @ApiOperation("上传图片并添加水印")
    @PostMapping("admin/upload/image")
    @ResponseBody
    public ApiRestResponse uploadImage(HttpServletRequest httpServletRequest, @RequestParam("file")MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        createFile(file, fileDirectory, destFile, fileDirectory.mkdir());
        Thumbnails.of(destFile).size(Constant.IMAGE_SIZE, Constant.IMAGE_SIZE)
                .watermark(Positions.CENTER, ImageIO.read(new File(Constant.FILE_UPLOAD_DIR+Constant.WATER_MARK_JPG)),Constant.IMAGE_OPACITY)
                .toFile(new File(Constant.FILE_UPLOAD_DIR + newFileName));
        //StringBuffer+""变String格式

        //修改ip
        String address = uri;
        return ApiRestResponse.success("http://"+address+"/images/"+newFileName);
    }


    private void createFile(@RequestParam("file") MultipartFile file, File fileDirectory, File destFile, boolean mkdir) {
        if (!fileDirectory.exists()) {
            if (!mkdir) {
                throw new EthanMailException(EthanMallExceptionEnum.MKDIR_FAILED);
            }
        }
        //
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @ApiOperation("后台批量更新商品")
    @PostMapping("admin/product/batchUpdate")
    @ResponseBody
    public ApiRestResponse batchUpdateProduct(@Valid @RequestBody List<UpdateProductReq> updateProductReqList){
        for(int i = 0 ; i < updateProductReqList.size() ; i++){
            UpdateProductReq updateProductReq = updateProductReqList.get(i);
            if(updateProductReq.getPrice() < 1){
                throw new EthanMailException(EthanMallExceptionEnum.PRICE_TOO_LOW);
            }
            if(updateProductReq.getStatus() < 10000){
                throw new EthanMailException(EthanMallExceptionEnum.STOCK_TOO_MANY);
            }
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq,product);
            productService.update(product);
        }
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量更新商品，ValidList验证")
    @PostMapping("admin/product/batchUpdate2")
    @ResponseBody
    public ApiRestResponse batchUpdateProduct2(@Valid @RequestBody ValidList<UpdateProductReq> updateProductReqList){
        for(int i = 0 ; i < updateProductReqList.size() ; i++){
            UpdateProductReq updateProductReq = updateProductReqList.get(i);
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq,product);
            productService.update(product);
        }
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量更新商品，@Validated验证")
    @PostMapping("admin/product/batchUpdate3")
    @ResponseBody
    public ApiRestResponse batchUpdateProduct3(@Valid @RequestBody ValidList<UpdateProductReq> updateProductReqList){
        for(int i = 0 ; i < updateProductReqList.size() ; i++){
            UpdateProductReq updateProductReq = updateProductReqList.get(i);
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq,product);
            productService.update(product);
        }
        return ApiRestResponse.success();
    }


}
