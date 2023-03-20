package com.rpc.provider.common.server.impl;

import com.rpc.protocal.handler.RpcCode;
import com.rpc.provider.common.handler.ConnectionHandler;
import com.rpc.provider.common.handler.RpcProviderHandler;
import com.rpc.provider.common.server.api.Server;
import com.rpc.register.api.RegistryService;
import com.rpc.register.api.config.RegistryConfig;
import com.rpc.register.zookeeper.ZookeeperRegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author xcx
 * @date
 */
public class BaseServe implements Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServe.class);
    private String host = "127.0.0.1";
    private int port = 1106;
    private ServerBootstrap bootstrap;
    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;

    private RegistryService registryService;
    protected Map<String, Object> handlermap;
    public BaseServe(){}

    public BaseServe(RegistryConfig registryConfig) throws Exception {
        if("zookeeper".equals(registryConfig.getRegisterType())){
            registryService = new ZookeeperRegistryService();
            registryService.init(registryConfig);
        }
    }

    public BaseServe(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void startNettyServe() {
        boss = new NioEventLoopGroup(2);
        work = new NioEventLoopGroup();
        try{
            bootstrap = new ServerBootstrap()
                    .group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new RpcCode());
                            //入站
                            channel.pipeline().addLast(new ConnectionHandler());
                            channel.pipeline().addLast(new RpcProviderHandler(handlermap));
                        }
                    });
            ChannelFuture future = bootstrap.bind(host, port).sync();
            LOGGER.info("Server started on {}:{}", host, port);
            future.channel().closeFuture().sync();
        }catch (Exception e){
            LOGGER.info("Server started error {}", e);
        }finally {
            work.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
