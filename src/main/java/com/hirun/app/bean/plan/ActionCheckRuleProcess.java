package com.hirun.app.bean.plan;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.PlanActionLimitCache;
import com.hirun.app.cache.PlanTargetLimitCache;
import com.hirun.app.dao.plan.PlanActionNumDAO;
import com.hirun.app.dao.plan.PlanCycleFinishInfoDAO;
import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.pub.domain.entity.param.PlanActionLimitEntity;
import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.hirun.pub.domain.entity.plan.PlanCycleFinishInfoEntity;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.tool.PlanTool;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-05.
 */
public class ActionCheckRuleProcess {

    public static String checkPlanAction(String executorId, String actionCode, int custNum, String planDate, JSONObject actionMap) throws Exception {
        StringBuilder errorMessage = new StringBuilder();
        PlanDAO planDAO = new PlanDAO("ins");

        String actionName = ActionCache.getAction(actionCode).getActionName();

        //校验自身动作数量限制
        PlanTargetLimitEntity planTargetLimitEntity = PlanTargetLimitCache.getPlanTargetLimit(actionCode);
        if(planTargetLimitEntity != null) {
            int timeInterval = Integer.parseInt(planTargetLimitEntity.getTimeInterval());
            int unit = Integer.parseInt(planTargetLimitEntity.getUnit());
            int limitNum = Integer.parseInt(planTargetLimitEntity.getLimitNum());

            PlanCycleFinishInfoDAO cyclePlanFinishInfoDAO = new PlanCycleFinishInfoDAO("ins");
            PlanCycleFinishInfoEntity cyclePlanFinishInfoEntity = cyclePlanFinishInfoDAO.getCyclePlanFinishInfoEntity(executorId, actionCode);
            int preTotalUnfinishNum = 0;
            int currCycleFinishNum = 0;
            String preCycleEndDate = null;
            int interval = 1;
            if(cyclePlanFinishInfoEntity == null) {
                preCycleEndDate = TimeTool.addTime(planDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0,10);
                interval = 1;
                Map<String, String> newEntityMap = new HashMap<String, String>();
                newEntityMap.put("EXECUTOR_ID", executorId);
                newEntityMap.put("ACTION_CODE", actionCode);
                newEntityMap.put("PRE_CYCLE_END_DATE", preCycleEndDate);
                newEntityMap.put("UNFINISH_NUM", "0");
                newEntityMap.put("CURR_CYCLE_FINISH_NUM", "0");
                newEntityMap.put("CURR_CYCLE_IMPROPER_DAYS", "0");
                cyclePlanFinishInfoDAO.insert("INS_PLAN_CYCLE_FINISH_INFO", newEntityMap);
            } else  {
                preTotalUnfinishNum = Integer.parseInt(cyclePlanFinishInfoEntity.getUnfinishNum());
                preCycleEndDate = cyclePlanFinishInfoEntity.getPreCycleEndDate();
                currCycleFinishNum = Integer.parseInt(cyclePlanFinishInfoEntity.getCurrCycleFinishNum());
                interval = (int)TimeTool.getAbsDateDiffDay(LocalDate.parse(preCycleEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDate.parse(planDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            int totalLimitNum = 0;
//            if(interval == timeInterval) {
            totalLimitNum = preTotalUnfinishNum + limitNum;
            String startTime = TimeTool.addTime(planDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, (-timeInterval+1)).substring(0,10);
            PlanActionNumDAO planActionNumDAO = new PlanActionNumDAO("ins");
//            int currentCycleTotalNum = planActionNumDAO.getPlanActionFinishNumBetweenStartAndEnd(executorId,actionCode,startTime,planDate);
            if(totalLimitNum - currCycleFinishNum > custNum) {
                errorMessage.append(actionName + "数需至少" + (totalLimitNum - currCycleFinishNum) + "个");
                return errorMessage.toString();
            }
//            } else {
//                totalLimitNum = preTotalUnfinishNum;
//            }

            if(totalLimitNum > 0) {

            }
        }

        //校验与其他动作的限制
        PlanActionLimitEntity planActionLimitEntity = PlanActionLimitCache.getPlanActionLimit(actionCode);
        if(planActionLimitEntity != null) {
            String relActionCode = planActionLimitEntity.getRelActionCode();
            int multipleNum = JSONObject.parseObject(planActionLimitEntity.getLimitParam()).getInteger("NUM");
            if(!actionMap.containsKey(relActionCode)) {
                actionMap.put(relActionCode, 0);
            }

            int relActionCustNum = actionMap.getIntValue(relActionCode);
            if (custNum < relActionCustNum * multipleNum) {
                errorMessage.append(actionName + "数需至少" + relActionCustNum * multipleNum + "个");
                return errorMessage.toString();
            }
        }

        return "";
    }

    public static boolean isActionBindYesterdayPlan(String actionTime, String today, String executorId) throws Exception {
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);
        if(TimeTool.compareTwoTime(actionTime,today + " 09:00:00") <= 0) {
            //先取昨天计划处理
            String yesterday = TimeTool.addTime(today + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0,10);
            PlanEntity yesterdayPlanEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId,yesterday);

            return isCommonActionBindPlan(yesterdayPlanEntity, actionTime);
//            String yesterdaySummarizeDate = yesterdayPlanEntity.getSummarizeDate();
//            if(StringUtils.isBlank(yesterdaySummarizeDate)
//                    || TimeTool.compareTwoTime(yesterdaySummarizeDate, actionTime) >= 0) {
//                boolean hasDetail = "1".equals(yesterdayPlanEntity.getHasDetail()) ? true : false;
//                if(!PlanTool.isNormalWork(yesterdayPlanEntity)
//                        && !hasDetail) {
//                    return false;
//                } else {
//                    return true;
//                }
//            } else {
//                return false;
//            }

        } else {
            return false;
        }
    }

    public static boolean isActionBindTodayPlan(String actionTime, String today, String executorId) throws Exception {
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);
        PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, today);

        return isCommonActionBindPlan(planEntity, actionTime);
    }

    public static boolean isActionBindTomorrowPlan(String actionTime, String today, String executorId) throws Exception {
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);
        String tomorrow = TimeTool.addTime(today + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, 1).substring(0,10);
        PlanEntity tomorrowPlanEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId,tomorrow);

        return isCommonActionBindPlan(tomorrowPlanEntity, actionTime);
    }

    private static boolean isCommonActionBindPlan(PlanEntity planEntity, String actionTime) {
        if(planEntity == null) {
            return false;
        }

        String summarizeDate = planEntity.getSummarizeDate();

        if(StringUtils.isNotBlank(summarizeDate)
                && TimeTool.compareTwoTime(summarizeDate, actionTime) <= 0) {
            return false;
        }

        boolean hasDetail = "1".equals(planEntity.getHasDetail()) ? true : false;
        if(!PlanTool.isNormalWork(planEntity)
                && !hasDetail) {
            return false;
        }

        return true;
    }
}
