package com.hirun.app.dao.func;


import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;

import java.util.HashMap;
import java.util.Map;

@DatabaseName("all")
public class UserFuncDAO extends GenericDAO {

    public UserFuncDAO(String databaseName){
        super(databaseName);
    }

    public RecordSet queryUserFuncs(String userId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USER_ID", userId);

        StringBuilder sb = new StringBuilder();
        sb.append("select b.func_id, b.type, b.func_desc ");
        sb.append("from test_ins.ins_user_func a, test_sys.sys_func b ");
        sb.append("where a.user_id = :USER_ID ");
        sb.append("and b.func_id = a.func_id ");

        return this.queryBySql(sb.toString(), parameter);

    }

    public void deleteUserFuncs(String userId, String delFuncs) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USER_ID", userId);
        StringBuilder sb = new StringBuilder();
        sb.append(" delete from test_ins.ins_user_func  ");
        sb.append(" where user_id = :USER_ID ");
        sb.append(" and func_id in ("+delFuncs+") ");

        this.executeUpdate(sb.toString(), parameter);
    }
}
