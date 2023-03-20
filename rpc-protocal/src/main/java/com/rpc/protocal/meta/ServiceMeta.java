package com.rpc.protocal.meta;

import java.io.Serializable;

/**
 * 服务信息类
 * @author xcx
 * @date
 */
public class ServiceMeta implements Serializable {
    private String serviceName;
    private String version;
    private String group;
    private String address;
    private int port;

    public ServiceMeta(){}

    public ServiceMeta(String serviceName, String version, String group, String address, int port) {
        this.serviceName = serviceName;
        this.version = version;
        this.group = group;
        this.address = address;
        this.port = port;
        
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServiceMeta{" +
                "serviceName='" + serviceName + '\'' +
                ", version='" + version + '\'' +
                ", group='" + group + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                '}';
    }
}
