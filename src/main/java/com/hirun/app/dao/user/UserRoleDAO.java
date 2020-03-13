package com.hirun.app.dao.user;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: hirun
 * @description: 用户角色数据访问类
 * @author: jinnian
 * @create: 2020-03-13 11:19
 **/
@DatabaseName("ins")
public class UserRoleDAO extends StrongObjectDAO {

    public UserRoleDAO(String databaseName) {
        super(databaseName);
    }

    public boolean isCounselor(String userId) throws Exception {
        Map<String, String> param = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select user_id, role_id from ins_user_role ");
        sb.append("where user_id = :USER_ID ");
        sb.append("and end_date > now() ");
        sb.append("and role_id in (3,4) ");

        RecordSet recordSet = this.queryBySql(sb.toString(), param);
        if (recordSet != null && recordSet.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
