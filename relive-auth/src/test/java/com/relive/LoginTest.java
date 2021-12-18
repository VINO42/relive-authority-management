package com.relive;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.relive.utils.JsonUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author: ReLive
 * @date: 2021/11/25 1:35 下午
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class LoginTest {

    RestOperations restOperations;

    private static final ParameterizedTypeReference<Map<String, Object>> p = new ParameterizedTypeReference<Map<String, Object>>() {
    };


    @Test
    public void tt() {
        HttpHeaders headers=new HttpHeaders();
        headers.set("X-Access-Token","dfe799dcfd6385b02013e46d9c3d4f9d");
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectNode requestBody= JsonUtils.getObjectNode();
        requestBody.put("pageIndex", 1);
        requestBody.put("pageSize", 20);
        requestBody.put("paging", true);
        URI uri = UriComponentsBuilder.fromUriString("http://220.179.5.218:8090/api/v1/device/_detail/_query").build().toUri();
        RequestEntity requestEntity = new RequestEntity(requestBody,headers, HttpMethod.POST, uri);
        ResponseEntity<Map<String, Object>> exchange = restOperations.exchange(requestEntity, p);
        System.out.println(exchange.getBody());

    }

    @Test
    public void accessToken() {
        MessageDigest digest = DigestUtils.getMd5Digest();
        long timeMillis = System.currentTimeMillis();
        ObjectNode requestBody= JsonUtils.getObjectNode();
        requestBody.put("expires", 7200);
        digest.update(JsonUtils.objectToJson(requestBody).getBytes());
        digest.update(String.valueOf(timeMillis).getBytes());
        digest.update("NGZZMMXP5GH35heaNXHxyTDm".getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sign", Hex.encodeHexString(digest.digest()));
        headers.set("X-Client-Id", "TDHJTfEFpCp4aKHh");
        headers.set("X-Timestamp", String.valueOf(timeMillis));
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = UriComponentsBuilder.fromUriString("http://220.179.5.218:8090/api/v1/token").build().toUri();
        RequestEntity requestEntity = new RequestEntity(requestBody,headers, HttpMethod.POST, uri);
        ResponseEntity<Map<String, Object>> exchange = restOperations.exchange(requestEntity, p);
        System.out.println(exchange.getBody());
    }



    public final String transformation="AES/CBC/PKCS5Padding";
    public final String algorithm="AES";
    public final String key="wlsdfjiaxkllasdo";

    @Test
    public void t(){
        String code="L1h5BxZnZ+RvlIrSNCcCmRAVFhhOTGPgHAgQ4QnpcamJrEqZgQLbPWq7fCRsg40ETU8hS+1oCURKAKtSgg9Pj2zc9XMFZJN4vhktDKw6Sr5SY21klhQcyQp9JVJUZQUDp+pE9aIzi17yt7R21BBrhg==";
        System.out.println(decryptByAES(code));
    }

    /**
     * 解密
     * @param encrypted 需要解密的参数
     * @return
     * @throws Exception
     */
    public String decryptByAES(String encrypted) {
        try {
            // 获取Cipher
            Cipher cipher = Cipher.getInstance(transformation);
            // 生成密钥
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
            // 指定模式(解密)和密钥
            // 创建初始化向量
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            // cipher.init(Cipher.DECRYPT_MODE, keySpec);
            // 解密
            byte[] bytes = cipher.doFinal(Base64Utils.decode(encrypted.getBytes(StandardCharsets.UTF_8)));

            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
