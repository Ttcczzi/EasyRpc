package com.wt.provider;

import com.rpc.annotation.RpcService;
import com.wt.interfaces.TestInterface;

/**
 * @author xcx
 * @date
 */
@RpcService(interfaceClass = TestInterface.class)
public class TestController implements TestInterface {
    @Override
    public String test(String name) {
        return "hello," + name;
    }
}
