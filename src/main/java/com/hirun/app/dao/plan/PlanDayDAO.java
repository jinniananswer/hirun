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
}
