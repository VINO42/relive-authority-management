package com.relive.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ReLive
 * @Date 2021/2/26-22:18
 */
public class JwtUtils {
    private JwtUtils() {
    }

    private static Map<String, JWTVerifier> verifierMap = new HashMap<>();

    private static final String DEFAULT_SIGN_KEY = "relive-code";

    public static DecodedJWT verifyToken(String tokenString, String signingKey) {
        if (StringUtils.isEmpty(signingKey)) {
            signingKey = DEFAULT_SIGN_KEY;
        }
        JWTVerifier verifier = verifierMap.get(signingKey);
        if (verifier == null) {
            synchronized (verifierMap) {
                verifier = verifierMap.get(signingKey);
                if (verifier == null) {
                    Algorithm algorithm = Algorithm.HMAC256(signingKey);
                    verifier = JWT.require(algorithm).build();
                    verifierMap.put(signingKey, verifier);
                }
            }
        }
        DecodedJWT verify = verifier.verify(tokenString);
        return verify;
    }

    public static String generateToken(Map<String, Object> userInfo, long expireTime) {
        return generateToken(userInfo, expireTime, DEFAULT_SIGN_KEY);
    }

    public static String generateToken(Map<String, Object> userInfo, long expireTime, String signKey) {
        Date date = new Date(System.currentTimeMillis() + expireTime);
        // 私钥和加密算法
        Algorithm algorithm = Algorithm.HMAC256(signKey);
        // 设置头部信息
        Map<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        JWTCreator.Builder builder = JWT.create().withHeader(header).withExpiresAt(date);
        // 返回token字符串
        userInfo.keySet().stream().forEach(key -> {
            if (userInfo.get(key) instanceof String) {
                builder.withClaim(key, (String) userInfo.get(key));
            }
            if (userInfo.get(key) instanceof Long) {
                builder.withClaim(key, (Long) userInfo.get(key));
            }
            if (userInfo.get(key) instanceof List) {
                builder.withClaim(key, (List<?>) userInfo.get(key));
            }
            if (userInfo.get(key) instanceof Boolean) {
                builder.withClaim(key, (Boolean) userInfo.get(key));
            }
        });
        return builder.sign(algorithm);
    }

    public static DecodedJWT decodedJWT(String token) {
        DecodedJWT decode = JWT.decode(token);
        return decode;
    }

    public static String getUsername(String token) {
        DecodedJWT jwt = decodedJWT(token);
        return jwt.getClaim("username").asString();
    }

    public static Long getId(String token) {
        DecodedJWT jwt = decodedJWT(token);
        return jwt.getClaim("id").asLong();
    }


    public static List<String> getRoles(String token) {
        DecodedJWT jwt = decodedJWT(token);
        return jwt.getClaim("roles").asList(String.class);
    }

    public static List<String> getAuthorities(String token) {
        DecodedJWT jwt = decodedJWT(token);
        return jwt.getClaim("authorities").asList(String.class);
    }

    public static <T> T getCustomColumn(String token, String column, Class<T> clazz) {
        DecodedJWT jwt = decodedJWT(token);
        return jwt.getClaim(column).as(clazz);
    }
}
