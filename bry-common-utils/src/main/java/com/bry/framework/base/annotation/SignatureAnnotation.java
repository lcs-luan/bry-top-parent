package com.bry.framework.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 签名注解<br/>
 * 添加了SignatureAnnotation注解的接口将会被签名拦截，需要签名
 *
 * @author: LuoCS
 * @date: 上午 9:26 2021/4/19 0019
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface SignatureAnnotation {
    /**
     * 字典序排序
     */
    String ALPHA_SORT = "ALPHA_SORT";

    /**
     * 按照order值排序(暂不支持)
     */
    String ORDER_SORT = "ORDER_SORT";

    String sort() default SignatureAnnotation.ALPHA_SORT;
}
