package com.rpc.register.api.config;

import java.io.Serializable;

/**
 * @author xcx
 * @date
 */
public class RegistryConfig implements Serializable {
    private String registerAddress;
    private String registerType;

    public RegistryConfig(String registerAddress, String registerType) {
        this.registerAddress = registerAddress;
        this.registerType = registerType;
    }
}
