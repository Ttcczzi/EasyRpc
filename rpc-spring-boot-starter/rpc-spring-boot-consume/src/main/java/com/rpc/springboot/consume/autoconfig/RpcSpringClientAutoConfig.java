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
public class RpcSpringClientAutoConfig {
    @Bean
    @ConfigurationProperties(prefix = "easyrpc.consume")
    public RpcSpringClientConfig rpcSpringClientConfig() {
        return new RpcSpringClientConfig();
    }

    @Bean
    public RpcSpringComsume rpcSpringComsume(final RpcSpringClientConfig rpcSpringClientConfig) throws Exception {
        return
                new RpcSpringComsume(rpcSpringClientConfig.getRegisterAddress(), rpcSpringClientConfig.getRegisterType());
    }
}
