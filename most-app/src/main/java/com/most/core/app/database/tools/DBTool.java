package com.most.core.app.database.tools;

import com.most.core.app.database.wrapper.ResultSetWrapper;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;

import java.sql.SQLException;

/**
 * @Author jinnian
 * @Date 2018/3/25 17:45
 * @Description:
 */
public class DBTool {

    public static RecordSet transform(ResultSetWrapper resultSet) throws SQLException{
        RecordSet recordSet = new RecordSet();
        int colNum = resultSet.getColumnNum();
        while (resultSet.next()){
            Record record = new Record();
            for(int i=1;i<=colNum;i++){
                String columnLabel = resultSet.getColumnLabel(i).toUpperCase();
                String value = resultSet.getString(columnLabel);
                record.put(columnLabel, value);
            }
            recordSet.add(record);
        }
        return recordSet;
    }
}
