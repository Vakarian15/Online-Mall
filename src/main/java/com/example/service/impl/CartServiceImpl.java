package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.*;
import com.example.exception.MallException;
import com.example.service.CartService;
import com.example.mapper.*;
import com.example.response.ResponseEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
@Service
@Slf4j
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    @Transactional
    public Boolean add(Cart cart) {

        int insert = cartMapper.insert(cart);
        if(insert != 1){
            throw new MallException(ResponseEnum.CART_ADD_ERROR);
        }
        //Decrease Stock
        Integer stock = productMapper.getStockById(cart.getProductId());
        if(stock == null){
            log.info("Product Not Exists");
            throw new MallException(ResponseEnum.PRODUCT_NOT_EXISTS);
        }

        Integer newStock = stock - cart.getQuantity();
        if(newStock < 0){
            log.info("Product Stock Error");
            throw new MallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        this.productMapper.updateStockById(cart.getProductId(), newStock);
        return true;
    }

    @Override
    public List<CartVO> findVOListByUserId(Integer userId) {
        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<Cart> cartList=cartMapper.selectList(queryWrapper);
        List<CartVO> cartVOList=new ArrayList<>();
        for (Cart cart:cartList){
            Product product=productMapper.selectById(cart.getProductId());
            CartVO cartVO=new CartVO();
            BeanUtils.copyProperties(product,cartVO);
            BeanUtils.copyProperties(cart,cartVO);
            cartVOList.add(cartVO);
        }
        return cartVOList;
    }

    @Override
    @Transactional
    public Boolean update(Integer id, Integer quantity, Float cost) {
        Cart cart=cartMapper.selectById(id);
        Integer oldQuantity=cart.getQuantity();
        if (quantity.equals(oldQuantity)){
            log.info("Updating Cart Parameter Error");
            throw new MallException(ResponseEnum.CART_UPDATE_PARAMETER_ERROR);
        }

        Integer stock=productMapper.getStockById(cart.getProductId());
        Integer newStock=stock-(quantity-oldQuantity);
        if (newStock<0){
            log.info("Product Stock Error");
            throw new MallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        int i=productMapper.updateStockById(cart.getProductId(),newStock);
        if (i!=1){
            log.info("Updating Cart Stock Error");
            throw new MallException(ResponseEnum.CART_UPDATE_STOCK_ERROR);
        }
        int update=cartMapper.update(id,quantity,cost);
        if (update!=1){
            log.info("Updating Cart Failed");
            throw new MallException(ResponseEnum.CART_UPDATE_ERROR);
        }

        return true;
    }

    @Override
    @Transactional
    public Boolean delete(Integer id) {
        Cart cart=cartMapper.selectById(id);
        Integer stock=productMapper.getStockById(cart.getProductId());
        Integer newStock=stock+cart.getQuantity();
        int i=productMapper.updateStockById(cart.getProductId(),newStock);
        if (i!=1){
            log.info("Updating Cart Stock Error");
            throw new MallException(ResponseEnum.CART_UPDATE_STOCK_ERROR);
        }
        int d=cartMapper.deleteById(id);
        if (d!=1){
            log.info("Cart-removing Error");
            throw new MallException(ResponseEnum.CART_REMOVE_ERROR);
        }
        return true;
    }

    @Override
    @Transactional
    public Orders commit(String userAddress, String address, String remark, User user) {
        //Handle Address
        if (!userAddress.equals("newAddress")){
            address=userAddress;
        }
        else {
            int i=userAddressMapper.setDefault(user.getId());
            if (i==0){
                log.info("Setting Default Address Failed");
                throw new MallException(ResponseEnum.USER_ADDRESS_SET_DEFAULT_ERROR);
            }

            UserAddress newAddress=new UserAddress();
            newAddress.setIsdefault(1);
            newAddress.setUserId(user.getId());
            newAddress.setRemark(remark);
            newAddress.setAddress(address);
            int insert=userAddressMapper.insert(newAddress);
            if (insert==0){
                log.info("Adding Address Failed");
                throw new MallException(ResponseEnum.USER_ADDRESS_ADD_ERROR);
            }
        }
        //Create Order Record
        Orders orders=new Orders();
        orders.setUserId(user.getId());
        orders.setLoginName(user.getLoginName());
        orders.setUserAddress(address);
        orders.setCost(cartMapper.getCostByUserId(user.getId()));
        String seriaNumber = null;
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<32;i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            seriaNumber =  result.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        orders.setSerialnumber(seriaNumber);
        int insert=ordersMapper.insert(orders);
        if (insert!=1){
            log.info("Orders-creating Error");
            throw new MallException(ResponseEnum.ORDERS_CREATE_ERROR);
        }
        //Create Order Detail Record
        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<Cart> cartList=cartMapper.selectList(queryWrapper);
        for (Cart cart:cartList){
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            int insertOD=orderDetailMapper.insert(orderDetail);
            if (insertOD==0){
                log.info("Order-detail-creating Error");
                throw new MallException(ResponseEnum.ORDER_DETAIL_CREATE_ERROR);
            }
        }
        //Clear Cart
        int delete=cartMapper.delete(queryWrapper);
        if (delete==0){
            log.info("Cart-removing Error");
            throw new MallException(ResponseEnum.CART_REMOVE_ERROR);
        }
        return orders;
    }
}
