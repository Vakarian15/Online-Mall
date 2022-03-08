package com.example.service;

import com.example.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.form.UserLoginForm;
import com.example.form.UserRegisterForm;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2022-03-04
 */
public interface UserService extends IService<User> {
    User register(UserRegisterForm userRegisterForm);
    User login(UserLoginForm userLoginForm);
}
