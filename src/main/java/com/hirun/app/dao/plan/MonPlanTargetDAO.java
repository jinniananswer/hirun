package com.hirun.app.dao.plan;

import com.hirun.pub.domain.entity.plan.MonPlanTargetEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-05-04.
 */
@DatabaseName("ins")
public class MonPlanTargetDAO extends StrongObjectDAO{

    public MonPlanTargetDAO(String databaseName) {
        super(databaseName);
    }

    public MonPlanTargetEntity getMonPlanTargetEntityByObjTypeAndObj(String objType, String obj, String targetTimeType, String targetTimeValue) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EFFECT_OBJ_TYPE", objType);
        parameter.put("EFFECT_OBJ", obj);
        parameter.put("TARGET_TIME_TYPE", targetTimeType);
        parameter.put("TARGET_TIME_VALUE", targetTimeValue);

        List<MonPlanTargetEntity> monPlanTargetEntityList = this.query(MonPlanTargetEntity.class, "INS_MON_PLAN_TARGET", parameter);
        if(ArrayTool.isEmpty(monPlanTargetEntityList)) {
            return null;
        } else {
            return monPlanTargetEntityList.get(0);
        }
    }
}
