package com.rpc.proxy.proxyfactory;

import com.rpc.proxy.api.config.ProxyConfig;
import com.rpc.proxy.api.send.Send;
import com.rpc.proxy.proxyfactory.jdk.ProxyObjectHandler;

/**
 * 代理类生成工厂
 * 当你需要更换所代理的接口时，要重新init来修改配置
 *
 * @author xcx
 * @date
 */
public abstract class BaseProxyFactory<T> implements ProxyFactory {
    protected Send send;

    protected ProxyObjectHandler proxyObjectHandler;

    public void setConsumer(Send send) {
        this.send = send;
    }

    @Override
    public <T> void init(ProxyConfig<T> proxyConfig) {
        this.proxyObjectHandler = new ProxyObjectHandler<>(proxyConfig.isAsync(), proxyConfig.isOneway(),
                null, proxyConfig.getInterfaceClass(), proxyConfig.getVersion(), proxyConfig.getGroup(),
                proxyConfig.getConsumer(), proxyConfig.getSerializationType(), proxyConfig.getOutTime());
    }


}
