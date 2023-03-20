package com.rpc.register.factory;

import com.rpc.register.api.RegistryService;
import com.rpc.register.api.config.RegistryConfig;
import com.rpc.register.zookeeper.ZookeeperRegistryService;

/**
 * 注册工厂，根据注册中心类型返回具体的注册对象
 * @author xcx
 * @date
 */
public class RegistryFacotry {
    public static RegistryService getRegistryImpl(RegistryConfig registryConfig) throws Exception {
        if("zookeeper".equals(registryConfig.getRegisterType())){
            RegistryService registryService = new ZookeeperRegistryService();
            registryService.init(registryConfig);
            return registryService;
        }
        return null;
    }
}
