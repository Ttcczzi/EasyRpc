package com.rpc.protocal;

import com.rpc.protocal.base.RpcMessage;
import com.rpc.protocal.header.RpcHeader;

import java.io.Serializable;

/**
 * @author xcx
 * @date
 */
public class RpcProtocal<T> implements Serializable {
    private static final long serialVersionUID = 292789485166173277L;

    private RpcHeader header;

    private T message;

    public RpcHeader getHeader() {
        return header;
    }

    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RpcProtocal{" +
                "header=" + header +
                ", message=" + message +
                '}';
    }
}
