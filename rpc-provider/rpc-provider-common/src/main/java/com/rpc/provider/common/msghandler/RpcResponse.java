package com.rpc.provider.common.msghandler;

import com.rpc.common.constant.RpcConstants;
import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.header.RpcHeaderFactory;
import com.rpc.protocal.message.HeartBeatMessage;
import com.rpc.protocal.message.ResponseMessage;

/**
 * 创建响应结果
 * @author xcx
 * @date
 */
public class RpcResponse {
    public static RpcProtocal createErrorProtocal(String serializationType, int msgType, Long requestId, Object msg){
        RpcHeader requestHeader = RpcHeaderFactory.getRequestHeader(serializationType, msgType, requestId);

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setError(RpcConstants.PROVIDER_ERROR);
        responseMessage.setResult(msg);

        RpcProtocal<ResponseMessage> protocal = new RpcProtocal<>();
        protocal.setHeader(requestHeader);
        protocal.setMessage(responseMessage);

        return protocal;
    }

    public static RpcProtocal createSuccessProtocal(String serializationType, int msgType, Long requestId,  Object msg){
        RpcHeader rpcHeader = RpcHeaderFactory.getRequestHeader(serializationType, msgType, requestId);

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setSuccess(RpcConstants.PROVIDER_SUCCESS);
        responseMessage.setResult(msg);

        RpcProtocal<ResponseMessage> protocal = new RpcProtocal<>();
        protocal.setHeader(rpcHeader);
        protocal.setMessage(responseMessage);

        return protocal;
    }

    public static RpcProtocal createHeartBeatProtocal(String serializationType, int msgType, Long requestId, String msg){
        RpcHeader rpcHeader = RpcHeaderFactory.getRequestHeader(serializationType, msgType, requestId);

        HeartBeatMessage heartBeatMessage = new HeartBeatMessage();
        heartBeatMessage.setResult(msg);

        RpcProtocal<HeartBeatMessage> heartBeatMessageRpcProtocal = new RpcProtocal<>();
        heartBeatMessageRpcProtocal.setHeader(rpcHeader);
        heartBeatMessageRpcProtocal.setMessage(heartBeatMessage);

        return heartBeatMessageRpcProtocal;
    }
}
