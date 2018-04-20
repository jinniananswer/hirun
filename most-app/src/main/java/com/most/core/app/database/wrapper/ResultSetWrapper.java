package com.most.core.app.database.wrapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @Author jinnian
 * @Date 2018/3/23 14:42
 * @Description:
 */
public class ResultSetWrapper {

    private ResultSet resultSet;

    private int count = 0;

    private static final int DATA_LENGTH_LIMIT = 10000;

    public ResultSetWrapper(ResultSet resultSet){
        this.resultSet = resultSet;
    }

    public boolean next() throws SQLException {
        boolean next = resultSet.next();
        if(next && DATA_LENGTH_LIMIT > 0){
            this.count++;
            if(this.count > DATA_LENGTH_LIMIT)
                throw new SQLException("系统过载保护，返回的数据结果集数量过大，请重新检查SQL是否合适");
        }
        return next;
    }

    public void close() throws SQLException {
        resultSet.close();
    }

    public ResultSetMetaData getMetaData() throws SQLException{
        return this.resultSet.getMetaData();
    }

    public String getColumnLabel(int index) throws  SQLException{
        return this.resultSet.getMetaData().getColumnLabel(index);
    }

    public int getColumnNum() throws SQLException {
        return resultSet.getMetaData().getColumnCount();
    }

    public String getString(int columnIndex) throws SQLException {
        return resultSet.getString(columnIndex);
    }

    public String getString(String columnLabel) throws SQLException{
        return resultSet.getString(columnLabel);
    }

    public Object getObject(String columnLabel) throws SQLException{
        return resultSet.getObject(columnLabel);
    }

    public Object getObject(int columnIndex) throws SQLException{
        return resultSet.getObject(columnIndex);
    }
}
