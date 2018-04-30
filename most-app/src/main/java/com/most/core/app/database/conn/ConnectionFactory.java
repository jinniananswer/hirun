package com.most.core.app.database.conn;

import com.most.core.app.database.conn.pools.factory.ConnectionPoolsFactory;
import com.most.core.app.database.wrapper.ConnectionWrapper;
import com.most.core.app.database.meta.MetaDataFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author jinnian
 * @Date 2018/2/20 22:38
 * @Description:
 */
public class ConnectionFactory {

    private static Logger log = LogManager.getLogger(ConnectionFactory.class.getName());

    public static ConnectionWrapper getConnection(String databaseName){
        DataSource ds = ConnectionPoolsFactory.getDataSource(databaseName);
        try {
            Connection conn = ds.getConnection();
            conn.setAutoCommit(false);
            ConnectionWrapper connectionWrapper = new ConnectionWrapper(databaseName, conn);
            return connectionWrapper;
        } catch (SQLException e) {
            log.error(e);
        }

        return null;
    }

    /**
     * 初始化连接工厂
     */
    public static void init(){
        //初始化连接池工厂
        if(log.isDebugEnabled()){
            log.debug("***************************连接池工厂初始化***************************");
        }
        ConnectionPoolsFactory.init();
        MetaDataFactory.init();
    }
}
