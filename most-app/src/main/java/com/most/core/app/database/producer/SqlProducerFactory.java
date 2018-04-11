package com.most.core.app.database.producer;

import com.most.core.app.database.config.DatabaseConfig;
import com.most.core.app.database.config.DatabaseConfigFactory;
import com.most.core.app.database.producer.impls.MySqlProducer;
import com.most.core.app.database.producer.impls.OracleSqlProducer;
import com.most.core.pub.tools.datastruct.MapTool;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author jinnian
 * @Date 2018/3/19 21:52
 * @Description:
 */
public class SqlProducerFactory {

    private static Map<String, ISqlProducer> producerCache = new HashMap<String, ISqlProducer>();

    public static ISqlProducer getSqlProducer(String databaseName){
        return producerCache.get(databaseName);
    }

    static{
        Map<String, DatabaseConfig> databaseConfigs = DatabaseConfigFactory.getAllDatabaseConfig();
        if(MapTool.isNotEmpty(databaseConfigs)){
            Set<String> keys = databaseConfigs.keySet();
            for(String key : keys){
                DatabaseConfig databaseConfig = databaseConfigs.get(key);
                String databaseType = databaseConfig.getDatabaseType();
                String databaseName = databaseConfig.getName();
                if(!producerCache.containsKey(databaseType)){
                    if(StringUtils.equals("MySql", databaseType)){
                        producerCache.put(databaseName, new MySqlProducer());
                    }
                    else if(StringUtils.equals("Oracle", databaseType)){
                        producerCache.put(databaseName, new OracleSqlProducer());
                    }
                    else{
                        /** 默认Mysql*/
                        producerCache.put(databaseName, new MySqlProducer());
                    }
                }
            }
        }
    }
}
