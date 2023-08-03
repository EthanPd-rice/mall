package com.ethan.mall.service.impl;

import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.dao.CartMapper;
import com.ethan.mall.model.dao.ProductMapper;
import com.ethan.mall.model.pojo.Cart;
import com.ethan.mall.model.pojo.Product;
import com.ethan.mall.model.vo.CartVO;
import com.ethan.mall.service.CartService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CartMapper cartMapper;


    @Override
    public List<CartVO> list(Integer userId){
        List<CartVO> cartVOS = cartMapper.selectCartVOById(userId);
        for(int i = 0 ; i < cartVOS.size() ; i++){
            cartVOS.get(i).setTotalPrice(cartVOS.get(i).getQuantity() * cartVOS.get(i).getPrice());
        }
        return cartVOS;
    }

    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count){
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null || !product.getStatus().equals(1) || count > product.getStock() ){
            throw new EthanMailException(EthanMallExceptionEnum.CART_NOT_SELL);
        }else{
            Cart cart =cartMapper.selectByUserProductId(userId, productId);
            if(cart != null ){
                count = cart.getQuantity() + count;
                if(count > product.getStock()){
                    throw new EthanMailException(EthanMallExceptionEnum.CART_NOT_STOCK);
                }else{
                    cart.setQuantity(count);
                    cartMapper.updateByPrimaryKeySelective(cart);
                }
            }else{
                Cart cartNew = new Cart();
                cartNew.setProductId(productId);
                cartNew.setUserId(userId);
                cartNew.setQuantity(count);
                cartNew.setSelected(Constant.CartChecked.CHECKED);
                cartMapper.insertSelective(cartNew);
            }
        }
        return this.list(userId);
    }

}
