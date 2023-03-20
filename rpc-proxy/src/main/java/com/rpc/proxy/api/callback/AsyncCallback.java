package com.rpc.proxy.api.callback;

import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.message.ResponseMessage;

/**
 * @author xcx
 * @date
 */
public interface AsyncCallback {
    void success(RpcProtocal<ResponseMessage> protocal);
    void defeat(RuntimeException exception);
}
