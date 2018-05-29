package com.hirun.app.dao.plan;

import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
@DatabaseName("ins")
public class PlanDAO extends StrongObjectDAO {

    public PlanDAO(String databaseName){
        super(databaseName);
    }

    public PlanEntity getPlanEntityByEidAndPlanDate(String planExecutorId, String planDate) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_EXECUTOR_ID", planExecutorId);
        parameter.put("PLAN_DATE", planDate);
        List<PlanEntity> list = this.query(PlanEntity.class, "INS_PLAN", parameter);
        if(ArrayTool.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public PlanEntity getPlanInfoById(String planId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_ID", planId);
        List<PlanEntity> list = this.query(PlanEntity.class, "INS_PLAN", parameter);
        if(ArrayTool.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public int getBeforePlanNumByEid(String planExecutorId) throws Exception {
        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
//        parameter.put("PLAN_DATE", planDate);
        parameter.put("PLAN_EXECUTOR_ID", planExecutorId);

        sql.append(" SELECT COUNT(*) NUM FROM INS_PLAN ");
        sql.append(" WHERE PLAN_EXECUTOR_ID = :PLAN_EXECUTOR_ID ");
//        sql.append(" AND PLAN_DATE < :PLAN_DATE ");
        RecordSet recordSet = this.queryBySql(sql.toString(), parameter);
        int num = recordSet.getInt(0, "NUM");

        return num;
    }

    public PlanEntity getInitialPlanDate(String planExecutorId) throws Exception {
        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_EXECUTOR_ID", planExecutorId);

        sql.append(" SELECT * FROM INS_PLAN A ");
        sql.append(" WHERE A.PLAN_EXECUTOR_ID = :PLAN_EXECUTOR_ID ");
        sql.append(" AND A.PLAN_DATE = (SELECT MIN(PLAN_DATE) FROM INS_PLAN B WHERE B.PLAN_EXECUTOR_ID = :PLAN_EXECUTOR_ID) ");
        List<PlanEntity> list = this.queryBySql(PlanEntity.class, sql.toString(), parameter);
        if(ArrayTool.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
