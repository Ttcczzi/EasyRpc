package com.rpc.serializatiion;

import com.rpc.common.constant.RpcConstants;
import com.rpc.serializatiion.impl.JdkSerilization;
import com.rpc.serializatiion.impl.JsonSerilization;
import com.rpc.serializatiion.impl.ProtostuffSerilization;

/**
 * 序列化实现类工厂：通过序列化类型返回具体序列化工具
 * @author xcx
 * @date
 */
public class SerializationFactory {
    public static Serialization getSerializationByType(String str){
        switch (str){
            case RpcConstants.JDKSERIALIZATION:
                return JdkSerilization.JDK_SERILIZATION;
            case RpcConstants.JSONSERIALIZATION:
                return JsonSerilization.JSON_SERILIZATION;
            case RpcConstants.PROTOSTUFFSERIALIZATION:
                return ProtostuffSerilization.PROTOSTUFF_SERILIZATION;
            default:
                return null;
        }
    }
}
