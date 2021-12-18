package com.relive.security.handler;

import com.relive.api.Result;
import com.relive.utils.JsonUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * @author: ReLive
 * @date: 2021/12/2 8:53 下午
 */
public interface CommonAuthenticationFailureHandler {
    static final MediaType DEFAULT_CONTENT_TYPE = MediaType.valueOf("application/json;charset=UTF-8");

    default void responseResult(HttpServletResponse response, Integer responseCode, String responseMessage) throws IOException {
        response.setContentType(DEFAULT_CONTENT_TYPE.getType());
        response.setStatus(HttpServletResponse.SC_OK);
        String responseResult = JsonUtils.objectToJson(Result.error(responseCode, responseMessage));
        try (Writer writer = response.getWriter()) {
            writer.write(responseResult);
        }
    }
}
