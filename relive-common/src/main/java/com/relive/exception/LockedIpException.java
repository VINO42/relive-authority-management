package com.relive.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author: ReLive
 * @date: 2021/10/7 9:19 下午
 */
public class LockedIpException extends AuthenticationException {

    public LockedIpException(String msg) {
        super(msg);
    }

    public LockedIpException(String msg, Throwable t) {
        super(msg, t);
    }
}
