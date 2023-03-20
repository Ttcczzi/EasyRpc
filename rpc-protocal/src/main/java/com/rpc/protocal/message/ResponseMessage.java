package com.rpc.protocal.message;

import com.rpc.protocal.base.RpcMessage;

/**
 * @author xcx
 * @date
 */
public class ResponseMessage extends RpcMessage {
    private static final long serialVersionUID = 425335064405584525L;
    private String error;
    private String success;
    private Object result;

    public boolean isError() {
        return error != null;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }
}
