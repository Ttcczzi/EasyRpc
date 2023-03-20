package com.rpc.consume.common.send;

import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.consumer.Consumer;
import com.rpc.proxy.api.future.ResponesFutures;
import com.rpc.proxy.api.future.RpcFuture;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;

/**
 * 发送消息工具类
 * @author xcx
 * @date
 */
public class SendRequest implements Consumer {

    private static Channel channel;
    private SendRequest(){

    }

    private static SendRequest sendRequest = new SendRequest();

    public static SendRequest instance(Channel targetcChannel){
        channel = targetcChannel;
        return sendRequest;
    }

    public RpcFuture sendRequestSync(RpcProtocal<RequestMessage> protocal, AsyncCallback callback) throws ExecutionException, InterruptedException {
        RpcFuture future = createFuture(protocal, callback);

        channel.writeAndFlush(protocal);

        future.get();
        return future;
    }

    public  RpcFuture sendRequestAsync(RpcProtocal<RequestMessage> protocal, AsyncCallback callback){
        RpcFuture future = createFuture(protocal, callback);

        channel.writeAndFlush(protocal);

        return future;
    }

    public void sendRequestOneWay(RpcProtocal<RequestMessage> protocal){

        channel.writeAndFlush(protocal);
    }

    private static RpcFuture createFuture(RpcProtocal<RequestMessage> protocal, AsyncCallback callback) {
        RpcFuture rpcFuture;
        if(callback == null){
            rpcFuture = new RpcFuture(protocal);
        }else{
            rpcFuture = new RpcFuture(protocal, callback);
        }

        RpcHeader header = protocal.getHeader();
        ResponesFutures.futures.put(header.getRequestId(), rpcFuture);

        return rpcFuture;
    }
}
