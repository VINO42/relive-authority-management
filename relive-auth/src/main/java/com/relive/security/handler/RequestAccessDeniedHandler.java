package com.relive.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author ReLive
 * @Date 2021/5/8-15:59
 */
public class RequestAccessDeniedHandler implements AccessDeniedHandler, CommonAuthenticationFailureHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        responseResult(response, 403, "授权失败");
    }
}