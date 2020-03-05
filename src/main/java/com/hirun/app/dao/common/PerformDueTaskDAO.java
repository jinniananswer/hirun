package com.hirun.app.dao.common;

import com.hirun.pub.domain.entity.common.PerformDueTaskEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-06-09.
 */
@DatabaseName("ins")
public class PerformDueTaskDAO extends StrongObjectDAO{

    public PerformDueTaskDAO(String databaseName) {
        super(databaseName);
    }

    public List<PerformDueTaskEntity> queryPerformDueTaskList() throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT TASK_ID,TASK_TYPE,OBJECT_ID,OBJECT_TYPE,EXEC_TIME,PARAMS,DEAL_TAG,CREATE_USER_ID,CREATE_TIME ");
        sql.append(" FROM INS_PERFORM_DUE_TASK ");
        sql.append(" WHERE DEAL_TAG = :DEAL_TAG ");
        sql.append(" AND EXEC_TIME < now() ");
        sql.append(" ORDER BY EXEC_TIME DESC ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("DEAL_TAG", "0");

        return this.queryBySql(PerformDueTaskEntity.class, sql.toString(), parameter);
    }
}
