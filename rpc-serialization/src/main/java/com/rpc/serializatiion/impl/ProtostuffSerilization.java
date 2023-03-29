package com.rpc.serializatiion.impl;


import com.rpc.serializatiion.Serialization;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.commons.lang3.SerializationException;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xcx
 * @date
 */
public enum ProtostuffSerilization implements Serialization {
    PROTOSTUFF_SERILIZATION();

    private final Logger LOGGER = LoggerFactory.getLogger(ProtostuffSerilization.class);

    private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    //缓存Schema
    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<Class<?>, Schema<?>>();
    //序列化方法，把指定对象序列化成字节数组
    @SuppressWarnings("unchecked")
    public  <T> byte[] serialize(T obj) {
        LOGGER.info("protostuff serialize");
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        byte[] data;
        try {
            data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return data;
    }
    //反序列化方法，将字节数组反序列化成指定Class类型
    public <T> T deserilize(byte[] data, Class<T> clazz) {
        LOGGER.info("protostuff deserilize {}", clazz);
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            if (schema == null) {
                schemaCache.put(clazz, schema);
            }
        }
        return schema;
    }

}
