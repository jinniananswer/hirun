package com.hirun.app.bean.plan;

import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.domain.enums.plan.PlanStatus;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.exception.GenericException;
import com.most.core.pub.tools.time.TimeTool;

import java.time.temporal.ChronoUnit;

/**
 * Created by pc on 2018-05-18.
 */
public class PlanRuleProcess {

    public static void planEntryInitCheck(String executorId, String planDate) throws Exception {
        StringBuilder errorMessage = new StringBuilder();
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);

        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        int planNum = planDAO.getBeforePlanNumByEid(executorId);
        if(planNum == 0) {
            //第一次录计划
            return;
        }

        PlanEntity lastestPlanEntity = planDAO.getLastestPlanEntity(executorId);
        if(lastestPlanEntity == null) {
            return;
        }

        StringBuilder errorInfo = new StringBuilder();
        String lastestPlanDate = lastestPlanEntity.getPlanDate();
        String lastestPlanStatus = lastestPlanEntity.getPlanStatus();

        if(lastestPlanDate.compareTo(planDate) > 0) {
            //最后一次录计划的日期大于当前要录计划的日期
            if(lastestPlanStatus.equals(PlanStatus.summarized.getValue())) {
                //已总结
                String tomorrow = TimeTool.addTime(lastestPlanDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, 1).substring(0, 10);
                errorInfo.append("您最新的一个计划日期是[").append(lastestPlanDate).append("]，");
                errorInfo.append("请从[").append(tomorrow).append("]开始录计划");
                throw new GenericException(userId, "-1", errorInfo.toString());
            } else {
                //未总结
                errorInfo.append("您最新的一个计划日期是[").append(lastestPlanDate).append("]，");
                errorInfo.append("且该计划还没有总结，请先总结");
                throw new GenericException(userId, "-1", errorInfo.toString());
            }
        } else if(lastestPlanDate.compareTo(planDate) == 0) {
            //最后一次录计划的日期等于当前要录计划的日期
            throw new GenericException(userId, "-1", "您已经录过【" + planDate + "】的计划了");
        } else {
            //最后一次录计划的日期小于当前要录计划的日期
            if(lastestPlanStatus.equals(PlanStatus.summarized.getValue())) {
                //已总结
                String yesterday = TimeTool.addTime(planDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0, 10);
                if(yesterday.equals(lastestPlanDate)) {
                    //这种情况没有问题
                } else {
                    String tomorrow = TimeTool.addTime(lastestPlanDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, 1).substring(0, 10);
                    errorInfo.append("您最新的一个计划日期是[").append(lastestPlanDate).append("]，");
                    errorInfo.append("请从[").append(tomorrow).append("]开始录计划");
                    throw new GenericException(userId, "-1", errorInfo.toString());
                }
            } else {
                //未总结
                errorInfo.append("您最新的一个计划日期是[").append(lastestPlanDate).append("]，");
                errorInfo.append("且该计划还没有总结，请先总结");
                throw new GenericException(userId, "-1", errorInfo.toString());
            }
        }

        PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, planDate);
        if(planEntity != null) {
            throw new GenericException(userId, "-100", "您已经录过【" + planDate + "】的计划了");
        }

        //获取昨天
        String yesterday = TimeTool.addTime(planDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0, 10);
        PlanEntity yesterdayPlanEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, yesterday);
        if(yesterdayPlanEntity == null) {
            throw new GenericException(userId, "-100", yesterday + "的计划您还没有录，请先补录");
        }
        if(!PlanStatus.summarized.getValue().equals(yesterdayPlanEntity.getPlanStatus())) {
            throw new GenericException(userId, "-100", yesterday + "的计划您还没有总结，请先总结");
        }

    }
}
