package com.bry.common.sentinel.exceptions;

import com.bry.common.sentinel.enums.SentinelErrorCodeEnum;
import jakarta.servlet.http.HttpServletResponse;


/**
 * 异常处理类
 *
 * @description
 * @author: liu
 * @create: 2022-09-13 09:43
 **/
public interface SentinelLimitReturnHandler {

    /**
     * 返回错误数据
     *
     * @param response
     * @param sentinelErrorCodeEnum
     */
    void writeErrorResponse(HttpServletResponse response, SentinelErrorCodeEnum sentinelErrorCodeEnum) throws Exception;
}
