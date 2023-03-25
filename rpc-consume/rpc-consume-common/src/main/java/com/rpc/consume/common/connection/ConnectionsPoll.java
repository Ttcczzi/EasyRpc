package com.rpc.consume.common.connection;

import com.rpc.consume.common.handler.ConnectionHandler;
import com.rpc.proxy.threadpool.CallBackThreadPool;
import com.rpc.consume.common.handler.RpcConsumerHandler;
import com.rpc.protocal.handler.RpcCode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
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

    public static Channel getChannel(String key, String host, int port) {
//        if (channelsPool.containsKey(key)) {
//            return channelsPool.get(key);
//        }
//        Channel channel = tryConnect(key, host, port);

        Channel channel = channelsPool.computeIfAbsent(key, (a) -> tryConnect(key, host, port));

        return channel;
    }

    public static void disconnect(String channelKey) {
        if(channelsPool.containsKey(channelKey)){
            Channel channel = channelsPool.remove(channelKey);

            if(channel != null){
                channel.close();
            }

        }
    }

    public static void putChannel(String key, String host, int port, Channel channel){
        channelsPool.putIfAbsent(key, channel);
    }


    public static Channel reconnect(String key, String host, int port){
        Channel channel = getChannel(key, host, port);
        return channel;
    }

    public static ConcurrentHashMap<String, Channel> getChannelsPool(){
        return channelsPool;
    }

    static private NioEventLoopGroup group = new NioEventLoopGroup();
    static private Bootstrap bootstrap;

    static {
        bootstrap = new Bootstrap();

        group = new NioEventLoopGroup();

    }

    private static Channel tryConnect(String key, String host, int port) {
        try {
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new RpcCode());
                    ch.pipeline().addLast(new RpcConsumerHandler());
                    ch.pipeline().addLast(new ConnectionHandler(host, port));
                }
            });
            Channel channel = bootstrap.connect(host, port).sync().channel();


//            channel.closeFuture().addListener(future -> {
//
//                group.shutdownGracefully();
//            });


            return channel;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }




    public static void close() {
        for (Channel channel : channelsPool.values()) {
            channel.close();
        }
        group.shutdownGracefully();

    }


}
