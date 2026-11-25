package com.bry.common.sentinel.exceptions;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.bry.common.sentinel.enums.SentinelErrorCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * 限流异常拦截处理
 *
 * @description
 * @author: liu
 * @create: 2022-08-12 16:30
 **/
@Slf4j
public class SentinelExceptionHandler implements BlockExceptionHandler {

    /**
     * 限流拦截时，处理类
     */
    private final SentinelLimitReturnHandler limitReturnHandler;

    /**
     * 构造器
     *
     * @param limitReturnHandler 限流拦截时的处理类
     */
    public SentinelExceptionHandler(SentinelLimitReturnHandler limitReturnHandler) {
        this.limitReturnHandler = limitReturnHandler;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {
        // 计算限流errorCode 枚举
        SentinelErrorCodeEnum sentinelErrorCodeEnum = handleSentinelErrorCodeEnum(ex);

        // 打印出入参
        logPrint(request, sentinelErrorCodeEnum);

        // 写返回数据
        writeErrorResponse(response, sentinelErrorCodeEnum);
    }

    /**
     * 打印参数
     *
     * @param request
     * @param sentinelErrorCodeEnum
     */
    protected void logPrint(HttpServletRequest request, SentinelErrorCodeEnum sentinelErrorCodeEnum) {
        String path = request.getServletPath();
        if (log.isDebugEnabled()) {
            log.error("接口[{}]{}", path, sentinelErrorCodeEnum.getDesc());
        }
    }

    /**
     * 返回错误数据
     *
     * @param response
     * @param sentinelErrorCodeEnum
     */
    protected void writeErrorResponse(HttpServletResponse response, SentinelErrorCodeEnum sentinelErrorCodeEnum) throws Exception {
        limitReturnHandler.writeErrorResponse(response, sentinelErrorCodeEnum);
    }

    /**
     * 通过异常类型，计算限流errorCode
     *
     * @param ex
     * @return
     */
    private SentinelErrorCodeEnum handleSentinelErrorCodeEnum(BlockException ex) {
        SentinelErrorCodeEnum sentinelErrorCodeEnum = null;
        if (ex instanceof FlowException) {
            sentinelErrorCodeEnum = SentinelErrorCodeEnum.FLOW_ERROR;
        } else if (ex instanceof DegradeException) {
            sentinelErrorCodeEnum = SentinelErrorCodeEnum.DEGRADE_ERROR;
        } else if (ex instanceof ParamFlowException) {
            sentinelErrorCodeEnum = SentinelErrorCodeEnum.PARAM_FLOW_ERROR;
        } else if (ex instanceof SystemBlockException) {
            sentinelErrorCodeEnum = SentinelErrorCodeEnum.SYSTEM_BLOCK_ERROR;
        } else if (ex instanceof AuthorityException) {
            sentinelErrorCodeEnum = SentinelErrorCodeEnum.AUTHORITY_ERROR;
        }
        return sentinelErrorCodeEnum;
    }
}
