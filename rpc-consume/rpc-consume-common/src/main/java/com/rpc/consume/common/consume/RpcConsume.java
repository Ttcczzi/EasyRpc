package com.rpc.consume.common.consume;

import com.rpc.common.constant.RpcConstants;
import com.rpc.common.referenceinfo.ReferenceInfo;
import com.rpc.common.scanner.referencescanner.RpcReferenceScanner;
import com.rpc.common.utils.RpcServiceHelper;
import com.rpc.consume.common.connection.ConnectionsPoll;
import com.rpc.consume.common.heartbeat.HeartBeatFixedTime;
import com.rpc.consume.common.sendrequest.SendRequest;
import com.rpc.protocal.meta.ServiceMeta;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.config.ProxyConfig;
import com.rpc.proxy.async.AsyncProxy;
import com.rpc.proxy.proxyfactory.ProxyFactory;
import com.rpc.proxy.proxyfactory.jdk.JdkProxyFactory;
import com.rpc.proxy.proxyfactory.jdk.ProxyObjectHandler;
import com.rpc.proxy.threadpool.CallBackThreadPool;
import com.rpc.register.api.RegistryService;
import com.rpc.register.api.config.RegistryConfig;
import com.rpc.register.factory.RegistryFacotry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消费者 ——包括扫描包，服务发现，建立连接，生成代理，返回代理实例
 *
 * @author xcx
 * @date
 */
public abstract class RpcConsume {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsume.class);
    protected String regsitryAddress;
    protected String registryType;
    protected static ProxyFactory proxyFactory = new JdkProxyFactory<>();
    //代理对象实例集合
    protected static ConcurrentHashMap<String, Object> proxyObjects = new ConcurrentHashMap<>();
    protected static HeartBeatFixedTime  heartBeatFixedTime;
    public RpcConsume(String regsitryAddress, String registryType) {
        this.regsitryAddress = regsitryAddress;
        this.registryType = registryType;

        heartBeatFixedTime = new HeartBeatFixedTime();
        heartBeatFixedTime.start();

    }

    public void serviceDiscovery(String packageName) throws Exception {
        //扫描包
        Map<Class<?>, ReferenceInfo> reference =
                new RpcReferenceScanner().getClassesByRpcReference(packageName);

        //服务发现
        for (Class<?> interfaceClass : reference.keySet()) {
            //获得注解里的信息
            ReferenceInfo referenceInfo = reference.get(interfaceClass);
            //获得用来注册服务的具体类 --zookeeper
            RegistryService registryImpl = RegistryFacotry.getRegistryImpl(new RegistryConfig(referenceInfo.getRegistryAddress(), referenceInfo.getRegistryCenterType()));
            //服务名称
            String serviceName = RpcServiceHelper.buildServiceKey(interfaceClass.getName(), referenceInfo.getVersion(), referenceInfo.getGroup());
            //远程服务的信息
            ServiceMeta discover = registryImpl.discover(serviceName, 0);
            //重置代理工厂的配置
            initConfig(new ProxyConfig(interfaceClass,
                    referenceInfo.getVersion(), referenceInfo.getGroup(),
                    referenceInfo.getOutTime(), referenceInfo.getSerializationtype(), referenceInfo.isAsync(), referenceInfo.isOneway()), discover.getAddress(), discover.getPort());

            Object proxy = proxyFactory.getProxy(interfaceClass);
            proxyObjects.put(serviceName, proxy);
        }
    }

    public void serviceDiscovery() {
    }

    /**
     * 重置代理工厂的配置
     *
     * @param proxyConfig
     * @param host
     * @param port
     * @return
     */
    protected void initConfig(ProxyConfig proxyConfig, String host, int port) {
        //根据host与port获得远程服务调用工具类
        SendRequest sendRequest = getRemoteAddress(host, port);

        proxyConfig.setConsumer(sendRequest);
        proxyFactory.init(proxyConfig);
    }

    public static SendRequest getRemoteAddress(String host, int port) {
        return SendRequest.instance(host, port);
    }


    public <T> T getProxyService(Class<T> interfaceClass, String version, String group) {
        String serviceName = RpcServiceHelper.buildServiceKey(interfaceClass.getName(), version, group);
        return (T) proxyObjects.get(serviceName);
    }

    public <T> T getProxyService(Class<T> interfaceClass) {
        String serviceName = RpcServiceHelper.buildServiceKey(interfaceClass.getName(), "1.0.0", "default");
        if(!proxyObjects.containsKey(serviceName)){

        }
        return (T) proxyObjects.get(serviceName);
    }


    public static <T> AsyncProxy getAsyncProxyService(Class<T> interfaceClass, String host, int port) {
        return new ProxyObjectHandler<>(true, false, null, interfaceClass, "1.0.0", "default", getRemoteAddress(host, port), RpcConstants.JDKSERIALIZATION, 5000L);
    }

    public static <T> AsyncProxy getAsyncProxyService(Class<T> interfaceClass, AsyncCallback callback, String host, int port) {
        return new ProxyObjectHandler<>(true, false, callback, interfaceClass, "1.0.0", "default", getRemoteAddress(host, port), RpcConstants.JDKSERIALIZATION, 5000L);
    }

    public static void close() {
        LOGGER.warn("RpcConsume close");

        ConnectionsPoll.close();

        heartBeatFixedTime.end();

        CallBackThreadPool.shutdown();
    }


    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public void setRegsitryAddress(String regsitryAddress) {
        this.regsitryAddress = regsitryAddress;
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