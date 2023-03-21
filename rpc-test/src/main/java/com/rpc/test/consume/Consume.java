package com.rpc.test.consume;

import com.rpc.consume.client.RpcClient;
import com.rpc.consume.client.RpcNativeConsume;
import com.rpc.consume.common.consume.RpcConsume;
import com.rpc.protocal.RpcProtocal;
import com.rpc.protocal.message.ResponseMessage;
import com.rpc.proxy.api.callback.AsyncCallback;
import com.rpc.proxy.api.config.ProxyConfig;
import com.rpc.proxy.api.future.RpcFuture;
import com.rpc.proxy.async.AsyncProxy;
import com.rpc.test.interfaces.DemoInterface;
import com.rpc.test.interfaces.TestInterface;

import java.util.concurrent.ExecutionException;

/**
 * @author xcx
 * @date
 */
public class Consume {
    public static void main(String[] args) throws Exception {
        RpcNativeConsume instance = RpcNativeConsume.getInstance();
        instance.serviceDiscovery("com.rpc.test.consume");

        RpcClient rpcClient = new RpcClient();
        DemoInterface demoInterface = rpcClient.getSyncProxy(DemoInterface.class);
        String test = demoInterface.test();
        System.out.println(test);

        TestInterface testInterface = rpcClient.getSyncProxy(TestInterface.class);
        String wt = testInterface.test("wt");
        System.out.println(wt);

//        RpcClient rpcClient = new RpcClient();
//        AsyncProxy asyncProxy = rpcClient.getAsyncProxy(DemoInterface.class, new AsyncCallback() {
//            @Override
//            public void success(RpcProtocal<ResponseMessage> protocal) {
//                System.out.println(protocal.getMessage().getResult());
//            }
//
//            @Override
//            public void defeat(RuntimeException exception) {
//
//            }
//        });
//
//        asyncProxy.call("test", null);

    }

}
