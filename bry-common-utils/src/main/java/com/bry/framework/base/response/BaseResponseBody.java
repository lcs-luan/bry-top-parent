package com.bry.framework.base.response;

import com.bry.framework.base.code.ErrorCodeInterface;
import com.bry.framework.base.config.SystemCodeConts;
import com.bry.framework.base.enums.BaseErrorCodeEnum;
import com.bry.framework.base.exception.common.InvalidCodeException;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 通用返回体，对应老版本是ServiceResult实体
 * 对应的工具类为 BaseResponseUtil
 *
 * @author: lcs
 * @Date: create in 2020/11/3 18:30
 * @see BaseResponseUtil
 */
@Schema(description = "通用返回体，对应老版本是ServiceResult实体")
public class BaseResponseBody<T> implements Serializable {
    private static final long serialVersionUID = 7812556364757470645L;

    /**
     * 返回码，当返回成功时errorCode=0
     * 当返回异常时，采用系统码和业务组合产生的码，产生一个6位数的异常码
     * 系统码通过在配置文件中配置 system.response.code，@see NacosBaseCommonConfig
     */
    @Schema(description = "系统码+业务码", requiredMode = Schema.RequiredMode.REQUIRED)
    private int errorCode;

    @Schema(description = "返回描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message = "操作成功";

    @Schema(description = "是否成功", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean success = true;

    @Schema(description = "返回内容")
    private T body;


    public BaseResponseBody() {
        this.errorCode = 0;
    }

    /**
     * 处理公共异常[0-99的异常]
     */
    public BaseResponseBody(BaseErrorCodeEnum errorCodeEnum) {
        if (errorCode < ErrorCodeInterface.BASE_MIN || errorCode > ErrorCodeInterface.BASE_MAX) {
            throw new InvalidCodeException(BaseErrorCodeEnum.INVALID_CODE.getErrorCode(), BaseErrorCodeEnum.INVALID_CODE.getErrorMsg());
        }
        if (!errorCodeEnum.needCombineCode()) {
            this.errorCode = errorCodeEnum.getErrorCode();
        } else {
            this.errorCode = Integer.parseInt(SystemCodeConts.getSystemCode() + errorCodeEnum.getErrorCodeStr());
        }
        this.message = errorCodeEnum.getErrorMsg();
    }

    /**
     * 处理普通业务异常[100-999的异常]
     */
    public BaseResponseBody(ErrorCodeInterface errorCodeEnum) {
        this(errorCodeEnum.getErrorCode(), errorCodeEnum.getErrorMsg());
    }

    /**
     * 处理普通业务异常[异常码范围：100-999]
     */
    public BaseResponseBody(int errorCode, String message) {
        if (errorCode < ErrorCodeInterface.BUSS_MIN || errorCode > ErrorCodeInterface.BUSS_MAX) {
            throw new InvalidCodeException(BaseErrorCodeEnum.INVALID_CODE.getErrorCode(), BaseErrorCodeEnum.INVALID_CODE.getErrorMsg());
        }
        this.errorCode = Integer.parseInt(SystemCodeConts.getSystemCode() + String.format("%03d", errorCode));
        this.message = message;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * 处理普通业务异常[异常码范围：100-999]
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setFullErrorCode(int errorCode) {
        if (errorCode < ErrorCodeInterface.BUSS_MIN || errorCode > ErrorCodeInterface.BUSS_MAX) {
            throw new InvalidCodeException(BaseErrorCodeEnum.INVALID_CODE.getErrorCode(), BaseErrorCodeEnum.INVALID_CODE.getErrorMsg());
        }
        this.errorCode = Integer.parseInt(SystemCodeConts.getSystemCode() + String.format("%03d", errorCode));
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return this.body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(60);
        builder.append("{errorCode=").append(errorCode)
                .append(", message='").append(message)
                .append(", success='").append(success)
                .append("', body=").append(body)
                .append('}');
        return builder.toString();
    }

}