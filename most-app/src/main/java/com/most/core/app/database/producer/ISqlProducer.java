package com.most.core.app.database.producer;

import com.most.core.app.database.wrapper.ConnectionWrapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/3/14 15:15
 * @Description:
 */
public interface ISqlProducer {

    public PreparedStatement generateQueryPkSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException;

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException;

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, Map<String, String> parameter, int startNum, int endNum) throws SQLException;

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, String[] whereCols, Map<String, String> parameter) throws SQLException;

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, String[] whereCols, String[] values) throws SQLException;

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, String[] whereCols, String[] values, int startNum, int endNum) throws SQLException;

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, String[] whereCols, Map<String, String> parameter, int startNum, int endNum) throws SQLException;

    public PreparedStatement generateQuerySqlBySql(ConnectionWrapper conn, String sql, Map<String, String> parameter) throws SQLException;

    public PreparedStatement generateQuerySqlBySql(ConnectionWrapper conn, String sql, Map<String, String> parameter, int startNum, int endNum) throws SQLException;

    public PreparedStatement generateInsertSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException;

    public PreparedStatement generateInsertBatchSql(ConnectionWrapper conn, String tableName, List<Map<String, String>> parameters) throws SQLException;

    public PreparedStatement generateUpdateSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException;

    public PreparedStatement generateUpdateSql(ConnectionWrapper conn, String tableName, String[] columns, Map<String, String> parameter) throws SQLException;

    public PreparedStatement generateUpdateBatchSql(ConnectionWrapper conn, String tableName, List<Map<String, String>> parameters) throws SQLException;

    public PreparedStatement generateUpdateBatchSql(ConnectionWrapper conn, String tableName, String[] columns, List<Map<String, String>> parameters) throws SQLException;

    public PreparedStatement generateDeleteSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException;

    public PreparedStatement generateDeleteSql(ConnectionWrapper conn, String tableName, String[] cols, Map<String, String> parameter) throws SQLException;

    public PreparedStatement generateDeleteBatchSql(ConnectionWrapper conn, String tableName, List<Map<String, String>> parameters) throws SQLException;

    public PreparedStatement generateDeleteBatchSql(ConnectionWrapper conn, String tableName, String[] cols, List<Map<String, String>> parameters) throws SQLException;
}
