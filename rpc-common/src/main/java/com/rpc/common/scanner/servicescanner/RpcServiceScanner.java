package com.rpc.common.scanner.servicescanner;

import com.rpc.annotation.RpcService;
import com.rpc.common.scanner.ClassCanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫描 带有@RpcService注解的类
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
                if (rpcService != null){
                    LOGGER.info(className + "=============================");
                    LOGGER.info(rpcService.interfaceClass().toString());
                    LOGGER.info(rpcService.interfaceName());

                    Class<?> interfaceClass = rpcService.interfaceClass();

                    String key = ObjectUtils.isEmpty(interfaceClass)? rpcService.interfaceName() : interfaceClass.getName();
                    key = key.concat(rpcService.version()).concat(rpcService.group());

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
