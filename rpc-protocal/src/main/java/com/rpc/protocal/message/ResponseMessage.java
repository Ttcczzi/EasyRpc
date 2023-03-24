package com.rpc.protocal.message;

import com.rpc.protocal.base.RpcMessage;

/**
 * @author xcx
 * @date
 */
public class ResponseMessage extends RpcMessage {
    private static final long serialVersionUID = 425335064405584525L;
    private Object result;


    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


}
