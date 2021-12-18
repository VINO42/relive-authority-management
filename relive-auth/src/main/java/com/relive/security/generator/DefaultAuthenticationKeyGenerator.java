package com.relive.security.generator;

import com.relive.security.token.SimpleAuthenticationToken;
import com.relive.utils.NetUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: ReLive
 * @date: 2021/10/7 3:17 下午
 */
public class DefaultAuthenticationKeyGenerator implements AuthenticationKeyGenerator {
    private static final String IP = "ip";
    private static final String USERNAME = "username";

    @Override
    public String extractKey(Map<String, Object> var) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        if (var.containsKey(USERNAME)) {
            map.put(USERNAME, var.get(USERNAME));
        }
        if (var.containsKey(IP)) {
            String ip = String.valueOf(var.get(IP));
            if (NetUtils.isIp(ip)) {
                map.put(IP, ip);
            }
        }
        return generateKey(map);
    }


    protected String generateKey(Map<String, Object> values) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(values.toString().getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException var4) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", var4);
        } catch (UnsupportedEncodingException var5) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).", var5);
        }
    }
}
