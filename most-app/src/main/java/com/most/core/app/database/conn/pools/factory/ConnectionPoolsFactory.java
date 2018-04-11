package com.most.core.app.database.conn.pools.factory;


import com.most.core.app.database.config.DatabaseConfig;
import com.most.core.app.database.config.DatabaseConfigFactory;
import com.most.core.app.database.conn.pools.IConnectionPools;
import com.most.core.app.database.conn.pools.impl.DbcpConnectionPools;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author jinnian
 * @Date 2018/2/14 22:40
 * @Description: 连接池工厂类，目前默认使用DBCP数据库连接池
 */
public class ConnectionPoolsFactory {

    private static Logger log = LogManager.getLogger(ConnectionPoolsFactory.class.getName());

    private static Map<String, DataSource> connectionPools = new HashMap<String, DataSource>();

    /**
     * 根据数据库配置信息获取对应的数据库连接池类
     * @param databaseConfig 数据库配置信息装载类
     * @return 数据库连接池实现类
     */
    public static IConnectionPools getConnectionPools(DatabaseConfig databaseConfig){
        String type = databaseConfig.getType();
        if(StringUtils.equals("dbcp", type))
            return new DbcpConnectionPools();
        else
            return new DbcpConnectionPools();
    }

    /**
     * 根据数据库名称获取数据库连接池所对应的数据源
     * @param databaseName 数据库名称
     * @return 连接池所对应的数据源，可根据数据源获取连接池中的数据库连接
     */
    public static DataSource getDataSource(String databaseName){
        return connectionPools.get(databaseName);
    }

    /**
     * 初始化方法，获取应用配置的所有数据库连接，构造数据库连接池，与数据库名称对应存在map结构中，同时走到缓存的作用
     */
    public static void init(){
        Map<String, DatabaseConfig> databaseConfig = DatabaseConfigFactory.getAllDatabaseConfig();
        Set<String> keys = databaseConfig.keySet();
        for(String key : keys){
            DatabaseConfig config = databaseConfig.get(key);
            IConnectionPools pools = getConnectionPools(config);
            DataSource dataSource = null;
            try {
                dataSource = pools.getDataSource(config);
                if(dataSource != null)
                    connectionPools.put(key, dataSource);
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }

}
