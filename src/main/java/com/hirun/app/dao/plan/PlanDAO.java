package com.hirun.app.dao.plan;

import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
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
}
