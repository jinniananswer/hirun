package com.most.core.app.database.conn.pools;

import com.most.core.app.database.config.DatabaseConfig;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author jinnian
 * @Date 2018/2/14 23:22
 * @Description:
 */
public interface IConnectionPools {

    /**
     * 根据数据库的配置获取连接池所对应的数据源
     * @param dbConfig 数据库配置装载类
     * @return 数据源对象，可以用来获取连接池中的数据库连接
     * @throws SQLException
     */
    public DataSource getDataSource(DatabaseConfig dbConfig) throws SQLException;
}
