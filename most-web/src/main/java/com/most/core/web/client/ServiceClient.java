package com.most.core.web.client;

import com.alibaba.fastjson.JSONObject;
import com.most.core.app.service.invoker.ServiceInvoker;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.web.session.HttpSessionManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/17 13:10
 * @Description:
 */
public class ServiceClient {

    public static ServiceResponse call(String serviceName, ServiceRequest request) throws Exception{
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        String sessionId = session.getId();
        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(sessionId);
        if(sessionEntity != null)
            request.getHeader().putAll(sessionEntity.getData());
        ServiceResponse response = ServiceInvoker.invoke(serviceName, request);
        return response;
    }

    public static ServiceResponse call(String serviceName, JSONObject parameter) throws Exception{
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        String sessionId = session.getId();
        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(sessionId);

        ServiceRequest request = new ServiceRequest(parameter);
        if(sessionEntity != null)
            request.getHeader().putAll(sessionEntity.getData());
        ServiceResponse response = ServiceInvoker.invoke(serviceName, request);
        return response;
    }

    public static ServiceResponse call(String serviceName, Map parameter) throws Exception{
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        String sessionId = session.getId();
        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(sessionId);
        JSONObject jsonObject = new JSONObject(parameter);
        ServiceRequest request = new ServiceRequest(jsonObject);
        if(sessionEntity != null)
            request.getHeader().putAll(sessionEntity.getData());
        ServiceResponse response = ServiceInvoker.invoke(serviceName, request);
        return response;
    }
}
