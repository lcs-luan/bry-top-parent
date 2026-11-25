package com.bry.framework.base.exception.common;

/**
 * 业务异常<br>
 * 不符合业务预期的异常（如某个list不能为空，但查询后返回了null，抛出异常）
 *
 * @author: LuoCS
 * @date: create in 2020/11/3 18:27
 */
public class BusinessException extends RuntimeException {
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


    public BusinessException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(int errorCode, String errorMsg, Throwable t) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.throwable = t;
    }

    public BusinessException(int errorCode, String errorMsg, Object requestParam) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.requestParam = requestParam;
    }

    public BusinessException(int errorCode, String errorMsg, Throwable t, Object requestParam) {
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
