package com.most.core.app.database.meta.impls;

import com.most.core.app.database.conn.ConnectionFactory;
import com.most.core.app.database.wrapper.ConnectionWrapper;
import com.most.core.app.database.meta.IMetaDataLoader;
import com.most.core.app.database.meta.data.ColumnData;
import com.most.core.app.database.meta.data.DataFieldType;
import com.most.core.app.database.meta.data.TableMetaData;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/3/12 10:40
 * @Description: 装载MYSQL相关的元数据定义
 */
public class MySqlMetaDataLoader implements IMetaDataLoader {

    private Map<String, TableMetaData> cached = new HashMap<String, TableMetaData>();

    /**
     * 根据数据库名和表名加载该表的源数据定义
     * @param databaseName 数据库名
     * @param tableName 表名
     * @throws SQLException
     */
    public TableMetaData loadMetaData(String databaseName, String tableName) throws SQLException {
        if(cached.containsKey(tableName.toUpperCase())){
            return cached.get(tableName.toUpperCase());
        }
        TableMetaData metaData = new TableMetaData();
        metaData.setTableName(tableName);
        ConnectionWrapper connectionWrapper = ConnectionFactory.getConnection(databaseName);
        Connection conn = connectionWrapper.getConnection();

        PreparedStatement statement = conn.prepareStatement("select b.column_name from information_schema.TABLE_CONSTRAINTS a, information_schema.KEY_COLUMN_USAGE b where a.table_name = b.table_name and a.table_schema = ? and b.table_name = ?");
        statement.setString(1, databaseName);
        statement.setString(2, tableName);
        ResultSet resultSet = statement.executeQuery();

        List<String> primaryKeys = new ArrayList<String>();
        while(resultSet.next()){
            String columnName = resultSet.getString("COLUMN_NAME");
            primaryKeys.add(columnName);
        }
        if(ArrayTool.isEmpty(primaryKeys))
            throw new SQLException("没有找到表"+tableName+"的主键信息，请检查数据库");

        metaData.setPrimaryKeys(primaryKeys.toArray(new String[0]));

        statement = conn.prepareStatement("select column_name, is_nullable, data_type, column_default, character_maximum_length, column_comment from information_schema.columns t where t.table_name = ?");
        statement.setString(1, tableName);
        resultSet = statement.executeQuery();
        List<ColumnData> columns = new ArrayList<ColumnData>();
        while(resultSet.next()){
            ColumnData column = new ColumnData();
            column.setName(resultSet.getString("COLUMN_NAME").toUpperCase());
            column.setNullable(StringUtils.equals("NO", resultSet.getString("IS_NULLABLE"))?false:true);
            column.setDefaultValue(resultSet.getString("COLUMN_DEFAULT"));
            String length = resultSet.getString("CHARACTER_MAXIMUM_LENGTH");
            if(StringUtils.isBlank(length))
                length = "-1";
            column.setLength(Integer.parseInt(length));
            column.setType(this.transferFieldType(resultSet.getString("DATA_TYPE")));
            column.setComment(resultSet.getString("COLUMN_COMMENT"));
            columns.add(column);
        }

        if(ArrayTool.isEmpty(columns))
            throw new SQLException("没有找到该表"+tableName+"的列定义信息，请检查数据库");

        resultSet.close();
        statement.close();
        conn.close();
        metaData.setColumns(columns);
        this.cached.put(tableName.toUpperCase(), metaData);
        return metaData;
    }

    private DataFieldType transferFieldType(String type){
        if(StringUtils.equals("varchar", type))
            return DataFieldType.STRING;
        else if(StringUtils.equals("datetime", type))
            return DataFieldType.DATE;
        else if(StringUtils.equals("int", type) || StringUtils.equals("numeric", type))
            return DataFieldType.NUMBER;
        else
            return DataFieldType.STRING;
    }
}
