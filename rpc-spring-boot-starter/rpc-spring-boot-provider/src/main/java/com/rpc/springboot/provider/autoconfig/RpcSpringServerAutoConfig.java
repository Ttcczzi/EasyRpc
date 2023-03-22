package com.rpc.springboot.provider.autoconfig;

import com.rpc.provider.spring.RpcSpringProvider;
import com.rpc.springboot.provider.config.RpcSpringServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xcx
 * @date
 */

@Configuration
@ConfigurationProperties("easyrpc.provider")
public class RpcSpringServerAutoConfig {

    private String host = "127.0.0.1";
    private String regsitryAddress = "127.0.0.1";
    private String registryType = "zookeeper";

    @Bean
    public RpcSpringProvider rpcSpringProviderServer() throws Exception {
        return
                new RpcSpringProvider(host ,1106 ,
                        regsitryAddress, registryType);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRegsitryAddress() {
        return regsitryAddress;
    }

    public void setRegsitryAddress(String regsitryAddress) {
        this.regsitryAddress = regsitryAddress;
    }

    public String getRegisterType() {
        return registryType;
    }

    public void setRegisterType(String registryType) {
        this.registryType = registryType;
    }
}
