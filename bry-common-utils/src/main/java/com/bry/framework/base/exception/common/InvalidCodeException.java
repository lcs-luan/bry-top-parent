package com.bry.framework.base.exception.common;

/**
 * 非法错误码<br>
 * 通常是处理try-catch捕获到的运行时异常
 * 错误码定义：https://wiki.ds1688.com/pages/viewpage.action?pageId=18157038
 *
 * @author: lcs
 * @Description:
 * @Date: create in 2020/11/3 18:27
 */
public class InvalidCodeException extends RuntimeException {
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

    public InvalidCodeException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public InvalidCodeException(int errorCode, String errorMsg, Throwable t) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.throwable = t;
    }

    public InvalidCodeException(int errorCode, String errorMsg, Object requestParam) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.requestParam = requestParam;
    }

    public InvalidCodeException(int errorCode, String errorMsg, Throwable t, Object requestParam) {
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