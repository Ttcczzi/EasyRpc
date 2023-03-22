package com.rpc.springboot.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xcx
 * @date
 */

public class RpcSpringServerConfig {
    private String host = "127.0.0.1";
    private String regsitryAddress ;
    private String registerType;

    public RpcSpringServerConfig() {
    }

    public RpcSpringServerConfig(String registerAddress, String registerType) {
        this.regsitryAddress = registerAddress;
        this.registerType = registerType;
    }

    public String getRegisterAddress() {
        return regsitryAddress;
    }

    public void setRegisterAddress(String regsitryAddress) {
        this.regsitryAddress = regsitryAddress;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
