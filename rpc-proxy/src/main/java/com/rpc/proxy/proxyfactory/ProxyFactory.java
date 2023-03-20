package com.rpc.proxy.proxyfactory;

import com.rpc.proxy.api.config.ProxyConfig;
import com.rpc.proxy.async.AsyncProxy;

/**
 * @author xcx
 * @date
 */
public interface ProxyFactory {
    default <T> T getProxy(Class<T> tClass){
        return null;
    };

    default <T> void init(ProxyConfig<T> proxyConfig){}


}
