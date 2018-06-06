package com.hirun.app.bean.plan;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.stat.PlanFinishMonDAO;
import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.domain.entity.plan.PlanFinishMonEntity;
import com.most.core.app.database.dao.factory.DAOFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pc on 2018-06-06.
 */
public class PlanStatBean {

    public static void statMonPlanFinishActionByPlanEntity(PlanEntity planEntity) throws Exception {
        String planId = planEntity.getPlanId();
        String employeeId = planEntity.getPlanExecutorId();
        String statMon = planEntity.getPlanDate().substring(0, 4) + planEntity.getPlanDate().substring(5,7);
        CustActionDAO custActionDAO = DAOFactory.createDAO(CustActionDAO.class);
        PlanFinishMonDAO planFinishMonDAO = DAOFactory.createDAO(PlanFinishMonDAO.class);

        List<CustActionEntity> custActionList = custActionDAO.queryCustFinishActionListByPlanId(planEntity.getPlanId());
        JSONObject jsonFinishInfo = new JSONObject();
        for(CustActionEntity custActionEntity : custActionList) {
            String actionCode = custActionEntity.getActionCode();
            if(!jsonFinishInfo.containsKey(actionCode)) {
                jsonFinishInfo.put(actionCode, 0);
            }
            jsonFinishInfo.put(actionCode, jsonFinishInfo.getIntValue(actionCode)+1);
        }

        PlanFinishMonEntity planFinishMonEntity = planFinishMonDAO.getPlanFinishMonEntityByStatMonAndEid(statMon, employeeId);
        if(planFinishMonEntity == null) {
            planFinishMonEntity = new PlanFinishMonEntity();
            planFinishMonEntity.setStatMon(statMon);
            planFinishMonEntity.setEmployeeId(employeeId);
            planFinishMonEntity.setStatResult(jsonFinishInfo.toJSONString());
            planFinishMonDAO.insert("stat_plan_finish_mon", planFinishMonEntity.getContent());
        } else {
            JSONObject jsonStatResult = JSONObject.parseObject(planFinishMonEntity.getStatResult());
            Iterator<String> iter = jsonFinishInfo.keySet().iterator();
            while(iter.hasNext()) {
                String action = iter.next();
                int num = jsonStatResult.getIntValue(action);
                int finishNum = jsonFinishInfo.getIntValue(action);
                jsonStatResult.put(action, num+finishNum);
            }
            planFinishMonEntity.setStatResult(jsonStatResult.toJSONString());
            planFinishMonDAO.update("stat_plan_finish_mon", new String[]{"STAT_MON", "EMPLOYEE_ID"}, planFinishMonEntity.getContent());
        }
    }
}
