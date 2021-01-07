package com.ylwq.scaffold.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

/**
 * Jackson Json序列化和反序列化工具类
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public class JsonUtil {
    /**
     * 定义Jackson对象
     */
    private static final ObjectMapper MAPPER = JsonUtil.getMapper();

    /**
     * 将属性值中的null转为空字符串""
     *
     * @return ObjectMapper
     */
    private static ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<>() {
            @Override
            public void serialize(Object paramT, JsonGenerator paramJsonGenerator,
                                  SerializerProvider paramSerializerProvider) throws IOException {
                paramJsonGenerator.writeString("");
            }
        });
        return objectMapper;
    }

    /**
     * 将对象转换成Json字符串。
     */
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Json结果集转化为对象
     *
     * @param jsonData Json数据
     * @param beanType 对象中的object类型
     */
    public static <T> T jsonToObject(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Json数据转换成pojo对象list
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
