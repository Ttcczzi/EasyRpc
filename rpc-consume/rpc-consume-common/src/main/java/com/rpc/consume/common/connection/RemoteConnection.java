package com.rpc.consume.common.connection;

import io.netty.channel.Channel;

/**
 * @author xcx
 * @date
 */
public interface RemoteConnection {
    Channel tryConnect(String host, int port);
}
