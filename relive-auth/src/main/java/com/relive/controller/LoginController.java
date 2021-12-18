package com.relive.controller;

import com.relive.dto.LoginUserInfoDTO;
import com.relive.security.constant.RedisTokenKeyConstant;
import com.relive.utils.CaptchaUtils;
import com.relive.utils.MD5Utils;
import com.relive.utils.NetUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: ReLive
 * @date: 2021/8/26 11:52 上午
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/captcha")
    public Map<String, String> captcha(HttpServletRequest request) {
        String code = CaptchaUtils.getCode(5);
        Map<String, String> map = new HashMap<>();
        map.put("ip", NetUtils.getWebClientIp(request));
        String ipMD5 = MD5Utils.encryptMD5(map);
        redissonClient.getBucket(String.format(RedisTokenKeyConstant.GRAPH_CAPTCHA_KEY, ipMD5)).set(code, 5, TimeUnit.MINUTES);
        Map<String, String> result = new HashMap<>();
        result.put("captcha", code);
        return result;

    }

    @PostMapping("/principal")
    public LoginUserInfoDTO principal(Authentication authentication) {
        return null;
    }
}
