package com.rpc.protocal.base;

import java.io.Serializable;

/**
 * @author xcx
 * @date
 */
public class RpcMessage implements Serializable {
    private String error;
    private String success;

    public String getError() {
        return error;
    }

    public boolean isError(){
        return error != null;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
