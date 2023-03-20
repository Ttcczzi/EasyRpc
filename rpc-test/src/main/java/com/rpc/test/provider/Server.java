package com.rpc.test.provider;

import com.rpc.provider.RpcSingleServer;

import java.io.IOException;

/**
 * @author xcx
 * @date
 */
public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        RpcSingleServer rpcSingleServer = new RpcSingleServer("com.rpc.test.provider");
        rpcSingleServer.startNettyServe();

    }
}
