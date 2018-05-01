package com.most.core.app.service.invoker;

import com.most.core.app.service.GenericService;
import com.most.core.app.service.config.ServiceConfig;
import com.most.core.app.service.config.ServiceConfigFactory;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

/**
 * @Author jinnian
 * @Date 2018/4/16 22:09
 * @Description:
 */
public class ServiceInvoker {

    private static Logger log = LogManager.getLogger(ServiceInvoker.class.getName());

    public static ServiceResponse invoke(String serviceName, ServiceRequest request) throws Exception{
        ServiceConfig serviceConfig = ServiceConfigFactory.getServiceConfig(serviceName);
        Class<GenericService> classes = (Class<GenericService>) serviceConfig.getServiceClass();
        Method method = serviceConfig.getServiceMethod();
        ServiceResponse response = null;
        AppSession session = null;
        try {
            GenericService service = classes.newInstance();
            //1,创建session
            session = SessionManager.getSession();

            SessionEntity sessionEntity = new SessionEntity(request.getHeader().getData());
            session.setSessionEntity(sessionEntity);

            //2.方法执行
            response = (ServiceResponse) method.invoke(service, request);

            if(response != null){
                if(StringUtils.isBlank(response.getResultCode())){
                    response.setSuccess();
                }
            }

            //3.提交事务
            session.commit();


        } catch (Exception e) {
            if(session != null)
                session.rollback();
            log.error(e);
            throw(e);
        }
        finally {
            if(session != null) {
                //4.释放连接
                session.close();

                //5.注销session
                SessionManager.destroy();
            }
        }
//        if(object == null){
//            throw new Exception("服务"+serviceName+"调用失败");
//        }

        return response;
    }
}
