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
public class RpcSpringServerAutoConfig {

    @Bean
    @ConfigurationProperties(prefix = "easyrpc.provider")
    public RpcSpringServerConfig rpcSpringServerConfig(){
        return new RpcSpringServerConfig();
    }

    @Value("server.port")
    String port;

    @Bean
    public RpcSpringProvider rpcSpringProviderServer(final RpcSpringServerConfig rpcSpringServerConfig) throws Exception {
        return
                new RpcSpringProvider(rpcSpringServerConfig.getHost() ,1106 ,rpcSpringServerConfig.getRegisterAddress(), rpcSpringServerConfig.getRegisterType());
    }
}
