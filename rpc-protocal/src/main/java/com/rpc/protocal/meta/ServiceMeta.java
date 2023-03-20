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

    public ServiceMeta(String serviceName, String version, String group, String address, int port) {
        this.serviceName = serviceName;
        this.version = version;
        this.group = group;
        this.address = address;
        this.port = port;
        
    }
}
