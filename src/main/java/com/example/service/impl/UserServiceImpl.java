package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.form.UserLoginForm;
import com.example.utils.MD5Util;
import com.example.entity.User;
import com.example.form.UserRegisterForm;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.RegexValidateUtil;
import com.example.exception.MallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.response.ResponseEnum;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User register(UserRegisterForm userRegisterForm) {
        //CheckLogin Name
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("login_name",userRegisterForm.getLoginName());
        User one=userMapper.selectOne(queryWrapper);
        if (one!=null){
            log.info("User Already Exists");
            throw new MallException(ResponseEnum.USERNAME_EXISTS);
        }
        //Check Email
        if (!RegexValidateUtil.checkEmail((userRegisterForm.getEmail()))){
            log.info("Email Format Error");
            throw new MallException(ResponseEnum.EMAIL_ERROR);
        }
        //Check Mobile Number
        if (!RegexValidateUtil.checkMobile((userRegisterForm.getMobile()))){
            log.info("Phone Number Format Error");
        }
        //Store in Database
        User user=new User();
        BeanUtils.copyProperties(userRegisterForm,user);
        user.setPassword(MD5Util.getSaltMD5(userRegisterForm.getPassword()));
        int insert = userMapper.insert(user);
        if (insert!=1){
            log.info("Registration failed");
            throw new MallException(ResponseEnum.USER_REGISTER_ERROR);
        }
        return user;
    }

    @Override
    public User login(UserLoginForm userLoginForm) {
        //CheckLogin Name
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("login_name",userLoginForm.getLoginName());
        User one=userMapper.selectOne(queryWrapper);
        if (one==null){
            log.info("User Not Exists");
            throw new MallException(ResponseEnum.USERNAME_NOT_EXISTS);
        }
        //Check Password
        if (!MD5Util.getSaltverifyMD5(userLoginForm.getPassword(),one.getPassword())){
            log.info("Password Error");
            throw new MallException(ResponseEnum.PASSWORD_ERROR);
        }
        return one;
    }
}
