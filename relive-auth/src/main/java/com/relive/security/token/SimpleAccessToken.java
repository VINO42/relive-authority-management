package com.relive.security.token;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ReLive
 * @date: 2021/11/21 2:42 下午
 */
@Data
public class SimpleAccessToken {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Map<String, Object> clams = new HashMap<>();
    private String jti;
}
