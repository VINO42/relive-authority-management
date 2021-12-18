package com.relive.security.authentication;

import com.relive.security.token.SimpleAuthenticationToken;
import com.relive.utils.JsonUtils;
import com.relive.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author: ReLive
 * @date: 2021/8/27 3:49 下午
 */
@Slf4j
public class SimpleAuthenticationConverter implements AuthenticationConverter {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CAPTCHA = "captcha";
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    public SimpleAuthenticationConverter() {
        this(new WebAuthenticationDetailsSource());
    }

    public SimpleAuthenticationConverter(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    @Override
    public SimpleAuthenticationToken convert(HttpServletRequest request) {
        BufferedReader reader = null;
        try {
            StringBuilder builder = new StringBuilder();
            reader = request.getReader();
            String temp;
            while ((temp = reader.readLine()) != null) {
                builder.append(temp);
            }
            Map<String, String> map = JsonUtils.jsonToObject(builder.toString(), Map.class);
            String username = trimBlankCharacter(map.get(USERNAME));
            String password = trimBlankCharacter(map.get(PASSWORD));
            String captcha = trimBlankCharacter(map.get(CAPTCHA));
            String ip = NetUtils.getWebClientIp(request);
            SimpleAuthenticationToken result = new SimpleAuthenticationToken(username, password, captcha, ip);
            result.setDetails(this.authenticationDetailsSource.buildDetails(request));
            return result;
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid authentication token");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected String trimBlankCharacter(String value) {
        return value.replaceAll("\\s*", "");
    }

    public AuthenticationDetailsSource<HttpServletRequest, ?> getAuthenticationDetailsSource() {
        return authenticationDetailsSource;
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
    }
}
