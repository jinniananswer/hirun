package com.hirun.app.dao.plan;

import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-04.
 */
public class PlanActionNumDAO extends StrongObjectDAO{

    public PlanActionNumDAO(String databaseName) {
        super(databaseName);
    }

    public int getPlanActionNumBetweenStartAndEnd(String planExecutorId, String actionCode, String startDate, String endDate) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_EXECUTOR_ID", planExecutorId);
        parameter.put("START_DATE", startDate);
        parameter.put("END_DATE", endDate);
        parameter.put("ACTION_CODE", actionCode);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(*) NUM FROM INS_PLAN_ACTION_NUM A ");
        sql.append(" WHERE A.PLAN_EXECUTOR_ID = :PLAN_EXECUTOR_ID ");
        sql.append(" AND A.ACTION_CODE = :ACTION_CODE ");
        sql.append(" AND A.PLAN_DATE >= :START_DATE ");
        sql.append(" AND A.PLAN_DATE < :END_DATE ");
        RecordSet recordSet = this.queryBySql(sql.toString(), parameter);
        int num = recordSet.getInt(0, "NUM");

        return num;
    }
}
