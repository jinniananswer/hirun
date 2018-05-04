package com.most.core.app.database.dao;

import com.most.core.app.database.producer.ISqlProducer;
import com.most.core.app.database.producer.SqlProducerFactory;
import com.most.core.app.database.tools.DBTool;
import com.most.core.app.database.wrapper.ConnectionWrapper;
import com.most.core.app.database.wrapper.ResultSetWrapper;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/2/25 1:23
 * @Description: 数据访问对象
 */
public class GenericDAO {

    protected Logger log = LogManager.getLogger(GenericDAO.class.getName());

    protected ConnectionWrapper connection;

    protected ISqlProducer producer;

    protected static final int fetchSize = 2000;

    public GenericDAO(ConnectionWrapper connection){
        this.connection = connection;
        this.producer = SqlProducerFactory.getSqlProducer(this.connection.getDatabaseName());
    }

    public GenericDAO(String databaseName){
        this.connection = SessionManager.getSession().getConnection(databaseName);
        this.producer = SqlProducerFactory.getSqlProducer(this.connection.getDatabaseName());
    }

    public RecordSet query(String tableName, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateQuerySql(this.connection, tableName, parameter);
        return this.query(stmt);
    }

    public RecordSet query(String tableName, Map<String, String> parameter, int startNum, int endNum) throws SQLException{
        PreparedStatement stmt = producer.generateQuerySql(this.connection, tableName, parameter, startNum, endNum);
        return this.query(stmt);
    }

    public RecordSet query(String tableName, String[] whereCols, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateQuerySql(this.connection, tableName, whereCols, parameter);
        return this.query(stmt);
    }

    public RecordSet query(String tableName, String[] whereCols, Map<String, String> parameter, int startNum, int endNum) throws SQLException{
        PreparedStatement stmt = producer.generateQuerySql(this.connection, tableName, whereCols, parameter, startNum, endNum);
        return this.query(stmt);
    }

    public RecordSet query(String tableName, String[] cols, String[] values) throws SQLException{
        PreparedStatement stmt = producer.generateQuerySql(this.connection, tableName, cols, values);
        return this.query(stmt);
    }

    public RecordSet query(String tableName, String[] cols, String[] values, int startNum, int endNum) throws SQLException{
        PreparedStatement stmt = producer.generateQuerySql(this.connection, tableName, cols, values, startNum, endNum);
        return this.query(stmt);
    }

    public Record queryByPk(String tableName, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateQueryPkSql(this.connection, tableName, parameter);
        RecordSet recordset =  this.query(stmt);
        if(recordset != null && recordset.size() > 0)
            return recordset.get(0);

        return null;
    }

    public RecordSet queryBySql(String sql, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateQuerySqlBySql(this.connection, sql, parameter);
        return this.query(stmt);
    }

    public RecordSet queryBySql(String sql, Map<String, String> parameter, int startNum, int endNum) throws SQLException{
        PreparedStatement stmt = producer.generateQuerySqlBySql(this.connection, sql, parameter, startNum, endNum);
        return this.query(stmt);
    }

    protected RecordSet query(PreparedStatement stmt) throws SQLException{
        long start = System.currentTimeMillis();
        stmt.setFetchSize(fetchSize);
        ResultSet resultSet = stmt.executeQuery();
        long end = System.currentTimeMillis();
        if(log.isDebugEnabled()){
            log.debug("根据指定字段获取表数据共耗时："+(end-start)+"ms");
        }

        ResultSetWrapper wrapper = new ResultSetWrapper(resultSet);
        RecordSet recordSet = DBTool.transform(wrapper);
        wrapper.close();
        return recordSet;
    }


    public int insert(String tableName, Map<String, String> parameter) throws SQLException {
        PreparedStatement stmt = producer.generateInsertSql(this.connection, tableName, parameter);
        return this.executeUpdate(stmt);
    }

    public long insertAutoIncrement(String tableName, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateInsertAutoIncrementSql(this.connection, tableName, parameter);
        return this.executeInsertAutoIncrement(stmt);
    }

    public int[] insertBatch(String tableName, List<Map<String, String>> parameters) throws SQLException{
        PreparedStatement stmt = producer.generateInsertBatchSql(this.connection, tableName, parameters);
        return this.executeUpdateBatch(stmt);
    }

    public int update(String tableName, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateUpdateSql(this.connection, tableName, parameter);
        return this.executeUpdate(stmt);
    }

    public int update(String tableName, String[] cols, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateUpdateSql(this.connection, tableName, cols, parameter);
        return this.executeUpdate(stmt);
    }

    public int[] updateBatch(String tableName, List<Map<String, String>> parameters) throws SQLException{
        PreparedStatement stmt = producer.generateUpdateBatchSql(this.connection, tableName, parameters);
        return this.executeUpdateBatch(stmt);
    }

    public int[] updateBatch(String tableName, String[] cols, List<Map<String, String>> parameters) throws SQLException{
        PreparedStatement stmt = producer.generateUpdateBatchSql(this.connection, tableName, cols, parameters);
        return this.executeUpdateBatch(stmt);
    }

    public int delete(String tableName, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateDeleteSql(this.connection, tableName, parameter);
        return this.executeUpdate(stmt);
    }

    public int delete(String tableName, String[] cols, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateDeleteSql(this.connection, tableName, cols, parameter);
        return this.executeUpdate(stmt);
    }

    public int[] deleteBatch(String tableName, List<Map<String, String>> parameters) throws SQLException{
        PreparedStatement stmt = producer.generateDeleteBatchSql(this.connection, tableName, parameters);
        return this.executeUpdateBatch(stmt);
    }

    public int[] deleteBatch(String tableName, String[] cols, List<Map<String, String>> parameters) throws SQLException{
        PreparedStatement stmt = producer.generateDeleteBatchSql(this.connection, tableName, cols, parameters);
        return this.executeUpdateBatch(stmt);
    }

    public int executeUpdate(String sql, Map<String, String> parameter) throws SQLException{
        PreparedStatement stmt = producer.generateExecuteSqlBySql(this.connection, sql, parameter);
        return this.executeUpdate(stmt);
    }

    protected int executeUpdate(PreparedStatement stmt) throws SQLException{
        long start = System.currentTimeMillis();
        int num = stmt.executeUpdate();

        long end = System.currentTimeMillis();
        if(log.isDebugEnabled()){
            log.debug("IUD操作表数据耗时"+(end-start)+"ms");
        }
        stmt.close();
        return num;
    }

    public long executeInsertAutoIncrement(PreparedStatement stmt) throws SQLException{
        long start = System.currentTimeMillis();
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        long id = 0;
        if (rs.next()) {
            id = rs.getLong(1);
        }

        long end = System.currentTimeMillis();
        if(log.isDebugEnabled()){
            log.debug("insert操作表数据耗时"+(end-start)+"ms");
        }
        stmt.close();
        return id;
    }

    public int[] executeUpdateBatch(PreparedStatement stmt) throws SQLException{
        long start = System.currentTimeMillis();
        int[] nums = stmt.executeBatch();
        long end = System.currentTimeMillis();
        if(log.isDebugEnabled()){
            log.debug("批量IUD操作表数据耗时"+(end-start)+"ms");
        }
        stmt.close();
        return nums;
    }

    public int save(String tableName, Map<String, String> parameter) throws SQLException{
        long start = System.currentTimeMillis();
        Record record = this.queryByPk(tableName, parameter);
        if(record == null)
            throw new SQLException("根据表"+tableName+"的主键，没有查询到数据库记录");

        Map<String, String> data = record.getData();
        data.putAll(parameter);

        int num = this.update(tableName, data);
        long end = System.currentTimeMillis();
        if(log.isDebugEnabled())
            log.debug("根据主键save操作共计耗时："+(end-start)+"ms");
        return num;
    }

    public int save(String tableName, String[] cols, Map<String, String> parameter) throws SQLException{
        long start = System.currentTimeMillis();
        RecordSet recordset = this.query(tableName, cols, parameter);
        if(recordset == null || recordset.size() <= 0)
            throw new SQLException("根据表"+tableName+"的指定列，没有查询到数据库记录");
        Record record = recordset.get(0);
        Map<String, String> data = record.getData();
        data.putAll(parameter);
        int num = this.update(tableName, data);
        long end = System.currentTimeMillis();
        if(log.isDebugEnabled())
            log.debug("根据指定列save操作共计耗时："+(end-start)+"ms");

        return num  ;
    }

}
