package com.relive.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.relive.security.jwt.JwksGenerater;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

/**
 * @author: ReLive
 * @date: 2021/11/29 10:43 上午
 */
public class JwtTest {


    @Test
    public void encodeRSAJWT() throws JOSEException {
        //私钥加密
        RSAKey rsaJwk = JwksGenerater.DEFAULT_RSA_JWK;
        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(new JWKSet(rsaJwk));
        NimbusJwtEncoder jwsEncoder = new NimbusJwtEncoder(jwkSource);
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();
        JwtClaimsSet jwtClaimsSet = TestJwtClaimsSets.jwtClaimsSet().build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);
        Jwt encodedJws = jwsEncoder.encode(jwtEncoderParameters);

        //公钥解密
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaJwk.toRSAPublicKey()).build();
        Jwt decode = jwtDecoder.decode(encodedJws.getTokenValue());
        Assertions.assertEquals(decode.getClaims(), jwtClaimsSet.getClaims());
    }

    @Test
    public void encodeJWTAndDecodeJWK() throws JOSEException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, ParseException {
        //私钥加密
        RSAKey rsaJwk = JwksGenerater.DEFAULT_RSA_JWK;
        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(new JWKSet(rsaJwk));
        NimbusJwtEncoder jwsEncoder = new NimbusJwtEncoder(jwkSource);
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();
        JwtClaimsSet jwtClaimsSet = TestJwtClaimsSets.jwtClaimsSet().build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);
        Jwt encodedJws = jwsEncoder.encode(jwtEncoderParameters);

        //jwk解密
        JWKSelector jwkSelector = new JWKSelector(new JWKMatcher.Builder().build());
        JWKSet jwkSet = new JWKSet(jwkSource.get(jwkSelector, null));
        JWK keyByKeyId = jwkSet.getKeyByKeyId("rsa-jwk-kid");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(keyByKeyId.toRSAKey().toRSAPublicKey()).build();
        Jwt decode = jwtDecoder.decode(encodedJws.getTokenValue());
        Assertions.assertEquals(decode.getClaims(), jwtClaimsSet.getClaims());
    }

    @Test
    public void encodeRSAJwtAndDecodeJwkString() throws JOSEException, ParseException {
        //私钥加密
        RSAKey rsaJwk = JwksGenerater.DEFAULT_RSA_JWK;
        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(new JWKSet(rsaJwk));
        NimbusJwtEncoder jwsEncoder = new NimbusJwtEncoder(jwkSource);
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();
        JwtClaimsSet jwtClaimsSet = TestJwtClaimsSets.jwtClaimsSet().build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);
        Jwt encodedJws = jwsEncoder.encode(jwtEncoderParameters);

        //jwk json字符串解密
        JWKSelector jwkSelector = new JWKSelector(new JWKMatcher.Builder().build());
        JWKSet jwkSet = new JWKSet(jwkSource.get(jwkSelector, null));
        JWKSet parse = JWKSet.parse(jwkSet.toString());
        JWK keyByKeyId1 = parse.getKeyByKeyId("rsa-jwk-kid");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(keyByKeyId1.toRSAKey().toRSAPublicKey()).build();
        Jwt decode = jwtDecoder.decode(encodedJws.getTokenValue());
        Assertions.assertEquals(decode.getClaims(), jwtClaimsSet.getClaims());
    }

}

