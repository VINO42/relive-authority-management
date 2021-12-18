package com.relive.security.handler;

import com.relive.exception.BadCaptchaException;
import com.relive.exception.LockedIpException;
import com.relive.security.cache.CacheService;
import com.relive.security.generator.AuthenticationKeyGenerator;
import com.relive.security.generator.DefaultAuthenticationKeyGenerator;
import com.relive.utils.DateUtils;
import com.relive.utils.MD5Utils;
import org.redisson.api.RList;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author: ReLive
 * @date: 2021/10/7 3:51 下午
 */
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler, CommonAuthenticationFailureHandler {
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private final CacheService cacheService;
    private static final String AUTHENTICATION_USERNAME = "_SPRING_SECURITY_SIMPLE_AUTHENTICATION_FILTER_USERNAME";
    private static final String AUTHENTICATION_REMOTE_HOST = "_SPRING_SECURITY_SIMPLE_AUTHENTICATION_FILTER_REMOTE_HOST";

    public LoginAuthenticationFailureHandler(CacheService cacheService) {
        Assert.notNull(cacheService, "CacheService is not null");
        this.cacheService = cacheService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>();
        map.put("username", request.getAttribute(AUTHENTICATION_USERNAME));
        String remoteHost = (String) request.getAttribute(AUTHENTICATION_REMOTE_HOST);
        map.put("ip", remoteHost);
        String captchaKey = MD5Utils.encryptMD5(Collections.singletonMap("ip", remoteHost));
        cacheService.deleteGraphCaptcha(captchaKey);
        String extractKey = authenticationKeyGenerator.extractKey(map);
        RList<String> errorCountRList = cacheService.getLoginErrorCount(extractKey);
        errorCountRList.add(DateUtils.dateFormat(new Date()));
        List<String> errorCountList = errorCountRList.readAll();
        Date startDate = DateUtils.stringToDate(errorCountList.get(0));
        Date endDate = DateUtils.stringToDate(errorCountList.get(errorCountList.size() - 1));
        Date newDate = DateUtils.addMinute(startDate, 5);
        if (endDate.after(newDate)) {
            Date unLockTime = DateUtils.addMinute(null, 5);
            cacheService.addBlackIP(remoteHost, unLockTime);
            errorCountRList.delete();
        }
        if (e instanceof BadCaptchaException) {
            responseResult(response, 400, e.getMessage());
        } else if (e instanceof LockedIpException) {
            responseResult(response, 400, e.getMessage());
        } else {
            responseResult(response, 400, "认证失败");
        }
    }

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

}
