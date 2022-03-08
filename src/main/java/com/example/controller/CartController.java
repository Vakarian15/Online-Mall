package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Cart;
import com.example.entity.Orders;
import com.example.entity.User;
import com.example.entity.UserAddress;
import com.example.service.CartService;
import com.example.service.UserAddressService;
import com.example.exception.MallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import com.example.response.ResponseEnum;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
@Controller
@RequestMapping("/cart")
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;

    @GetMapping("/add/{productId}/{price}/{quantity}")
    public String add(@PathVariable("productId") Integer productId, @PathVariable("price") Float price, @PathVariable("quantity") Integer quantity, HttpSession session){
        if(productId == null || price == null || quantity == null){
            log.info("Parameter Null Error");
            throw new MallException(ResponseEnum.PARAMETER_NULL);
        }
        User user = (User) session.getAttribute("user");
        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setCost(price * quantity);
        Boolean add = cartService.add(cart);
        if(!add){
            log.info("Adding To Cart Failed");
            throw new MallException(ResponseEnum.CART_ADD_ERROR);
        }
        return "redirect:/cart/get";
    }

    @GetMapping("/get")
    public ModelAndView get(HttpSession httpSession){
        User user=(User) httpSession.getAttribute("user");
        if (user==null){
            log.info("Not Login");
            throw new MallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("settlement1");
        modelAndView.addObject("cartList",cartService.findVOListByUserId(user.getId()));
        return modelAndView;
    }

    @PostMapping("/update/{id}/{quantity}/{cost}")
    @ResponseBody
    public String update(
            @PathVariable("id") Integer id,
            @PathVariable("quantity") Integer quantity,
            @PathVariable("cost") Float cost,
            HttpSession session
    ){
        if(id == null || quantity == null || cost == null){
            log.info("Updating Cart Parameter Error");
            throw new MallException(ResponseEnum.CART_UPDATE_PARAMETER_ERROR);
        }

        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("Not Login");
            throw new MallException(ResponseEnum.NOT_LOGIN);
        }
        return cartService.update(id, quantity, cost)?"success":"fail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id,HttpSession session){
        if(id == null){
            log.info("Updating Cart Parameter Error");
            throw new MallException(ResponseEnum.CART_UPDATE_PARAMETER_ERROR);
        }

        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("Not Login");
            throw new MallException(ResponseEnum.NOT_LOGIN);
        }
        Boolean delete = cartService.delete(id);
        return delete?"redirect:/cart/get":null;
    }

    @GetMapping("/confirm")
    public ModelAndView confirm(HttpSession session){
        User user=(User)session.getAttribute("user");
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("settlement2");
        modelAndView.addObject("cartList",cartService.findVOListByUserId(user.getId()));
        QueryWrapper<UserAddress> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        modelAndView.addObject("addressList",userAddressService.list(queryWrapper));
        return modelAndView;
    }

    @PostMapping("/commit")
    public ModelAndView commit(
            String userAddress,
            String address,
            String remark,
            HttpSession session){
        if(userAddress == null || address == null || remark == null){
            log.info("Parameter Null Error");
            throw new MallException(ResponseEnum.PARAMETER_NULL);
        }

        User user=(User)session.getAttribute("user");
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("settlement3");
        Orders orders=cartService.commit(userAddress,address,remark,user);
        if (orders!=null){
            modelAndView.addObject("orders",orders);
            modelAndView.addObject("cartList",cartService.findVOListByUserId(user.getId()));
            return modelAndView;
        }
        return null;
    }
}

