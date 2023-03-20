package com.rpc.register.api;

import com.rpc.protocal.meta.ServiceMeta;
import com.rpc.register.api.config.RegistryConfig;

import java.io.Serializable;

/**
 * @author xcx
 * @date
 */
public interface RegistryService {

    void regsitry(ServiceMeta serviceMeta);

    void unRegistry(Serializable serializable);

    ServiceMeta discover(String serviceName, int invokerHashCode);

    void restory();

    default void init(RegistryConfig registryConfig){

    }
}
