package com.most.core.app.database.meta;

import com.most.core.app.database.config.DatabaseConfig;
import com.most.core.app.database.config.DatabaseConfigFactory;
import com.most.core.app.database.meta.data.TableMetaData;
import com.most.core.app.database.meta.impls.MySqlMetaDataLoader;
import com.most.core.app.database.meta.impls.OracleMetaDataLoader;
import com.most.core.pub.tools.datastruct.MapTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author jinnian
 * @Date 2018/3/12 12:46
 * @Description:
 */
public class MetaDataFactory {

    private static Map<String, IMetaDataLoader> loaderCache = new HashMap<String, IMetaDataLoader>();

    private static Logger log = LogManager.getLogger(MetaDataFactory.class.getName());

    public static void init(){
        String[] preTables = new String[]{"SYS_STATIC_DATA"};

        for(String table : preTables){
            IMetaDataLoader loader = loaderCache.get("hirun_sys");
            TableMetaData tableMetaData = null;
            try {
                tableMetaData = loader.loadMetaData("hirun_sys",table);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            log.debug(tableMetaData.toString());
        }
    }

    public static TableMetaData getTableMetaData(String databaseName, String tableName){
        IMetaDataLoader loader = loaderCache.get(databaseName);
        if(loader == null){
            return null;
        }

        TableMetaData tableMetaData = null;
        try {
            tableMetaData = loader.loadMetaData(databaseName, tableName);
        } catch (SQLException e) {
            log.error(tableMetaData);
        }
        return tableMetaData;
    }

    static{
        Map<String, DatabaseConfig> databaseConfigs = DatabaseConfigFactory.getAllDatabaseConfig();
        if(MapTool.isNotEmpty(databaseConfigs)){
            Set<String> keys = databaseConfigs.keySet();
            for(String key : keys){
                DatabaseConfig databaseConfig = databaseConfigs.get(key);
                String databaseType = databaseConfig.getDatabaseType();
                String databaseName = databaseConfig.getName();
                if(!loaderCache.containsKey(databaseName)){
                    if(StringUtils.equals("MySql", databaseType)){
                        loaderCache.put(databaseName, new MySqlMetaDataLoader());
                    }
                    else if(StringUtils.equals("Oracle", databaseType)){
                        loaderCache.put(databaseName, new OracleMetaDataLoader());
                    }
                    else{
                        /** 默认Mysql*/
                        loaderCache.put(databaseName, new MySqlMetaDataLoader());
                    }
                }
            }
        }
    }
}
