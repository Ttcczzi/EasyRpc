package com.rpc.consume.common.heartbeat;

import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.base.RpcMessage;
import com.rpc.protocal.message.HeartBeatMessage;

import io.netty.channel.Channel;

/**
 * @author xcx
 * @date
 */
public interface HeartBeat {
    void start();

    void sendHeartBeat(Channel channel, RpcProtocal<RpcMessage> protocal);
}
