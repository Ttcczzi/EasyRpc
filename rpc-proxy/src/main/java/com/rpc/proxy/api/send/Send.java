package com.rpc.proxy.api.send;

import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.base.RpcMessage;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.future.RpcFuture;

import java.util.concurrent.ExecutionException;

public interface Send {
    /**
     * 消费者发送 request 请求
     */
    public RpcFuture sendRequestSync(RpcProtocal<RpcMessage> protocal, AsyncCallback callback) throws ExecutionException, InterruptedException;

    public RpcFuture sendRequestAsync(RpcProtocal<RpcMessage> protocal, AsyncCallback callback);

    public void sendRequestOneWay(RpcProtocal<RpcMessage> protocal);
}
