package com.rpc.consume.spring;

import com.rpc.annotation.RpcReference;
import com.rpc.annotation.RpcService;
import com.rpc.common.referenceinfo.ReferenceInfo;
import com.rpc.common.utils.RpcServiceHelper;
import com.rpc.consume.common.consume.RpcConsume;
import com.rpc.protocal.meta.ServiceMeta;
import com.rpc.proxy.api.config.ProxyConfig;
import com.rpc.proxy.proxyfactory.jdk.ProxyObjectHandler;
import com.rpc.register.api.RegistryService;
import com.rpc.register.api.config.RegistryConfig;
import com.rpc.register.factory.RegistryFacotry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xcx
 * @date
 */
public class RpcSpringComsume extends RpcConsume implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcSpringComsume.class);

    private String regsitryAddress;

    private String registryType;

    private ApplicationContext applicationContext;

    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> rpcRefBeanDefinitionMap = new LinkedHashMap<>();

    public RpcSpringComsume(String regsitryAddress, String registryType) {
        this.regsitryAddress = regsitryAddress;
        this.registryType = registryType;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void serviceDiscovery() {

    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] names = beanFactory.getBeanDefinitionNames();
        for(String beanName: names){
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();
            if(beanClassName != null){
                Class<?> aClass = ClassUtils.resolveClassName(beanClassName, classLoader);

                ReflectionUtils.doWithFields(aClass, this::filedsHandler);
            }
        }

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.rpcRefBeanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if(this.applicationContext.containsBean(beanName)){
                throw new IllegalArgumentException("the bean '" + beanName + "' already existed");
            }

            registry.registerBeanDefinition(beanName, beanDefinition);

            LOGGER.info("the bean '" + beanName + "' be registered successfully");
        });
    }

    private void filedsHandler(Field field)  {
        try {
            RpcReference annotation = field.getAnnotation(RpcReference.class);
            RegistryService registryImpl = RegistryFacotry.getRegistryImpl(new RegistryConfig(regsitryAddress, registryType));

            if (annotation != null){
                String interfaceClassName = field.getClass().getName();
                String version = annotation.version();
                String group = annotation.group();
                //服务名称
                String serviceName = RpcServiceHelper.buildServiceKey(interfaceClassName, version, version);
                //远程服务的信息
                ServiceMeta discover = registryImpl.discover(serviceName, 0);

                //重置代理工厂的配置
                initConfig(new ProxyConfig(field.getClass(),
                        version, group,
                        annotation.outTime(), annotation.serializationtype(), annotation.async(), annotation.oneway()), discover.getAddress(), discover.getPort());

                Object proxy = proxyFactory.getProxy(field.getType());

                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
                //todo 扩展
                builder.addPropertyValue("interfaceClass", field.getType());
                builder.addPropertyValue("version", version);
                builder.addPropertyValue("group", group);
                builder.addPropertyValue("outTime", annotation.outTime());
                builder.addPropertyValue("serializationType", annotation.serializationtype());
                builder.addPropertyValue("async", annotation.async());
                builder.addPropertyValue("oneway", annotation.oneway());

                BeanDefinition beanDefinition = builder.getBeanDefinition();

                rpcRefBeanDefinitionMap.put(interfaceClassName, beanDefinition);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
