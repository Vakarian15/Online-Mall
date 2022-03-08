package com.example.service;

import com.example.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Orders;
import com.example.entity.User;
import com.example.vo.CartVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
public interface CartService extends IService<Cart> {
    Boolean add(Cart cart);
    List<CartVO> findVOListByUserId(Integer userId);
    Boolean update(Integer id,Integer quantity,Float cost);
    Boolean delete(Integer id);
    Orders commit(String userAddress, String address, String remark, User user);
}
