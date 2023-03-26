package com.rpc.consume.common.connection;

import com.rpc.consume.common.consume.RpcConsume;
import com.rpc.consume.common.handler.ConnectionHandler;
import com.rpc.consume.common.sendrequest.SendRequest;
import com.rpc.proxy.threadpool.CallBackThreadPool;
import com.rpc.consume.common.handler.RpcConsumerHandler;
import com.rpc.protocal.handler.RpcCode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 连接池
 *
 * @author xcx
 * @date
 */
public class ConnectionsPoll {
    private static int count = 20;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionsPoll.class);

    private static ConcurrentHashMap<String, Channel> channelsPool = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, CountDownLatch> countDownLatchs = new ConcurrentHashMap<>();

    private static Object lock = new Object();

    public static Channel getChannel(String key, String host, int port) {
        Channel channel = channelsPool.getOrDefault(key, tryConnect(key, host, port));

        return channel;
    }

    public static void disconnect(String channelKey) {
        if (channelsPool.containsKey(channelKey)) {
            Channel channel = channelsPool.remove(channelKey);

            synchronized (lock) {
                if (channel != null) {
                    channel.close();
                }
            }
        }
    }

    public static void putChannel(String key, String host, int port, Channel channel) {
        channelsPool.computeIfAbsent(key, (a) -> channel);
    }


    public static Channel reconnect(String key, String host, int port) throws InterruptedException {
        Channel channel = reconnect0(key, host, port);
        //putChannel(key, host, port, channel);
        // 说明重连成功
        CountDownLatch countDownLatch = countDownLatchs.remove(key);
        if(countDownLatch == null){
            throw new NullPointerException();
        }
        countDownLatch.countDown();

        return channel;
    }

    public static ConcurrentHashMap<String, CountDownLatch> getcountDownLatchs() {
        return countDownLatchs;
    }

    public static ConcurrentHashMap<String, Channel> getChannelsPool() {
        return channelsPool;
    }

    static private NioEventLoopGroup group = new NioEventLoopGroup();

    static {
        group = new NioEventLoopGroup();
    }

    private static Channel tryConnect(String key, String host, int port) {
//        if(channelsPool.size() > count){
//            return null;
//        }
        Bootstrap bootstrap = new Bootstrap();
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
        Channel channel = null;
        try {
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }

        return channel;
    }

    private static Channel reconnect0(String key, String host, int port) throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            LOGGER.warn("try to reconnect {}th", i);
            Channel channel = tryConnect(key, host, port);
            if (channel == null) {
                Thread.sleep(2000);
                continue;
            }
            return channel;
        }
        RpcConsume.close();
        throw new RuntimeException("unable to connect to " + host + ":" + port);
    }


    public static void close() {
        LOGGER.warn("ConnectionsPoll close");
        for (Channel channel : channelsPool.values()) {
            channel.close();
        }
        group.shutdownGracefully();

    }


}
