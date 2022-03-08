package com.example.service;

import com.example.entity.OrderDetail;
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
public interface OrderDetailService extends IService<OrderDetail> {
    List<OrdersVO> findAllByUserId(Integer id);
}
