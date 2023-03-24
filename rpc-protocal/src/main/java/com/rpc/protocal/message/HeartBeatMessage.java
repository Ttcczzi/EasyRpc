package com.rpc.protocal.message;

import com.rpc.protocal.base.RpcMessage;

/**
 * @author xcx
 * @date
 */
public class HeartBeatMessage extends RpcMessage {
    private Object result;

    private String channelKey;

    public HeartBeatMessage(){

    }

    public void setChannelKey(String channelKey) {
        this.channelKey = channelKey;
    }

    public String getChannelKey() {
        return channelKey;
    }

    public HeartBeatMessage(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object data) {
        this.result = data;
    }
}
