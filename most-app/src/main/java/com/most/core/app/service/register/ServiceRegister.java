package com.most.core.app.service.register;

import com.most.core.app.service.GenericService;
import com.most.core.app.service.config.ServiceConfig;
import com.most.core.app.service.config.ServiceConfigFactory;
import com.most.core.pub.tools.datastruct.MapTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @Author jinnian
 * @Date 2018/4/16 22:36
 * @Description:
 */
public class ServiceRegister {

    private static Logger log = LogManager.getLogger(ServiceRegister.class.getName());

    public static void register(){
        Map<String, ServiceConfig> serviceConfigs = ServiceConfigFactory.getAllServiceConfigs();
        if(MapTool.isEmpty(serviceConfigs)){
            log.error("没有找到任何服务配置，请检查配置文件");
        }

        Set<String> keys = serviceConfigs.keySet();
        for(String key : keys){
            ServiceConfig config = serviceConfigs.get(key);
            String classPath = config.getClassPath();
            try {
                Class<GenericService> classes = (Class<GenericService>) Class.forName(classPath);
                Method method = classes.getMethod(config.getMethodName(), new Class<?>[]{Map.class});
                config.setServiceClass(classes);
                config.setServiceMethod(method);
            }
            catch (ClassNotFoundException e){
                log.debug("服务"+config.getServiceName()+"注册失败，未到找定义的服务类");
            } catch (NoSuchMethodException e) {
                log.debug("服务"+config.getServiceName()+"注册失败，未到找定义的服务方法");
            }
        }
    }
}
