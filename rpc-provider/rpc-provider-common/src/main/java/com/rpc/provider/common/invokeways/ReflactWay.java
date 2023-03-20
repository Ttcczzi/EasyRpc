package com.rpc.provider.common.invokeways;

import com.rpc.protocal.message.RequestMessage;

import java.lang.reflect.Method;

/**
 * 通过反射调用方法
 * @author xcx
 * @date
 */
public class ReflactWay {
    public static Object invoke(Object target, RequestMessage message) {
        try {
            Class<?> aClass = target.getClass();

            //反射调用对象target 的方法
            String methodName = message.getMethodName();
            Class<?>[] paramsType = message.getParamsType();
            Method targetMethod = null;

            targetMethod = aClass.getMethod(methodName, paramsType);


            Object[] params = message.getParams();
            Object result = targetMethod.invoke(target, params);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
