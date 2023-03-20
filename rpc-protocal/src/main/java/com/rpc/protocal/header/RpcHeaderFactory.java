package com.rpc.protocal.header;

import com.rpc.common.constant.RpcConstants;
import com.rpc.common.id.IdFactory;

public class RpcHeaderFactory {

    public static RpcHeader getRequestHeader(String serializationType, int messageType){
        RpcHeader header = new RpcHeader();
        long requestId = IdFactory.getId();
        header.setMagic(RpcConstants.MAGIC);
        header.setRequestId(requestId);
        header.setMsgType((byte) messageType);
        header.setStatus((byte) 0x1);
        header.setSerializationType(serializationType);
        return header;
    }

    public static RpcHeader getRequestHeader(String serializationType, int messageType, Long requestId){
        RpcHeader header = new RpcHeader();
        header.setMagic(RpcConstants.MAGIC);
        header.setRequestId(requestId);
        header.setMsgType((byte) messageType);
        header.setStatus((byte) 0x1);
        header.setSerializationType(serializationType);
        return header;
    }
}