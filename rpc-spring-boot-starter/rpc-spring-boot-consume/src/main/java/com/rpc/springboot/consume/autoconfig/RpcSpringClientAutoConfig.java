package com.rpc.springboot.consume.autoconfig;

import com.rpc.consume.spring.RpcSpringComsume;
import com.rpc.springboot.consume.config.RpcSpringClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xcx
 * @date
 */
@Configuration
@ConfigurationProperties("easyrpc.consume")
public class RpcSpringClientAutoConfig {

    private String regsitryAddress = "127.0.0.1";

    private String registryType = "zookeeper";

    @Bean
    public RpcSpringComsume rpcSpringComsume() throws Exception {
        return
                new RpcSpringComsume(regsitryAddress, registryType);
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
