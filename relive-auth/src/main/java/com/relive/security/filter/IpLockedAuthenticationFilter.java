package com.relive.security.filter;

import com.relive.exception.LockedIpException;
import com.relive.security.cache.CacheService;
import com.relive.security.handler.LoginAuthenticationFailureHandler;
import com.relive.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: ReLive
 * @date: 2021/10/7 6:42 下午
 */
@Slf4j
public final class IpLockedAuthenticationFilter extends OncePerRequestFilter {
    private final CacheService cacheService;
    private AuthenticationFailureHandler failureHandler;
    private RequestMatcher requiresAuthenticationRequestMatcher;

    public IpLockedAuthenticationFilter(String defaultFilterProcessesUrl, CacheService cacheService) {
        this(cacheService);
        this.setFilterProcessesUrl(defaultFilterProcessesUrl);
    }


    public IpLockedAuthenticationFilter(CacheService cacheService) {
        Assert.notNull(cacheService, "CacheService must not be null");
        this.cacheService = cacheService;
        this.failureHandler = new LoginAuthenticationFailureHandler(cacheService);
        this.setFilterProcessesUrl("/auth/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requiresAuthentication(request, response)) {
            filterChain.doFilter(request, response);
        } else {
            try {
                String ip = NetUtils.getWebClientIp(request);
                String ipLockDate = cacheService.getBlackIPLockTime(ip);
                if (StringUtils.isNotEmpty(ipLockDate)) {
                    throw new LockedIpException("该用户已被锁定，请于" + ipLockDate + "后重试");
                }
                filterChain.doFilter(request, response);
            } catch (AuthenticationException e) {
                this.unsuccessfulAuthentication(request, response, e);
                return;
            }

        }
    }

    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return this.requiresAuthenticationRequestMatcher.matches(request);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        if (log.isDebugEnabled()) {
            log.debug("Authentication request failed: ip lock");
            log.debug("Delegating to authentication failure handler " + this.failureHandler);
        }
        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

    public void setFilterProcessesUrl(String filterProcessesUrl) {
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(filterProcessesUrl));
    }

    public final void setRequiresAuthenticationRequestMatcher(RequestMatcher requestMatcher) {
        Assert.notNull(requestMatcher, "requestMatcher cannot be null");
        this.requiresAuthenticationRequestMatcher = requestMatcher;
    }
}
