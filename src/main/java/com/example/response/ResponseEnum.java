package com.example.response;

public enum ResponseEnum {
    USER_INFO_NULL(300,"User Info cannot be null"),
    EMAIL_ERROR(301,"Email Format Error"),
    MOBILE_ERROR(302,"Phone Number Format Error"),
    USERNAME_EXISTS(303,"User Already Exists"),
    USER_REGISTER_ERROR(304,"Registration failed"),
    USERNAME_NOT_EXISTS(305,"User Not Exists"),
    PASSWORD_ERROR(306,"Password Error"),
    PARAMETER_NULL(307,"Parameter Null Error"),
    NOT_LOGIN(308,"Not Login"),
    CART_ADD_ERROR(309,"Adding To Cart Failed"),
    PRODUCT_NOT_EXISTS(310,"Product Not Exists"),
    PRODUCT_STOCK_ERROR(311,"Product Stock Error"),
    CART_UPDATE_ERROR(312,"Updating Cart Failed"),
    CART_UPDATE_PARAMETER_ERROR(313,"Updating Cart Parameter Error"),
    CART_UPDATE_STOCK_ERROR(314,"Updating Cart Stock Error"),
    CART_REMOVE_ERROR(315,"Cart-removing Error"),
    ORDERS_CREATE_ERROR(316,"Orders-creating Error"),
    ORDER_DETAIL_CREATE_ERROR(317,"Order-detail-creating Error"),
    USER_ADDRESS_ADD_ERROR(318,"Adding Address Failed"),
    USER_ADDRESS_SET_DEFAULT_ERROR(319,"Setting Default Address Failed");

    private final Integer code;
    private final String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
