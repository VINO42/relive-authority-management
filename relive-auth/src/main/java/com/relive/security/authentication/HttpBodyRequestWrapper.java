package com.relive.security.authentication;

import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author: ReLive
 * @date: 2021/12/1 7:57 下午
 */
public class HttpBodyRequestWrapper extends HttpServletRequestWrapper {
    @Nullable
    private ServletInputStream inputStream;
    @Nullable
    private BufferedReader reader;

    public HttpBodyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
    }

    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = this.getRequest().getInputStream();
        }

        return this.inputStream;
    }

    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(this.getInputStream(), this.getCharacterEncoding()));
        }

        return this.reader;
    }

}
