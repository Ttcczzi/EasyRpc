package com.rpc.consume.common.connection;

import com.rpc.common.threadpool.CallBackThreadPool;
import com.rpc.consume.common.handler.RpcConsumerHandler;
import com.rpc.protocal.handler.RpcCode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接池
 *
 * @author xcx
 * @date
 */
public class ConnectionsPoll   {
    private static ConcurrentHashMap<String, Channel> channelsPool = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, EventLoopGroup> groups = new ConcurrentHashMap<>();

    public static Channel getChannel(String key, String address, int port) {
        if (channelsPool.contains(key)) {
            return channelsPool.get(key);
        }
        Channel channel = tryConnect(key, address, port);

        return channel;
    }


    private static Channel tryConnect(String key, String address, int port) {
        try {
            NioEventLoopGroup group = new NioEventLoopGroup(1);

            Bootstrap bootstrap;
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

            groups.put(key, group);

            return channel;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        for (Channel channel : channelsPool.values()) {
            channel.close();
        }
    }
}
