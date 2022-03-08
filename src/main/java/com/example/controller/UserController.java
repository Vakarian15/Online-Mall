package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.User;
import com.example.entity.UserAddress;
import com.example.form.UserLoginForm;
import com.example.form.UserRegisterForm;
import com.example.service.CartService;
import com.example.service.OrdersService;
import com.example.service.UserAddressService;
import com.example.service.UserService;
import com.example.exception.MallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import com.example.response.ResponseEnum;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;
    /**
     * Register
     * @param userRegisterForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterForm userRegisterForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.info("User info cannot be empty");
            throw new MallException(ResponseEnum.USER_INFO_NULL);
        }
        User register = userService.register(userRegisterForm);
        if (register==null){
            log.info("Registration failed");
            throw new MallException(ResponseEnum.USER_REGISTER_ERROR);
        }
        return "redirect:/login";
    }

    /**
     * Login
     * @param bindingResult
     * @param httpSession
     * @return
     */
    @PostMapping("/login")
    public String login(@Valid UserLoginForm userLoginForm, BindingResult bindingResult, HttpSession httpSession){
        if (bindingResult.hasErrors()){
            log.info("User info cannot be empty");
            throw new MallException(ResponseEnum.USER_INFO_NULL);
        }
        User user=userService.login(userLoginForm);
        httpSession.setAttribute("user",user);
        return "redirect:/productCategory/main";
    }

    @GetMapping("/orderList")
    public ModelAndView ordersList(HttpSession session){
        User user=(User)session.getAttribute("user");
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("orderList");
        ordersService.findAllByUserId(user.getId());
        modelAndView.addObject("orderList", ordersService.findAllByUserId(user.getId()));
        modelAndView.addObject("cartList",cartService.findVOListByUserId(user.getId()));
        return modelAndView;
    }

    @GetMapping("/addressList")
    public ModelAndView addressList(HttpSession session){
        User user=(User)session.getAttribute("user");
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("userAddressList");
        modelAndView.addObject("addressList",userAddressService.list(new QueryWrapper<UserAddress>().eq("user_id",user.getId())));
        modelAndView.addObject("cartList",cartService.findVOListByUserId(user.getId()));
        return modelAndView;
    }
}

