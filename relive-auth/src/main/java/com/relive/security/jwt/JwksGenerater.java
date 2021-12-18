/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.relive.security.jwt;

import com.nimbusds.jose.jwk.*;

import javax.crypto.SecretKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public final class JwksGenerater {

	
	public static final RSAKey DEFAULT_RSA_JWK =
			jwk(
					DefaultKeys.DEFAULT_PUBLIC_KEY,
					DefaultKeys.DEFAULT_PRIVATE_KEY
			).build();
	

	
	public static final ECKey DEFAULT_EC_JWK =
			jwk(
					(ECPublicKey) DefaultKeys.DEFAULT_EC_KEY_PAIR.getPublic(),
					(ECPrivateKey) DefaultKeys.DEFAULT_EC_KEY_PAIR.getPrivate()
			).build();
	

	
	public static final OctetSequenceKey DEFAULT_SECRET_JWK =
			jwk(
					DefaultKeys.DEFAULT_SECRET_KEY
			).build();
	

	private JwksGenerater() {
	}

	public static RSAKey.Builder jwk(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
		
		return new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyUse(KeyUse.SIGNATURE)
				.keyID("rsa-jwk-kid");
		
	}

	public static ECKey.Builder jwk(ECPublicKey publicKey, ECPrivateKey privateKey) {
		
		Curve curve = Curve.forECParameterSpec(publicKey.getParams());
		return new ECKey.Builder(curve, publicKey)
				.privateKey(privateKey)
				.keyUse(KeyUse.SIGNATURE)
				.keyID("ec-jwk-kid");
		
	}

	public static OctetSequenceKey.Builder jwk(SecretKey secretKey) {
		
		return new OctetSequenceKey.Builder(secretKey)
				.keyUse(KeyUse.SIGNATURE)
				.keyID("secret-jwk-kid");
		
	}

}
