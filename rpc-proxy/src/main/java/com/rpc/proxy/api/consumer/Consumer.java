package com.rpc.proxy.api.consumer;

import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.future.RpcFuture;

import java.util.concurrent.ExecutionException;

public interface Consumer {
    /**
     * 消费者发送 request 请求
     */
    public RpcFuture sendRequestSync(RpcProtocal<RequestMessage> protocal, AsyncCallback callback) throws ExecutionException, InterruptedException;

    public RpcFuture sendRequestAsync(RpcProtocal<RequestMessage> protocal, AsyncCallback callback);

    public void sendRequestOneWay(RpcProtocal<RequestMessage> protocal);
}
