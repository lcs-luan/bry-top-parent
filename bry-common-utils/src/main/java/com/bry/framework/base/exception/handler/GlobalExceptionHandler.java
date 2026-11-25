package com.bry.framework.base.exception.handler;

import com.bry.framework.base.config.NacosBaseCommonConfig;
import com.bry.framework.base.enums.BaseErrorCodeEnum;
import com.bry.framework.base.enums.SystemCodeEnum;
import com.bry.framework.base.exception.common.AuthorizationException;
import com.bry.framework.base.exception.common.BusinessException;
import com.bry.framework.base.exception.common.InvalidCodeException;
import com.bry.framework.base.exception.common.SystemException;
import com.bry.framework.base.response.BaseResponseBody;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 统一异常拦截
 *
 * @author: zeminsong
 * @Description:
 * @Date: create in 2020/11/3 18:27
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final NacosBaseCommonConfig commonConfig;

    public GlobalExceptionHandler(NacosBaseCommonConfig commonConfig) {
        this.commonConfig = commonConfig;
    }

    /**
     * 系统异常：通常是处理try-catch捕获到的运行时异常
     */
    @ExceptionHandler(SystemException.class)
    public BaseResponseBody systemExceptionHandler(SystemException ex) {
        int errorCode = Integer.parseInt(commonConfig.getSystemResponseCode() + ex.getErrorCode());
        if (ex.getThrowable() == null) {
            log.warn("systemException[{}]:{}，参数:{}", ex.getErrorCode(), ex.getMessage(), ex.getRequestParam());
        } else {
            log.error("systemException[{}]:{}，参数:{}", ex.getErrorCode(), ex.getMessage(), ex.getRequestParam(), ex);
        }
        return new BaseResponseBody(errorCode, ex.getMessage());
    }

    /**
     * 业务异常：不符合业务预期的异常（如某个list不能为空，但查询后返回了null，抛出异常）
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public BaseResponseBody businessExceptionHandler(BusinessException ex) {
        int errorCode = Integer.parseInt(commonConfig.getSystemResponseCode() + ex.getErrorCode());
        if (ex.getThrowable() == null) {
            log.warn("BusinessException[{}]:{}，参数:{}", ex.getErrorCode(), ex.getMessage(), ex.getRequestParam());
        } else {
            log.error("BusinessException[{}]:{}，参数:{}", ex.getErrorCode(), ex.getMessage(), ex.getRequestParam(), ex);
        }
        return new BaseResponseBody(errorCode, ex.getMessage());
    }

    /**
     * 授权异常：账号不存在、权限不足、token过期
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public BaseResponseBody authorizationExceptionHandler(BusinessException ex) {
        log.error("AuthorizationException[{}]:{}，参数:{}", ex.getErrorCode(), ex.getMessage(), ex.getRequestParam(), ex);
        //过期的code指定返回code码值
        if (SystemCodeEnum.BRY_USER_CENTER.getStringCode().equals(commonConfig.getSystemResponseCode()) && ex.getErrorCode() == 401) {
            return new BaseResponseBody(401, ex.getMessage());
        }
        int errorCode = Integer.parseInt(commonConfig.getSystemResponseCode() + ex.getErrorCode());
        return new BaseResponseBody(errorCode, ex.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public BaseResponseBody SQLException(SQLException ex) {
        int errorCode = BaseErrorCodeEnum.SQL_ERROR.getFullErrorCode(commonConfig.getSystemResponseCode());
        log.error("[{}]:{}", errorCode, ex.getMessage(), ex);
        return new BaseResponseBody(errorCode, "sql执行异常,详情请查看日志");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponseBody MissingServletRequestParameterHandler(MissingServletRequestParameterException ex) {
        int errorCode = BaseErrorCodeEnum.INVALID_PARAM.getFullErrorCode(commonConfig.getSystemResponseCode());
        //log.error("[{}]:{}", errorCode, ex.getMessage(), ex);
        return new BaseResponseBody(errorCode, "请求参数缺失");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponseBody HttpMessageNotReadableHandler(HttpMessageNotReadableException ex) {
        int errorCode = BaseErrorCodeEnum.INVALID_PARAM.getFullErrorCode(commonConfig.getSystemResponseCode());
        log.error("[{}]:{}", errorCode, ex.getMessage(), ex);
        return new BaseResponseBody(errorCode, "http请求参数转换异常");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponseBody methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        int errorCode = BaseErrorCodeEnum.INVALID_PARAM.getFullErrorCode(commonConfig.getSystemResponseCode());
        log.error("[{}]:{}", errorCode, ex.getBindingResult().getAllErrors(), ex);
        return new BaseResponseBody(errorCode, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponseBody ConstraintViolationException(ConstraintViolationException ex) {
        int errorCode = BaseErrorCodeEnum.INVALID_PARAM.getFullErrorCode(commonConfig.getSystemResponseCode());
        String[] msgArray = ex.getMessage().split(": ");
        log.error("[{}]:{}", errorCode, ex.getMessage(), ex);
        return new BaseResponseBody(errorCode, msgArray[msgArray.length - 1]);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponseBody HttpRequestMethodNotSupportedHandler(HttpRequestMethodNotSupportedException ex) {
        int errorCode = BaseErrorCodeEnum.INVALID_REQUEST_METHOD.getFullErrorCode(commonConfig.getSystemResponseCode());
        //log.error("[{}]:{}", errorCode, ex.getMessage(), ex);
        return new BaseResponseBody(errorCode, "请求方法不支持");
    }

    /**
     * 异常码不符合编码规范
     * 错误码定义：https://wiki.ds1688.com/pages/viewpage.action?pageId=18157038
     */
    @ExceptionHandler(InvalidCodeException.class)
    public BaseResponseBody invalidCodeExceptionHandler(InvalidCodeException ex) {
        int errorCode = BaseErrorCodeEnum.INVALID_CODE.getFullErrorCode(commonConfig.getSystemResponseCode());
        log.error("[{}]:异常码非法", errorCode, ex);
        return new BaseResponseBody(errorCode, ex.getErrorMsg());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponseBody otherExceptionHandler(Exception ex) {
        int errorCode = BaseErrorCodeEnum.SYSTEM_ERROR.getFullErrorCode(commonConfig.getSystemResponseCode());
        log.error("[{}]:系统异常", errorCode, ex);
        return new BaseResponseBody(errorCode, "系统异常，请稍后重试");
    }

}