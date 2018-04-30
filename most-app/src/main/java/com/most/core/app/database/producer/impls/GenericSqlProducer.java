package com.most.core.app.database.producer.impls;

import com.most.core.app.database.meta.MetaDataFactory;
import com.most.core.app.database.meta.data.ColumnData;
import com.most.core.app.database.meta.data.DataFieldType;
import com.most.core.app.database.meta.data.TableMetaData;
import com.most.core.app.database.producer.ISqlProducer;
import com.most.core.app.database.wrapper.ConnectionWrapper;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.datastruct.MapTool;
import com.most.core.pub.tools.datastruct.StringTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;

/**
 * @Author jinnian
 * @Date 2018/3/14 15:21
 * @Description:
 */
public class GenericSqlProducer implements ISqlProducer {

    protected transient Logger log = LogManager.getLogger(GenericSqlProducer.class.getName());

    //SQL语句的相关缓存
    protected static Map<String, String> cache = new HashMap<String, String>();

    public PreparedStatement generateQueryPkSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException {
        return this.generateQuerySql(conn, tableName, null, parameter, -1, -1);
    }

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException{
        return this.generateQuerySql(conn, tableName, parameter, -1, -1);
    }

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, String[] whereCols, Map<String, String> parameter) throws SQLException{
        return this.generateQuerySql(conn, tableName, whereCols, parameter, -1, -1);
    }

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, String[] whereCols, String[] values) throws SQLException{
        return this.generateQuerySql(conn, tableName, whereCols, values, -1, -1);
    }

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, Map<String, String> parameter, int startNum, int endNum) throws SQLException{
        if(MapTool.isEmpty(parameter))
            throw new SQLException("指定字段查询表" + tableName + "，参数不能为空");

        Set<String> keys = parameter.keySet();
        String[] cols = new String[keys.size()];
        int index = 0;
        for(String key : keys){
            cols[index] = key;
            index++;
        }

        return this.generateQuerySql(conn, tableName, cols, parameter, startNum, endNum);
    }


    public PreparedStatement generateQuerySql(ConnectionWrapper conn , String tableName, String[] whereCols, String[] values, int startNum, int endNum) throws SQLException{
        if(ArrayTool.isEmpty(whereCols) || ArrayTool.isEmpty(values))
            throw new SQLException("指定查询条件的列或者值不能为空");
        int colLength = whereCols.length;
        int valueLength = values.length;
        if(colLength != valueLength)
            throw new SQLException("指定查询条件的列和值的数量不匹配");

        Map<String, String> parameter = new HashMap<String, String>();
        for(int i=0;i<colLength;i++){
            parameter.put(whereCols[i], values[i]);
        }
        return this.generateQuerySql(conn, tableName, whereCols, parameter, startNum, endNum);
    }

    public PreparedStatement generateQuerySql(ConnectionWrapper conn, String tableName, String[] whereCols, Map<String, String> parameter, int startNum, int endNum) throws SQLException{

        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        String sql = this.generateSelectAllCols(conn, tableName);
        sb.append(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        if(ArrayTool.isEmpty(whereCols)) {
            //没传入where条件的，强制按主键查询
            whereCols = tableMetaData.getPrimaryKeys();
        }

        sb.append(this.generateWhereSql(whereCols, false));
        long end = System.currentTimeMillis();
        if(log.isDebugEnabled()){
            log.debug("根据指定字段生成select语句耗时"+(end-start)+"ms");
            log.debug("根据指定字段查询表"+ tableName + "的sql：" + sb.toString());
        }

        this.appendPageSql(sb, startNum, endNum);
        PreparedStatement stmt = conn.getConnection().prepareStatement(sb.toString());

        this.bindValue(conn, stmt, tableName, whereCols, parameter);
        return stmt;
    }

    public PreparedStatement generateQuerySqlBySql(ConnectionWrapper conn, String sql, Map<String, String> parameter) throws SQLException{
        return this.generateQuerySqlBySql(conn, sql, parameter, -1, -1);
    }

    public PreparedStatement generateQuerySqlBySql(ConnectionWrapper conn, String sql, Map<String, String> parameter, int startNum, int endNum) throws SQLException{
        Object[] objects = StringTool.parseVariableText(sql, ':', "?");
        StringBuilder sb = (StringBuilder)objects[0];
        List<String> variables = (List<String>)objects[1];
        this.appendPageSql(sb, startNum, endNum);
        if(log.isDebugEnabled())
            log.debug("生成的select语句为:"+sb.toString());

        PreparedStatement stmt = conn.getConnection().prepareStatement(sb.toString());
        this.bindValueWithoutColumnType(stmt, variables, parameter);
        return stmt;
    }

    public PreparedStatement generateInsertSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException{
        String sql = generateInsertAllCols(conn, tableName);
        PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        this.bindValue(stmt, tableMetaData.getColumns(), parameter);
        if(log.isDebugEnabled())
            log.debug("生成的insert语句为："+sql);
        return stmt;
    }

    public PreparedStatement generateInsertAutoIncrementSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException{
        String sql = generateInsertAllCols(conn, tableName);
        PreparedStatement stmt = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        this.bindValue(stmt, tableMetaData.getColumns(), parameter);
        if(log.isDebugEnabled())
            log.debug("生成的insert语句为："+sql);
        return stmt;
    }

    public PreparedStatement generateInsertBatchSql(ConnectionWrapper conn, String tableName, List<Map<String, String>> parameters) throws SQLException{
        if(ArrayTool.isEmpty(parameters))
            throw new SQLException("批量插入数据时，绑定参数集合不能为空");
        String sql = this.generateInsertAllCols(conn, tableName);

        if(log.isDebugEnabled())
            log.debug("生成的insert语句为"+sql);

        PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        List<ColumnData> cols = tableMetaData.getColumns();
        for(Map<String, String> parameter : parameters){
            this.bindValue(stmt, cols, parameter);
            stmt.addBatch();
        }
        return stmt;
    }


    /**
     * 生成根据主键更新所有字段的SQL
     * @param conn
     * @param tableName
     * @param parameter
     * @return
     * @throws SQLException
     */
    public PreparedStatement generateUpdateSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException{
        String sql = generateUpdateAllCols(conn, tableName, null);

        if(log.isDebugEnabled())
            log.debug("生成的update语句为："+sql);
        PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        List<ColumnData> columns = new ArrayList<ColumnData>();
        List<ColumnData> primaryColumns = tableMetaData.getPrimaryColumns();
        columns.addAll(tableMetaData.getColumns());
        columns.addAll(primaryColumns);
        this.bindValue(stmt, columns ,parameter);
        return stmt;
    }

    public PreparedStatement generateUpdateBatchSql(ConnectionWrapper conn, String tableName, List<Map<String, String>> parameters) throws SQLException{
        if(ArrayTool.isEmpty(parameters))
            throw new SQLException("批量修改数据时，绑定参数集合不能为空");

        String sql = generateUpdateAllCols(conn, tableName, null);

        if(log.isDebugEnabled())
            log.debug("生成的update语句为："+sql);

        PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        List<ColumnData> columns = new ArrayList<ColumnData>();
        List<ColumnData> primaryColumns = tableMetaData.getPrimaryColumns();
        columns.addAll(tableMetaData.getColumns());
        columns.addAll(primaryColumns);

        for(Map<String, String> parameter : parameters) {
            this.bindValue(stmt, columns, parameter);
            stmt.addBatch();
        }
        return stmt;
    }

    /**
     * 生成根据指定列更新所有列的SQL
     * @param conn
     * @param tableName
     * @param columns
     * @param parameter
     * @return
     * @throws SQLException
     */
    public PreparedStatement generateUpdateSql(ConnectionWrapper conn, String tableName, String[] columns, Map<String, String> parameter) throws SQLException{
        if(ArrayTool.isEmpty(columns)){
            return this.generateUpdateSql(conn, tableName, parameter);
        }
        String sql = this.generateUpdateAllCols(conn, tableName, columns);

        if(log.isDebugEnabled())
            log.debug("生成的update语句为："+sql);

        PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        List<ColumnData> columnDatas = new ArrayList<ColumnData>();
        columnDatas.addAll(tableMetaData.getColumns());
        for(String column : columns){
            ColumnData columnData = tableMetaData.getColumn(column);
            columnDatas.add(columnData);
        }

        this.bindValue(stmt, columnDatas ,parameter);
        return stmt;
    }

    public PreparedStatement generateUpdateBatchSql(ConnectionWrapper conn, String tableName, String[] columns, List<Map<String, String>> parameters) throws SQLException{
        if(ArrayTool.isEmpty(columns)){
            return this.generateUpdateBatchSql(conn, tableName, parameters);
        }

        if(ArrayTool.isEmpty(parameters))
            throw new SQLException("批量修改数据时，绑定参数集合不能为空");

        String sql = this.generateUpdateAllCols(conn, tableName, columns);

        if(log.isDebugEnabled())
            log.debug("生成的update语句为："+sql);

        PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        List<ColumnData> columnDatas = new ArrayList<ColumnData>();
        columnDatas.addAll(tableMetaData.getColumns());
        for(String column : columns){
            ColumnData columnData = tableMetaData.getColumn(column);
            columnDatas.add(columnData);
        }
        for(Map<String, String> parameter : parameters) {
            this.bindValue(stmt, columnDatas, parameter);
            stmt.addBatch();
        }
        return stmt;
    }

    /**
     * 生成根据主键删除单条数据的SQL语句
     * @param conn
     * @param tableName
     * @param parameter
     * @return
     * @throws SQLException
     */
    public PreparedStatement generateDeleteSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException{
        String sql = "DELETE FROM " + tableName;
        StringBuilder sb = new StringBuilder(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        String[] primaryKeys = tableMetaData.getPrimaryKeys();
        String whereSql = this.generateWhereSql(primaryKeys, false);
        sb.append(whereSql);

        if(log.isDebugEnabled())
            log.debug("生成的delete语句为："+sb.toString());

        PreparedStatement stmt = conn.getConnection().prepareStatement(sb.toString());
        this.bindValue(stmt, tableMetaData.getPrimaryColumns(), parameter);
        return stmt;
    }

    /**
     * 根据指定列删除数据
     * @param conn
     * @param tableName
     * @param cols
     * @param parameter
     * @return
     * @throws SQLException
     */
    public PreparedStatement generateDeleteSql(ConnectionWrapper conn, String tableName, String[] cols, Map<String, String> parameter) throws SQLException{
        if(ArrayTool.isEmpty(cols))
            return this.generateDeleteSql(conn, tableName, parameter);
        String sql = "DELETE FROM " + tableName;
        StringBuilder sb = new StringBuilder(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        List<ColumnData> columnDatas = new ArrayList<ColumnData>();
        for(String col : cols){
            ColumnData columnData = tableMetaData.getColumn(col);
            columnDatas.add(columnData);
        }
        String whereSql = this.generateWhereSql(cols, false);
        sb.append(whereSql);

        if(log.isDebugEnabled())
            log.debug("生成的delete语句为："+sb.toString());

        PreparedStatement stmt = conn.getConnection().prepareStatement(sb.toString());
        this.bindValue(stmt, columnDatas, parameter);
        return stmt;
    }

    public PreparedStatement generateDeleteBatchSql(ConnectionWrapper conn, String tableName, List<Map<String, String>> parameters) throws SQLException{
        String sql = "DELETE FROM " + tableName;
        StringBuilder sb = new StringBuilder(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        String[] primaryKeys = tableMetaData.getPrimaryKeys();
        String whereSql = this.generateWhereSql(primaryKeys, false);
        sb.append(whereSql);

        if(log.isDebugEnabled())
            log.debug("生成的delete语句为："+sb.toString());

        PreparedStatement stmt = conn.getConnection().prepareStatement(sb.toString());
        for(Map<String, String> parameter : parameters) {
            this.bindValue(stmt, tableMetaData.getPrimaryColumns(), parameter);
            stmt.addBatch();
        }
        return stmt;
    }

    public PreparedStatement generateDeleteBatchSql(ConnectionWrapper conn, String tableName, String[] cols, List<Map<String, String>> parameters) throws SQLException{
        if(ArrayTool.isEmpty(cols)){
            return this.generateDeleteBatchSql(conn, tableName, parameters);
        }

        String sql = "DELETE FROM " + tableName;
        StringBuilder sb = new StringBuilder(sql);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        List<ColumnData> columnDatas = new ArrayList<ColumnData>();
        for(String col : cols){
            ColumnData columnData = tableMetaData.getColumn(col);
            columnDatas.add(columnData);
        }
        String whereSql = this.generateWhereSql(cols, false);
        sb.append(whereSql);

        if(log.isDebugEnabled())
            log.debug("生成的delete语句为："+sb.toString());

        PreparedStatement stmt = conn.getConnection().prepareStatement(sb.toString());
        for(Map<String, String> parameter : parameters) {
            this.bindValue(stmt, columnDatas, parameter);
            stmt.addBatch();
        }
        return stmt;
    }

    /**
     * 需要子类覆写
     * @param sb
     * @param startNum
     * @param endNum
     */
    public void appendPageSql(StringBuilder sb, int startNum, int endNum){

    }

    protected String generateSelectAllCols(ConnectionWrapper conn, String tableName){
        String cacheKey = tableName+"_SELECT";
        if(cache.containsKey(cacheKey)){
            return cache.get(cacheKey);
        }

        String databaseName = conn.getDatabaseName();
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(databaseName, tableName);
        String sql = this.generateSelectAllCols(tableMetaData);
        cache.put(cacheKey, sql);
        return sql;
    }

    /**
     * 根据表源数据类生成不带where条件的SQL语句，可由子类覆写
     * @param tableMetaData
     * @return
     */
    protected String generateSelectAllCols(TableMetaData tableMetaData){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        String columns = tableMetaData.getColumnNames();
        sb.append(columns + " ");
        sb.append("FROM " + tableMetaData.getTableName().toUpperCase() + " ");
        return sb.toString();
    }

    protected String generateInsertAllCols(ConnectionWrapper conn, String tableName){
        String cacheKey = tableName.toUpperCase()+"_INS";
        if(cache.containsKey(cacheKey)){
            return cache.get(cacheKey);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + tableName.toUpperCase() + "(");
        String databaseName = conn.getDatabaseName();
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(databaseName, tableName);
        String columns = tableMetaData.getColumnNames();
        sb.append(columns + ") VALUES(");
        int columnSize = tableMetaData.getColumns().size();
        for(int i=0;i<columnSize;i++){
            if(i == columnSize - 1)
                sb.append("?");
            else
                sb.append("?,");
        }
        sb.append(")");
        String sql = sb.toString();
        cache.put(cacheKey, sql);
        return sql;
    }

    protected String generateUpdateAllCols(ConnectionWrapper conn, String tableName, String[] whereCols){
        String cacheKey = tableName + "_UPD";
        if(cache.containsKey(cacheKey))
            return cache.get(cacheKey);

        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE " + tableName.toUpperCase() + " SET ");
        List<ColumnData> cols = tableMetaData.getColumns();
        int index = 0;
        int colSize = cols.size();
        for(ColumnData col : cols){
            sb.append(col.getName() +" = ?");
            index++;
            if(index != colSize)
                sb.append(",");
        }
        if(ArrayTool.isEmpty(whereCols))
            whereCols = tableMetaData.getPrimaryKeys();

        sb.append(this.generateWhereSql(whereCols, false));

        String sql = sb.toString();
        cache.put(cacheKey, sql);
        return sql;
    }

    protected String generateWhereSql(String[] cols, boolean isColon) {
        if (ArrayTool.isEmpty(cols))
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE ");
        int i = 0;
        for (String col : cols) {
            if (i != 0)
                sb.append("AND ");
            sb.append(col + " = " );
            if(isColon)
                sb.append(":" + col + " ");
            else
                sb.append("? ");
            i++;
        }
        return sb.toString();
    }

    protected void bindValue(ConnectionWrapper conn, PreparedStatement preparedStatement, String tableName, String[] columns, Map<String, String> parameter) throws SQLException{
        if(ArrayTool.isEmpty(columns))
            return;
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        List<ColumnData> columnDatas = new ArrayList<ColumnData>();
        for(String whereCol : columns){
            ColumnData columnData = tableMetaData.getColumn(whereCol);
            columnDatas.add(columnData);
        }

        this.bindValue(preparedStatement, columnDatas, parameter);
    }

    protected void bindValue(PreparedStatement preparedStatement, List<ColumnData> columns, Map<String, String> parameter) throws SQLException{
        if(ArrayTool.isEmpty(columns))
            return;

        int i = 1;
        for(ColumnData column : columns){
            DataFieldType type = column.getType();
            String value = parameter.get(column.getName());

            if(log.isDebugEnabled())
                log.debug("绑定参数" + i + ": " + column.getName() + ",其绑定值为" + value + ";\n");

            if(StringUtils.isBlank(value)) {
                preparedStatement.setNull(i, Types.VARCHAR);
            } else {
                if(type == DataFieldType.NUMBER){
                    if(value.length() <= 18)
                        preparedStatement.setInt(i, Integer.parseInt(value));
                    else
                        preparedStatement.setLong(i, Long.parseLong(value));
                }
                else if(type == DataFieldType.STRING)
                    preparedStatement.setString(i, value);
                else if(type == DataFieldType.DATE)
                    preparedStatement.setString(i, value);
                else
                    preparedStatement.setString(i, value);
            }

            i++;
        }
    }

    protected void bindValueWithoutColumnType(PreparedStatement preparedStatement, List<String> columns, Map<String, String> parameter) throws SQLException{
        if(ArrayTool.isEmpty(columns))
            return;

        int i = 1;
        for(String column : columns){
            String value = parameter.get(column);
            if(log.isDebugEnabled())
                log.debug("绑定参数" + i + ": " + column + ",其绑定值为" + value + ";\n");
            preparedStatement.setString(i, value);
            i++;
        }
    }
}
