package com.relive.utils;

import com.relive.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA非对称加密
 *
 * @Author ReLive
 * @Date 2021/3/26-21:52
 */
@Slf4j
public class RSAEncrypt {
    private RSAEncrypt() {
    }

    private static final String ALGORITHM = "RSAEncrypt";

    private static final String SIGNATURE_ALGORITHM = "sha256withrsa";

    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final int MAX_ENCRYPT_BLOCK = 117;

    private static final String CHARSET = "UTF-8";

    public static String decrypt(String publicKey, String content) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(publicKey))
            return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            PublicKey key = getPublicKey(publicKey);
            cipher.init(2, key);
            byte[] decode = Base64.decodeBase64(content);
            byte[] decryptData = piecewise(decode, MAX_DECRYPT_BLOCK, cipher);
            return new String(decryptData, Charset.forName(CHARSET));
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("");
        }
    }

    public static String encrypt(String privateKey, String content) {
        if (StringUtils.isEmpty(content))
            return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            PrivateKey key = getPrivateKey(privateKey);
            cipher.init(1, key);
            byte[] encryptData = piecewise(content.getBytes(), MAX_ENCRYPT_BLOCK, cipher);
            return Base64.encodeBase64String(encryptData);
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("");
        }
    }

    private static byte[] piecewise(byte[] content, int maxBlock, Cipher cipher) {
        int length = content.length;
        int offset = 0;
        int i = 0;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                while (length - offset > 0) {
                    byte[] cache;
                    if (length - offset > maxBlock) {
                        cache = cipher.doFinal(content, offset, maxBlock);
                    } else {
                        cache = cipher.doFinal(content, offset, length - offset);
                    }
                    outputStream.write(cache, 0, cache.length);
                    i++;
                    offset = i * maxBlock;
                }
                return outputStream.toByteArray();
            } finally {
                if (Collections.<ByteArrayOutputStream>singletonList(outputStream).get(0) != null)
                    outputStream.close();
            }
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("");
        }
    }

    public static PublicKey getPublicKey(String publicKey) {
        if (StringUtils.isEmpty(publicKey))
            return null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("");
        }
    }

    public static PrivateKey getPrivateKey(String privateKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("");
        }
    }

    public static void generateKeyPair(String priPath, String pubPath) {
        Map<String, String> map = generateKeyPairMap();
        String privateKey = null;
        String publicKey = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            privateKey = entry.getKey();
            publicKey = entry.getValue();
        }
        try {
            FileUtils.writeStringToFile(new File(priPath), privateKey, Charset.forName(CHARSET));
            FileUtils.writeStringToFile(new File(pubPath), publicKey, Charset.forName(CHARSET));
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("");
        }
    }

    public static Map<String, String> generateKeyPairMap() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            byte[] privateKeyEncoded = privateKey.getEncoded();
            byte[] publicKeyEncoded = publicKey.getEncoded();
            String privateEncode = Base64.encodeBase64String(privateKeyEncoded);
            String publicEncode = Base64.encodeBase64String(publicKeyEncoded);
            Map<String, String> map = new HashMap<>();
            map.put(privateEncode, publicEncode);
            return map;
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("");
        }
    }

    public static boolean verifySignature(String content, String publicKey, String signatureData) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            PublicKey key = getPublicKey(publicKey);
            signature.initVerify(key);
            signature.update(content.getBytes());
            return signature.verify(Base64.decodeBase64(signatureData));
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("");
        }
    }

    public static String getSignature(String privateKey, String content) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(getPrivateKey(privateKey));
            signature.update(content.getBytes());
            byte[] sign = signature.sign();
            return Base64.encodeBase64String(sign);
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("");
        }
    }
}
