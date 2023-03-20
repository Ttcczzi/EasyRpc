package com.rpc.consume.common.context;

import com.rpc.proxy.api.future.RpcFuture;

/**
 * 存储当下线程的RpcFuture
 * @author xcx
 * @date
 */
public class RpcContext {
    private RpcContext() {

    }

    public static final RpcContext AGENT = new RpcContext();

    private static ThreadLocal<RpcFuture> RPCFUTURE_THREAD_LOCAL = ThreadLocal.withInitial(() -> null);

    public void setRpcFuture(RpcFuture future){
        RPCFUTURE_THREAD_LOCAL.set(future);
    }

    public RpcFuture removeRpcFuture(RpcFuture future){
        RpcFuture rpcFuture = RPCFUTURE_THREAD_LOCAL.get();
        RPCFUTURE_THREAD_LOCAL.remove();

        return rpcFuture;
    }
}
