package com.relive.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author ReLive
 * @Date 2021/5/11-10:23
 */
public class RequestAuthenticationFailureEntryPoint implements AuthenticationEntryPoint, CommonAuthenticationFailureHandler {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        responseResult(response, 401, "认证失败");
    }
}
