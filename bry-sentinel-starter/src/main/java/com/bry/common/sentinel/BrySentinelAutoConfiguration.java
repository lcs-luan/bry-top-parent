package com.bry.common.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.bry.common.sentinel.exceptions.DefaultSentinelLimitReturnHandler;
import com.bry.common.sentinel.exceptions.SentinelExceptionHandler;
import com.bry.common.sentinel.exceptions.SentinelLimitReturnHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @description
 * @author: liu
 * @create: 2022-09-08 21:39
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.sentinel.enabled", matchIfMissing = true)
public class BrySentinelAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public SentinelLimitReturnHandler sentinelLimitReturnHandler() {
        return new DefaultSentinelLimitReturnHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler defaultSentinelExceptionHandler(SentinelLimitReturnHandler handler) {
        return new SentinelExceptionHandler(handler);
    }

}
