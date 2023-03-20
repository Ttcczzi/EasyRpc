package com.rpc.proxy.async;

import com.rpc.proxy.api.future.RpcFuture;

/**
 * @author xcx
 * @date
 */
public interface AsyncProxy {
    RpcFuture call(String methodName, Object ...args) throws NoSuchMethodException;
}
