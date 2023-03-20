package com.rpc.test.register;

import com.rpc.protocal.meta.ServiceMeta;
import com.rpc.register.api.config.RegistryConfig;
import com.rpc.register.zookeeper.ZookeeperRegistryService;
import com.rpc.test.interfaces.DemoInterface;

import java.util.List;

/**
 * @author xcx
 * @date
 */
public class ZookeeperRegisterTest {
    public static void main(String[] args) throws Exception {
        ZookeeperRegistryService zookeeperRegister = new ZookeeperRegistryService();
        RegistryConfig registryConfig = new RegistryConfig("127.0.0.1", "zookeeper");

        zookeeperRegister.init(registryConfig);

        zookeeperRegister.regsitry(new ServiceMeta(DemoInterface.class.getName(), "1.0.0", "test","127.0.0.1", 8999));
        ServiceMeta discover = zookeeperRegister.discover("com.rpc.test.interfaces.DemoInterface#1.0.0#test", 0);
        System.out.println(discover);
    }
}
