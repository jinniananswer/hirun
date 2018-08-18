package com.hirun.app.dao.func;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;

import java.util.HashMap;
import java.util.Map;

@DatabaseName("sys")
public class FuncDAO extends GenericDAO {

    public FuncDAO(String databaseName){
        super(databaseName);
    }

    public RecordSet queryJobFunc(String jobRole) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("JOB_ROLE", jobRole);
        return this.query("sys_job_func", parameter);
    }

    public RecordSet queryRestFunc(String existsFuncIds) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("select * from sys_func ");
        sb.append("where func_id not in ("+existsFuncIds+") ");

        return this.queryBySql(sb.toString(), null);
    }
}
