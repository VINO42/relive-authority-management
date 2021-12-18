package com.relive.security.jwt;

import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.InitializingBean;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;
import java.util.List;

/**
 * @author: ReLive
 * @date: 2021/11/29 10:54 上午
 */
public final class RotateJWKSource implements JWKSource<SecurityContext>, InitializingBean {

    private int keyId = 1000;

    private JWKSet jwkSet;

    public RotateJWKSource(){
        init();
    }

    @Override
    public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) {
        return jwkSelector.select(this.jwkSet);
    }

    private void init() {
        //TODO 后续更改为动态分配
        RSAKey rsaJwk = JwksGenerater.jwk(DefaultKeys.DEFAULT_PUBLIC_KEY, DefaultKeys.DEFAULT_PRIVATE_KEY)
                .keyID("rsa-jwk-kid")
                .build();
        ECKey ecJwk = JwksGenerater.jwk((ECPublicKey) DefaultKeys.DEFAULT_EC_KEY_PAIR.getPublic(), (ECPrivateKey) DefaultKeys.DEFAULT_EC_KEY_PAIR.getPrivate())
                .keyID("ec-jwk-" + this.keyId++)
                .build();
        OctetSequenceKey secretJwk = JwksGenerater.jwk(DefaultKeys.DEFAULT_SECRET_KEY)
                .keyID("secret-jwk-" + this.keyId++)
                .build();
        this.jwkSet = new JWKSet(Arrays.asList(rsaJwk, ecJwk, secretJwk));
    }

    public void rotate() {
        init();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
