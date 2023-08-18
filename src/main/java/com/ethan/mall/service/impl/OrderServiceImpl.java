package com.ethan.mall.service.impl;

import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.filter.UserFilter;
import com.ethan.mall.model.dao.CartMapper;
import com.ethan.mall.model.dao.OrderItemMapper;
import com.ethan.mall.model.dao.OrderMapper;
import com.ethan.mall.model.dao.ProductMapper;
import com.ethan.mall.model.pojo.*;
import com.ethan.mall.model.request.CreateOrderReq;
import com.ethan.mall.model.vo.CartVO;
import com.ethan.mall.model.vo.OrderItemVO;
import com.ethan.mall.model.vo.OrderVO;
import com.ethan.mall.service.CartService;
import com.ethan.mall.service.OrderService;
import com.ethan.mall.util.OrderCodeFactory;
import com.ethan.mall.util.QRCodeGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：  订单Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private CartService cartService;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Value("${file.upload.uri}")
    String uri;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq){
        //拿到用户ID
        Integer userId = UserFilter.currentUser.getId();
        //从购物车查找已经勾选的商品
        List<CartVO> cartVOList = cartService.list(userId);
        ArrayList<CartVO> cartVOListTemp = new ArrayList<>();
        for(int i = 0 ; i < cartVOList.size();i++){
            CartVO cartVO = cartVOList.get(i);
            if(cartVO.getSelected() == 1) {
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList = cartVOListTemp;
        //如果购物车已勾选的为空，报错
        if(cartVOList == null){
            throw new EthanMailException(EthanMallExceptionEnum.CART_CHECK_NULL);
        }
        //判断商品是否存在、上下架状态、库存
        validSaleStatusAndStock(cartVOList);
        //把购物车对象转为订单item对象
        List<OrderItem> orderItems = cartVOListOrderItemList(cartVOList);
        //扣库存
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem =  orderItems.get(i);
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock = product.getStock()-orderItem.getQuantity();
            if(stock < 0){
                throw new EthanMailException(EthanMallExceptionEnum.CART_NOT_STOCK);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }
        //把购物车中的已勾选商品删除
        cleanCart(cartVOList);
        //生成订单
        Order order = new Order();
        //生成订单号，有独立的规则
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItems));
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        orderMapper.insertSelective(order);
        //循环保存每个商品到order_item表
        for(int i = 0 ; i < orderItems.size();i++){
            OrderItem orderItem = orderItems.get(i);
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }
        //把结果返回
        return orderNo;
    }

    private Integer totalPrice(List<OrderItem> orderItems) {
        int totalPrice = 0;
        for (int i = 0; i < orderItems.size() ; i++) {
            totalPrice+=orderItems.get(i).getTotalPrice();
        }
        return totalPrice;
    }

    private void cleanCart(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size() ; i++) {
            CartVO cartVO = cartVOList.get(i);
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }


    private List<OrderItem> cartVOListOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            //记录商品快照信息
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            if(product==null || !product.getStatus().equals(1)){
                throw new EthanMailException(EthanMallExceptionEnum.CART_NOT_SELL);
            }
            if(cartVO.getQuantity() > product.getStock()){
                throw new EthanMailException(EthanMallExceptionEnum.CART_NOT_STOCK);
            }
        }
    }

    @Override
    public OrderVO detail(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new EthanMailException(EthanMallExceptionEnum.NO_ORDER);
        }
        Integer userId = UserFilter.currentUser.getId();
        if(!userId.equals(order.getUserId())){
            throw new EthanMailException(EthanMallExceptionEnum.NO_YOU_ORDER);
        }
        OrderVO orderVO = getOrderVO(order);
        return orderVO;
    }

    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        //获取订单对应的orderItemVOList
        List<OrderItemVO> orderItemVOS = new ArrayList<>();
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(orderVO.getOrderNo());
        for (int i = 0; i < orderItems.size() ; i++) {
            OrderItemVO orderItemVO = new OrderItemVO();
            OrderItem orderItem = orderItems.get(i);
            BeanUtils.copyProperties(orderItem,orderItemVO);
            orderItemVOS.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOS);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }

    @Override
    public PageInfo listForCustomer(Integer pageNum,Integer pageSize){
        Integer userId = UserFilter.currentUser.getId();
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        //这里之所以用的是new PageInfo<>(orderList)，是因为
        //在创建pageInfo对象时，应该传入通过mapper查询出来的数据，也就是orderList。
        //PageInfo中存储的是分页的信息，PageInfo会根据传入的list设置pageNum，pageSize等值，
        //所以PageInfo应该传入通过mapper直接查询出来的数据，也就是orders，而不能是经过修改的OrderVO。
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            OrderVO orderVO = getOrderVO(order);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    @Override
    public void cancel(String orderNo){
        Integer userId = UserFilter.currentUser.getId();
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new EthanMailException(EthanMallExceptionEnum.NO_ORDER);
        }
        if(!order.getUserId().equals(userId)){
            throw new EthanMailException(EthanMallExceptionEnum.NO_YOU_ORDER);
        }
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new EthanMailException(EthanMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public String qrcode(String orderNo){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String address = uri;
        String payUrl = "http://"+address+"/pay?orderNo="+orderNo;
        try {
            QRCodeGenerator.generateQRCodeImage(payUrl,350,350, Constant.FILE_UPLOAD_DIR+orderNo+".png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pngAddress = "http://"+address+"/images/"+orderNo+".png";
        return pngAddress;
    }

    @Override
    public void pay(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new EthanMailException(EthanMallExceptionEnum.NO_ORDER);
        }
        //只有订单为未付款状态，才能进行付款
        if(order.getOrderStatus() == Constant.OrderStatusEnum.NOT_PAID.getCode()){
            order.setOrderStatus(Constant.OrderStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new EthanMailException(EthanMallExceptionEnum.PAY_WRONG_ORDER_STATUS);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectAllForAdmin();
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        //这里之所以用的是new PageInfo<>(orderList)，是因为
        //在创建pageInfo对象时，应该传入通过mapper查询出来的数据，也就是orderList。
        //PageInfo中存储的是分页的信息，PageInfo会根据传入的list设置pageNum，pageSize等值，
        //所以PageInfo应该传入通过mapper直接查询出来的数据，也就是orders，而不能是经过修改的OrderVO。
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    @Override
    public void delivered(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new EthanMailException(EthanMallExceptionEnum.NO_ORDER);
        }
        if(order.getOrderStatus() == Constant.OrderStatusEnum.PAID.getCode()){
            order.setOrderStatus(Constant.OrderStatusEnum.DELIVERED.getCode());
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new EthanMailException(EthanMallExceptionEnum.DELIVER_WRONG_ORDER_STATUS);
        }
    }

    @Override
    public void finish(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new EthanMailException(EthanMallExceptionEnum.NO_ORDER);
        }
        Integer userId = UserFilter.currentUser.getId();
        if(!UserFilter.currentUser.getRole().equals(2) && !order.getUserId().equals(UserFilter.currentUser.getId())){
            throw new EthanMailException(EthanMallExceptionEnum.NO_YOU_ORDER);
        }
        if(order.getOrderStatus() == Constant.OrderStatusEnum.DELIVERED.getCode()){
            order.setOrderStatus(Constant.OrderStatusEnum.FINISHED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new EthanMailException(EthanMallExceptionEnum.FINISH_WRONG_ORDER_STATUS);
        }
    }

}
