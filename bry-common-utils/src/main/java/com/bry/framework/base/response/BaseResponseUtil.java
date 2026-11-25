package com.bry.framework.base.response;

import com.bry.framework.base.code.ErrorCodeInterface;
import com.bry.framework.base.enums.BaseErrorCodeEnum;

/**
 * 组装返回体 BaseResponseBody
 *
 * @author: lcs
 * @Description:
 * @Date: create in 2020/11/3 18:30
 */
public class BaseResponseUtil {

    /**
     * 处理普通业务异常[100-999的异常]
     */
    public static <T> BaseResponseBody<T> resultError(int errCode, String desc) {
        BaseResponseBody<T> ret = new BaseResponseBody<>();
        ret.setSuccess(false);
        ret.setFullErrorCode(errCode);
        ret.setMessage(desc);
        return ret;
    }

    /**
     * 处理普通业务异常[100-999的异常]
     */
    public static <T> BaseResponseBody<T> resultError(int errCode, String desc, T data) {
        BaseResponseBody<T> ret = resultError(errCode, desc);
        ret.setBody(data);
        return ret;
    }

    /**
     * 处理特殊异常 40101，注意：返回该异常时，App和PC端用户会退出登录
     */
    public static <T> BaseResponseBody<T> resultAuthError() {
        BaseResponseBody<T> ret = new BaseResponseBody<>(BaseErrorCodeEnum.AUTH_FAIL);
        ret.setSuccess(false);
        return ret;
    }

    /**
     * 处理公共异常[0-99的异常]
     */
    public static <T> BaseResponseBody<T> resultBaseError(BaseErrorCodeEnum baseError) {
        BaseResponseBody<T> ret = new BaseResponseBody<>(baseError);
        ret.setSuccess(false);
        return ret;
    }

    /**
     * 处理普通业务异常[100-999的异常]
     */
    public static <T> BaseResponseBody<T> resultBusinessError(ErrorCodeInterface errorCodeInterface) {
        BaseResponseBody<T> ret = new BaseResponseBody<>(errorCodeInterface);
        ret.setSuccess(false);
        return ret;
    }

    /**
     * 成功，返回码为0
     */
    public static <T> BaseResponseBody<T> resultSuccess() {
        BaseResponseBody<T> ret = new BaseResponseBody<>(BaseErrorCodeEnum.SUCCESS);
        ret.setSuccess(true);
        return ret;
    }

    /**
     * 成功，返回码为0
     */
    public static <T> BaseResponseBody<T> resultSuccess(T body) {
        BaseResponseBody<T> ret = resultSuccess();
        ret.setBody(body);
        return ret;
    }

    /**
     * 成功，返回码为0
     */
    public static <T> BaseResponseBody<T> resultSuccess(String desc) {
        BaseResponseBody<T> ret = resultSuccess();
        ret.setMessage(desc);
        return ret;
    }

    /**
     * 成功，返回码为0
     */
    public static <T> BaseResponseBody<T> resultSuccess(T body, String desc) {
        BaseResponseBody<T> ret = resultSuccess(body);
        ret.setMessage(desc);
        return ret;
    }

    /**
     * 成功，返回码为200
     */
    public static <T> BaseResponseBody<T> resultSuccess200() {
        BaseResponseBody<T> ret = new BaseResponseBody<>(BaseErrorCodeEnum.SUCCESS_200);
        ret.setSuccess(true);
        return ret;
    }

    /**
     * 成功，返回码为200
     */
    public static <T> BaseResponseBody<T> resultSuccess200(T body) {
        BaseResponseBody<T> ret = resultSuccess200();
        ret.setBody(body);
        return ret;
    }
}