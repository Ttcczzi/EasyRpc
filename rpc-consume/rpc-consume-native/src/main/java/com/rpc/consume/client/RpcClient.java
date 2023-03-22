package com.rpc.consume.client;

import com.rpc.common.constant.RpcConstants;
import com.rpc.consume.common.consume.RpcConsume;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.config.ProxyConfig;
import com.rpc.proxy.async.AsyncProxy;

/**
 * @author xcx
 * @date
 */
public class RpcClient {
    int port;
    String host;

    String group = "default";

    String version = "1.0.0";

    String serializationtype = RpcConstants.JDKSERIALIZATION;

    long outTime = 5000;

    public RpcClient() {
        this.host = "127.0.0.1";
        this.port = 1106;
    }

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public <T> T getSyncProxy(Class<T> interfaceClass) {
        return (T) RpcNativeConsume.getInstance().getProxyService(interfaceClass);
    }

    public <T> AsyncProxy getAsyncProxy(Class<T> interfaceClass) {
        return RpcConsume.getAsyncProxyService(interfaceClass, host, port);
    }

    public <T> AsyncProxy getAsyncProxy(Class<T> interfaceClass, AsyncCallback callback) {
        return RpcConsume.getAsyncProxyService(interfaceClass, callback, host, port);
    }

    public void close() {
        RpcConsume.close();
    }
}
