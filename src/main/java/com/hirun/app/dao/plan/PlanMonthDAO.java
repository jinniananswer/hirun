package com.hirun.app.dao.plan;

import com.hirun.pub.domain.entity.plan.PlanMonthEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
@DatabaseName("ins")
public class PlanMonthDAO extends StrongObjectDAO {

    public PlanMonthDAO(String databaseName){
        super(databaseName);
    }

    public PlanMonthEntity getPlanMonthByStatMonthAndStatTypeAndOid(String statMonth, String statType, String objectId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("STAT_MONTH", statMonth);
        parameter.put("STAT_TYPE", statType);
        parameter.put("OBJECT_ID", objectId);
        List<PlanMonthEntity> list = this.query(PlanMonthEntity.class, "STAT_PLAN_MONTH", parameter);
        if(ArrayTool.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<PlanMonthEntity> queryPlanMonthListByMonthAndStatTypeAndOid(String statMonth, String statType, String objectId) throws Exception{
        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("STAT_MONTH", statMonth);
        parameter.put("STAT_TYPE", statType);
        parameter.put("OBJECT_ID", objectId);

        sql.append(" SELECT * FROM stat_plan_month ");
        sql.append(" where 1=1 ");
        sql.append(" and stat_month = :STAT_MONTH ");
        sql.append(" AND stat_type = :STAT_TYPE ");
        sql.append(" AND OBJECT_ID = :OBJECT_ID ");
        List<PlanMonthEntity> list = this.queryBySql(PlanMonthEntity.class, sql.toString(), parameter);

        return list;
    }
}
