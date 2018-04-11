package com.most.core.app.database.meta.data;

import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/3/12 10:43
 * @Description:
 */
public class TableMetaData {

    private String tableName;

    private String[] primaryKeys;

    private List<ColumnData> columns;

    public String[] getPrimaryKeys(){
        return primaryKeys;
    }

    public List<ColumnData> getColumns(){
        return columns;
    }

    public void setPrimaryKeys(String[] primaryKeys){
        this.primaryKeys = primaryKeys;
    }

    public void setColumns(List<ColumnData> columns){
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取列名，多列列名以逗号分隔
     * @return
     */
    public String getColumnNames(){
        if(ArrayTool.isEmpty(this.columns)){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int size = this.columns.size();
        int i = 0;
        for(ColumnData col : this.columns){
            sb.append(col.getName());
            i++;
            if(i != size)
                sb.append(",");
        }

        return sb.toString();
    }

    public List<ColumnData> getPrimaryColumns(){
        if(ArrayTool.isEmpty(primaryKeys))
            return null;

        List<ColumnData> primaryColumns = new ArrayList<ColumnData>();
        for(String primaryKey : this.primaryKeys){
            ColumnData primaryColumn = this.getColumn(primaryKey);
            if(primaryColumn != null)
                primaryColumns.add(primaryColumn);
        }
        return primaryColumns;
    }

    public ColumnData getColumn(String columnName){
        if(ArrayTool.isEmpty(this.columns))
            return null;

        for(ColumnData column : this.columns){
            if(StringUtils.equals(columnName.toUpperCase(), column.getName()))
                return column;
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("**************************table "+ this.tableName + "*************************************\n");
        sb.append("primary key : ");
        if(ArrayTool.isNotEmpty(this.primaryKeys)){
            for(String primaryKey : this.primaryKeys){
                sb.append(primaryKey + ";");
            }
        }
        if(ArrayTool.isNotEmpty(this.columns)){
            for(ColumnData col : this.columns){
                sb.append(col.toString());
            }
        }
        sb.append("**************************************************************************************\n");
        return sb.toString();
    }
}
