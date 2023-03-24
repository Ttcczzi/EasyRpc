package com.rpc.test.provider;

import com.rpc.annotation.RpcService;
import com.rpc.test.interfaces.TestInterface;

/**
 * @author xcx
 * @date
 */
@RpcService(interfaceClass = TestInterface.class)
public class TestInterfaceImpl implements TestInterface {
    @Override
    public String test(String name) {
        return name + "DADSD";
    }
}
