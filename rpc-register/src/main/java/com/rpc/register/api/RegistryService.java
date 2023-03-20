package com.rpc.register.api;

import com.rpc.protocal.meta.ServiceMeta;
import com.rpc.register.api.config.RegistryConfig;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @author xcx
 * @date
 */
public interface RegistryService {

    void regsitry(ServiceMeta serviceMeta) throws Exception;

    void unRegistry(ServiceMeta serviceMeta) throws Exception;

    ServiceMeta discover(String serviceName, int invokerHashCode) throws Exception;

    List<String> showAllServices(String serviceName) throws Exception;

    void restory() throws IOException;

    default void init(RegistryConfig registryConfig) throws Exception {

    }
}
