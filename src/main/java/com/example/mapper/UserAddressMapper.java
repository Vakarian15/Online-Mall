package com.example.mapper;

import com.example.entity.UserAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    int setDefault(Integer id);
}
