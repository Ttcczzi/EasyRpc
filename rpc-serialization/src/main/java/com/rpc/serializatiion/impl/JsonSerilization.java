package com.rpc.serializatiion.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rpc.serializatiion.Serialization;
import org.apache.commons.lang3.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author xcx
 * @date
 */
public enum JsonSerilization implements Serialization {
    JSON_SERILIZATION();

    private static final Logger LOGGER =  LoggerFactory.getLogger(JsonSerilization.class);

    ObjectMapper mapper = new ObjectMapper();

    {
        //美观打印
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        //允许序列化空
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public <T> byte[] serialize(T obj) {
        LOGGER.info("JsonSerilization serialize ");
        if(obj == null){
            throw new SerializationException("对象为空");
        }
        byte[] bytes;
        try {
             bytes = mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @Override
    public <T> T deserilize(byte[] bytes, Class<T> tClass) {
        LOGGER.info("JsonSerilization dserialize {}", tClass);
        if(bytes == null || bytes.length <= 0){
            throw new SerializationException("字节数组为空");
        }

        T obj = null;
        try {
            obj = mapper.readValue(bytes, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
}
