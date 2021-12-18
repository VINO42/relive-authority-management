package com.relive.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: ReLive
 * @date: 2021/8/28 5:40 下午
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient redissonClient(RedisProperties properties) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + properties.getHost() + ":" + properties.getPort());
        return Redisson.create(config);
    }
}
