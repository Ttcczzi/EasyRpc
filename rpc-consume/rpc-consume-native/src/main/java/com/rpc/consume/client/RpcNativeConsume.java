package com.rpc.consume.client;

import com.rpc.consume.common.consume.RpcConsume;

/**
 * @author xcx
 * @date
 */
public class RpcNativeConsume extends RpcConsume {
    private RpcNativeConsume(){}

    private static class inner{
        public static RpcNativeConsume consume = new RpcNativeConsume();
    }

    public static RpcNativeConsume getInstance(){
        return inner.consume;
    }
}
