package com.rpc.common.scanner.referencescanner;

import com.rpc.annotation.RpcReference;
import com.rpc.common.referenceinfo.ReferenceInfo;
import com.rpc.common.scanner.ClassCanner;
import com.rpc.common.utils.RpcServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫描 带有@RpcReference注解 的属性
 * @author xcx
 * @date
 */
public class RpcReferenceScanner extends ClassCanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferenceScanner.class);

    public Map<Class<?>, ReferenceInfo> getClassesByRpcReference(String packageName) throws IOException {
        HashMap<Class<?>, ReferenceInfo> handlerMap = new HashMap<>();

        List<String> classNames = super.getClassNames(packageName);

        classNames.stream().forEach(className -> {
            try {
                Class<?> aClass = Class.forName(className);
                Field[] fields = aClass.getDeclaredFields();

                Arrays.stream(fields).forEach(field -> {
                    RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                    if(rpcReference != null){
                        LOGGER.info(aClass.getName() + ": " + field.getName() + "====================");
                        //TODO 处理后续逻辑，将@RpcReference注解标注的接口引用代理对象，放入全局缓存中
                        LOGGER.info("当前标注了@RpcReference注解的字段名称===>>> " + field.getName());
                        LOGGER.info("@RpcReference注解上标注的属性信息如下：");
                        LOGGER.info("version===>>> " + rpcReference.version());
                        LOGGER.info("group===>>> " + rpcReference.group());
                        LOGGER.info("registryType===>>> " + rpcReference.registryCenterType());
                        LOGGER.info("registryAddress===>>> " + rpcReference.registryAddress());

                        ReferenceInfo referenceInfo = new ReferenceInfo();

                        referenceInfo.setVersion(rpcReference.version());
                        referenceInfo.setGroup(rpcReference.group());
                        referenceInfo.setOutTime(rpcReference.outTime());
                        referenceInfo.setAsync(rpcReference.async());
                        referenceInfo.setOneway(rpcReference.oneway());
                        referenceInfo.setLoadBlanceType(rpcReference.loadBlanceType());
                        referenceInfo.setProxytype(referenceInfo.getProxytype());
                        referenceInfo.setSerializationtype(rpcReference.serializationtype());
                        referenceInfo.setRegistryAddress(rpcReference.registryAddress());
                        referenceInfo.setRegistryCenterType(rpcReference.registryCenterType());

                        handlerMap.put(field.getType(), referenceInfo);
                    }

                });

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return handlerMap;
    }
}
