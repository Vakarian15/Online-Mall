package com.example.mymall;

import com.example.service.ProductCategoryService;
import com.example.vo.ProductCategoryVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MymallApplicationTests {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Test
    void contextLoads() {
        List<ProductCategoryVO> productCategoryVOList=productCategoryService.buildProductCategoryMenu();
        int i=0;
    }

}
