package com.rpc.proxy.proxyfactory.jdk;

import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.async.AsyncProxy;
import com.rpc.proxy.proxyfactory.BaseProxyFactory;
import io.netty.channel.Channel;
import net.sf.cglib.proxy.Factory;

import java.lang.reflect.Proxy;

/**
 * @author xcx
 * @date
 */
public class JdkProxyFactory<T> extends BaseProxyFactory<T>  {
    @Override
    public <T> T getProxy(Class<T> tClass) {
        try {
            Object o = Proxy.newProxyInstance(
                    proxyObjectHandler.getInterfaceClass().getClassLoader(),
                    new Class[]{proxyObjectHandler.getInterfaceClass()},
                    proxyObjectHandler
            );
            return (T) o;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
