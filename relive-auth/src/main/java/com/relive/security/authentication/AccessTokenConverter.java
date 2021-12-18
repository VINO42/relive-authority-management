package com.relive.security.authentication;

import org.springframework.security.core.Authentication;

/**
 * @author: ReLive
 * @date: 2021/11/29 8:24 下午
 */
public interface AccessTokenConverter {

    AccessTokenResponse convert(Authentication authentication);
}
