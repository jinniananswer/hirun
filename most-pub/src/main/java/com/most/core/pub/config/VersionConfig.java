package com.most.core.pub.config;

import com.most.core.pub.tools.file.PropertiesTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.Version;

import java.util.Map;
import java.util.Properties;

public class VersionConfig {

    private static String VERSION = "";

    private static Logger log = LogManager.getLogger(VersionConfig.class.getName());

    public static void init(){
        try {
            Properties properties = PropertiesTool.getProperties("version.properties");
            if (log.isDebugEnabled()) {
                log.debug("version.properties文件解析完成" + properties);
            }
            Map<String, String> versionConfig = PropertiesTool.getProperties(properties);
            VERSION = versionConfig.get("version");
        }catch (Exception e){

        }
    }

    public static String getVersion(){
        return VERSION;
    }
}
