package com.rpc.proxy.proxyfactory;

import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.config.ProxyConfig;
import com.rpc.proxy.api.consumer.Consumer;
import com.rpc.proxy.async.AsyncProxy;
import com.rpc.proxy.proxyfactory.jdk.ProxyObjectHandler;

/**
 * 代理类生成工厂
 * 当你需要更换所代理的接口时，要重新init来修改配置
 * @author xcx
 * @date
 */
public abstract class BaseProxyFactory<T> implements ProxyFactory {

    protected Consumer consumer;

    protected ProxyObjectHandler proxyObjectHandler;

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public <T> void init(ProxyConfig<T> proxyConfig) {
        this.proxyObjectHandler = new ProxyObjectHandler<>(proxyConfig.isAsync(), proxyConfig.isOneway(),
                proxyConfig.getCallback(), proxyConfig.getInterfaceClass(), proxyConfig.getVersion(),
                proxyConfig.getGroup(), proxyConfig.getConsumer());
    }


}
