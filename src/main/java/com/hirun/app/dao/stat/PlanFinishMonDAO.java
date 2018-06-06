package com.hirun.app.dao.stat;

import com.hirun.pub.domain.entity.plan.PlanFinishMonEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-06-06.
 */
@DatabaseName("ins")
public class PlanFinishMonDAO extends StrongObjectDAO {

    public PlanFinishMonDAO(String databaseName) {
        super(databaseName);
    }

    public PlanFinishMonEntity getPlanFinishMonEntityByStatMonAndEid(String statMon, String employeeId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("STAT_MON", statMon);
        parameter.put("EMPLOYEE_ID", employeeId);

        List<PlanFinishMonEntity> list = this.query(PlanFinishMonEntity.class, "STAT_PLAN_FINISH_MON", parameter);
        if(ArrayTool.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
