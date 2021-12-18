package com.relive.authentication;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: ReLive
 * @date: 2021/12/4 2:40 下午
 */
public class AuthenticationRequestTest {

    @Test
    public void requestMatcherTest(){
        String filterIgnoreUrl="/auth/login,/auth/captcha";
        AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(filterIgnoreUrl);
    }
}
