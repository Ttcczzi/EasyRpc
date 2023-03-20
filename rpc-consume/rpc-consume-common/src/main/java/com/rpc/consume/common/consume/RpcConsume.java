package com.rpc.consume.common.consume;

import com.rpc.annotation.RpcReference;
import com.rpc.common.referenceinfo.ReferenceInfo;
import com.rpc.common.scanner.referencescanner.RpcReferenceScanner;
import com.rpc.common.utils.RpcServiceHelper;
import com.rpc.consume.common.connection.Connections;
import com.rpc.consume.common.send.SendRequest;
import com.rpc.protocal.meta.ServiceMeta;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.config.ProxyConfig;
import com.rpc.proxy.async.AsyncProxy;
import com.rpc.proxy.proxyfactory.ProxyFactory;
import com.rpc.proxy.proxyfactory.jdk.JdkProxyFactory;
import com.rpc.proxy.proxyfactory.jdk.ProxyObjectHandler;
import com.rpc.register.api.RegistryService;
import com.rpc.register.api.config.RegistryConfig;
import com.rpc.register.factory.RegistryFacotry;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消费者
 *
 * @author xcx
 * @date
 */
public class RpcConsume {

    private static Connections connections = new Connections();
    private static ProxyFactory proxyFactory = new JdkProxyFactory<>();

    private static ConcurrentHashMap<String, Object> proxyObjects = new ConcurrentHashMap<>();

    private static class inner {
        static RpcConsume rpcConsume = new RpcConsume();

    }

    public static RpcConsume getInstance(){
        return inner.rpcConsume;
    }

    public static void serviceDiscovery(String packageName) throws Exception {
        //扫描包

        Map<Class<?>, ReferenceInfo> reference =
                new RpcReferenceScanner().getClassesByRpcReference(packageName);
        //服务发现
        for (Class<?> interfaceClass : reference.keySet()) {
            ReferenceInfo referenceInfo = reference.get(interfaceClass);

            RegistryService registryImpl = RegistryFacotry.getRegistryImpl(new RegistryConfig(referenceInfo.getRegistryAddress(), referenceInfo.getRegistryCenterType()));

            String serviceName = RpcServiceHelper.buildServiceKey(interfaceClass.getName(), referenceInfo.getVersion(), referenceInfo.getGroup());

            ServiceMeta discover = registryImpl.discover(serviceName, 0);

            //重置代理工厂
            initConfig(new ProxyConfig(interfaceClass,
                    referenceInfo.getVersion(), referenceInfo.getGroup(),
                    referenceInfo.getOutTime(), referenceInfo.getSerializationtype(), referenceInfo.isAsync(), referenceInfo.isOneway()), discover.getAddress(), discover.getPort());

            proxyObjects.put(serviceName, proxyFactory.getProxy(interfaceClass));
        }
    }

    public static RpcConsume initConfig(ProxyConfig proxyConfig, String host, int port) {
        SendRequest sendRequest = getRemoteAddress(host, port);

        proxyConfig.setConsumer(sendRequest);
        proxyFactory.init(proxyConfig);
        return inner.rpcConsume;
    }

    public static SendRequest getRemoteAddress(String host, int port) {
        return SendRequest.instance(host, port);
    }

    private RpcConsume() {

    }

    public <T> T getProxyService(Class<T> interfaceClass, String version, String group) {
        String serviceName = RpcServiceHelper.buildServiceKey(interfaceClass.getName(), version, group);
        return (T)proxyObjects.get(serviceName);
    }

    public <T> T getProxyService(Class<T> interfaceClass) {
        String serviceName = RpcServiceHelper.buildServiceKey(interfaceClass.getName(), "1.0.0", "default");
        return (T)proxyObjects.get(serviceName);
    }

    public static <T> AsyncProxy getAsyncProxyService(Class<T> interfaceClass, String host, int port) {
        return new ProxyObjectHandler<>(true, false, null, interfaceClass, "1.0.0", "", getRemoteAddress(host, port));
    }

    public static <T> AsyncProxy getAsyncProxyService(Class<T> interfaceClass, AsyncCallback callback, String host, int port) {
        return new ProxyObjectHandler<>(true, false, callback, interfaceClass, "1.0.0", "", getRemoteAddress(host, port));
    }

    public static void close() {
        connections.close();
    }
}

//channelFuture.get();
//channelFuture.get(); 无用，因为这只是发送数据，发送与对方的返回是两个事件，该操作并不关心返回值有没有到达，值关系有没有发送成功，
//所以即使调用get()阻塞也不能保证能拿到返回值，只能等接收方收到结果再唤醒该线程将值返回给调用者

//DefaultPromise defaultPromise = new DefaultPromise(currentChannel.eventLoop());
//RpcConsumerHandler.PROMISES.put(id, defaultPromise);

////等待RpcResponseMessageHandler的结果
//RpcConsumerHandler.WAIT_FOR_COEECTION.await();
//defaultPromise.await();
//if (defaultPromise.isSuccess()) {
//    return (T) defaultPromise.getNow();
//} else {
//    throw new RuntimeException(defaultPromise.cause());
//}