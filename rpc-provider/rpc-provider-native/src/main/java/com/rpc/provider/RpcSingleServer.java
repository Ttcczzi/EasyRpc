package com.rpc.provider;

import com.rpc.provider.common.server.impl.BaseServe;
import com.rpc.common.scanner.servicescanner.RpcServiceScanner;
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
}
