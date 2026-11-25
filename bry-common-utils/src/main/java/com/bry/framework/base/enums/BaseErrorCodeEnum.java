package com.bry.framework.base.enums;

import com.bry.framework.base.code.ErrorCodeInterface;

/**
 * 功能：公用基础返回码 <br/>
 * 描述：系统返回码规范：https://wiki.ds1688.com/pages/viewpage.action?pageId=18157038
 *
 * @author LuoCS
 * @date 2022/4/14 0014
 */
public enum BaseErrorCodeEnum implements ErrorCodeInterface {

    SUCCESS("0", "操作成功"),
    SUCCESS_200("200", "操作成功"),
    AUTH_FAIL("40101", "没有操作权限"),


    INVALID_CODE("001", "异常码非法"),

    INVALID_PARAM("002", "请求参数非法"),

    INVALID_HEADER("003", "请求头非法"),

    INVALID_REQUEST_METHOD("004", "请求方法不支持"),

    INVALID_SIGN("005", "签名验证失败"),

    REPEAT_COMMIT("006", "接口不能重复提交"),


    NO_USER_ERROR("007", "用户未登录或不存在"),


    NO_PERMISSION("008", "操作权限不足"),


    SQL_ERROR("009", "SQL异常"),

    /**
     * 099-系统异常(共用异常最大值)
     */
    SYSTEM_ERROR("099", "系统异常，请稍后重试"),

    ;


    String errorCode;

    String errorMsg;


    BaseErrorCodeEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return Integer.parseInt(errorCode);
    }

    @Override
    public String getErrorCodeStr() {
        return errorCode;
    }

    /**
     * 代码系统码的完整错误码
     *
     * @param systemCode
     * @return int
     */
    public int getFullErrorCode(String systemCode) {
        return Integer.parseInt(systemCode + errorCode);
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 是否需要拼接系统码（系统码+异常码）
     *
     * @param
     * @return boolean
     */
    public boolean needCombineCode() {
        if (this == SUCCESS || this == SUCCESS_200 || this == AUTH_FAIL) {
            return false;
        }
        return true;
    }

}
