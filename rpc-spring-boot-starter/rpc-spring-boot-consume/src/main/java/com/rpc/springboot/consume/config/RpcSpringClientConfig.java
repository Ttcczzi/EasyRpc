package com.rpc.springboot.consume.config;

/**
 * @author xcx
 * @date
 */
public class RpcSpringClientConfig {
    private String regsitryAddress;

    private String registerType;

    public RpcSpringClientConfig() {
    }

    public RpcSpringClientConfig(String registerAddress, String registerType) {
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
}
