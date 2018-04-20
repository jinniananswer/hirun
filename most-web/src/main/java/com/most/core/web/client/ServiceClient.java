package com.most.core.web.client;

import com.alibaba.fastjson.JSONObject;
import com.most.core.app.service.invoker.ServiceInvoker;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;

import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/17 13:10
 * @Description:
 */
public class ServiceClient {

    public static ServiceResponse call(String serviceName, ServiceRequest request) throws Exception{
        ServiceResponse response = ServiceInvoker.invoke(serviceName, request);
        return response;
    }

    public static ServiceResponse call(String serviceName, JSONObject parameter) throws Exception{
        ServiceRequest request = new ServiceRequest(parameter);
        ServiceResponse response = ServiceInvoker.invoke(serviceName, request);
        return response;
    }

    public static ServiceResponse call(String serviceName, Map parameter) throws Exception{
        JSONObject jsonObject = new JSONObject(parameter);
        ServiceRequest request = new ServiceRequest(jsonObject);
        ServiceResponse response = ServiceInvoker.invoke(serviceName, request);
        return response;
    }
}
