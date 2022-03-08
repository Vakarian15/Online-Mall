package com.example.service;

import com.example.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.OrdersVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
public interface OrdersService extends IService<Orders> {
    List<OrdersVO> findAllByUserId(Integer id);
}
