package com.rpc.common.referenceinfo;

/**
 * @author xcx
 * @date
 */
public class ReferenceInfo {
    String version;

    /**
     * 分组
     */
    String group;

    /**
     * 注册中心的类型
     */
    String registryCenterType;

    /**
     * 注册中心的地址
     */
    String registryAddress;

    /**
     * 序列化类型
     */
    String serializationtype;

    /**
     * 负载均衡方式
     */
    String loadBlanceType;

    /**
     * 超时时间
     */
    long outTime;

    /**
     * 代理类型
     */
    String proxytype;

    /**
     * 是否异步执行
     */
    boolean async;

    /**
     * 是否单向调用
     */
    boolean oneway;

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

    public String getRegistryCenterType() {
        return registryCenterType;
    }

    public void setRegistryCenterType(String registryCenterType) {
        this.registryCenterType = registryCenterType;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getSerializationtype() {
        return serializationtype;
    }

    public void setSerializationtype(String serializationtype) {
        this.serializationtype = serializationtype;
    }

    public String getLoadBlanceType() {
        return loadBlanceType;
    }

    public void setLoadBlanceType(String loadBlanceType) {
        this.loadBlanceType = loadBlanceType;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public String getProxytype() {
        return proxytype;
    }

    public void setProxytype(String proxytype) {
        this.proxytype = proxytype;
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
}
