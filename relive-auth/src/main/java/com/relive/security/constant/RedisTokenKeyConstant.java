package com.relive.security.constant;

/**
 * @author: ReLive
 * @date: 2021/8/28 1:10 下午
 */
public class RedisTokenKeyConstant {
    public static final String BASE_KEY = "auth-server:%s";
    public static final String TOKEN_KEY = BASE_KEY + ":token";
    public static final String GRAPH_CAPTCHA_KEY = BASE_KEY + ":graph_captcha";
    public static final String PHONE_CAPTCHA_KEY = BASE_KEY + ":phone_captcha";
    public static final String AUTHORIZE_KEY = BASE_KEY + ":authorize";
    public static final String ERROR_COUNT = BASE_KEY + ":errorCount";
    public static final String BLACK_IP = "auth-server:blackIP";
}
