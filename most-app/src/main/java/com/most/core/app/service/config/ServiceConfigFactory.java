package com.most.core.app.service.config;

import com.most.core.app.service.config.loader.IServiceConfigLoader;
import com.most.core.app.service.config.loader.impl.DatabaseServiceConfigLoader;
import com.most.core.app.service.config.loader.impl.XmlServiceConfigLoader;
import com.most.core.pub.config.GlobalConfig;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/16 21:58
 * @Description:
 */
public class ServiceConfigFactory {

    private static Logger log = LogManager.getLogger(ServiceConfigFactory.class.getName());

    private static Map<String, ServiceConfig> serviceConfigs = new HashMap<String, ServiceConfig>();

    public static IServiceConfigLoader getServiceConfigLoader(){
        String serviceConfigType = GlobalConfig.SERVICE_CONFIG_TYPE;

        if(StringUtils.equals("xml", serviceConfigType))
            return new XmlServiceConfigLoader();
        else if(StringUtils.equals("db", serviceConfigType))
            return new DatabaseServiceConfigLoader();
        else
            return new XmlServiceConfigLoader();
    }

    public static ServiceConfig getServiceConfig(String serviceName){
        return serviceConfigs.get(serviceName);
    }

    public static Map<String, ServiceConfig> getAllServiceConfigs(){
        return serviceConfigs;
    }

    static{
        IServiceConfigLoader loader = getServiceConfigLoader();
        List<ServiceConfig> configs = loader.load();
        if(ArrayTool.isNotEmpty(configs)){
            for(ServiceConfig config : configs){
                String serviceName = config.getServiceName();
                serviceConfigs.put(serviceName, config);
            }
        }
    }
}
