package com.rpc.provider;

import com.rpc.provider.common.server.impl.BaseServe;
import com.rpc.common.scanner.servicescanner.RpcServiceScanner;
import com.rpc.register.api.config.RegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 提供方的对外连接服务
 * @author xcx
 * @date
 */
public class RpcSingleServer extends BaseServe {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcSingleServer.class);

    public RpcSingleServer(String scanPackage) throws IOException {
        this.handlermap = RpcServiceScanner.getClassesByRpcService(scanPackage);

        LOGGER.info(handlermap.toString());
        startNettyServe();
    }

    public RpcSingleServer(String scanPackage, RegistryConfig registryConfig) throws Exception {
        super(registryConfig);

        this.handlermap = RpcServiceScanner.getClassesByRpcService(scanPackage);
        LOGGER.info(handlermap.toString());
        //服务注册 name -- com.rpc.test.interfaces.DemoInterface#1.0.0#test
        for(String name: handlermap.keySet()){
            int index = name.lastIndexOf("#");

        }
        startNettyServe();
    }
}
