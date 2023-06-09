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
    //private static int count = 20;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionsPoll.class);
    private static ConcurrentHashMap<String, Channel> channelsPool = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, CountDownLatch> countDownLatchs = new ConcurrentHashMap<>();
    private static Object lock = new Object();

    //按时不需要考虑并发（对于相同host：port的主机进行重复连接）问题，该方法只有两种时机被调用，
    //第一种，启动时，由于启动时只有主线程，所以线程安全
    //第一种，重连时，由于重连机制在前面步骤已经考虑了并发问题，到这一步时已经不存在并发问题了
    public static Channel getChannel(String key, String host, int port) {
        //getOrDefault需要先创建default
        if(channelsPool.containsKey(key)){
            return channelsPool.get(key);
        }
//        LOGGER.info("null {}", Thread.currentThread().getName());
        Channel channel = tryConnect(key, host, port);

        return channel;
    }
    public static void disconnect(String channelKey) {
        LOGGER.warn("disconnect a channel, {} ", channelKey);
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

        //到这说明重连成功 -> 删除 countDownLatchs 里面的 CountDownLatch
        LOGGER.info("reconnect successfully, {}", channel);
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

        LOGGER.info("{} get a channel {}", Thread.currentThread().getName(), channel);
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
