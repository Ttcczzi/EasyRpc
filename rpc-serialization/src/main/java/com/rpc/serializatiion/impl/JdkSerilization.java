package com.rpc.serializatiion.impl;

import com.rpc.serializatiion.Serialization;
import org.apache.commons.lang3.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author xcx
 * @date
 */
public enum JdkSerilization implements Serialization {

    JDK_SERILIZATION();

    private final Logger LOGGER = LoggerFactory.getLogger(JdkSerilization.class);

    @Override
    public <T> byte[] serialize(T obj)  {
        LOGGER.info("JdkSerilization serialize");
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
    public <T> T deserilize(byte[] bytes, Class<T> tClass) {
        LOGGER.info("JdkSerilization deserilize");
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
