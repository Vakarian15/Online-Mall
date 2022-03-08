package com.example.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserLoginForm {
    @NotEmpty(message = "LoginName cannot be Empty")
    private String loginName;

    @NotEmpty(message = "UserName cannot be Empty")
    private String password;
}
