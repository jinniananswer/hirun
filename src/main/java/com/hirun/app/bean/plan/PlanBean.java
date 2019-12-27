package com.hirun.app.bean.plan;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.common.MsgBean;
import com.hirun.app.bean.cust.CustBean;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.hirun.app.cache.PlanTargetLimitCache;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.cust.CustOriginalActionDAO;
import com.hirun.app.dao.plan.PlanCycleFinishInfoDAO;
import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.cust.CustOriginalActionEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.hirun.pub.domain.entity.plan.PlanCycleFinishInfoEntity;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.hirun.pub.domain.enums.common.MsgType;
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
        CustOriginalActionDAO custOriginalActionDAO = DAOFactory.createDAO(CustOriginalActionDAO.class);
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);

        Map<String, String> doCustResult = CustBean.isCreateOrBindNewCust(identifyCode, planDate, executorId);
        String doCust = doCustResult.get("DO_CUST");
        String custId = doCustResult.get("CUST_ID");
        String now = TimeTool.now();

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
            customerEntity.setCreateTime(now);
            customerEntity.setCreateUserId("0");
            customerEntity.setUpdateTime(now);
            customerEntity.setUpdateUserId("0");
            custId = String.valueOf(custDAO.insertAutoIncrement("INS_CUSTOMER", customerEntity.getContent()));
        }

        if(ArrayTool.isEmpty(custActionDAO.queryCustFinishActionByCustIdAndActionCodeAndEid(custId, actionCode, executorId))) {
            //update or insert cust_action
            PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, planDate);
            CustActionEntity custActionEntity = custActionDAO.queryCustActionByCustIdAndActionCodeAndPlanId(custId, actionCode, planEntity.getPlanId());
            if(custActionEntity != null) {
                //update
                String actionId = custActionEntity.getActionId();
                custActionEntity.setFinishTime(finishTime);
                custActionEntity.setUpdateUserId("0");
                custActionEntity.setUpdateTime(now);
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
                custActionEntity.setCreateTime(now);
                custActionEntity.setCreateUserId("0");
                custActionEntity.setUpdateTime(now);
                custActionEntity.setUpdateUserId("0");
                custActionDAO.insertAutoIncrement("INS_CUST_ACTION", custActionEntity.getContent());
            }
        }

        if(ArrayTool.isEmpty(custActionDAO.queryCustFinishActionByCustIdAndActionCodeAndEid(custId, "JW", executorId))) {
            //update or insert cust_action
            PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, planDate);
            CustActionEntity custActionEntity = custActionDAO.queryCustActionByCustIdAndActionCodeAndPlanId(custId, "JW", planEntity.getPlanId());
            if(custActionEntity != null) {
                //update
                String actionId = custActionEntity.getActionId();
                custActionEntity.setFinishTime(finishTime);
                custActionEntity.setUpdateUserId("0");
                custActionEntity.setUpdateTime(now);
                custActionDAO.update("INS_CUST_ACTION", custActionEntity.getContent());
            } else {
                //insert
                custActionEntity = new CustActionEntity();
                custActionEntity.setCustId(custId);
                custActionEntity.setActionCode("JW");
                custActionEntity.setPlanId(planEntity.getPlanId());
                custActionEntity.setActionStatus(ActionStatus.outerPlan.getValue());
                custActionEntity.setPlanDealDate(planEntity.getPlanDate());
                custActionEntity.setFinishTime(finishTime);
                custActionEntity.setExecutorId(executorId);
                custActionEntity.setCreateTime(now);
                custActionEntity.setCreateUserId("0");
                custActionEntity.setUpdateTime(now);
                custActionEntity.setUpdateUserId("0");
                custActionDAO.insertAutoIncrement("INS_CUST_ACTION", custActionEntity.getContent());
            }
        }
    }

    /**
     * 原始数据转换为客户完成动作
     */
    public static boolean transOriginalDataToAction(JSONObject originalData, String date, String actionCode) throws Exception {
        CustOriginalActionDAO custOriginalActionDAO = DAOFactory.createDAO(CustOriginalActionDAO.class);
        boolean isTrans = false;

        String nickName = originalData.getString("NICKNAME");
        String operTime = originalData.getString("OPER_TIME");
        String staffId = originalData.getString("STAFF_ID");
        String openId = originalData.getString("OPENID");
        String id = originalData.getString("ID");

        if(StringUtils.isBlank(staffId) || "0".equals(staffId)) {
            return false;
        }

        if(isSignToDone(originalData)) {
            //是否直接移历史表
            return true;
        }

        String houseCounselorId = staffId;
        String now = TimeTool.now();

        //记下原始数据。
        //这里与计划绑定动作的地方不在一起，原因是我想动作可能绑不到某个计划上，比如没录计划或休假
        //但原始数据就不要依赖计划了
        //不过还是需依赖客户已存在
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);

        if(ActionCheckRuleProcess.isActionBindYesterdayPlan(operTime, date, houseCounselorId)) {
            //归到昨日计划里
            String yesterday = TimeTool.addTime(date + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0,10);
            PlanBean.actionBindPlan(nickName, openId, actionCode, yesterday, houseCounselorId, operTime);
//            signToDone(id, now);
            isTrans = true;
        }

        if(!isTrans && ActionCheckRuleProcess.isActionBindTodayPlan(operTime, date, houseCounselorId)) {
            //归到今日计划里
            PlanBean.actionBindPlan(nickName, openId, actionCode, date, houseCounselorId, operTime);
//            signToDone(id,now);
            isTrans = true;
        }

        if(!isTrans && ActionCheckRuleProcess.isActionBindTomorrowPlan(operTime, date, houseCounselorId)) {
            //归到明日计划里
            String tomorrow = TimeTool.addTime(date + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, 1).substring(0,10);
            PlanBean.actionBindPlan(nickName, openId, actionCode, tomorrow, houseCounselorId, operTime);
//            signToDone(id,now);
            isTrans = true;
        }

        CustomerEntity customerEntity = custDAO.getCustomerEntityByIdentifyCode(openId);
        if(customerEntity != null) {
            if(custOriginalActionDAO.getCustOriginalActionEntityByOutIdAndActionCodeAndCid(customerEntity.getCustId(), id, actionCode) == null) {
                //只有根据out表的id、actioncode，custid查不到的时候才新增一条
                CustOriginalActionEntity custOriginalActionEntity = new CustOriginalActionEntity();
                custOriginalActionEntity.setCustId(customerEntity.getCustId());
                custOriginalActionEntity.setActionCode(actionCode);
                custOriginalActionEntity.setFinishTime(operTime);
                custOriginalActionEntity.setEmployeeId(houseCounselorId);
                custOriginalActionEntity.setOutId(id);
                custOriginalActionEntity.setCreateUserId("0");
                custOriginalActionEntity.setCreateTime(now);
                custOriginalActionDAO.insert("INS_CUST_ORIGINAL_ACTION", custOriginalActionEntity.getContent());

                //如果没有加微的原始动作，则也记一条加微的
                if(ArrayTool.isEmpty(custOriginalActionDAO.getCustOriginalActionEntityByActionCodeAndCidAndEid(customerEntity.getCustId(), "JW", houseCounselorId))) {
                    CustOriginalActionEntity custJWOriginalActionEntity = new CustOriginalActionEntity();
                    custJWOriginalActionEntity.setCustId(customerEntity.getCustId());
                    custJWOriginalActionEntity.setActionCode("JW");
                    custJWOriginalActionEntity.setFinishTime(operTime);
                    custJWOriginalActionEntity.setEmployeeId(houseCounselorId);
                    custJWOriginalActionEntity.setCreateUserId("0");
                    custJWOriginalActionEntity.setCreateTime(now);
                    custOriginalActionDAO.insert("INS_CUST_ORIGINAL_ACTION", custJWOriginalActionEntity.getContent());
                }


                String custRelaHounselorId = customerEntity.getHouseCounselorId();
                if(!houseCounselorId.equals(custRelaHounselorId)) {
                    //发消息通知双方
                    String actionName = ActionCache.getAction(actionCode).getActionName();
                    EmployeeEntity custRelaEmployeeEntity = EmployeeBean.getEmployeeByEmployeeId(custRelaHounselorId);
                    UserEntity custRelaUserEntity = userDAO.queryUserByEmployeeId(custRelaHounselorId);

                    EmployeeEntity actionEmployeeEntity = EmployeeBean.getEmployeeByEmployeeId(houseCounselorId);
                    UserEntity actionUserEntity = userDAO.queryUserByEmployeeId(houseCounselorId);
                    if(custRelaEmployeeEntity != null && custRelaUserEntity != null
                            && actionEmployeeEntity != null && actionUserEntity != null) {
                        //1、发给客户归属家装顾问
                        StringBuilder msgContent = new StringBuilder();
                        msgContent.append(actionEmployeeEntity.getName())
                                .append("正在接触归属于你的客户【")
                                .append(customerEntity.getCustName()).append("】，")
                                .append("接触动作是【")
                                .append(actionName)
                                .append("】，")
                                .append("接触时间是")
                                .append(operTime);
                        MsgBean.sendMsg(custRelaUserEntity.getUserId(),msgContent.toString(),"0",TimeTool.now(), MsgType.sys);

                        //1、发给触发动作的家装顾问
                        msgContent = new StringBuilder();
                        msgContent.append("你正在接触的客户【")
                                .append(customerEntity.getCustName())
                                .append("】,是归属于")
                                .append(custRelaEmployeeEntity.getName())
                                .append("，接触动作是【")
                                .append(actionName)
                                .append("】，")
                                .append("接触时间是")
                                .append(operTime);
                        MsgBean.sendMsg(actionUserEntity.getUserId(),msgContent.toString(),"0",TimeTool.now(), MsgType.sys);
                    }
                }
            }
        }

        //无法归到任意计划里时，跳过
        return isTrans;
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
        planEntity.setCreateTime(now);
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

    private static boolean isSignToDone(JSONObject originalData) throws Exception{
        boolean result = false;

        String nickName = originalData.getString("NICKNAME");
        String operTime = originalData.getString("OPER_TIME");
        String staffId = originalData.getString("STAFF_ID");
        String openId = originalData.getString("OPENID");
        String id = originalData.getString("ID");

        boolean isChangeSha = EmployeeBean.isChangshaEmployee(staffId);
        String compareTime = "2018-08-31 00:00:00";
        if(TimeTool.compareTwoTime(operTime,compareTime) <= 0 && !isChangeSha) {
            result = true;
        }

        return result;
    }

    public static void resetPlanCycleFinishInfo(String executorId, String planDate) throws Exception {
        PlanCycleFinishInfoDAO cyclePlanFinishInfoDAO = DAOFactory.createDAO(PlanCycleFinishInfoDAO.class);
        String[] actions = new String[] {"HXJC", "ZX"};
        String preCycleEndDate = TimeTool.addTime(planDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0,10);

        for(int i = 0, size = actions.length; i <size; i++) {
            String actionCode = actions[i];
            PlanCycleFinishInfoEntity cyclePlanFinishInfoEntity = cyclePlanFinishInfoDAO.getCyclePlanFinishInfoEntity(executorId, actionCode);
            Map<String, String> newEntityMap = new HashMap<String, String>();
            newEntityMap.put("EXECUTOR_ID", executorId);
            newEntityMap.put("ACTION_CODE", actionCode);
            newEntityMap.put("PRE_CYCLE_END_DATE", preCycleEndDate);
            newEntityMap.put("UNFINISH_NUM", "0");
            newEntityMap.put("CURR_CYCLE_FINISH_NUM", "0");
            newEntityMap.put("CURR_CYCLE_IMPROPER_DAYS", "0");
            if(cyclePlanFinishInfoEntity == null) {
                cyclePlanFinishInfoDAO.insert("INS_PLAN_CYCLE_FINISH_INFO", newEntityMap);
            } else  {
                cyclePlanFinishInfoDAO.save("INS_PLAN_CYCLE_FINISH_INFO", new String[] {"EXECUTOR_ID", "ACTION_CODE"}, newEntityMap);
            }
        }
    }
}
