package com.hirun.app.bean.plan;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.cust.CustBean;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.hirun.app.cache.PlanTargetLimitCache;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.plan.PlanActionNumDAO;
import com.hirun.app.dao.plan.PlanCycleFinishInfoDAO;
import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.hirun.pub.domain.entity.plan.PlanCycleFinishInfoEntity;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.domain.enums.cust.CustStatus;
import com.hirun.pub.domain.enums.plan.ActionStatus;
import com.hirun.pub.domain.enums.plan.PlanStatus;
import com.hirun.pub.domain.enums.plan.PlanType;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.exception.GenericException;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-05-16.
 */
public class PlanBean {

    public static int getTargetLowerLimit(String executorId, String actionCode) throws Exception {
        int lowerLimit = 0;

        PlanTargetLimitEntity planTargetLimitEntity = PlanTargetLimitCache.getPlanTargetLimit(actionCode);
        if(planTargetLimitEntity != null) {
            int limitNum = Integer.parseInt(planTargetLimitEntity.getLimitNum());

            PlanCycleFinishInfoDAO cyclePlanFinishInfoDAO = new PlanCycleFinishInfoDAO("ins");
            PlanCycleFinishInfoEntity cyclePlanFinishInfoEntity = cyclePlanFinishInfoDAO.getCyclePlanFinishInfoEntity(executorId, actionCode);
            int preTotalUnfinishNum = 0;
            int currCycleFinishNum = 0;
            if(cyclePlanFinishInfoEntity == null) {

            } else  {
                preTotalUnfinishNum = Integer.parseInt(cyclePlanFinishInfoEntity.getUnfinishNum());
                currCycleFinishNum = Integer.parseInt(cyclePlanFinishInfoEntity.getCurrCycleFinishNum());
            }

            int totalLimitNum = 0;
            totalLimitNum = preTotalUnfinishNum + limitNum;
            lowerLimit = totalLimitNum - currCycleFinishNum;
        }

        lowerLimit = lowerLimit > 0 ? lowerLimit : 0;
        return lowerLimit;
    }

    public static void actionBindPlan(String nickName, String identifyCode, String actionCode, String planDate, String executorId, String finishTime) throws Exception {
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        CustActionDAO custActionDAO = DAOFactory.createDAO(CustActionDAO.class);
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);
        Map<String, String> doCustResult = CustBean.isCreateOrBindNewCust(nickName, identifyCode, planDate, executorId);
        String doCust = doCustResult.get("DO_CUST");
        String custId = doCustResult.get("CUST_ID");

        if("BIND_VIRTUAL".equals(doCust)) {
            //UPDATE
            Map<String, String > custDbParam = new HashMap<String, String>();
            custDbParam.put("CUST_ID", custId);
            custDbParam.put("WX_NICK", nickName);
            custDbParam.put("IDENTIFY_CODE", identifyCode);
            custDAO.save("INS_CUSTOMER", custDbParam);
        }
        if("CREATE".equals(doCust)) {
            //INSERT
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setIdentifyCode(identifyCode);
            customerEntity.setWxNick(nickName);
            customerEntity.setCustName(nickName);
            customerEntity.setCustStatus(CustStatus.toBeFilled.getValue());
            customerEntity.setFirstPlanDate(planDate);
            customerEntity.setHouseCounselorId(executorId);
            customerEntity.setCreateDate(TimeTool.now());
            customerEntity.setCreateUserId("0");
            customerEntity.setUpdateTime(TimeTool.now());
            customerEntity.setUpdateUserId("0");
            custId = String.valueOf(custDAO.insertAutoIncrement("INS_CUSTOMER", customerEntity.getContent()));
        }

        if(ArrayTool.isEmpty(custActionDAO.queryCustFinishActionByCustIdAndActionCode(custId, actionCode))) {
            //update or insert cust_action
            PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, planDate);
            CustActionEntity custActionEntity = custActionDAO.queryCustActionByCustIdAndActionCodeAndPlanId(custId, actionCode, planEntity.getPlanId());
            if(custActionEntity != null) {
                //update
                String actionId = custActionEntity.getActionId();
                custActionEntity.setFinishTime(finishTime);
                custActionEntity.setUpdateUserId("0");
                custActionEntity.setUpdateTime(TimeTool.now());
                custActionDAO.update("INS_CUST_ACTION", custActionEntity.getContent());
            } else {
                //insert
                custActionEntity = new CustActionEntity();
                custActionEntity.setCustId(custId);
                custActionEntity.setActionCode(actionCode);
                custActionEntity.setPlanId(planEntity.getPlanId());
                custActionEntity.setActionStatus(ActionStatus.outerPlan.getValue());
                custActionEntity.setPlanDealDate(planEntity.getPlanDate());
                custActionEntity.setFinishTime(finishTime);
                custActionEntity.setExecutorId(executorId);
                custActionEntity.setCreateDate(TimeTool.now());
                custActionEntity.setCreateUserId("0");
                custActionEntity.setUpdateTime(TimeTool.now());
                custActionEntity.setUpdateUserId("0");
                custActionDAO.insertAutoIncrement("INS_CUST_ACTION", custActionEntity.getContent());
            }
        }
    }

    /**
     * 原始数据转换为需求蓝图指导书
     */
    public static boolean transOriginalDataToAction(JSONObject originalData, String date, String actionCode) throws Exception {
        String nickName = originalData.getString("NICKNAME");
        String operTime = originalData.getString("OPER_TIME");
        String staffId = originalData.getString("STAFF_ID");
        String openId = originalData.getString("OPENID");
        String id = originalData.getString("ID");

        if(StringUtils.isBlank(staffId) || "0".equals(staffId)) {
            return false;
        }

        String houseCounselorId = staffId;

        if(ActionCheckRuleProcess.isActionBindYesterdayPlan(operTime, date, houseCounselorId)) {
            //归到昨日计划里
            String yesterday = TimeTool.addTime(date + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0,10);
            PlanBean.actionBindPlan(nickName, openId, actionCode, yesterday, houseCounselorId, operTime);
//            signToDone(id, now);
            return true;
        }

        if(ActionCheckRuleProcess.isActionBindTodayPlan(operTime, date, houseCounselorId)) {
            //归到今日计划里
            PlanBean.actionBindPlan(nickName, openId, actionCode, date, houseCounselorId, operTime);
//            signToDone(id,now);
            return true;
        }

        if(ActionCheckRuleProcess.isActionBindTomorrowPlan(operTime, date, houseCounselorId)) {
            //归到明日计划里
            String tomorrow = TimeTool.addTime(date + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, 1).substring(0,10);
            PlanBean.actionBindPlan(nickName, openId, actionCode, tomorrow, houseCounselorId, operTime);
//            signToDone(id,now);
            return true;
        }

        //无法归到任意计划里时，跳过
        return false;
    }

    public static String getEmployeeIdByHirunPlusStaffId(String staffId) throws Exception {
        if(StringUtils.isBlank(staffId) || "0".equals(staffId)) {
            return null;
        }
        String mobileNo = HirunPlusStaffDataCache.getMobileByStaffId(staffId);
        if(StringUtils.isBlank(mobileNo)) {
            return null;
        }
        EmployeeEntity employeeEntity = EmployeeBean.getEmployeeByMobileNo(mobileNo);
        if(employeeEntity == null) {
            return null;
        }

        return employeeEntity.getEmployeeId();
    }

    public static void addHolidayPlansByStartAndEnd(String holidayStartDate, String holidayEndDate, String planExecutorId) throws Exception {
        PlanRuleProcess.planEntryInitCheck(planExecutorId, holidayStartDate);
        //计算两个时间相隔多少天
        int interval = (int)TimeTool.getAbsDateDiffDay(LocalDate.parse(holidayStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalDate.parse(holidayEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        for(int i = 0; i < interval+1; i++) {
            String planDate = TimeTool.addTime(holidayStartDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, i).substring(0,10);
            addImproperPlan(planDate, PlanType.holiday.getValue(), planExecutorId, "0");
        }
    }

    public static void addImproperPlan(String planDate, String planType, String planExecutorId, String isAdditionalRecord) throws Exception {
        String now = TimeTool.now();
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);
        PlanCycleFinishInfoDAO cyclePlanFinishInfoDAO = DAOFactory.createDAO(PlanCycleFinishInfoDAO.class);

        if(planDAO.getPlanEntityByEidAndPlanDate(planExecutorId, planDate) != null) {
            throw new GenericException("-1", "已经有" + planDate + "的计划了");
        }

        PlanEntity planEntity = new PlanEntity();
        planEntity.setPlanDate(planDate);
        planEntity.setPlanStatus(PlanStatus.summarized.getValue());
        planEntity.setSummarizeDate(now);
        planEntity.setSummarizeUserId(userId);
        planEntity.setPlanExecutorId(planExecutorId);
        planEntity.setPlanType(planType);
        planEntity.setCreateUserId(userId);
        planEntity.setCreateDate(now);
        planEntity.setUpdateUserId(userId);
        planEntity.setUpdateTime(now);
        planEntity.setIsAdditionalRecord(isAdditionalRecord);
        planDAO.insertAutoIncrement("INS_PLAN", planEntity.getContent());

        List<PlanTargetLimitEntity> planTargetLimitEntityList = PlanTargetLimitCache.getPlanTargetLimitList();
        PlanCycleFinishInfoEntity planCycleFinishInfoEntity = null;
        for(PlanTargetLimitEntity planTargetLimitEntity : planTargetLimitEntityList) {
            String actionCode = planTargetLimitEntity.getTargetCode();
            planCycleFinishInfoEntity = cyclePlanFinishInfoDAO.getCyclePlanFinishInfoEntity(planExecutorId, actionCode);
            //如果cyclePlanFinishInfo没数据，则先插一条数据
            if(planCycleFinishInfoEntity == null) {
                planCycleFinishInfoEntity = new PlanCycleFinishInfoEntity();
                planCycleFinishInfoEntity.setExecutorId(planExecutorId);
                planCycleFinishInfoEntity.setActionCode(actionCode);
                planCycleFinishInfoEntity.setPreCycleEndDate(planDate);
                planCycleFinishInfoEntity.setUnfinishNum("0");
                planCycleFinishInfoEntity.setCurrCycleFinishNum("0");
                planCycleFinishInfoEntity.setCurrCycleImproperDays("0");
                cyclePlanFinishInfoDAO.insert("INS_PLAN_CYCLE_FINISH_INFO", planCycleFinishInfoEntity.getContent());
            } else {
                //更新INS_PLAN_CYCLE_FINISH_INFO
                String preCycleEndDate = planCycleFinishInfoEntity.getPreCycleEndDate();
                int interval = (int)TimeTool.getAbsDateDiffDay(LocalDate.parse(preCycleEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDate.parse(planEntity.getPlanDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                if(interval == 1) {
                    //离上一个周期仅一天，则上一周期时间顺延一天
                    planCycleFinishInfoEntity.setPreCycleEndDate(planEntity.getPlanDate());
                } else {
                    int currCycleImproperDays = Integer.parseInt(planCycleFinishInfoEntity.getCurrCycleImproperDays());
                    currCycleImproperDays++;
                    planCycleFinishInfoEntity.setCurrCycleImproperDays(String.valueOf(currCycleImproperDays));
                }
                cyclePlanFinishInfoDAO.save("INS_PLAN_CYCLE_FINISH_INFO", planCycleFinishInfoEntity.getContent());
            }
        }
    }
}
