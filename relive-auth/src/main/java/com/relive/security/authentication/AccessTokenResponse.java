package com.relive.security.authentication;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @author: ReLive
 * @date: 2021/11/30 12:28 下午
 */
@Data
@Builder
public final class AccessTokenResponse {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String jtl;
    private Map<String, Object> params;
}
