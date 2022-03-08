package com.example.service;

import com.example.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.ProductCategoryVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
public interface ProductCategoryService extends IService<ProductCategory> {
    List<ProductCategoryVO> buildProductCategoryMenu();
    public List<ProductCategoryVO> getAllProductByCategoryLevelOne();
}
