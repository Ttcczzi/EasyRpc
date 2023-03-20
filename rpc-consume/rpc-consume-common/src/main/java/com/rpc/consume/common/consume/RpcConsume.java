package com.rpc.consume.common.consume;

import com.rpc.consume.common.connection.Connections;
import com.rpc.consume.common.send.SendRequest;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.config.ProxyConfig;
import com.rpc.proxy.async.AsyncProxy;
import com.rpc.proxy.proxyfactory.ProxyFactory;
import com.rpc.proxy.proxyfactory.jdk.JdkProxyFactory;
import com.rpc.proxy.proxyfactory.jdk.ProxyObjectHandler;

/**
 * 消费者
 * @author xcx
 * @date
 */
public class RpcConsume {
    private static SendRequest sendRequest ;
    private static Connections connections = new Connections();
    private static ProxyFactory proxyFactory = new JdkProxyFactory<>();

    static {
        sendRequest = SendRequest.instance(connections.getChannel());
    }
    private static class inner{
        static RpcConsume rpcConsume = new RpcConsume();
    }

    public static RpcConsume initConfig(ProxyConfig proxyConfig){
        proxyConfig.setConsumer(sendRequest);
        proxyFactory.init(proxyConfig);

        return inner.rpcConsume;
    }
    private RpcConsume(){

    }

    public <T> T getProxyService(Class<T> interfaceClass) {
        return proxyFactory.getProxy(interfaceClass);
    }

    public static <T> AsyncProxy getAsyncProxyService(Class<T> interfaceClass){
        return new ProxyObjectHandler<>(true, false, null, interfaceClass, "1.0.0", "", sendRequest);
    }

    public static <T> AsyncProxy getAsyncProxyService(Class<T> interfaceClass, AsyncCallback callback){
        return new ProxyObjectHandler<>(true, false, callback, interfaceClass, "1.0.0", "", sendRequest);
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