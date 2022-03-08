package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Product;
import com.example.entity.ProductCategory;
import com.example.mapper.ProductCategoryMapper;
import com.example.mapper.ProductMapper;
import com.example.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vo.ProductCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<ProductCategoryVO> buildProductCategoryMenu() {
        List<ProductCategory> productCategoryList=productCategoryMapper.selectList(null);
        List<ProductCategoryVO> productCategoryVOList = productCategoryList.stream().map(ProductCategoryVO::new).collect(Collectors.toList());
        List<ProductCategoryVO> levelOneList = buildMenu(productCategoryVOList);
        return levelOneList;
    }

    @Override
    public List<ProductCategoryVO> getAllProductByCategoryLevelOne() {
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 1);
        List<ProductCategory> productCategoryList = productCategoryMapper.selectList(queryWrapper);
        List<ProductCategoryVO> productCategoryVOList = productCategoryList.stream().map(ProductCategoryVO::new).collect(Collectors.toList());
        getLevelOneProduct(productCategoryVOList);
        return productCategoryVOList;
    }

    private void getLevelOneProduct(List<ProductCategoryVO> list){
        for (ProductCategoryVO vo:list){
            QueryWrapper<Product> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("categorylevelone_id",vo.getId());
            List<Product> productList=productMapper.selectList(queryWrapper);
            vo.setProductList(productList);
        }
    }

    private List<ProductCategoryVO> buildMenu(List<ProductCategoryVO> voList){
        List<ProductCategoryVO> levelOneList=voList.stream().filter(c->c.getParentId()==0).collect(Collectors.toList());
        for (ProductCategoryVO vo:levelOneList) {
            recurSetChildren(voList,vo);
        }
        return levelOneList;
    }

    private void recurSetChildren(List<ProductCategoryVO> voList,ProductCategoryVO vo){
        List<ProductCategoryVO> children=getChildren(voList,vo);
        vo.setChildren(children);
        if (children.size()>0){
            for(ProductCategoryVO c:children){
                recurSetChildren(voList,c);
            }
        }
    }

    private List<ProductCategoryVO> getChildren(List<ProductCategoryVO> voList,ProductCategoryVO vo){
        List<ProductCategoryVO> children=new ArrayList<>();
        for(ProductCategoryVO c:voList){
            if(c.getParentId().equals(vo.getId())){
                children.add(c);
            }
        }
        return children;
    }
}
