package com.most.core.app.database.config;

import com.most.core.app.database.config.loader.IDBConfigLoader;
import com.most.core.app.database.config.loader.impl.DatabaseDbConfigLoader;
import com.most.core.app.database.config.loader.impl.XmlDBConfigLoader;
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
 * @Date 2018/2/14 2:31
 * @Description: 数据库配置工厂类，用于获取数据库的配置
 */
public class DatabaseConfigFactory {

    private static Logger log = LogManager.getLogger(DatabaseConfigFactory.class.getName());

    /** 数据库配置缓存 */
    private static Map<String, DatabaseConfig> databaseConfig = new HashMap<String, DatabaseConfig>();

    /**
     * 获取应用所配置的所有数据库配置信息
     * @return
     */
    public static Map<String, DatabaseConfig> getAllDatabaseConfig(){
        return databaseConfig;
    }

    /**
     * 根据数据库名称获取对应的数据库配置信息
     * @param name
     * @return
     */
    public static DatabaseConfig getDatabaseConfig(String name){
        return databaseConfig.get(name);
    }

    /**
     * 根据配置方式获取对应的配置解析类
     * @return
     */
    public static IDBConfigLoader getDBConfigLoader(){
        String dbConfigType = GlobalConfig.DATABASE_CONFIG_TYPE;
        if(StringUtils.equals("xml", dbConfigType))
            return new XmlDBConfigLoader();
        else if(StringUtils.equals("db", dbConfigType))
            return new DatabaseDbConfigLoader();
        else
            return new XmlDBConfigLoader();
    }

    static{
        IDBConfigLoader loader = getDBConfigLoader();
        List<DatabaseConfig> configs = loader.load();
        if(ArrayTool.isNotEmpty(configs)){
            for(DatabaseConfig config: configs){
                String name = config.getName();
                databaseConfig.put(name, config);
            }
        }
    }
}
