package com.relive.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author: ReLive
 * @date: 2021/8/27 5:39 下午
 */
public class SimpleAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = 3139981455620353805L;
    private String captcha;
    private String remoteHost;

    public SimpleAuthenticationToken(Object principal, Object credentials, String captcha, String remoteHost) {
        super(principal, credentials);
        this.captcha = captcha;
        this.remoteHost = remoteHost;
    }

    public SimpleAuthenticationToken(Object principal, Object credentials, String remoteHost, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.captcha = captcha;
    }

    public String getCaptcha() {
        return captcha;
    }

    public String getRemoteHost() {
        return remoteHost;
    }
}
