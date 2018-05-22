package com.hirun.app.dao.out;

import com.hirun.pub.domain.out.DataGetTimeEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-05-04.
 */
@DatabaseName("out")
public class DataGetTimeDAO extends StrongObjectDAO{

    public DataGetTimeDAO(String databaseName) {
        super(databaseName);
    }

    public DataGetTimeEntity getDataGetTimeEntityByGetType(String getType) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("GET_TYPE", getType);

        List<DataGetTimeEntity> list = this.query(DataGetTimeEntity.class, "OUT_DATA_GET_TIME", parameter);
        if(ArrayTool.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
