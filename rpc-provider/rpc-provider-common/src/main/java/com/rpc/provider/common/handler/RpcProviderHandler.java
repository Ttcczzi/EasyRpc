package com.rpc.provider.common.handler;

import com.rpc.common.constant.RpcConstants;
import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.enumeration.Messagetype;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.message.HeartBeatMessage;
import com.rpc.protocal.message.RequestMessage;
import com.rpc.provider.common.msghandler.MsgHandler;
import com.rpc.provider.common.msghandler.RpcResponse;
import com.rpc.ratelimiter.common.api.InvokeRateLimiter;
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

    private String host;

    private int port;

    private boolean enableRateLimit = true;

    private InvokeRateLimiter rateLimiter;

    public RpcProviderHandler(Map map, String host, int port, boolean enableRateLimit, InvokeRateLimiter rateLimiter) {
        this.handlermap = map;
        this.host = host;
        this.port = port;

        this.enableRateLimit = enableRateLimit;
        this.rateLimiter = rateLimiter;

        LOGGER.info("ratelimiter is loaded？{}, {}",enableRateLimit, rateLimiter);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocal<Object> protocal) throws Exception {
        LOGGER.info("提供者收到的数据为：{}", protocal.toString());

        RpcHeader header = protocal.getHeader();
        byte msgType = header.getMsgType();
        Object message = protocal.getMessage();

        //限流
        if (enableRateLimit && msgType == Messagetype.REQUEST.getType()) {
            if (!rateLimiter.tryAcquire()) {
               MsgHandler.handlerUltraLimter(ctx, header);
            }
        }

        if (msgType == Messagetype.REQUEST.getType() && message instanceof RequestMessage) {
            MsgHandler.handlerRequestMessage(ctx, (RequestMessage) message, protocal.getHeader(), header.getRequestId(), handlermap);
        } else if (msgType == Messagetype.HEARTBEAT.getType() && message instanceof HeartBeatMessage) {
            MsgHandler.handlerHeartBeatMessage(ctx, (HeartBeatMessage) message, protocal.getHeader(), host, port);
        }
    }


}
