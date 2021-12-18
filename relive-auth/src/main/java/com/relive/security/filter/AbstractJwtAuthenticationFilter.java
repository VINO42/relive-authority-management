package com.relive.security.filter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.relive.security.handler.RequestAuthenticationFailureEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ReLive
 * @date: 2021/8/27 4:56 下午
 */
@Slf4j
public abstract class AbstractJwtAuthenticationFilter extends OncePerRequestFilter {
    private RequestMatcher requiresAuthenticationRequestMatcher;
    private AuthenticationEntryPoint authenticationEntryPoint = new RequestAuthenticationFailureEntryPoint();

    protected AbstractJwtAuthenticationFilter(String... defaultIgnoreUrl) {
        this.setFilterIgnoreUrl(defaultIgnoreUrl);
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (this.requiresIgnoreAuthentication(request, response)) {
            chain.doFilter(request, response);
        } else {
            Authentication authentication = null;
            try {
                authentication = this.attemptAuthentication(request, response);

                if (authentication == null) {
                    return;
                }
            } catch (KeySourceException e) {
                //TODO 自定义异常
                log.error("jwk source error", e);
                InternalAuthenticationServiceException exception = new InternalAuthenticationServiceException("jwk source error");
                this.unsuccessfulAuthentication(request, response, exception);
                return;
            } catch (JOSEException e) {
                log.error("jwt parse error", e);
                InternalAuthenticationServiceException exception = new InternalAuthenticationServiceException("jwt parse error");
                this.unsuccessfulAuthentication(request, response, exception);
                return;
            } catch (JwtValidationException e) {
                log.error("Jwt expired", e);
                InternalAuthenticationServiceException exception = new InternalAuthenticationServiceException("jwt expired");
                this.unsuccessfulAuthentication(request, response, exception);
                return;
            } catch (InternalAuthenticationServiceException e) {
                log.error("An internal error occurred while trying to authenticate the user.", e);
                this.unsuccessfulAuthentication(request, response, e);
                return;
            } catch (AuthenticationException e) {
                log.error("authentication error", e);
                this.unsuccessfulAuthentication(request, response, e);
                return;
            }
            this.successfulAuthentication(request, response, chain, authentication);
            chain.doFilter(request, response);
        }
    }

    protected boolean requiresIgnoreAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return this.requiresAuthenticationRequestMatcher.matches(request);
    }

    public abstract Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException, JOSEException;

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        if (log.isDebugEnabled()) {
            log.debug("Authentication request failed: " + failed.toString(), failed);
        }

        this.authenticationEntryPoint.commence(request, response, failed);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
    }


    public void setFilterIgnoreUrl(String... filterIgnoreUrl) {
        this.requiresAuthenticationRequestMatcher = new OrRequestMatcher(this.antMatchers(filterIgnoreUrl));
    }

    public final void setRequiresAuthenticationRequestMatcher(RequestMatcher requestMatcher) {
        Assert.notNull(requestMatcher, "requestMatchers cannot be null");
        this.requiresAuthenticationRequestMatcher = requestMatcher;
    }


    public static List<RequestMatcher> antMatchers(HttpMethod httpMethod, String... antPatterns) {
        String method = httpMethod == null ? null : httpMethod.toString();
        List<RequestMatcher> matchers = new ArrayList();
        String[] var4 = antPatterns;
        int var5 = antPatterns.length;

        for (int i = 0; i < var5; ++i) {
            String pattern = var4[i];
            matchers.add(new AntPathRequestMatcher(pattern, method));
        }

        return matchers;
    }

    public static List<RequestMatcher> antMatchers(String... antPatterns) {
        return antMatchers(null, antPatterns);
    }

}
