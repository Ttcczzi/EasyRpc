package com.rpc.proxy.proxyfactory.jdk;

import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.base.RpcMessage;
import com.rpc.protocal.enumeration.Messagetype;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.header.RpcHeaderFactory;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.send.Send;
import com.rpc.proxy.api.future.RpcFuture;
import com.rpc.proxy.async.AsyncProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author xcx
 * @date
 */
public class ProxyObjectHandler<T> implements InvocationHandler, AsyncProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyObjectHandler.class);
    private boolean async = false;
    private boolean oneway = false;
    private AsyncCallback callback;
    private Class<T> interfaceClass;
    private String version;
    private String group;
    private Send send;
    private String serializationType;

    private Long outTime;

    public ProxyObjectHandler(boolean async, boolean oneway,
                              AsyncCallback callback, Class<T> interfaceClass,
                              String version, String group, Send send, String serializationType, Long outTime) {
        this.async = async;
        this.oneway = oneway;
        this.callback = callback;
        this.interfaceClass = interfaceClass;
        this.version = version;
        this.group = group;
        this.send = send;
        this.serializationType = serializationType;
        this.outTime = outTime;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcProtocal<RpcMessage> protocal = createProtocal(interfaceClass, method.getName(), method.getParameterTypes(), args, group, version);
        RpcFuture rpcFuture = send.sendRequestSync(protocal, null);

        if (rpcFuture != null && rpcFuture.isDone()) {
            return rpcFuture.get();
        }

        return null;
    }

    @Override
    public RpcFuture call(String methodName, Object... args) throws NoSuchMethodException {
        Method method = interfaceClass.getMethod(methodName);
        RpcProtocal<RpcMessage> protocal = createProtocal(interfaceClass, methodName, method.getParameterTypes(), args, group, version);
        RpcFuture rpcFuture = null;
        try{
            rpcFuture = send.sendRequestAsync(protocal, callback);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return rpcFuture;
    }

    private RpcProtocal<RpcMessage> createProtocal(Class<?> interfaceClass, String methodName, Class<?>[] paramsTypes, Object[] args, String group, String version) {
        RpcProtocal<RpcMessage> protocal = new RpcProtocal<>();

        RpcHeader requestHeader = RpcHeaderFactory.getRequestHeader(serializationType, Messagetype.REQUEST.getType());
        Long id = requestHeader.getRequestId();

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setVersion(version);
        requestMessage.setGroup(group);
        requestMessage.setInterfaceName(interfaceClass.getName());
        requestMessage.setMethodName(methodName);
        requestMessage.setParamsType(paramsTypes);
        requestMessage.setParams(args);

        protocal.setHeader(requestHeader);
        protocal.setMessage(requestMessage);

        return protocal;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public AsyncCallback getCallback() {
        return callback;
    }

    public void setCallback(AsyncCallback callback) {
        this.callback = callback;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Send getConsumer() {
        return send;
    }

    public void setConsumer(Send send) {
        this.send = send;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        serializationType = serializationType;
    }
}
