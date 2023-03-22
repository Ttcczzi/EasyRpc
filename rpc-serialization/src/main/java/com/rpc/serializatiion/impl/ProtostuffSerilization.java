package com.rpc.serializatiion.impl;

import com.rpc.serializatiion.Serialization;

/**
 * @author xcx
 * @date
 */
public enum ProtostuffSerilization implements Serialization {
    PROTOSTUFF_SERILIZATION();

    @Override
    public <T> byte[] serialize(T obj) {
        return new byte[0];
    }

    @Override
    public <T> T dserialize(byte[] bytes, Class<T> tClass) {
        return null;
    }
}
