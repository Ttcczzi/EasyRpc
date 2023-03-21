package com.rpc.proxy.api.future;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Future集合
 * @author xcx
 * @date
 */
public class ResponesFutures {
    public static ConcurrentHashMap<Long, RpcFuture> futures= new ConcurrentHashMap<>();
}
