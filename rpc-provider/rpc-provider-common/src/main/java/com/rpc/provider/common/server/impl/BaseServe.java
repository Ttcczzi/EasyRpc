package com.rpc.provider.common.server.impl;

import com.rpc.protocal.handler.RpcCode;
import com.rpc.provider.common.handler.ConnectionHandler;
import com.rpc.provider.common.handler.RpcProviderHandler;
import com.rpc.provider.common.server.api.Server;
import com.rpc.ratelimiter.common.contants.RateLimiterConstants;
import com.rpc.ratelimiter.strategies.factory.RateLimiterFactory;
import com.rpc.register.api.RegistryService;
import com.rpc.register.api.config.RegistryConfig;
import com.rpc.register.factory.RegistryFacotry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xcx
 * @date
 */
public class BaseServe implements Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServe.class);
    protected String host = "127.0.0.1";
    protected int port = 1106;
    private boolean enableLimit = true;
    private int permites = 1000;
    private long limitTime = 2000;
    private String rateLimiterType = RateLimiterConstants.RL_TOKEN;
    private ServerBootstrap bootstrap;
    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;
    protected RegistryService registryService;
    protected Map<String, Object> handlermap = new HashMap<>();

    public BaseServe() {
    }


    public BaseServe(RegistryConfig registryConfig) throws Exception {
        this.registryService = RegistryFacotry.getRegistryImpl(registryConfig);
    }

    public BaseServe(String host, int port, RegistryConfig registryConfig, boolean enableLimit, int permites, long limitTime, String rateLimiterType) throws Exception {
        this(registryConfig);
        this.host = host;
        this.port = port;
        this.enableLimit = enableLimit;
        this.permites = permites;
        this.limitTime = limitTime;
        this.rateLimiterType = rateLimiterType;
    }

    @Override
    public void startNettyServe() {
        new Thread(() -> {
            boss = new NioEventLoopGroup();
            work = new NioEventLoopGroup();
            try {
                bootstrap = new ServerBootstrap()
                        .group(boss, work)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel channel) throws Exception {
                                //单个包的最大长度，偏移几个字节是长度域，长度域占几个字节，
                                channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,28,4,0,0));
                                channel.pipeline().addLast(new RpcCode());
                                //入站
                                channel.pipeline().addLast(new ConnectionHandler());
                                channel.pipeline().addLast(new RpcProviderHandler(handlermap, host, port, enableLimit, RateLimiterFactory.getRateLimiter(enableLimit, permites, limitTime, rateLimiterType)));
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture future = bootstrap.bind(host, port).sync();
                LOGGER.info("Server started on {}:{}", host, port);
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                LOGGER.info("Server started error {}", e);
            } finally {
                work.shutdownGracefully();
                boss.shutdownGracefully();
            }
        }).start();
    }


}
