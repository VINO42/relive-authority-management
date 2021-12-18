package com.relive.security.handler;

import com.relive.security.cache.CacheService;
import com.relive.security.token.SimpleAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: ReLive
 * @date: 2021/12/3 1:00 下午
 */
public class SimpleLogoutHandler implements LogoutHandler {
    private final CacheService cacheService;

    public SimpleLogoutHandler(CacheService cacheService) {
        Assert.notNull(cacheService, "CacheService is not null");
        this.cacheService = cacheService;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        SimpleAuthenticationToken authenticationToken = (SimpleAuthenticationToken) authentication;
        cacheService.deleteLoginToken(authenticationToken.getName());
    }
}
