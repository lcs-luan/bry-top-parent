package com.bry.framework.base.exception.common;

/**
 * 系统异常<br>
 * 通常是处理try-catch捕获到的运行时异常
 *
 * @author: lcs
 * @Description:
 * @Date: create in 2020/11/3 18:27
 */
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = -5946727112662314240L;

    /**
     * 编码
     */
    private final int errorCode;

    /**
     * 错误信息
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

    public SystemException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public SystemException(int errorCode, String errorMsg, Throwable t) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.throwable = t;
    }

    public SystemException(int errorCode, String errorMsg, Object requestParam) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.requestParam = requestParam;
    }

    public SystemException(int errorCode, String errorMsg, Throwable t, Object requestParam) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.throwable = t;
        this.requestParam = requestParam;
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