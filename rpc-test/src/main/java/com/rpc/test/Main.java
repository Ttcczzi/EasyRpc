package com.rpc.test;

import com.rpc.common.constant.RpcConstants;
import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.enumeration.RpcType;
import com.rpc.protocal.handler.RpcCode;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.header.RpcHeaderFactory;
import com.rpc.protocal.message.RequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author xcx
 * @date
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {


        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcCode());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<RpcProtocal<Object>>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocal<Object> protocal) throws Exception {
                                System.out.println(protocal);
                            }
                        });
                    }
                }).connect(new InetSocketAddress("127.0.01", 1106))
                .sync().channel();

        channel.closeFuture().addListener(future -> {
            group.shutdownGracefully();
        });

        RpcProtocal<RequestMessage> protocal = new RpcProtocal<>();
        RpcHeader rpcHeader = RpcHeaderFactory.getRequestHeader(RpcConstants.JDKSERIALIZATION, RpcType.REQUEST.getType());
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setGroup("");
        requestMessage.setVersion("1.0.0");
        requestMessage.setInterfaceName("com.rpc.test.interfaces.DemoInterface");
        requestMessage.setMethodName("test");


        protocal.setHeader(rpcHeader);
        protocal.setMessage(requestMessage);

        channel.writeAndFlush(protocal);

//        new Thread(() -> {
//            Scanner scanner = new Scanner(System.in);
//            while (true) {
//                String line = scanner.nextLine();
//                if ("q".equals(line)) {
//                    channel.close();
//                    break;
//                }
//                channel.writeAndFlush(line);
//            }
//        }).start();
    }
}
