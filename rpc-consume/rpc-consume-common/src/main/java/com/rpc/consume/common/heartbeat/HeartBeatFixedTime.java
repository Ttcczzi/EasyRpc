package com.rpc.consume.common.heartbeat;

import com.rpc.common.constant.RpcConstants;
import com.rpc.consume.common.connection.ConnectionsPoll;
import com.rpc.consume.common.sendrequest.SendRequest;
import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.base.RpcMessage;
import com.rpc.protocal.enumeration.Messagetype;
import com.rpc.protocal.header.RpcHeader;
import com.rpc.protocal.header.RpcHeaderFactory;
import com.rpc.protocal.message.HeartBeatMessage;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.future.FuturesSet;
import com.rpc.proxy.api.future.RpcFuture;
import io.netty.channel.Channel;
import io.protostuff.Rpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xcx
 * @date
 */
public class HeartBeatFixedTime implements HeartBeat {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatFixedTime.class);
    private int heartbeatInterval = 30000;

    //private long fixedTime = 10000l;

    private int scanNotActiveChannleInterval = 60000;
    private ScheduledThreadPoolExecutor heartBeattaskScheduler = new ScheduledThreadPoolExecutor(1);

    private ScheduledThreadPoolExecutor freeConnectiontaskScheduler = new ScheduledThreadPoolExecutor(1);

    private Map<String, Integer> defeatCount = new HashMap<>();

    @Override
    public void start() {
        //心跳
        heartBeattaskScheduler.scheduleAtFixedRate(() -> {
                    //检查 心跳失败的channel
                    ConcurrentHashMap<Long, RpcFuture> leftHeartBeatFutures = FuturesSet.heartBeat;

                    if(leftHeartBeatFutures.size() > 0){
                        for(RpcFuture future: leftHeartBeatFutures.values()){
                            RpcProtocal<RpcMessage> requestRrotocal = future.getRequestRrotocal();

                            HeartBeatMessage message = (HeartBeatMessage) requestRrotocal.getMessage();
                            String channelKey = message.getChannelKey();

                            Integer count = defeatCount.put(channelKey, defeatCount.getOrDefault(channelKey, 0) + 1);

                            if(count >= 3){
                                LOGGER.error("the channel {} reconnect failed {} times", channelKey, count);
                                ConnectionsPoll.disconnect(channelKey);
                                defeatCount.remove(channelKey);
                            }

                        }
                    }

                    ConcurrentHashMap<String, Channel> channelsPool = ConnectionsPoll.getChannelsPool();

                    RpcProtocal<RpcMessage> protocal = new RpcProtocal<>();

                    for (String key : channelsPool.keySet()) {
                        Channel channel = channelsPool.getOrDefault(key, null);
                        if (channel != null && channel.isWritable()){

                            HeartBeatMessage heartBeatMessage = new HeartBeatMessage("ping");
                            heartBeatMessage.setChannelKey(key);

                            protocal.setMessage(heartBeatMessage);

                            sendHeartBeat( channel, protocal);
                        }
                    }
                }
                , 30000, heartbeatInterval, TimeUnit.MILLISECONDS);

    }

    @Override
    public void sendHeartBeat(Channel channel, RpcProtocal<RpcMessage> protocal) {
        RpcHeader requestHeader = RpcHeaderFactory.getRequestHeader(RpcConstants.JSONSERIALIZATION, Messagetype.HEARTBEAT.getType());
        protocal.setHeader(requestHeader);

        createFuture(protocal, new AsyncCallback() {
            @Override
            public void success(RpcProtocal<RpcMessage> protocal) {
                LOGGER.info("receive a heartbeat reply {}", channel.toString());
            }

            @Override
            public void defeat(RuntimeException exception) {

            }
        });

        channel.writeAndFlush(protocal);
    }

    public  RpcFuture createFuture(RpcProtocal<RpcMessage> protocal, AsyncCallback callback) {
        RpcFuture rpcFuture;

        if (callback == null) {
            rpcFuture = new RpcFuture(protocal);
        } else {
            rpcFuture = new RpcFuture(protocal, callback);
        }
        rpcFuture.setResponseTimeThrshould(heartbeatInterval);

        RpcHeader header = protocal.getHeader();
        FuturesSet.heartBeat.put(header.getRequestId(), rpcFuture);

        return rpcFuture;
    }

    public void end() {
        LOGGER.warn("heartBeattaskScheduler Shutdown");
        if(!heartBeattaskScheduler.isShutdown()){
            heartBeattaskScheduler.shutdown();
        }
    }


}
