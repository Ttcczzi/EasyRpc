package com.rpc.protocal.handler;


import com.rpc.serializatiion.Serialization;
import com.rpc.serializatiion.impl.JdkSerilization;

/**
 * @author xcx
 * @date
 */
public interface Code {
    default Serialization getSerialization(){
        return new JdkSerilization();
    }
}
