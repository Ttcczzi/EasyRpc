package com.rpc.consume.common.connection;

import com.rpc.common.threadpool.CallBackThreadPool;
import com.rpc.consume.common.handler.RpcConsumerHandler;
import com.rpc.protocal.handler.RpcCode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 与提供方建立连接
 * @author xcx
 * @date
 */
public class Connections implements RemoteConnection{
    private static Channel channel = null;

    private static final Object LOCK = new Object();

    // 获取唯一的 channel 对象
    public  Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (LOCK) {
            if (channel != null) {
                return channel;
            }
            channel = tryConnect();
            return channel;
        }
    }

    private static NioEventLoopGroup group = new NioEventLoopGroup();

    private static Bootstrap bootstrap;

    public Channel tryConnect() {
        return tryConnect("127.0.0.1", 1106);
    }

    public Channel tryConnect(String address, int port) {
        try {
            bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new RpcCode());
                    ch.pipeline().addLast(new RpcConsumerHandler());
                }
            });
            Channel channel = bootstrap.connect(address, port).sync().channel();

            channel.closeFuture().addListener(future -> {

                CallBackThreadPool.shutdown();
                group.shutdownGracefully();
            });
            return channel;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (channel != null){
            channel.close();
        }
    }
}
