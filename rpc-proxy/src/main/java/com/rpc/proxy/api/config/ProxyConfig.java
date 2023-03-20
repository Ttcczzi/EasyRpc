package com.rpc.proxy.api.config;

import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.consumer.Consumer;
import com.rpc.serializatiion.Serialization;

import java.io.Serializable;

/**
 * 针对一个接口的代理配置
 * @author xcx
 * @date
 */
public class ProxyConfig<T> implements Serializable {

    private static final long serialVersionUID = 6648940252795742389L;

    /**
     * 接口
     */
    private Class<T> interfaceClass;

    /**
     * 版本
     */
    private String version;

    /**
     * 分组
     */
    private String group;

    /**
     * 超时时间
     */
    private long outTime;

    /**
     * 消费者接口
     */
    private Consumer consumer;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 方法回调
     */
    private AsyncCallback callback;

    private boolean async;

    private boolean oneway;

    public ProxyConfig(Class<T> interfaceClass){
        this.interfaceClass = interfaceClass;

        this.version = "1.0.0";
        this.group = "";
        this.serializationType = "jdk";
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

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
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

    public void setCallback(AsyncCallback callback) {
        this.callback = callback;
    }

    public AsyncCallback getCallback() {
        return callback;
    }
}
