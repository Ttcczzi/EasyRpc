package com.rpc.provider.common.msghandler;

import com.rpc.common.constant.RpcConstants;
import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.enumeration.Messagetype;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.header.RpcHeaderFactory;
import com.rpc.protocal.message.HeartBeatMessage;
import com.rpc.protocal.message.ResponseMessage;
import com.rpc.protocal.message.UltraLimitMessage;

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

    public static RpcProtocal createHeartBeatProtocal(String serializationType,  Long requestId, String msg){
        RpcHeader rpcHeader = RpcHeaderFactory.getRequestHeader(serializationType, Messagetype.HEARTBEAT.getType(), requestId);

        HeartBeatMessage heartBeatMessage = new HeartBeatMessage();
        heartBeatMessage.setResult(msg);

        RpcProtocal<HeartBeatMessage> heartBeatMessageRpcProtocal = new RpcProtocal<>();
        heartBeatMessageRpcProtocal.setHeader(rpcHeader);
        heartBeatMessageRpcProtocal.setMessage(heartBeatMessage);

        return heartBeatMessageRpcProtocal;
    }


    public static RpcProtocal createUltraLimitRpotocal(String serializationType, Long requestId){
        RpcHeader rpcHeader = RpcHeaderFactory.getRequestHeader(serializationType, Messagetype.ULTRALIMIT.getType(), requestId);

        UltraLimitMessage ultraLimitMessage = new UltraLimitMessage();
        ultraLimitMessage.setError(RpcConstants.ULTRALIMIT_ERROR);

        RpcProtocal<UltraLimitMessage> protocal = new RpcProtocal<>();
        protocal.setHeader(rpcHeader);
        protocal.setMessage(ultraLimitMessage);

        return protocal;
    }
}
