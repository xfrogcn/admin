package com.xfrog.framework.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
public class JsonHelper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 配置 ObjectMapper
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象序列化为 JSON 字符串
     *
     * @param object 要序列化的对象
     * @return 序列化后的 JSON 字符串，如果对象为 null 则返回 null
     */
    public static String serialize(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("序列化异常：", e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为对象
     *
     * @param json JSON 字符串
     * @param clazz 目标对象的类
     * @param <T> 目标对象的类型
     * @return 反序列化后的对象，如果 JSON 字符串为 null 则返回 null
     */
    public static <T> T deserialize(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            log.error("反序列化异常：", e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为对象
     *
     * @param json JSON 字符串
     * @param typeReference 目标对象的类型引用
     * @param <T> 目标对象的类型
     * @return 反序列化后的对象，如果 JSON 字符串为 null 则返回 null
     */
    public static <T> T deserialize(String json, TypeReference<T> typeReference) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("反序列化异常：", e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为对象
     *
     * @param json JSON 字符串
     * @param javaType 目标对象的 JavaType
     * @param <T> 目标对象的类型
     * @return 反序列化后的对象，如果 JSON 字符串为 null 则返回 null
     */
    public static <T> T deserialize(String json, com.fasterxml.jackson.databind.JavaType javaType) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            log.error("反序列化异常：", e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为 Map<String, Object>
     *
     * @param json JSON 字符串
     * @return 反序列化后的 Map，如果 JSON 字符串为 null 则返回 null
     */
    public static Map<String, Object> deserializeToMap(String json) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            log.error("反序列化异常：", e);
            return null;
        }
    }
}
