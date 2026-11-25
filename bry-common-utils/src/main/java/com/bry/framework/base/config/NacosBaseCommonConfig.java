package com.bry.framework.base.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 功能： Nacos公共配置<br/>
 * 描述：使用时将添加了@SpringBootApplication注解的启动类 XxxApplication 放在com.bry目录下，
 * 或者添加 @ComponentScan(basePackages = {"com.bry.xxx.your-project-package", "com.bry.framework.base.config"})
 *
 * @author LuoCS
 * @date 2022/4/14 0014
 */
@Data
@Component
@RefreshScope
@Slf4j
public class NacosBaseCommonConfig {

    @Value("${system.response.code}")
    private String systemResponseCode;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private ApplicationContext applicationContext;

    // spring容器加载完成时，初始化静态常量
    @PostConstruct
    public void init() {
        log.info("====>systemResponseCode[{}]:{}", applicationName, systemResponseCode);
        if (StringUtils.isBlank(systemResponseCode)
                || Integer.parseInt(systemResponseCode) < SystemCodeConts.MIN_SYSTEM_CODE
                || Integer.parseInt(systemResponseCode) > SystemCodeConts.MAX_SYSTEM_CODE) {
            log.error("===>>>系统码非法，请配置system.response.code的值，参考[https://wiki.ds1688.com/pages/viewpage.action?pageId=18157038]");
            int exit = SpringApplication.exit(applicationContext, () -> 0);
            System.exit(exit);
        }
        SystemCodeConts.fillSystemCode(systemResponseCode);
    }

}
