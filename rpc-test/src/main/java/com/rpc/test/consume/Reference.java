package com.rpc.test.consume;

import com.rpc.annotation.RpcReference;
import com.rpc.test.interfaces.DemoInterface;

/**
 * @author xcx
 * @date
 */
public class Reference {
    @RpcReference
    public DemoInterface demoInterface;
}
