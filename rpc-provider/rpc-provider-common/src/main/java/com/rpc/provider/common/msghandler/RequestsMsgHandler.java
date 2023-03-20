package com.rpc.provider.common.msghandler;

import com.rpc.common.constant.RpcConstants;
import com.rpc.protocal.enumeration.RpcType;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.provider.common.invokeways.ReflactWay;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 请求消息处理
 * @author xcx
 * @date
 */
public class RequestsMsgHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestsMsgHandler.class);
    public static void handlerMessage(ChannelHandlerContext ctx, RequestMessage message, Long requestId, Map<String, Object> handlerMap){

        String interfaceName = message.getInterfaceName();
        String key = interfaceName.concat(message.getVersion()).concat(message.getGroup());
        if(!handlerMap.containsKey(key)){
            ctx.channel().
                    writeAndFlush(RpcResponse.createErrorProtocal(RpcConstants.JDKSERIALIZATION, RpcType.RESPONSE.getType(), requestId, "the interface \'" + key + "\' not fount"));
        }else{
            try {
                Object target = handlerMap.get(key);
                //todo 扩展点——使用cglib方式调用方法
                Object result = ReflactWay.invoke(target, message);

                ctx.channel().writeAndFlush(RpcResponse.createSuccessProtocal(RpcConstants.JDKSERIALIZATION, RpcType.RESPONSE.getType(), requestId,  result));
            } catch (Exception e) {
                String errorMsg = e.getMessage();
                LOGGER.error(errorMsg);
                ctx.channel().
                        writeAndFlush(RpcResponse.createErrorProtocal(RpcConstants.JDKSERIALIZATION, RpcType.RESPONSE.getType(), requestId, errorMsg));
            }
        }
    }
}
