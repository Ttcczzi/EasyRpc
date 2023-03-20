package com.rpc.serializatiion;

/**
 * 消息体序列化工具
 * @author xcx
 * @date
 */
public interface Serialization {
    <T> byte[] serialize(T obj);

    <T> T dserialize(byte[] bytes, Class<T> tClass);
}
