package com.rpc.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消费者注解
 * @author xcx
 * @date
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Autowired
public @interface RpcReference {
    /**
     * 版本号
     */
    String version() default "1.0.0";

    /**
     * 分组
     */
    String group() default "default";

    /**
     * 注册中心的类型
     */
    String registryCenterType() default "zookeeper";

    /**
     * 注册中心的地址
     */
    String registryAddress() default "127.0.0.1";

    /**
     * 序列化类型
     */
    String serializationtype() default "protostuff";

    /**
     * 负载均衡方式
     */
    String loadBlanceType() default "zkconsistenthash";

    /**
     * 超时时间
     */
    long outTime() default 5000;

    /**
     * 代理类型
     */
    String proxytype() default "jdk";

    /**
     * 是否异步执行
     */
    boolean async() default false;

    /**
     * 是否单向调用
     */
    boolean oneway() default false;

}
