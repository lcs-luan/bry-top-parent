package com.bry.common.sentinel.enums;


/**
 * 限流熔断异常code
 *
 * @author: liu
 * @Description:
 */
public enum SentinelErrorCodeEnum {

    FLOW_ERROR(010, "请求被限流"),
    DEGRADE_ERROR(011, "请求被熔断降级"),
    PARAM_FLOW_ERROR(012, "请求被热点限流"),
    SYSTEM_BLOCK_ERROR(013, "请求被系统规则限制"),
    AUTHORITY_ERROR(014, "请求授权规则不通过"),
    ;

    private int code;
    private String detail;

    private SentinelErrorCodeEnum(int code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return detail;
    }
}
