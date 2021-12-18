package com.relive.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @author: ReLive
 * @date: 2021/8/28 1:21 下午
 * <p>
 * 验证码异常类
 */
public class BadCaptchaException extends AuthenticationException {
    public BadCaptchaException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadCaptchaException(String msg) {
        super(msg);
    }
}
