package com.most.core.app.database.producer.impls;

import com.most.core.app.database.meta.data.ColumnData;
import com.most.core.app.database.meta.data.DataFieldType;
import com.most.core.app.database.meta.data.TableMetaData;

import java.util.List;

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
}
