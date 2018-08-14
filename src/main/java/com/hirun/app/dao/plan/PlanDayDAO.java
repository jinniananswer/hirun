package com.hirun.app.dao.plan;

import com.hirun.pub.domain.entity.plan.PlanDayEntity;
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
public class PlanDayDAO extends StrongObjectDAO {

    public PlanDayDAO(String databaseName){
        super(databaseName);
    }

    public PlanDayEntity getPlanDayByStatDayAndStatTypeAndOid(String statDay, String statType, String objectId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("STAT_DAY", statDay);
        parameter.put("STAT_TYPE", statType);
        parameter.put("OBJECT_ID", objectId);
        List<PlanDayEntity> list = this.query(PlanDayEntity.class, "STAT_PLAN_DAY", parameter);
        if(ArrayTool.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<PlanDayEntity> queryPlanDayListByStartAndEndAndStatTypeAndOid(String startDate, String endDate, String statType, String objectId) throws Exception{
        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("START_DATE", startDate);
        parameter.put("END_DATE", endDate);
        parameter.put("STAT_TYPE", statType);
        parameter.put("OBJECT_ID", objectId);

        sql.append(" SELECT * FROM stat_plan_day ");
        sql.append(" where 1=1 ");
        sql.append(" and stat_day BETWEEN :START_DATE AND :END_DATE ");
        sql.append(" AND stat_type = :STAT_TYPE ");
        sql.append(" AND OBJECT_ID = :OBJECT_ID ");
        List<PlanDayEntity> list = this.queryBySql(PlanDayEntity.class, sql.toString(), parameter);

        return list;
    }
}
