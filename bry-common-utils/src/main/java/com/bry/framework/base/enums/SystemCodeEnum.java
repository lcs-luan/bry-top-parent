package com.bry.framework.base.enums;

/**
 * 全局系统码
 */
public enum SystemCodeEnum {
    BRY_USER_CENTER(100, "用户中心"),
    BRY_ORDER_PLATFORM(101, "订单系统"),
    BRY_PAYMENT_CENTER(102, "支付系统"),
    BRY_PLATFORM_MESSAGE(103, "消息系统"),

    ;

    private int code;
    private String detail;

    private SystemCodeEnum(int code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public Integer getCode() {
        return code;
    }

    public String getStringCode() {
        return String.valueOf(code);
    }

    public String getDesc() {
        return detail;
    }
}
