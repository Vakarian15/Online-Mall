package com.example.mapper;

import com.example.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
public interface CartMapper extends BaseMapper<Cart> {
    int update(Integer id,Integer quantity,Float cost);
    Float getCostByUserId(Integer id);
}
