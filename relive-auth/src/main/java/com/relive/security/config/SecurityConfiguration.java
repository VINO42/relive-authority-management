package com.relive.security.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.relive.security.access.JdbcFilterInvocationSecurityMetadataSource;
import com.relive.security.cache.CacheService;
import com.relive.security.filter.DefaultJwtAuthenticationFilter;
import com.relive.security.filter.IpLockedAuthenticationFilter;
import com.relive.security.filter.JwkSetEndpointFilter;
import com.relive.security.filter.SimpleAuthenticationFilter;
import com.relive.security.handler.RequestAccessDeniedHandler;
import com.relive.security.handler.RequestAuthenticationFailureEntryPoint;
import com.relive.security.service.SimpleUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author: ReLive
 * @date: 2021/8/26 3:41 下午
 */
@EnableWebSecurity(debug = false)
public class SecurityConfiguration {


//    @Configuration
//    @Order(1)
//    public static class DefaultSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//        @Autowired
//        private SimpleUserDetailService simpleUserDetailService;
//        @Autowired
//        private CacheService cacheService;
//        @Autowired
//        private AuthenticationSuccessHandler authenticationSuccessHandler;
//        @Autowired
//        private AuthenticationFailureHandler authenticationFailureHandler;
//        @Autowired
//        private JWKSource<SecurityContext> jwkSource;
//        @Autowired
//        private AccessDecisionManager accessDecisionManager;
//        @Autowired
//        private JdbcFilterInvocationSecurityMetadataSource jdbcFilterInvocationSecurityMetadataSource;
//
//
//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(simpleUserDetailService);
//        }
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests()
//                    .antMatchers("/auth/captcha").permitAll()
//                    .anyRequest().authenticated().withObjectPostProcessor(filterSecurityInterceptorObjectPostProcessor())
//                    .and()
//                    .csrf().disable()
//                    .addFilterBefore(ipLockedAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                    .addFilterBefore(defaultJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                    .addFilterAfter(simpleAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                    .addFilterAfter(jwkSetEndpointFilter(), UsernamePasswordAuthenticationFilter.class)
//                    .exceptionHandling().accessDeniedHandler(new RequestAccessDeniedHandler())
//                    .authenticationEntryPoint(new RequestAuthenticationFailureEntryPoint())
//                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                    .logout().logoutUrl("/auth/logout")
//            ;
//
//        }
//
//
//        /**
//         * 登录认证过滤器
//         *
//         * @return
//         * @throws Exception
//         */
//        @Bean
//        public SimpleAuthenticationFilter simpleAuthenticationFilter() throws Exception {
//            SimpleAuthenticationFilter simpleAuthenticationFilter = new SimpleAuthenticationFilter(cacheService);
//            simpleAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
//            simpleAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
//            simpleAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
//            return simpleAuthenticationFilter;
//        }
//
//
//        /**
//         * JWT认证过滤器
//         *
//         * @return
//         */
//        @Bean
//        public DefaultJwtAuthenticationFilter defaultJwtAuthenticationFilter() {
//            return new DefaultJwtAuthenticationFilter(jwkSource);
//        }
//
//        /**
//         * IP锁定过滤器
//         *
//         * @return
//         */
//        @Bean
//        public IpLockedAuthenticationFilter ipLockedAuthenticationFilter() {
//            return new IpLockedAuthenticationFilter(cacheService);
//        }
//
//        /**
//         * JWK过滤器
//         *
//         * @return
//         */
//        @Bean
//        public JwkSetEndpointFilter jwkSetEndpointFilter() {
//            return new JwkSetEndpointFilter(jwkSource);
//        }
//
//        private ObjectPostProcessor<FilterSecurityInterceptor> filterSecurityInterceptorObjectPostProcessor() {
//            return new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                @Override
//                public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                    object.setAccessDecisionManager(accessDecisionManager);
//                    object.setSecurityMetadataSource(jdbcFilterInvocationSecurityMetadataSource);
//                    return object;
//                }
//            };
//        }
//
//    }

    @Configuration
    @Order(2)
    public static class OAuth2AuthenticationServer extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
            RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
            http.requestMatcher(endpointsMatcher).authorizeRequests((authorizeRequests) -> {
                ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) authorizeRequests.anyRequest()).authenticated();
            }).csrf((csrf) -> {
                csrf.ignoringRequestMatchers(new RequestMatcher[]{endpointsMatcher});
            }).apply(authorizationServerConfigurer);
            http.formLogin();
        }

    }

}
