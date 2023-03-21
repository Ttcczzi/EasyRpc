package com.rpc.provider.spring;

import com.rpc.annotation.RpcService;
import com.rpc.common.exception.ServiceExceeption;
import com.rpc.common.utils.RpcServiceHelper;
import com.rpc.protocal.meta.ServiceMeta;
import com.rpc.provider.common.server.impl.BaseServe;
import com.rpc.register.api.config.RegistryConfig;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author xcx
 * @date
 */
public class RpcSpringProvider extends BaseServe implements ApplicationContextAware, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcSpringProvider.class);

    public RpcSpringProvider(String host, int port, String registerAddress, String registerType) throws Exception {
        super(host, port, new RegistryConfig(registerAddress, registerType));
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object bean : serviceBeanMap.values()) {
                RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);

                if (rpcService != null) {
                    LOGGER.info(bean.getClass().getName() + "=============================");
                    LOGGER.info(rpcService.interfaceClass().toString());
                    LOGGER.info(rpcService.interfaceName());

                    Class<?> interfaceClass = rpcService.interfaceClass();

                    String serviceName = interfaceClass.equals(Void.class) ? rpcService.interfaceName() : interfaceClass.getName();

                    if (serviceName == null || serviceName == "") {
                        throw new ServiceExceeption("serviceName: " + serviceName + "，服务名称不合法");
                    }

                    String key = RpcServiceHelper.buildServiceKey(
                            serviceName,
                            rpcService.version(),
                            rpcService.group());

                    handlermap.put(key, bean);
                }
            }

            LOGGER.info(handlermap.toString());

            //服务注册 name -- com.rpc.test.interfaces.DemoInterface#1.0.0#test
            for (String name : handlermap.keySet()) {
                String[] service = name.split("#");
                if (service.length != 3) {
                    LOGGER.error("name: " + name + " is illegal");
                }

                try {
                    registryService.regsitry(new ServiceMeta(service[0], service[1], service[2], host, port));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            startNettyServe();
        }
    }
}
