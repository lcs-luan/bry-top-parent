package com.bry.framework.base.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 功能描述： <br/>
 *
 * @author LuoCS
 * @date 2025/6/16 0016
 */
@Configuration
@ConditionalOnProperty(value = "spring.data.redis.password")
public class RedisConfig {

    /**
     * 自定义序列化（解决Java对象存储问题）
     *
     * @param redisConnectionFactory
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.Object, java.lang.Object>
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 1.创建 redisTemplate 模版
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        // 2.关联 redisConnectionFactory
        template.setConnectionFactory(redisConnectionFactory);
        // 3.创建 序列化类
        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
        // 6.序列化类，对象映射设置
        // 7.设置 value 的转化格式和 key 的转化格式
        template.setValueSerializer(genericToStringSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 自定义序列化-JSON序列化（解决Java对象存储问题）
     *
     * @param redisConnectionFactory
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.Object, java.lang.Object>
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplateTwo(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplateTwo = new RedisTemplate<>();
        redisTemplateTwo.setConnectionFactory(redisConnectionFactory);
        //使用Jackson2JsonRedisSerializer替换默认的序列化规则
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        redisTemplateTwo.setHashKeySerializer(new StringRedisSerializer());
        redisTemplateTwo.setHashValueSerializer(jackson2JsonRedisSerializer);
        //设置value的序列化规则
        redisTemplateTwo.setValueSerializer(jackson2JsonRedisSerializer);
        //设置key的序列化规则
        redisTemplateTwo.setKeySerializer(new StringRedisSerializer());
        redisTemplateTwo.afterPropertiesSet();
        return redisTemplateTwo;
    }
}
