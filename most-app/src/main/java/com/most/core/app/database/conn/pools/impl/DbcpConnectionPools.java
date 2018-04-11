package com.most.core.app.database.conn.pools.impl;

import com.most.core.app.database.config.DatabaseConfig;
import com.most.core.app.database.conn.pools.IConnectionPools;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author jinnian
 * @Date 2018/2/14 23:22
 * @Description: 根据配置创建DBCP连接池
 */
public class DbcpConnectionPools implements IConnectionPools{

    private Logger log = LogManager.getLogger(DbcpConnectionPools.class.getName());

    /**
     * 获取DBCP数据库连接池所对应的数据源
     * @param dbConfig
     * @return
     * @throws SQLException
     */
    public DataSource getDataSource(DatabaseConfig dbConfig) throws SQLException{
        long start = System.currentTimeMillis();
        BasicDataSource dataSource = new BasicDataSource();
        String initialSize = dbConfig.getInitialSize();
        String maxIdle = dbConfig.getMaxIdle();
        String maxActive = dbConfig.getMaxActive();
        String maxWait = dbConfig.getMaxWait();

        if(StringUtils.isBlank(initialSize)){
            initialSize = "1";
        }

        if(StringUtils.isBlank(maxIdle)){
            maxIdle = "5";
        }

        if(StringUtils.isBlank(maxActive)){
            maxActive = "1";
        }

        if(StringUtils.isBlank(maxWait)){
            maxWait = "10000";
        }

        dataSource.setDriverClassName(dbConfig.getDriver());
        dataSource.setUsername(dbConfig.getUserName());
        dataSource.setPassword(dbConfig.getPassword());
        dataSource.setUrl(dbConfig.getUrl());
        dataSource.setInitialSize(Integer.parseInt(initialSize));
        dataSource.setMaxIdle(Integer.parseInt(maxIdle));
        dataSource.setMaxActive(Integer.parseInt(maxActive));
        dataSource.setMaxWait(Integer.parseInt(maxWait));
        dataSource.setMinIdle(Integer.parseInt(initialSize));
        //指明连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除.
        dataSource.setTestWhileIdle(true);
        //默认值是true。false表示每次从连接池中取出连接时，不需要执行validationQuery = "SELECT 1" 中的SQL进行测试。若配置为true,对性能有非常大的影响，性能会下降7-10倍。所在一定要配置为false.
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(180);
        dataSource.setNumTestsPerEvictionRun(Integer.parseInt(maxIdle));
        //池中的连接空闲30分钟后被回收,默认值就是30分钟。
        dataSource.setMinEvictableIdleTimeMillis(1800000);
        //每30秒运行一次空闲连接回收器（独立线程）。并每次检查3个连接，如果连接空闲时间超过30分钟就销毁。销毁连接后，连接数量就少了，如果小于minIdle数量，就新建连接，维护数量不少于minIdle，过行了新老更替。
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        //在每次空闲连接回收器线程(如果有)运行时检查的连接数量，默认值就是3.
        dataSource.setNumTestsPerEvictionRun(3);

        String validQuerySql = dbConfig.getValidQuery();
        if(StringUtils.isBlank(validQuerySql))
            validQuerySql = "select 1";
        dataSource.setValidationQuery(validQuerySql);
        dataSource.setValidationQueryTimeout(1000);

        if(log.isDebugEnabled()){
            log.debug("开始数据库连接池" + dbConfig.getName() + "的初始化");
        }

        /**
         * 验证数据库连接池初始化后，创建的连接是否正常
         */
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("数据源初始化失败，"+dbConfig.getName()+","+e);
            throw e;
        } finally {
            if(conn != null)
                conn.close();
        }

        if(log.isDebugEnabled()){
            log.debug("数据库连接池" + dbConfig.getName() + "初始化完成");
            log.debug("数据库驱动:" + dbConfig.getDriver());
            log.debug("数据库URL:" + dbConfig.getUrl());
            log.debug("探测SQL:" + validQuerySql);
            log.debug("初始连接数:" + dbConfig.getInitialSize());
            log.debug("最大连接数:" + dbConfig.getMaxActive());
            log.debug("最大闲置数:" + dbConfig.getMaxIdle());
            log.debug("最大等待时间:" + dbConfig.getMaxWait() + "毫秒");
            log.debug("创建" + dbConfig.getUserName() + "数据库连接耗时" + (System.currentTimeMillis() - start) + "毫秒");
        }

        return dataSource;
    }

    /**
     * 测试数据库连接是否有效
     * @param conn
     */
    private void testConnection(Connection conn){
        try {
            PreparedStatement statement = conn.prepareStatement("select count(*) num from sys_employee_login");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                String num = resultSet.getString("num");
                log.debug("test sql result is " + num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
