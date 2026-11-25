package com.bry.framework.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 访问延时拦截注解：暂不支持<br/>
 * 添加了AccessDelayTimeAnnotation注解的接口将拦截请求时间和到达服务器时间差值过大的；
 * 请求需要有timestamp参数
 *
 * @author: LuoCS
 * @date: 上午 9:26 2021/4/19 0019
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface AccessDelayTimeAnnotation {

    /**
     * 延时时间（单位：10分钟）
     */
    int delayTime() default 600;
}
