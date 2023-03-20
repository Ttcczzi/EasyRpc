package com.rpc.consume.common.handler;


import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.message.ResponseMessage;
import com.rpc.proxy.api.future.ResponesFutures;
import com.rpc.proxy.api.future.RpcFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *
 * @author xcx
 * @date
 */
public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocal<ResponseMessage>> {

    //序号用来接收结果的 promise 对象
    //public static final Map<Long, Promise<Object>> PROMISES = new ConcurrentHashMap<>();
    //public static CountDownLatch WAIT_FOR_COEECTION = new CountDownLatch(1);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocal<ResponseMessage> protocal) throws Exception {
        RpcHeader header = protocal.getHeader();
        ResponseMessage message = protocal.getMessage();

        RpcFuture rpcFuture = ResponesFutures.futures.remove(header.getRequestId());
        if(rpcFuture != null){
            String error = message.getError();
            if(error != null){
                throw new RuntimeException(error);
            }
            rpcFuture.Done(protocal);
        }

        // 拿到空的 promise
//        Promise<Object> promise = PROMISES.remove(header.getRequestId());
//        if (promise != null) {
//            Object returnValue = message.getResult();
//            String error = message.getError();
//            if (error != null ) {
//                promise.setFailure(new Throwable(error));
//            } else {
//                promise.setSuccess(returnValue);
//            }
//        }
//        WAIT_FOR_COEECTION.countDown();
    }
}
