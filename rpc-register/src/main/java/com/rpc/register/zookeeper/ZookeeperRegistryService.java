package com.rpc.register.zookeeper;

import com.rpc.common.scanner.referencescanner.RpcReferenceScanner;
import com.rpc.common.utils.RpcServiceHelper;
import com.rpc.protocal.meta.ServiceMeta;
import com.rpc.register.api.RegistryService;
import com.rpc.register.api.config.RegistryConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author xcx
 * @date
 */
public class ZookeeperRegistryService implements RegistryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistryService.class);
    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;
    public static final String ZK_BASE_PATH = "/easyRpc";

    private ServiceDiscovery<ServiceMeta> serviceDiscovery;
    //负载均衡接口
    //private ServiceLoadBalancer<ServiceMeta> serviceLoadBalancer;

    @Override
    public void init(RegistryConfig registryConfig) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory
                .newClient(registryConfig.getRegisterAddress(),
                        new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
        this.serviceDiscovery.start();
//        this.serviceLoadBalancer = ExtensionLoader.getExtension(ServiceLoadBalancer.class, registryConfig.getRegistryLoadBalanceType());
    }

    @Override
    public void regsitry(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getVersion(), serviceMeta.getGroup()))
                .address(serviceMeta.getAddress())
                .port(serviceMeta.getPort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegistry(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getVersion(), serviceMeta.getGroup()))
                .address(serviceMeta.getAddress())
                .port(serviceMeta.getPort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public ServiceMeta discover(String serviceName, int invokerHashCode) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        return selectOne((List<ServiceInstance<ServiceMeta>>) serviceInstances).getPayload();
    }

    @Override
    public List<String> showAllServices(String serviceName) throws Exception {

            List<ServiceMeta> serviceMetaList = new ArrayList<>();
            Collection<String> names = serviceDiscovery.queryForNames();

            return (List<String>) names;

    }

    public ServiceInstance<ServiceMeta> selectOne(List<ServiceInstance<ServiceMeta>> instances){
        int len = instances.size();
        if(len > 0){
            int index = (int) (len * Math.random());
            return instances.get(index);
        }
        return null;
    }

    @Override
    public void restory() throws IOException {
        serviceDiscovery.close();
    }
}
