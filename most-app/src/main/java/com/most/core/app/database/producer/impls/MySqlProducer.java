package com.most.core.app.database.producer.impls;

import com.most.core.app.database.meta.MetaDataFactory;
import com.most.core.app.database.meta.data.ColumnData;
import com.most.core.app.database.meta.data.DataFieldType;
import com.most.core.app.database.meta.data.TableMetaData;
import com.most.core.app.database.wrapper.ConnectionWrapper;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/3/19 21:50
 * @Description:
 */
public class MySqlProducer extends GenericSqlProducer {

    @Override
    protected String generateSelectAllCols(TableMetaData tableMetaData){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        List<ColumnData> cols = tableMetaData.getColumns();
        int size = cols.size();
        int i = 0;
        for(ColumnData col : cols){
            DataFieldType type = col.getType();
            String colName = col.getName();
            if(type == DataFieldType.DATE)
                sb.append("date_format("+colName+", '%Y-%m-%d %H:%I:%S') "+colName);
            else
                sb.append(colName);

            i++;
            if(i != size)
                sb.append(", ");
            else
                sb.append(" ");
        }
        sb.append("FROM " + tableMetaData.getTableName().toUpperCase() + " ");
        return sb.toString();
    }

    @Override
    public void appendPageSql(StringBuilder sb, int startNum, int endNum){
        if(startNum != -1 && endNum != -1 && endNum > startNum){
            sb.append(" LIMIT "+startNum+","+(endNum-startNum+1));
        }
    }

    @Override
    public PreparedStatement generateInsertSql(ConnectionWrapper conn, String tableName, Map<String, String> parameter) throws SQLException {
        String sql = generateInsertAllCols(conn, tableName);
        PreparedStatement stmt = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        this.bindValue(stmt, tableMetaData.getColumns(), parameter);
        if(log.isDebugEnabled())
            log.debug("生成的insert语句为："+sql);
        return stmt;
    }

    @Override
    public PreparedStatement generateInsertBatchSql(ConnectionWrapper conn, String tableName, List<Map<String, String>> parameters) throws SQLException{
        if(ArrayTool.isEmpty(parameters))
            throw new SQLException("批量插入数据时，绑定参数集合不能为空");
        String sql = this.generateInsertAllCols(conn, tableName);

        if(log.isDebugEnabled())
            log.debug("生成的insert语句为"+sql);

        PreparedStatement stmt = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//返回自增主键
        TableMetaData tableMetaData = MetaDataFactory.getTableMetaData(conn.getDatabaseName(), tableName);
        List<ColumnData> cols = tableMetaData.getColumns();
        for(Map<String, String> parameter : parameters){
            this.bindValue(stmt, cols, parameter);
            stmt.addBatch();
        }
        return stmt;
    }
}
