package com.relive.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ReLive
 * @Date 2021/3/19-12:27
 */
@Slf4j
public class NetUtils {
    public static final String UNKNOWN_IP = "UNKNOWN";

    public static final String LOCALHOST = "127.0.0.1";


    public static boolean isIp(String ipAddress) {
        if (StringUtils.isBlank(ipAddress))
            return false;
        Pattern pattern = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static String getWebClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isEmpty(ip) || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotBlank(ip)) {
                String[] ips = ip.split(",");
                for (String ipTemp : ips) {
                    if (!"null".equalsIgnoreCase(ip) && "UNKNOWN".equalsIgnoreCase(ip))
                        ip = ipTemp;
                }
            }
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN_IP.equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isEmpty(ip) || UNKNOWN_IP.equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isEmpty(ip) || UNKNOWN_IP.equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.isEmpty(ip) || UNKNOWN_IP.equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.isEmpty(ip) || UNKNOWN_IP.equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        if (log.isDebugEnabled())
            log.info("ipAddress is {}", ip);
        return ip;
    }


    public static boolean portInRange(int port) {
        return (port > 0 && port <= 65535);
    }

}
