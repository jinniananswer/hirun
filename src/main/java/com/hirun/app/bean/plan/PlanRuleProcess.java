package com.hirun.app.bean.plan;

import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.domain.enums.plan.PlanStatus;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.tools.time.TimeTool;

import java.time.temporal.ChronoUnit;

/**
 * Created by pc on 2018-05-18.
 */
public class PlanRuleProcess {

    public static String planEntryInitCheck(String executorId, String planDate) throws Exception {
        StringBuilder errorMessage = new StringBuilder();
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);

        int planNum = planDAO.getBeforePlanNumByPlanDate(executorId, planDate);
        if(planNum == 0) {
            //第一次录计划
            return "";
        }

        PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, planDate);
        if(planEntity != null) {
            errorMessage.append("您已经录过【" + planDate + "】的计划了");
            return errorMessage.toString();
        }

        //获取昨天
        String yesterday = TimeTool.addTime(planDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0, 10);
        PlanEntity yesterdayPlanEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, planDate);
        if(yesterdayPlanEntity == null) {
            errorMessage.append(yesterday + "的计划您还没有录，请先补录");
            return errorMessage.toString();
        }
        if(!PlanStatus.summarized.getValue().equals(yesterdayPlanEntity.getPlanStatus())) {
            errorMessage.append(yesterday + "的计划您还没有总结，请先总结");
            return errorMessage.toString();
        }

        return "";
    }
}