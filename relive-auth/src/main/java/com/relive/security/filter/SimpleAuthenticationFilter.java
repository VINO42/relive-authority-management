package com.relive.security.filter;

import com.relive.exception.BadCaptchaException;
import com.relive.security.authentication.HttpBodyRequestWrapper;
import com.relive.security.authentication.SimpleAuthenticationConverter;
import com.relive.security.cache.CacheService;
import com.relive.security.token.SimpleAuthenticationToken;
import com.relive.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author: ReLive
 * @date: 2021/8/27 3:47 下午
 */
@Slf4j
public final class SimpleAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private boolean debug = log.isDebugEnabled();
    private CacheService cacheService;
    private AuthenticationConverter authenticationConverter = new SimpleAuthenticationConverter();
    private static final String AUTHENTICATION_USERNAME = "_SPRING_SECURITY_SIMPLE_AUTHENTICATION_FILTER_USERNAME";
    private static final String AUTHENTICATION_REMOTE_HOST = "_SPRING_SECURITY_SIMPLE_AUTHENTICATION_FILTER_REMOTE_HOST";

    public SimpleAuthenticationFilter(CacheService cacheService) {
        super(new AntPathRequestMatcher("/auth/login", "POST"));
        this.cacheService = cacheService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        SimpleAuthenticationToken authentication = (SimpleAuthenticationToken) authenticationConverter.convert(request);
        if (StringUtils.isBlank(authentication.getCaptcha())) {
            throw new BadCaptchaException("验证码不能为空");
        }
        String captcha = cacheService.getGraphCaptcha(MD5Utils.encryptMD5(Collections.singletonMap("ip", authentication.getRemoteHost())));
        if (StringUtils.isBlank(captcha)) {
            throw new BadCaptchaException("验证码已过期");
        }
        if (!captcha.equalsIgnoreCase(authentication.getCaptcha())) {
            throw new BadCaptchaException("验证码错误，请重新输入");
        }
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("用户名密码不能为空");
        }
        this.setDetails(request, authentication);
        Authentication authenticate = this.getAuthenticationManager().authenticate(authentication);
        request.setAttribute(AUTHENTICATION_USERNAME, authentication.getName());
        request.setAttribute(AUTHENTICATION_REMOTE_HOST, authentication.getRemoteHost());
        return createSuccessAuthentication(authenticate, authentication.getRemoteHost());
    }

    protected Authentication createSuccessAuthentication(Authentication authentication, String remoteHost) {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, () -> {
            return this.messages.getMessage("SimpleAuthenticationFilter.onlySupports", "Only UsernamePasswordAuthenticationToken is supported");
        });
        SimpleAuthenticationToken result = new SimpleAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), remoteHost, authentication.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
