package com.relive.security.generator;

import java.util.Map;

/**
 * @author: ReLive
 * @date: 2021/10/7 3:10 下午
 */
public interface AuthenticationKeyGenerator {

    String extractKey(Map<String,Object> var1);
}
