package com.most.core.app.database.dao;

import com.most.core.pub.data.GenericEntity;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 11:47
 * @Description:
 */
public class StrongObjectDAO extends GenericDAO {

    public StrongObjectDAO(String databaseName){
        super(databaseName);
    }

    public <K extends GenericEntity> K queryByPk(Class<K> classes, String tableName, Map<String, String> parameter) throws SQLException{
        List<K> objects = this.query(classes, tableName, parameter);

        if(ArrayTool.isEmpty(objects))
            return null;
        return objects.get(0);
    }

    public <K extends GenericEntity> List<K> query(Class<K> classes, String tableName, Map<String, String> parameter) throws SQLException{
        RecordSet recordSet = this.query(tableName, parameter);
        List<K> objects = this.trans(classes, recordSet);
        return objects;
    }

    public <K extends GenericEntity> List<K> query(Class<K> classes, String tableName, String[] cols, Map<String, String> parameter) throws SQLException{
        RecordSet recordSet = this.query(tableName, cols, parameter);

        List<K> objects = this.trans(classes, recordSet);
        return objects;
    }

    public <K extends GenericEntity> List<K> queryBySql(Class<K> classes, String sql, Map<String, String> parameter) throws SQLException{
        RecordSet recordSet = this.queryBySql(sql, parameter);

        List<K> objects = this.trans(classes, recordSet);
        return objects;
    }

    private <K extends GenericEntity> List<K> trans(Class<K> classes, RecordSet recordSet){
        if(recordSet == null || recordSet.size() <= 0){
            return null;
        }
        int size = recordSet.size();
        List<K> objects = new ArrayList<K>();
        for(int i=0;i<size;i++){
            Record record = recordSet.get(i);
            Map<String, String> data = record.getData();
            try {
                K object = classes.newInstance();
                object.setContent(data);
                objects.add(object);
            } catch (InstantiationException e) {
                this.log.error(e);
            } catch (IllegalAccessException e) {
                this.log.error(e);
            }
        }
        return objects;
    }
}
