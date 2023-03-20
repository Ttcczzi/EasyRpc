package com.rpc.common.scanner.servicescanner;

import com.rpc.annotation.RpcService;
import com.rpc.common.exception.ServiceExceeption;
import com.rpc.common.scanner.ClassCanner;
import com.rpc.common.utils.RpcServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫描 带有@RpcService注解的类
 *
 * @author xcx
 * @date
 */
public class RpcServiceScanner extends ClassCanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceScanner.class);

    public static Map<String, Object> getClassesByRpcService(String packageName) throws IOException {
        HashMap<String, Object> handlerMap = new HashMap<>();

        List<String> classNames = getClassNames(packageName);
        classNames.stream().forEach(className -> {
            Class<?> aClass = null;
            try {
                aClass = Class.forName(className);
                RpcService rpcService = aClass.getAnnotation(RpcService.class);
                if (rpcService != null) {
                    LOGGER.info(className + "=============================");
                    LOGGER.info(rpcService.interfaceClass().toString());
                    LOGGER.info(rpcService.interfaceName());

                    Class<?> interfaceClass = rpcService.interfaceClass();

                    String serviceName = interfaceClass.equals(Void.class) ? rpcService.interfaceName() : interfaceClass.getName();

                    if(serviceName == null || serviceName == ""){
                        throw new ServiceExceeption("serviceName: " + serviceName + "，服务名称不合法");
                    }

                    String key =
                            RpcServiceHelper.buildServiceKey(
                                    serviceName,
                                    rpcService.version(),
                                    rpcService.group());


                    handlerMap.put(key, aClass.newInstance());
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return handlerMap;
    }
}
