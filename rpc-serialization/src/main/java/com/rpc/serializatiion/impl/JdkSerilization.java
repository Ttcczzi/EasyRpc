package com.rpc.serializatiion.impl;

import com.rpc.serializatiion.Serialization;
import org.apache.commons.lang3.SerializationException;

import java.io.*;

/**
 * @author xcx
 * @date
 */
public class JdkSerilization implements Serialization {
    @Override
    public <T> byte[] serialize(T obj)  {
        if(obj == null){
            throw new SerializationException("对象为空");
        }

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public <T> T dserialize(byte[] bytes, Class<T> tClass) {
        if(bytes == null || bytes.length <= 0){
            throw new SerializationException("字节数组为空");
        }

        try {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(is);

            return (T)ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
