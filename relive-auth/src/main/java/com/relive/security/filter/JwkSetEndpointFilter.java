package com.relive.security.filter;

import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * @author: ReLive
 * @date: 2021/12/9 12:33 下午
 */
public final class JwkSetEndpointFilter extends OncePerRequestFilter {
    public static final String DEFAULT_JWK_URI = "/auth/jwk";
    private final RequestMatcher requestMatcher;
    private final JWKSource<SecurityContext> jwkSource;
    private final JWKSelector jwkSelector;

    public JwkSetEndpointFilter(JWKSource<SecurityContext> jwkSource) {
        this(jwkSource, DEFAULT_JWK_URI);
    }

    public JwkSetEndpointFilter(JWKSource<SecurityContext> jwkSource, String jwkSetEndpointUri) {
        this.jwkSource = jwkSource;
        this.requestMatcher = new AntPathRequestMatcher(jwkSetEndpointUri);
        this.jwkSelector=new JWKSelector(new JWKMatcher.Builder().build());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        JWKSet jwkSet;
        try {
            jwkSet = new JWKSet(this.jwkSource.get(this.jwkSelector, null));
        }
        catch (Exception ex) {
            throw new IllegalStateException("Failed to select the JWK(s) -> " + ex.getMessage(), ex);
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (Writer writer = response.getWriter()) {
            writer.write(jwkSet.toString());
        }
    }
}
