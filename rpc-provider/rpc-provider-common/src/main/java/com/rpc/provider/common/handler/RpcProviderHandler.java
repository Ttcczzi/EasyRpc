package com.rpc.provider.common.handler;

import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.provider.common.msghandler.RequestsMsgHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 消息处理器
 *
 * @author xcx
 * @date
 */
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocal<Object>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlermap;

    public RpcProviderHandler(Map map){
        this.handlermap = map;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocal<Object> protocal) throws Exception {
        LOGGER.info("提供者收到的数据为：{}", protocal.toString());

        RpcHeader header = protocal.getHeader();
        byte msgType = header.getMsgType();
        Object message = protocal.getMessage();
        if(msgType == 1 && message instanceof RequestMessage){
            RequestsMsgHandler.handlerMessage(ctx, (RequestMessage) message, protocal.getHeader(), header.getRequestId(), handlermap);
        }
    }


}
