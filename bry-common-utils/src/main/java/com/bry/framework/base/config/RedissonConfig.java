package com.bry.framework.base.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * 过时：启用redisson,单机和集群配置合并，不推荐开启(仅为了兼容老版本) <br/>
 * 默认不需要该配置即可启用redisson，只需要添加pom依赖
 *
 * @Author: zeminsong
 * @Description:
 * @Date: create in 2020/12/7 15:23
 * @deprecated
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "spring.data.redis.redisson.enabled", havingValue = "true")
@Deprecated
public class RedissonConfig {

    public static final String REDIS_PREFIX = "redis://";

    @Value("${spring.data.redis.password:}")
    private String redisPasswd;

    @Value("${spring.data.redis.cluster.nodes}")
    private String redisNodes;

    @Value("${spring.data.redis.database:0}")
    private int database;

    @Bean
    public RedissonClient getRedissonClient() {
        String[] nodes = this.redisNodes.split(",");
        // 单机模式
        if (nodes.length == 1) {
            Config config = new Config();
            if (!StringUtils.isEmpty(this.redisPasswd)) {
                config.useSingleServer().setAddress(REDIS_PREFIX + nodes[0]).setPassword(this.redisPasswd).setDatabase(database);
            } else {
                //支持一下未设置密码连接
                log.warn("redis no passwd and redis is '' connect");
                config.useSingleServer().setAddress(REDIS_PREFIX + nodes[0]).setDatabase(database);
            }
            RedissonClient redissonClient = Redisson.create(config);
            return redissonClient;
        }

        // 集群模式
        Config config = new Config();
        ClusterServersConfig clusterConfig = config.useClusterServers();
        clusterConfig.setScanInterval(2000);
        for (String node : nodes) {
            clusterConfig.addNodeAddress(new String[]{REDIS_PREFIX + node});
        }
        if (!StringUtils.isEmpty(this.redisPasswd)) {
            clusterConfig.setPassword(this.redisPasswd);
        }
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
