package com.relive.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.relive.exception.UtilsException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @Author ReLive
 * @Date 2021/2/26-21:44
 */
@Slf4j
public class JsonUtils {
    private JsonUtils() {
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //设置时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //忽略在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static ObjectNode getObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static ArrayNode getArrayNode() {
        return objectMapper.createArrayNode();
    }

    public static String objectToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UtilsException("json转换错误");
        }
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("json数据为:{}", json);
            throw new UtilsException("json转换错误");
        }
    }

    public static ObjectNode jsonToObject(String json) {
        try {
            return objectMapper.readValue(json, ObjectNode.class);
        } catch (JsonProcessingException e) {
            log.error("json数据为:{}", json);
            throw new UtilsException("json转换错误");
        }

    }
}
