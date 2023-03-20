package com.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 提供者注解
 * @author xcx
 * @date
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    /**
     * 接口的Class
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 接口的全类名
     */
    String interfaceName() default "";

    /**
     * 服务的版本
     */
    String version() default "1.0.0";

    /**
     * 服务的分组
     */
    String group() default "";
}
