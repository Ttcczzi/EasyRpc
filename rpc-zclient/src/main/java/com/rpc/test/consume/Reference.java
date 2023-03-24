package com.rpc.test.consume;

import com.rpc.annotation.RpcReference;
import com.rpc.common.constant.RpcConstants;
import com.rpc.test.interfaces.DemoInterface;
import com.rpc.test.interfaces.TestInterface;

/**
 * @author xcx
 * @date
 */
public class Reference {
    @RpcReference(serializationtype = RpcConstants.PROTOSTUFFSERIALIZATION)
    public DemoInterface demoInterface;

    @RpcReference
    public TestInterface testInterface;
}
