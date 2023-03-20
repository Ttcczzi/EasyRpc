package com.rpc.provider.common.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xcx
 * @date
 */
public class ConnectionHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER =  LoggerFactory.getLogger(ConnectionHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("{} 连接到服务", ctx.channel().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("{}, {}", cause.getMessage(), ctx.channel().toString());
    }
}
