package com.example.form;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserRegisterForm {
    @NotEmpty(message = "LoginName cannot be Empty")
    private String loginName;
    @NotEmpty(message = "UserName cannot be Empty")
    private String userName;
    @NotEmpty(message = "UserName cannot be Empty")
    private String password;
    @NotNull(message = "Gender cannot be null")
    private Integer gender;
    @NotEmpty(message = "UserName cannot be Empty")
    private String email;
    @NotEmpty(message = "UserName cannot be Empty")
    private String mobile;
}
