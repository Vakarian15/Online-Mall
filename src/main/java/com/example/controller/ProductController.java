package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.service.CartService;
import com.example.service.ProductCategoryService;
import com.example.service.ProductService;
import com.example.exception.MallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import com.example.response.ResponseEnum;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CartService cartService;

    @GetMapping("/list/{type}/{id}")
    public ModelAndView list(@PathVariable("type") Integer type, @PathVariable("id") Integer productCategoryId, HttpSession httpSession){
        if (type==null||productCategoryId==null){
            log.info("Parameter Null Error");
            throw new MallException(ResponseEnum.PARAMETER_NULL);
        }
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("productList");
        modelAndView.addObject("productList",productService.getAllByTypeAndProductCategoryId(type,productCategoryId));
        modelAndView.addObject("list",productCategoryService.buildProductCategoryMenu());

        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList", new ArrayList<>());
        }
        else{
            modelAndView.addObject("cartList", cartService.findVOListByUserId(user.getId()));
        }
        return modelAndView;
    }

    @PostMapping("/search")
    public ModelAndView search(String keyWord,HttpSession session) {
        if (keyWord == null) {
            log.info("Parameter Null Error");
            throw new MallException(ResponseEnum.PARAMETER_NULL);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productList");
        //Search
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyWord);
        modelAndView.addObject("productList", productService.list(queryWrapper));

        User user = (User) session.getAttribute("user");
        if (user == null) {
            modelAndView.addObject("cartList", new ArrayList<>());
        } else {
            modelAndView.addObject("cartList", cartService.findVOListByUserId(user.getId()));
        }

        modelAndView.addObject("list", productCategoryService.buildProductCategoryMenu());
        return modelAndView;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable("id") Integer id,HttpSession session){
        if(id == null){
            log.info("Parameter Null Error");
            throw new MallException(ResponseEnum.PARAMETER_NULL);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productDetail");

        User user = (User) session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList", new ArrayList<>());
        }else{
            modelAndView.addObject("cartList", cartService.findVOListByUserId(user.getId()));
        }

        modelAndView.addObject("list", productCategoryService.buildProductCategoryMenu());
        modelAndView.addObject("product", productService.getById(id));
        return modelAndView;
    }

}

