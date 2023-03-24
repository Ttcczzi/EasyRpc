package com.rpc.proxy.api.callback;

import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.base.RpcMessage;
import com.rpc.protocal.message.ResponseMessage;

/**
 * @author xcx
 * @date
 */
public interface AsyncCallback {
    void success(RpcProtocal<RpcMessage> protocal);
    void defeat(RuntimeException exception);
}
