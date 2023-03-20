package com.rpc.serializatiion;

import com.rpc.common.constant.RpcConstants;
import com.rpc.serializatiion.impl.JdkSerilization;

/**
 * 序列化实现类工厂：通过序列化类型返回具体序列化工具
 * @author xcx
 * @date
 */
public class SerializationFactory {
    public static Serialization getSerializationByType(String str){
        switch (str){
            case RpcConstants.JDKSERIALIZATION:
                return new JdkSerilization();
            default:
                return null;
        }
    }
}
