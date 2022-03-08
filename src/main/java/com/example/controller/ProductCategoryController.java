package com.example.controller;


import com.example.entity.User;
import com.example.service.CartService;
import com.example.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

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
@RequestMapping("/productCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CartService cartService;

    @GetMapping("/main")
    public ModelAndView main(HttpSession httpSession){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("main");
        modelAndView.addObject("list",productCategoryService.buildProductCategoryMenu());
        modelAndView.addObject("levelOneProductList",productCategoryService.getAllProductByCategoryLevelOne());
        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList", new ArrayList<>());
        }
        else{
            modelAndView.addObject("cartList", cartService.findVOListByUserId(user.getId()));
        }
        return modelAndView;
    }
}

