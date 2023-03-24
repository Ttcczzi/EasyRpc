package com.rpc.consume.common.sendrequest;

import com.rpc.consume.common.connection.ConnectionsPoll;
import com.rpc.consume.common.heartbeat.HeartBeatFixedTime;
import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.base.RpcMessage;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.send.Send;
import com.rpc.proxy.api.future.FuturesSet;
import com.rpc.proxy.api.future.RpcFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * 发送消息工具类
 *
 * @author xcx
 * @date
 */
public class SendRequest implements Send {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendRequest.class);
    private static String remoteHost;

    private static int remotePort;

    private Channel channel;

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    private SendRequest() {

    }

    private static ConcurrentHashMap<String, SendRequest> sendRequestPool = new ConcurrentHashMap();
    //private static HashMap<String, SendRequest> sendRequestPool = new HashMap();

    public static SendRequest instance(String host, int port) {
        String key = host.concat(":").concat(String.valueOf(port));
        if (sendRequestPool.containsKey(key)) {
            return sendRequestPool.get(key);
        }

        SendRequest sendRequest = new SendRequest();
        sendRequest.setChannel(ConnectionsPoll.getChannel(key, host, port));
        sendRequestPool.putIfAbsent(key, sendRequest);

        remoteHost = host;
        remotePort = port;

        return sendRequest;
    }

    public RpcFuture sendRequestSync(RpcProtocal<RpcMessage> protocal, AsyncCallback callback) throws ExecutionException, InterruptedException {
        if (! channel.isWritable()){
            channel = reconnect();
        }

        RpcFuture future = createFuture(protocal, callback);

        channel.writeAndFlush(protocal);

        future.get();

        return future;
    }

    public RpcFuture sendRequestAsync(RpcProtocal<RpcMessage> protocal, AsyncCallback callback) {
        if (! channel.isWritable()){
            channel = reconnect();
        }

        RpcFuture future = createFuture(protocal, callback);

        channel.writeAndFlush(protocal);

        return future;
    }

    public void sendRequestOneWay(RpcProtocal<RpcMessage> protocal) {
        if (! channel.isWritable()){
            channel = reconnect();
        }

        channel.writeAndFlush(protocal);
    }

    public static RpcFuture createFuture(RpcProtocal<RpcMessage> protocal, AsyncCallback callback) {
        RpcFuture rpcFuture;
        if (callback == null) {
            rpcFuture = new RpcFuture(protocal);
        } else {
            rpcFuture = new RpcFuture(protocal, callback);
        }

        RpcHeader header = protocal.getHeader();
        FuturesSet.futures.put(header.getRequestId(), rpcFuture);

        return rpcFuture;
    }

    public Channel reconnect() {
        LOGGER.warn("the channel {}:{} try to reconnect", remoteHost, remotePort);
        String key = remoteHost.concat(":").concat(String.valueOf(remoteHost));
        return ConnectionsPoll.reconnect(key, remoteHost, remotePort);
    }
}
