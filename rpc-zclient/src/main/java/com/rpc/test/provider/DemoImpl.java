package com.rpc.test.provider;

import com.rpc.annotation.RpcService;
import com.rpc.test.interfaces.DemoInterface;

/**
 * @author xcx
 * @date
 */
@RpcService(interfaceClass = DemoInterface.class)
public class DemoImpl implements DemoInterface{
    @Override
    public String test() {
        return "test";
    }
}
