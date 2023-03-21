package com.rpc.springboot.provider.config;

/**
 * @author xcx
 * @date
 */
public class RpcSpringServerConfig {
    private String host = "127.0.0.1";
    private String regsitryAddress = "127.0.0.1";

    private String registerType = "zookeeper";

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
