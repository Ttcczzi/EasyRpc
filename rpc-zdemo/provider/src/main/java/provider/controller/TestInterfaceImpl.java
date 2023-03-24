package provider.controller;

import com.rpc.annotation.RpcService;
import interfaces.TestInterface;

/**
 * @author xcx
 * @date
 */
//需要指定接口
@RpcService(interfaceClass = TestInterface.class)
public class TestInterfaceImpl implements TestInterface {
    @Override
    public String test(String name) {
        return name;
    }
}
