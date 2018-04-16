package com.most.core.app.service.config;

import java.lang.reflect.Method;

/**
 * @Author jinnian
 * @Date 2018/4/16 21:39
 * @Description: 服务配置类
 */
public class ServiceConfig {

    private String serviceName;

    private String classPath;

    private String methodName;

    private String desc;

    private Class<?> serviceClass;

    private Method serviceMethod;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Method getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
    }
}
