package com.bry.framework.base.interceptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

/**
 * 功能描述： feign请求拦截器：微服务之间使用Feign调用接口时, 透传header参数信息<br/>
 *
 * @author LuoCS
 * @date 2023/1/5 0005
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                // 指定跳过的header参数
                if (name.equals("content-length")) {
                    continue;
                }
                // 此处可以指定加入的header，暂时不做控制
                requestTemplate.header(name, values);
            }
        }
    }
}
