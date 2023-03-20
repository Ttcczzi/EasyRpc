package com.rpc.protocal.message;

import com.rpc.protocal.base.RpcMessage;

/**
 * @author xcx
 * @date
 */
public class RequestMessage extends RpcMessage {
    private static final long serialVersionUID = 5555776886650396129L;

    private String interfaceName;

    private String methodName;

    private Class<?>[] paramsType;

    private Object[] params;

    private String version;

    private String group;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamsType() {
        return paramsType;
    }

    public void setParamsType(Class<?>[] paramsType) {
        this.paramsType = paramsType;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
