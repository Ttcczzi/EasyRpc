package com.rpc.consume.client;

import com.rpc.consume.common.consume.RpcConsume;

/**
 * @author xcx
 * @date
 */
public class RpcNativeConsume extends RpcConsume {
    private RpcNativeConsume(String registryAddress, String registryType){
        super(registryAddress, registryType);

    }

   static RpcNativeConsume rpcNativeConsume;

    public static RpcNativeConsume getInstance(String registryAddress, String registryType){
        if(rpcNativeConsume == null){
            rpcNativeConsume = new RpcNativeConsume( registryAddress,  registryType);
        }

        return rpcNativeConsume;
    }

}
