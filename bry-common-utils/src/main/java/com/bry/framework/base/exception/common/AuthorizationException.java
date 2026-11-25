package com.bry.framework.base.exception.common;

/**
 * 特殊的授权异常<br>
 * 产生该异常码时，前端会退出登录
 *
 * @author: LuoCS
 * @date: create in 2020/11/3 18:27
 */
public class AuthorizationException extends RuntimeException {
    private static final long serialVersionUID = -5946727112662314241L;

    /**
     * 异常编码
     */
    private final int errorCode;

    /**
     * 异常描述
     */
    private final String errorMsg;

    /**
     * 异常内容
     */
    private Throwable throwable;

    /**
     * 记录异常时的请求参数
     */
    private Object requestParam;


    public AuthorizationException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public AuthorizationException(int errorCode, String errorMsg, Throwable t) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.throwable = t;
    }

    public AuthorizationException(int errorCode, String errorMsg, Object requestParam) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.requestParam = requestParam;
    }

    public AuthorizationException(int errorCode, String errorMsg, Throwable t, Object requestParam) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.requestParam = requestParam;
        this.throwable = t;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Object getRequestParam() {
        return requestParam;
    }

}
