package com.relive.security.authentication;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: ReLive
 * @date: 2021/11/30 12:31 下午
 */
@Setter
public class DefaultAccessTokenConverter implements AccessTokenConverter {
    private JWKSource<SecurityContext> jwkSource;

    public DefaultAccessTokenConverter(JWKSource<SecurityContext> jwkSource) {
        this.jwkSource = jwkSource;
    }

    public DefaultAccessTokenConverter() {
    }

    @Override
    public AccessTokenResponse convert(Authentication authentication) {
        NimbusJwtEncoder jwsEncoder = new NimbusJwtEncoder(jwkSource);
        JwsHeader joseHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();
        //TODO 过期时间改为配置
        JwtClaimsSet accessTokenJwtClaimsSet = jwtClaimsSet(authentication, 60).build();
        Jwt accessToken = jwsEncoder.encode(JwtEncoderParameters.from(joseHeader, accessTokenJwtClaimsSet));
        JwtClaimsSet refreshTokenJwtClaimsSet = jwtClaimsSet(authentication, 70).build();
        Jwt refreshToken = jwsEncoder.encode(JwtEncoderParameters.from(joseHeader, refreshTokenJwtClaimsSet));
        return AccessTokenResponse.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(refreshToken.getTokenValue())
                .tokenType("Bearer")
                .jtl(UUID.randomUUID().toString())
                .build();
    }

    private JwtClaimsSet.Builder jwtClaimsSet(Authentication authentication, Integer expireTime) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(expireTime, ChronoUnit.MINUTES);
        Map<String, Object> claims = new HashMap<>();
        claims.put("principal", authentication.getPrincipal());
        return JwtClaimsSet.builder()
                .issuer("http://localhost:8097")
                .subject("user")
                .audience(Collections.singletonList("client-1"))
                .issuedAt(issuedAt)
                .notBefore(issuedAt)
                .expiresAt(expiresAt)
                .id("jti")
                .claims((claim) -> claim.putAll(claims));
    }
}
