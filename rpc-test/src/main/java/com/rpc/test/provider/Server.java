package com.rpc.test.provider;

import com.rpc.provider.RpcSingleServer;
import com.rpc.register.api.config.RegistryConfig;

import java.io.IOException;

/**
 * @author xcx
 * @date
 */
public class Server {
    public static void main(String[] args) throws Exception {
        RpcSingleServer rpcSingleServer = new RpcSingleServer("com.rpc.test.provider", new RegistryConfig("127.0.0.1", "zookeeper"));
        rpcSingleServer.startNettyServe();

    }
}
