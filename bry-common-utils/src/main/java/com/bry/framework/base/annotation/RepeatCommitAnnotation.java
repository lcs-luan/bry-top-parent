package com.bry.framework.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 接口重复提交拦截注解<br/>
 * 添加了RepeatCommitAnnotation注解的接口需要传sign参数，表示请求签名；
 * 不同参数的请求会生成不同的sign值，如果2个请求的sign相同，则认为是同一个请求；
 *
 * @author: LuoCS
 * @date: 上午 9:26 2021/6/9
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface RepeatCommitAnnotation {

    /**
     * 重复请求判断时间间隔（单位：秒）,默认值10s
     */
    int checkTimeRange() default 10;

    /**
     * 校验的方法名，如果为空，则取注解所在[类名.方法]的名称，如OrderController.createOrder
     */
    String checkMethod() default "";
}
