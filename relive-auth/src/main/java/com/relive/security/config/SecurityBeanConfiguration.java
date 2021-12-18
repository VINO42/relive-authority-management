package com.relive.security.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.relive.security.access.JdbcFilterInvocationSecurityMetadataSource;
import com.relive.security.cache.CacheService;
import com.relive.security.handler.LoginAuthenticationFailureHandler;
import com.relive.security.handler.LoginAuthenticationSuccessHandler;
import com.relive.security.handler.SimpleLogoutHandler;
import com.relive.security.handler.SimpleLogoutSuccessHandler;
import com.relive.security.jwt.RotateJWKSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.List;

/**
 * @author: ReLive
 * @date: 2021/12/6 12:17 下午
 */
@Configuration
public class SecurityBeanConfiguration {

    @Autowired
    private CacheService cacheService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录成功处理器
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new LoginAuthenticationSuccessHandler(cacheService, jwkSource());
    }

    /**
     * 登录失败处理器
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new LoginAuthenticationFailureHandler(cacheService);
    }


    /**
     * 登出处理器
     *
     * @return
     */
    @Bean
    public SimpleLogoutHandler simpleLogoutHandler() {
        return new SimpleLogoutHandler(cacheService);
    }

    /**
     * 登出成功处理器
     *
     * @return
     */
    @Bean
    public SimpleLogoutSuccessHandler simpleLogoutSuccessHandler() {
        return new SimpleLogoutSuccessHandler();
    }

    /**
     * 元数据加载器
     *
     * @return
     */
    @Bean
    public JdbcFilterInvocationSecurityMetadataSource jdbcFilterInvocationSecurityMetadataSource() {
        return new JdbcFilterInvocationSecurityMetadataSource();
    }

    /**
     * 角色选举器
     *
     * @return
     */
    @Bean
    public RoleVoter roleVoter() {
        return new RoleVoter();
    }

    /**
     * 基于肯定的访问处理器
     *
     * @param decisionVoters
     * @return
     */
    @Bean
    public AccessDecisionManager accessDecisionManager(List<AccessDecisionVoter<?>> decisionVoters) {
        return new AffirmativeBased(decisionVoters);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RotateJWKSource rotateJWKSource = new RotateJWKSource();
        return (jwkSelector, securityContext) -> rotateJWKSource.get(jwkSelector, securityContext);
    }
}
