package com.relive.security.handler;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.relive.api.Result;
import com.relive.security.authentication.AccessTokenConverter;
import com.relive.security.authentication.AccessTokenResponse;
import com.relive.security.authentication.DefaultAccessTokenConverter;
import com.relive.security.cache.CacheService;
import com.relive.security.generator.AuthenticationKeyGenerator;
import com.relive.security.generator.DefaultAuthenticationKeyGenerator;
import com.relive.security.token.SimpleAuthenticationToken;
import com.relive.utils.JsonUtils;
import com.relive.utils.MD5Utils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: ReLive
 * @date: 2021/10/7 3:50 下午
 */
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final MediaType DEFAULT_CONTENT_TYPE = MediaType.valueOf("application/json;charset=UTF-8");
    private final AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private final CacheService cacheService;
    private final AccessTokenConverter accessTokenConverter;

    public LoginAuthenticationSuccessHandler(CacheService cacheService, JWKSource<SecurityContext> jwkSource) {
        this.cacheService = cacheService;
        this.accessTokenConverter = new DefaultAccessTokenConverter(jwkSource);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SimpleAuthenticationToken authenticationToken = (SimpleAuthenticationToken) authentication;
        Map<String, Object> map = new HashMap<>();
        map.put("username", authenticationToken.getName());
        map.put("ip", authenticationToken.getRemoteHost());
        String errorCountKey = authenticationKeyGenerator.extractKey(map);
        String captchaKey = MD5Utils.encryptMD5(Collections.singletonMap("ip", authenticationToken.getRemoteHost()));
        cacheService.deleteGraphCaptcha(captchaKey);
        cacheService.deleteLoginErrorCount(errorCountKey);
        response.setContentType(DEFAULT_CONTENT_TYPE.getType());
        response.setStatus(HttpServletResponse.SC_OK);
        AccessTokenResponse accessTokenResponse = accessTokenConverter.convert(authentication);
        cacheService.saveLoginToken(authenticationToken.getName(),accessTokenResponse.getAccessToken());
        String responseMessage = JsonUtils.objectToJson(Result.success(accessTokenResponse));
        try (Writer writer = response.getWriter()) {
            writer.write(responseMessage);
        }
    }
}
