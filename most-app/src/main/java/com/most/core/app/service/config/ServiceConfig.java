package com.most.core.app.service.config;

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
}
