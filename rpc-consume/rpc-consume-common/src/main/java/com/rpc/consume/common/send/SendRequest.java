package com.rpc.consume.common.send;

import com.rpc.consume.common.connection.ConnectionsPoll;
import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.consumer.Consumer;
import com.rpc.proxy.api.future.ResponesFutures;
import com.rpc.proxy.api.future.RpcFuture;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * 发送消息工具类
 *
 * @author xcx
 * @date
 */
public class SendRequest implements Consumer {
    private Channel channel;

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    private SendRequest() {

    }

    private static ConcurrentHashMap<String, SendRequest> sendRequestPool = new ConcurrentHashMap();


    public static SendRequest instance(String host, int port) {
        String key = host.concat(":").concat(String.valueOf(port));
        if (sendRequestPool.contains(key)) {
            return sendRequestPool.get(key);
        }

        SendRequest sendRequest = new SendRequest();
        sendRequest.setChannel(ConnectionsPoll.getChannel(key, host, port));
        sendRequestPool.putIfAbsent(key, sendRequest);

        return sendRequest;
    }

    public RpcFuture sendRequestSync(RpcProtocal<RequestMessage> protocal, AsyncCallback callback) throws ExecutionException, InterruptedException {
        RpcFuture future = createFuture(protocal, callback);

        channel.writeAndFlush(protocal);

        future.get();
        return future;
    }

    public RpcFuture sendRequestAsync(RpcProtocal<RequestMessage> protocal, AsyncCallback callback) {
        RpcFuture future = createFuture(protocal, callback);

        channel.writeAndFlush(protocal);

        return future;
    }

    public void sendRequestOneWay(RpcProtocal<RequestMessage> protocal) {

        channel.writeAndFlush(protocal);
    }

    private static RpcFuture createFuture(RpcProtocal<RequestMessage> protocal, AsyncCallback callback) {
        RpcFuture rpcFuture;
        if (callback == null) {
            rpcFuture = new RpcFuture(protocal);
        } else {
            rpcFuture = new RpcFuture(protocal, callback);
        }

        RpcHeader header = protocal.getHeader();
        ResponesFutures.futures.put(header.getRequestId(), rpcFuture);

        return rpcFuture;
    }
}
