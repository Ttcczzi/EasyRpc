package com.rpc.provider.common.msghandler;

import com.rpc.common.constant.RpcConstants;
import com.rpc.common.utils.RpcServiceHelper;
import com.rpc.protocal.enumeration.Messagetype;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.message.HeartBeatMessage;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.provider.common.invokeways.ReflactWay;
import io.netty.channel.ChannelHandlerContext;
import io.protostuff.Rpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 请求消息处理
 * @author xcx
 * @date
 */
public class MsgHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgHandler.class);

    //处理正常请求
    public static void handlerRequestMessage(ChannelHandlerContext ctx, RequestMessage message, RpcHeader header, Long requestId, Map<String, Object> handlerMap){

        String interfaceName = message.getInterfaceName();
        String key = RpcServiceHelper.buildServiceKey(interfaceName, message.getVersion(), message.getGroup());
        if(!handlerMap.containsKey(key)){
            ctx.channel().
                    writeAndFlush(RpcResponse.createErrorProtocal(header.getSerializationType(), Messagetype.RESPONSE.getType(), requestId, "the interface \'" + key + "\' not fount"));
        }else{
            try {
                Object target = handlerMap.get(key);
                //todo 扩展点——使用cglib方式调用方法
                Object result = ReflactWay.invoke(target, message);

                ctx.channel().writeAndFlush(RpcResponse.createSuccessProtocal(header.getSerializationType(), Messagetype.RESPONSE.getType(), requestId,  result));
            } catch (Exception e) {
                String errorMsg = e.getMessage();
                LOGGER.error(errorMsg);
                ctx.channel().
                        writeAndFlush(RpcResponse.createErrorProtocal(header.getSerializationType(), Messagetype.RESPONSE.getType(), requestId, errorMsg));
            }
        }
    }

    //处理心跳请求
    public static void handlerHeartBeatMessage(ChannelHandlerContext ctx, HeartBeatMessage message, RpcHeader header, String host, int port){
        LOGGER.info("recevive a heartbeat request from {}", ctx.channel());

        ctx.channel().writeAndFlush(RpcResponse.createHeartBeatProtocal(header.getSerializationType(), header.getRequestId(), host.concat(String.valueOf(port)) ) );
    }

    //处理超限请求
    public static void handlerUltraLimter(ChannelHandlerContext ctx, RpcHeader header){
        ctx.writeAndFlush(RpcResponse.createUltraLimitRpotocal(header.getSerializationType(),  header.getRequestId()));
    }

}
