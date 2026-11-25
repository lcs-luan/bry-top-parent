package com.bry.framework.base.exception.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 功能描述： 包装异常发生时的请求参数<br/>
 * 异常发生时的请求参数
 *
 * @author LuoCS
 * @date 2024/4/19 0019
 */
@Data
@AllArgsConstructor
public class ExceptionParameter {
    private String ucId;
    private String loginType;
    private String platformType;
    private Object data;

    public ExceptionParameter(Object data) {
        this.data = data;
    }

    public ExceptionParameter(String ucId, String loginType) {
        this.ucId = ucId;
        this.loginType = loginType;
    }

    public ExceptionParameter(String ucId, String loginType, String platformType) {
        this.ucId = ucId;
        this.loginType = loginType;
        this.platformType = platformType;
    }

    public ExceptionParameter(String ucId, String loginType, Object data) {
        this.ucId = ucId;
        this.loginType = loginType;
        this.data = data;
    }

    public static ExceptionParameter create(String ucId, String loginType) {
        return new ExceptionParameter(ucId, loginType);
    }

    public static ExceptionParameter create(String ucId, String loginType, String platformType) {
        return new ExceptionParameter(ucId, loginType, platformType);
    }

    public static ExceptionParameter create(String ucId, String loginType, Object data) {
        return new ExceptionParameter(ucId, loginType, data);
    }

    public static ExceptionParameter create(String ucId, String loginType, String platformType, Object data) {
        return new ExceptionParameter(ucId, loginType, platformType, data);
    }
}
