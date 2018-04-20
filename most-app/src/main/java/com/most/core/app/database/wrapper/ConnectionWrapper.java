package com.most.core.app.database.wrapper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author jinnian
 * @Date 2018/3/19 16:53
 * @Description:
 */
public class ConnectionWrapper {
    private Connection connection;
    private String databaseName;

    public ConnectionWrapper(String databaseName, Connection connection){
        this.databaseName = databaseName;
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void close() throws SQLException{
        this.connection.close();
    }

    public void rollback() throws SQLException{
        this.connection.rollback();
    }

    public void commit() throws  SQLException{
        this.connection.commit();
    }
}
