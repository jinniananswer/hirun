package com.hirun.app.dao.plan;

import com.hirun.pub.domain.entity.plan.CyclePlanFinishInfoEntity;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-05-04.
 */
public class CyclePlanFinishInfoDAO extends StrongObjectDAO{

    public CyclePlanFinishInfoDAO(String databaseName) {
        super(databaseName);
    }

    public CyclePlanFinishInfoEntity getCyclePlanFinishInfoEntity(String employeeId, String actionCode) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);
        parameter.put("ACTION_CODE", actionCode);
        List<CyclePlanFinishInfoEntity> cyclePlanFinishInfoEntityList = this.query(CyclePlanFinishInfoEntity.class, "INS_CYCLE_PLAN_FINISH_INFO", parameter);
        if(ArrayTool.isNotEmpty(cyclePlanFinishInfoEntityList)) {
            return cyclePlanFinishInfoEntityList.get(0);
        } else {
            return null;
        }
    }
}
