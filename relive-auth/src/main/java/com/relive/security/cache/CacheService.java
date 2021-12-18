package com.relive.security.cache;

import org.redisson.api.RList;

import java.util.Date;

/**
 * @author: ReLive
 * @date: 2021/10/19 7:59 下午
 */
public interface CacheService {

    String getGraphCaptcha(String key);

    void deleteGraphCaptcha(String key);

    RList<String> getLoginErrorCount(String key);

    void deleteLoginErrorCount(String key);

    String getPhoneCaptcha(String key);

    void addBlackIP(String key, Date unlockTime);

    String getBlackIPLockTime(String key);

    void saveLoginToken(String key, String tokenValue);

    boolean deleteLoginToken(String key);
}
