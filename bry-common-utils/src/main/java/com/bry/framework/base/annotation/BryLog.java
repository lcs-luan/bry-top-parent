package com.bry.framework.base.annotation;


import com.bry.framework.base.enums.BusinessOperateTypeEnum;
import com.bry.framework.base.enums.OperatorEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 后管日志注解，1.0版本先以打log日志为主，后续可能根据日志采集至“日志审计系统”进行归纳分析
 *
 * @description
 * @author: liu
 * @create: 2022-10-11 17:17
 **/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BryLog {
    /**
     * 模块
     */
    String title() default "";

    /**
     * 功能
     */
    BusinessOperateTypeEnum businessType() default BusinessOperateTypeEnum.OTHER;

    /**
     * 操作人类别
     */
    OperatorEnum operatorType() default OperatorEnum.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;
}
