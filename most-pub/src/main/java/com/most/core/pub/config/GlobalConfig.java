package com.most.core.pub.config;

import com.most.core.pub.tools.file.PropertiesTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Properties;

/**
 * @Author jinnian
 * @Date 2018/2/20 22:58
 * @Description:
 */
public class GlobalConfig {

    public final static String DATABASE_CONFIG_TYPE;

    public final static String SERVICE_CONFIG_TYPE;

    private static Logger log = LogManager.getLogger(GlobalConfig.class.getName());

    static{
        Properties properties = PropertiesTool.getProperties("global.properties");
        if(log.isDebugEnabled()){
            log.debug("global.properties文件解析完成" + properties);
        }
        Map<String, String> globalConfig = PropertiesTool.getProperties(properties);
        DATABASE_CONFIG_TYPE = globalConfig.get("dbconfigtype");
        SERVICE_CONFIG_TYPE = globalConfig.get("serviceconfigtype");
        if(log.isDebugEnabled()){
            log.debug("DATABASE_CONFIG_TYPE=" + DATABASE_CONFIG_TYPE);
        }
    }
}
