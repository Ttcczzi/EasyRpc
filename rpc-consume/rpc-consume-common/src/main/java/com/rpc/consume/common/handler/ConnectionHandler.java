package com.rpc.consume.common.handler;

import com.rpc.consume.common.connection.ConnectionsPoll;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER =  LoggerFactory.getLogger(ConnectionHandler.class);

    private String host;
    private int port;

    public ConnectionHandler(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("{} 连接到服务", ctx.channel().toString());
        String key = host.concat(":").concat(String.valueOf(port));
        Channel channel = ctx.channel();

        //在连接池中添加channel
        ConnectionsPoll.putChannel(key, host, port, channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("{}, {}", cause.getMessage(), ctx.channel().toString());
        String key = host.concat(":").concat(String.valueOf(port));

        //删除连接池的channel
        ConnectionsPoll.disconnect(key);
    }
}