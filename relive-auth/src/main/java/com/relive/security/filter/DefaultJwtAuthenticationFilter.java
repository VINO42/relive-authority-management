package com.relive.security.filter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.relive.security.service.DefaultUserDetails;
import com.relive.security.token.SimpleAuthenticationToken;
import com.relive.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author: ReLive
 * @date: 2021/12/3 8:33 下午
 */
@Slf4j
public final class DefaultJwtAuthenticationFilter extends AbstractJwtAuthenticationFilter {
    private static final String DEFAULT_TOKEN_SUFFIX = "Bearer";
    private static final String AUTHORIZATION = "Authorization";
    private JWKSource<SecurityContext> jwkSource;

    public DefaultJwtAuthenticationFilter(JWKSource<SecurityContext> jwkSource, String... defaultIgnoreUrl) {
        super(defaultIgnoreUrl);
        Assert.notNull(jwkSource, "JWKSource must not be null");
        this.jwkSource = jwkSource;
    }

    public DefaultJwtAuthenticationFilter(JWKSource<SecurityContext> jwkSource) {
        //默认过滤登录请求
        super("/auth/login", "/auth/captcha");
        Assert.notNull(jwkSource, "JWKSource must not be null");
        this.jwkSource = jwkSource;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException, JOSEException {
        String authenticationHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.isEmpty(authenticationHeader)) {
            if (log.isDebugEnabled()) {
                log.debug("DefaultJwtAuthenticationFilter -> authentication credential is null");
            }
            throw new AuthenticationCredentialsNotFoundException("authentication credential is null");
        }
        String authenticationToken = authenticationHeader.replace(DEFAULT_TOKEN_SUFFIX, "").trim();

        JWKSelector jwkSelector = new JWKSelector(new JWKMatcher.Builder().build());
        JWKSet jwkSet = new JWKSet(jwkSource.get(jwkSelector, null));
        JWK keyByKeyId = jwkSet.getKeyByKeyId("rsa-jwk-kid");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(keyByKeyId.toRSAKey().toRSAPublicKey()).build();

        //TODO 强转可能存在问题
        JSONObject principal = jwtDecoder.decode(authenticationToken).getClaim("principal");
        ObjectNode jsonNodes = JsonUtils.jsonToObject(principal.toJSONString());
        DefaultUserDetails user=new DefaultUserDetails(jsonNodes.path("username").asText(),null,Collections.singletonList(new SimpleGrantedAuthority("admin")));
        return new SimpleAuthenticationToken(user, null, null, user.getAuthorities());
    }
}
