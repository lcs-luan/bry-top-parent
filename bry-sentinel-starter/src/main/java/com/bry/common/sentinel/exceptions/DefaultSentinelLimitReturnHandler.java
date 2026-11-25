package com.bry.common.sentinel.exceptions;

import com.alibaba.fastjson.JSON;
import com.bry.common.sentinel.enums.SentinelErrorCodeEnum;
import com.bry.framework.base.response.BaseResponseBody;
import com.bry.framework.base.response.BaseResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * 默认异常处理类
 *
 * @description
 * @author: liu
 * @create: 2022-09-13 09:44
 **/
@Slf4j
public class DefaultSentinelLimitReturnHandler implements SentinelLimitReturnHandler {

    @Override
    public void writeErrorResponse(HttpServletResponse response, SentinelErrorCodeEnum sentinelErrorCodeEnum) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setContentType("application/json;charset=utf-8");
        BaseResponseBody<?> serviceResult = BaseResponseUtil.resultError(sentinelErrorCodeEnum.getCode(), sentinelErrorCodeEnum.getDesc());
        response.getWriter().write(JSON.toJSONString(serviceResult));
    }
}
