package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Product;
import com.example.mapper.ProductMapper;
import com.example.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<Product> getAllByTypeAndProductCategoryId(Integer type, Integer id) {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        String colum="";
        switch (type){
            case 1:
                colum="categorylevelone_id";
                break;
            case 2:
                colum="categoryleveltwo_id";
                break;
            case 3:
                colum="categorylevelthree_id";
                break;
        }
        queryWrapper.eq(colum,id);
        return productMapper.selectList(queryWrapper);
    }
}
