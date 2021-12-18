package com.relive.security.cache.impl;

import com.relive.security.cache.CacheService;
import com.relive.security.constant.RedisTokenKeyConstant;
import com.relive.utils.DateUtils;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author: ReLive
 * @date: 2021/10/19 8:00 下午
 */
@Service
public class RedisCacheService implements CacheService {

    private RedissonClient redissonClient;

    public RedisCacheService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public String getGraphCaptcha(String key) {
        return (String) redissonClient.getBucket(String.format(RedisTokenKeyConstant.GRAPH_CAPTCHA_KEY, key)).get();
    }

    @Override
    public void deleteGraphCaptcha(String key) {
        redissonClient.getBucket(String.format(RedisTokenKeyConstant.GRAPH_CAPTCHA_KEY, key)).delete();
    }

    @Override
    public RList<String> getLoginErrorCount(String key) {
        return redissonClient.getList(String.format(RedisTokenKeyConstant.ERROR_COUNT, key));
    }

    @Override
    public void deleteLoginErrorCount(String key) {
        redissonClient.getBucket(String.format(RedisTokenKeyConstant.ERROR_COUNT, key)).delete();
    }

    @Override
    public String getPhoneCaptcha(String key) {
        return null;
    }

    @Override
    public void addBlackIP(String key, Date unlockTime) {
        redissonClient.getBucket(RedisTokenKeyConstant.BLACK_IP + ":" + key).set(DateUtils.dateFormat(unlockTime), 5, TimeUnit.MINUTES);
    }

    @Override
    public String getBlackIPLockTime(String key) {
        return (String) redissonClient.getBucket(RedisTokenKeyConstant.BLACK_IP + ":" + key).get();
    }

    @Override
    public void saveLoginToken(String key, String tokenValue) {
        redissonClient.getBucket(RedisTokenKeyConstant.TOKEN_KEY + ":" + key).set(tokenValue);
    }

    @Override
    public boolean deleteLoginToken(String key) {
        return redissonClient.getBucket(RedisTokenKeyConstant.TOKEN_KEY + ":" + key).delete();
    }
}
