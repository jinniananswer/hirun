package com.hirun.app.biz.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.common.MsgBean;
import com.hirun.app.bean.common.PerformDueTaskBean;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.plan.*;
import com.hirun.app.cache.*;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.cust.CustOriginalActionDAO;
import com.hirun.app.dao.plan.PlanActionNumDAO;
import com.hirun.app.dao.plan.PlanCycleFinishInfoDAO;
import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.app.dao.plan.PlanWorkDAO;
import com.hirun.app.dao.stat.PlanFinishMonDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.common.PerformDueTaskEntity;
import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.cust.CustOriginalActionEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.hirun.pub.domain.entity.param.PlanUnfinishCauseEntity;
import com.hirun.pub.domain.entity.plan.*;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.hirun.pub.domain.enums.common.MsgType;
import com.hirun.pub.domain.enums.plan.ActionStatus;
import com.hirun.pub.domain.enums.plan.PlanStatus;
import com.hirun.pub.domain.enums.plan.PlanType;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.*;
import com.most.core.pub.exception.GenericException;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by pc on 2018-04-28.
 */
public class PlanService extends GenericService {

    public ServiceResponse addPlan(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();

        JSONObject requestData = request.getBody().getData();
        PlanDAO planDao = new PlanDAO("ins");
        CustActionDAO custActionDAO = new CustActionDAO("ins");
        PlanActionNumDAO planActionNumDAO = new PlanActionNumDAO("ins");
        PlanWorkDAO planWorkDAO = DAOFactory.createDAO(PlanWorkDAO.class);

        String planDate = requestData.getString("PLAN_DATE");
        String planType = requestData.getString("PLAN_TYPE");
        String planExecutorId = requestData.getString("PLAN_EXECUTOR_ID");
        String workMode = requestData.getString("WORK_MODE");
        String isAdditionalRecord = requestData.getString("IS_ADDITIONAL_RECORD");
        JSONArray planList = requestData.getJSONArray("PLANLIST");
        if(planList == null) {
            planList = new JSONArray();
        }

        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();
        String now = TimeTool.now();
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("1");

        //规则校验
        if("1".equals(planType)) {
            JSONObject actionMap = new JSONObject();
            for(int i = 0, size = planList.size(); i < size; i++) {
                JSONObject planActionInfo = planList.getJSONObject(i);
                JSONArray custList = planActionInfo.getJSONArray("CUSTLIST");
                actionMap.put(planActionInfo.getString("ACTION_CODE"), custList != null ? custList.size() : 0);
            }

            for(ActionEntity actionEntity : actionEntityList) {
                ActionCheckRuleProcess.checkPlanAction(planExecutorId,actionEntity.getActionCode(),actionMap.getIntValue(actionEntity.getActionCode()),planDate,actionMap);
//                String errorMessage = ActionCheckRuleProcess.checkPlanAction(planExecutorId,actionEntity.getActionCode(),actionMap.getIntValue(actionEntity.getActionCode()),planDate,actionMap);
//                if(StringUtils.isNotBlank(errorMessage)) {
//                    response.setError("-1", errorMessage);
//                    return response;
//                }
            }
        }

        Map<String, String> planEntityParameter = new HashMap<String, String>();
        planEntityParameter.put("PLAN_DATE", planDate);
        if(!"1".equals(planType) && planList.size() == 0) {
            //如果非正常活动,且没有具体计划内容，则直接为已总结
//            planEntityParameter.put("PLAN_STATUS", "2");
//            planEntityParameter.put("SUMMARIZE_USER_ID", userId);
//            planEntityParameter.put("SUMMARIZE_DATE", now);
            PlanBean.addImproperPlan(planDate, planType, planExecutorId, isAdditionalRecord);
            return response;
        }

        planEntityParameter.put("PLAN_STATUS", "0");
        planEntityParameter.put("PLAN_EXECUTOR_ID", planExecutorId);
        planEntityParameter.put("PLAN_TYPE", planType);
        planEntityParameter.put("CREATE_USER_ID", userId);
        planEntityParameter.put("CREATE_TIME", now);
        planEntityParameter.put("UPDATE_USER_ID", userId);
        planEntityParameter.put("UPDATE_TIME", now);
        planEntityParameter.put("IS_ADDITIONAL_RECORD", isAdditionalRecord);
        String planId = String.valueOf(planDao.insertAutoIncrement("INS_PLAN", planEntityParameter));

        //将今天的计划动作数记到INS_PLAN_ACTION_NUM
        /*
        for(ActionEntity actionEntity : actionEntityList) {
            int num = 0;
            String actionCode = actionEntity.getActionCode();
            for(int i = 0, size = planList.size(); i < size; i++) {
                JSONObject planActionInfo = planList.getJSONObject(i);
                if(actionEntity.getActionCode().equals(planActionInfo.getString("ACTION_CODE"))) {
                    JSONArray custList = planActionInfo.getJSONArray("CUSTLIST");
                    if(custList == null) {
                        custList = new JSONArray();
                    }
                    num = custList.size();

                    break;
                }
            }

            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("PLAN_ID", planId);
            parameter.put("PLAN_DATE", planDate);
            parameter.put("ACTION_CODE", actionCode);
            parameter.put("NUM", String.valueOf(num));
            parameter.put("PLAN_EXECUTOR_ID", planExecutorId);
            planActionNumDAO.insert("INS_PLAN_ACTION_NUM", parameter);
        }
        */

//        if("1".equals(planType)) {
        List<Map<String, String>> custActionList = new ArrayList<Map<String, String>>();
        for(int i = 0, sizeI = planList.size(); i < sizeI; i++) {
            JSONObject plan = planList.getJSONObject(i);
            String actionCode = plan.getString("ACTION_CODE");
            JSONArray custList = plan.getJSONArray("CUSTLIST");
            if(custList == null) {
                custList = new JSONArray();
            }
            for(int j = 0, sizeJ = custList.size(); j < sizeJ; j++) {
                String custId = custList.getJSONObject(j).getString("CUST_ID");
                Map<String, String> custActionParameter = new HashMap<String, String>();
                custActionParameter.put("PLAN_ID", planId);
                custActionParameter.put("ACTION_CODE", actionCode);
                custActionParameter.put("CUST_ID", custId);
                custActionParameter.put("ACTION_STATUS", "0");
                custActionParameter.put("PLAN_DEAL_DATE", planDate);
                custActionParameter.put("EXECUTOR_ID", planExecutorId);
                custActionParameter.put("CREATE_USER_ID", userId);
                custActionParameter.put("CREATE_TIME", now);
                custActionParameter.put("UPDATE_USER_ID", userId);
                custActionParameter.put("UPDATE_TIME", now);
                custActionList.add(custActionParameter);
            }
        }
        if(ArrayTool.isNotEmpty(custActionList)) {
            custActionDAO.insertBatch("INS_CUST_ACTION", custActionList);
        }
//        }
        if(planList.size() > 0) {
            PlanWorkEntity planWorkEntity = new PlanWorkEntity();
            planWorkEntity.setPlanId(planId);
            planWorkEntity.setWorkMode(workMode);
            planWorkEntity.setStartDate(planDate + " 09:00:00");
            planWorkEntity.setEndDate(planDate + " 18:00:00");
            planWorkDAO.insertAutoIncrement("INS_PLAN_WORK", planWorkEntity.getContent());
        }

        //统计计划数
        PlanDayEntity planDayEntity = new PlanDayEntity();
        planDayEntity.setStatDay(planDate);
        planDayEntity.setStatType("EMPLOYEE_PLAN");
        planDayEntity.setObjectId(planExecutorId);
        JSONObject statResult = new JSONObject();
        for(int i = 0, size = planList.size(); i < size; i++) {
            JSONObject plan = planList.getJSONObject(i);
            String actionCode = plan.getString("ACTION_CODE");
            JSONArray custList = plan.getJSONArray("CUSTLIST");
            if(custList == null) {
                custList = new JSONArray();
            }
            statResult.put(actionCode, custList.size());
        }
        planDayEntity.setStatResult(statResult.toJSONString());
        PlanStatBean.saveStatPlanDayEntity(planDayEntity);

        return response;
    }

    public ServiceResponse checkPlanTarget(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();

        JSONObject requestData = request.getBody().getData();
        JSONArray targetList = requestData.getJSONArray("PLAN_TARGET_LIST");
        String planDate = requestData.getString("PLAN_DATE");
        String executorId = requestData.getString("EXECUTOR_ID");

        JSONObject targetJSONObject = new JSONObject();
        for(int i = 0, size = targetList.size(); i< size; i++) {
            JSONObject target = targetList.getJSONObject(i);
            targetJSONObject.put(target.getString("ACTION_CODE"), target.getIntValue("NUM"));
        }
        Iterator<String> iter = targetJSONObject.keySet().iterator();
        while(iter.hasNext()) {
            String actionCode = (String) iter.next();
            int num = targetJSONObject.getIntValue(actionCode);
//                String errorMessage = null;
            ActionCheckRuleProcess.checkPlanAction(executorId, actionCode, num, planDate, targetJSONObject);
//                String errorMessage = ActionCheckRuleProcess.checkPlanAction(executorId, actionCode, num, planDate, targetJSONObject);
//                if (StringUtils.isNotBlank(errorMessage)) {
//                    response.setError("-1", errorMessage);
//                return response;
//            }
        }

        return response;
    }

    public ServiceResponse checkPlanAction(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        JSONObject requestData = request.getBody().getData();
        String actionCode = requestData.getString("ACTION_CODE");
        int custNum = requestData.getInteger("CUSTNUM");
        JSONArray planTargetList = requestData.getJSONArray("PLAN_TARGET_LIST");
        String planDate = requestData.getString("PLAN_DATE");
        String executorId = requestData.getString("EXECUTOR_ID");

//        JSONObject targetJSONObject = ConvertTool.toJSONObject(planTargetList, "ACTION_CODE");
        JSONObject targetJSONObject = new JSONObject();
        for(int i = 0, size = planTargetList.size(); i< size; i++) {
            JSONObject planTarget = planTargetList.getJSONObject(i);
            targetJSONObject.put(planTarget.getString("ACTION_CODE"), planTarget.getIntValue("NUM"));
        }

        ActionCheckRuleProcess.checkPlanAction(executorId, actionCode, custNum, planDate, targetJSONObject);
//        String errorMessage = ActionCheckRuleProcess.checkPlanAction(executorId, actionCode, custNum, planDate, targetJSONObject);
//        if (StringUtils.isNotBlank(errorMessage)) {
//            response.setError("-1", errorMessage);
//            return response;
//        }

        return response;
    }

    public ServiceResponse getPlanFinishedInfo(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

//        String planExecutorId = requestData.getString("PLAN_EXECUTOR_ID");
//        String planDate = requestData.getString("PLAN_DATE");
        String planId = requestData.getString("PLAN_ID");

        //查询计划
        PlanDAO planDAO = new PlanDAO("ins");

        PlanEntity planEntity = planDAO.getPlanInfoById(planId);
        if(planEntity == null) {
            throw new GenericException("-1", "没有找到编码为【" + planId + "】的计划");
//            response.setError("-1", "没有找到编码为【" + planId + "】的计划");
//            return response;
        }

        //获取计划时间的所有客户动作，包括执行的和未执行的
        JSONArray planList = new JSONArray();
        JSONArray finishActionList = new JSONArray();
        JSONArray unFinishActionList = new JSONArray();
        CustActionDAO custActionDAO = new CustActionDAO("ins");
        CustDAO custDAO = new CustDAO("ins");
        List<CustActionEntity> custActionEntityList = custActionDAO.queryCustActionListByPlanId(planId);
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("1");
        if(ArrayTool.isNotEmpty(custActionEntityList)) {
            //存计划action对应的custList，格式为{actionCode, custList}，后续再遍历成list
            JSONObject planCustActionMap = new JSONObject();
            //存完成action对应的custList，格式为{actionCode, custList}，后续再遍历成list
            JSONObject finishCustActionMap = new JSONObject();
            //存未完成action对应的custList，格式为{actionCode, custList}，后续再遍历成list
            JSONObject unFinishCustActionMap = new JSONObject();
            //存客户信息
            JSONObject custMap = new JSONObject();//{custId, cust}
            //存每个动作的虚拟新客户数量及返回到前台的名称
            JSONObject virtualCustActionMap = new JSONObject();

            //遍历custAction，存到planCustActionMap和finishCustActionMap中
            for(CustActionEntity custActionEntity : custActionEntityList) {
                String actionCode = custActionEntity.getActionCode();
                String custId = custActionEntity.getCustId();

                //保持客户信息，防止重复查询
                if(!custMap.containsKey(custId)) {
                    CustomerEntity customerEntity = custDAO.getCustById(custId);
                    if(customerEntity == null) {
                        //TODO 如果为NULL怎么处理？？？
                    }
                    custMap.put(custId, customerEntity.toJSON(new String[] {"CUST_ID", "CUST_NAME","IDENTIFY_CODE"}));
                }

                String identifyCode = custMap.getJSONObject(custId).getString("IDENTIFY_CODE");
                if(StringUtils.isBlank(identifyCode)) {
                    if(virtualCustActionMap.containsKey(actionCode)) {
                        virtualCustActionMap.put(actionCode, virtualCustActionMap.getIntValue(actionCode)+1);
                    } else {
                        virtualCustActionMap.put(actionCode, 1);
                    }
                    continue;
                }


                JSONObject jsonAction = custActionEntity.toJSON(new String[] {"ACTION_ID","CUST_ID"});
                jsonAction.putAll(custMap.getJSONObject(custId));

                if(ActionStatus.innerPlan.getValue().equals(custActionEntity.getActionStatus())) {//这里面也有完成的
                    //计划中
                    if(planCustActionMap.containsKey(actionCode)) {
                        planCustActionMap.getJSONArray(actionCode).add(jsonAction);
                    } else {
                        JSONArray custList = new JSONArray();
                        custList.add(jsonAction);
                        planCustActionMap.put(actionCode, custList);
                    }

                    if(StringUtils.isNotBlank(custActionEntity.getFinishTime())) {
                        //计划中且完成的
                        if(finishCustActionMap.containsKey(actionCode)) {
                            finishCustActionMap.getJSONArray(actionCode).add(jsonAction);
                        } else {
                            JSONArray custList = new JSONArray();
                            custList.add(jsonAction);
                            finishCustActionMap.put(actionCode, custList);
                        }
                    } else {
                        //计划中未完成的
//                        String identifyCode = jsonAction.getString("IDENTIFY_CODE");
//                        if(StringUtils.isNotBlank(identifyCode)) {
                        if(unFinishCustActionMap.containsKey(actionCode)) {
                            unFinishCustActionMap.getJSONArray(actionCode).add(jsonAction);
                        } else {
                            JSONArray custList = new JSONArray();
                            custList.add(jsonAction);
                            unFinishCustActionMap.put(actionCode, custList);
                        }
//                        }
                    }
                } else {
                    //计划外且完成的
                    if(finishCustActionMap.containsKey(actionCode)) {
                        finishCustActionMap.getJSONArray(actionCode).add(custMap.getJSONObject(custId));
                    } else {
                        JSONArray custList = new JSONArray();
                        custList.add(custMap.getJSONObject(custId));
                        finishCustActionMap.put(actionCode, custList);
                    }
                }
            }

            //从planCustActionMap和finishCustActionMap中遍历key，变成jsonArray
            Iterator planIter = planCustActionMap.keySet().iterator();
            for(ActionEntity actionEntity : actionEntityList) {
                String actionCode = actionEntity.getActionCode();
                JSONArray planCustActionList = new JSONArray();
                if(planCustActionMap.containsKey(actionCode)) {
                    planCustActionList = planCustActionMap.getJSONArray(actionCode);
                }

                if(virtualCustActionMap.containsKey(actionCode)) {
                    JSONObject virtualPlanCustAction = new JSONObject();
                    virtualPlanCustAction.put("CUST_ID", "0");//虚拟的没用
                    virtualPlanCustAction.put("ACTION_ID", "0");//虚拟的没用
                    virtualPlanCustAction.put("CUST_NAME", "新客户*" + virtualCustActionMap.getString(actionCode));
                    virtualPlanCustAction.put("CUST_NUM", virtualCustActionMap.getString(actionCode));
                    planCustActionList.add(virtualPlanCustAction);
                }

                if(planCustActionList.size() > 0) {
                    JSONObject plan = new JSONObject();
                    plan.put("ACTION_CODE", actionCode);
                    plan.put("CUSTLIST", planCustActionList);
                    planList.add(plan);
                }
            }

            /*
            while(planIter.hasNext()) {
                String actionCode = (String)planIter.next();
                JSONObject plan = new JSONObject();
                plan.put("ACTION_CODE", actionCode);
                JSONArray planCustActionList = planCustActionMap.getJSONArray(actionCode);
                if(virtualCustActionMap.containsKey(actionCode)) {
                    JSONObject virtualPlanCustAction = new JSONObject();
                    virtualPlanCustAction.put("CUST_ID", "0");//虚拟的没用
                    virtualPlanCustAction.put("ACTION_ID", "0");//虚拟的没用
                    virtualPlanCustAction.put("CUST_NAME", "新客户*" + virtualCustActionMap.getString(actionCode));
                    virtualPlanCustAction.put("CUST_NUM", virtualCustActionMap.getString(actionCode));
                    planCustActionList.add(virtualPlanCustAction);
                }
                plan.put("CUSTLIST", planCustActionList);
                planList.add(plan);
            }
            */

            Iterator finishIter = finishCustActionMap.keySet().iterator();
            while(finishIter.hasNext()) {
                String actionCode = (String)finishIter.next();
                JSONObject finish = new JSONObject();
                finish.put("ACTION_CODE", actionCode);
                finish.put("CUSTLIST", finishCustActionMap.getJSONArray(actionCode));
                finishActionList.add(finish);
            }
            Iterator unFinishIter = unFinishCustActionMap.keySet().iterator();
            while(unFinishIter.hasNext()) {
                String actionCode = (String)unFinishIter.next();
                JSONObject unFinish = new JSONObject();
                unFinish.put("ACTION_CODE", actionCode);
                unFinish.put("CUSTLIST", unFinishCustActionMap.getJSONArray(actionCode));
                unFinishActionList.add(unFinish);
            }
        }

        response.set("PLANLIST", planList);
        response.set("FINISH_ACTION_LIST", finishActionList);
        response.set("UNFINISH_ACTION_LIST", unFinishActionList);

        return response;
    }

    public ServiceResponse getCauseListByActionCode(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String actionCode = requestData.getString("ACTION_CODE");

        List<PlanUnfinishCauseEntity> entityList = PlanUnFinishCauseCache.getCauseListByActionCode(actionCode);
        response.set("CAUSE_LIST", ConvertTool.toJSONArray(entityList));

        return response;
    }

    public ServiceResponse getPlanBaseInfo(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        String planExecutorId = requestData.getString("PLAN_EXECUTOR_ID");
        String planDate = requestData.getString("PLAN_DATE");

        PlanDAO planDAO = new PlanDAO("ins");
        PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(planExecutorId, planDate);
        Body body = new Body();
        if(planEntity != null) {
            body  = new Body(planEntity.toJson());
        }

        response.setBody(body);

        return response;
    }

    public ServiceResponse summarizePlan(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        JSONObject requestData = request.getBody().getData();
        String planId = requestData.getString("PLAN_ID");
        String isAdditionalRecordSummarize = requestData.getString("IS_ADDITIONAL_RECORD_SUMMARIZE");
        JSONArray unFinishSummaryList = requestData.getJSONArray("UNFINISH_SUMMARY_LIST");
        JSONArray addExtraCustActionList = requestData.getJSONArray("ADD_EXTRA_CUST_ACTION_LIST");
        JSONArray transToFinishList = requestData.getJSONArray("TRANS_TO_FINISH_LIST");

        String userId = SessionManager.getSession().getSessionEntity().getUserId();

        String sysdate = TimeTool.now();
        CustActionDAO custActionDAO = new CustActionDAO("ins");
        CustOriginalActionDAO custOriginalActionDAO = DAOFactory.createDAO(CustOriginalActionDAO.class);
        PlanDAO planDAO = new PlanDAO("ins");
        Map<String, List<String>> custActionMap = new HashMap<String, List<String>>();

        Map<String, String> parameter  = new HashMap<String, String>();
        parameter.put("PLAN_ID", planId);
        List<PlanEntity> list = planDAO.query(PlanEntity.class, "INS_PLAN", parameter);
        if(ArrayTool.isEmpty(list)) {
            throw new GenericException("-1", "找不到计划【" + planId + "】");
//            response.setError("-1","找不到计划");
        }
        PlanEntity planEntity = list.get(0);
        parameter.put("PLAN_ID", planEntity.getPlanId());
        parameter.put("IS_ADDITIONAL_RECORD_SUMMARIZE", isAdditionalRecordSummarize);
        parameter.put("PLAN_STATUS", "2");
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", sysdate);
        parameter.put("SUMMARIZE_USER_ID", userId);
        parameter.put("SUMMARIZE_DATE", sysdate);
        planDAO.save("INS_PLAN", parameter);

        if(ArrayTool.isNotEmpty(unFinishSummaryList)) {
            for(int i = 0, size = unFinishSummaryList.size(); i < size; i++) {
                parameter = ConvertTool.toMap(unFinishSummaryList.getJSONObject(i));
                parameter.put("UPDATE_USER_ID", userId);
                parameter.put("UPDATE_TIME", sysdate);
                custActionDAO.save("INS_CUST_ACTION", parameter);
            }
        }

        if(ArrayTool.isNotEmpty(addExtraCustActionList)) {
            List<Map<String, String>> addExtraCustActionDbParamList = new ArrayList<Map<String, String>>();
            for(int i = 0, size = addExtraCustActionList.size(); i < size; i++) {
                Map<String, String> addExtraCustActionDbParam = ConvertTool.toMap(addExtraCustActionList.getJSONObject(i));
                if(!addExtraCustActionDbParam.containsKey("FINISH_TIME")) {
                    addExtraCustActionDbParam.put("FINISH_TIME", sysdate);
                }
                addExtraCustActionDbParam.put("EXECUTOR_ID", planEntity.getPlanExecutorId());
                addExtraCustActionDbParam.put("CREATE_USER_ID", userId);
                addExtraCustActionDbParam.put("CREATE_TIME", sysdate);
                addExtraCustActionDbParam.put("UPDATE_USER_ID", userId);
                addExtraCustActionDbParam.put("UPDATE_TIME", sysdate);
                addExtraCustActionDbParam.put("ACTION_STATUS", "1");
                addExtraCustActionDbParam.put("PLAN_ID",planEntity.getPlanId());
                addExtraCustActionDbParam.put("PLAN_DEAL_DATE",planEntity.getPlanDate());
                addExtraCustActionDbParamList.add(addExtraCustActionDbParam);

                String custId = addExtraCustActionDbParam.get("CUST_ID");
                String actionCode = addExtraCustActionDbParam.get("ACTION_CODE");
                if(!custActionMap.containsKey(custId)) {
                    custActionMap.put(custId, new ArrayList<String>());
                }
                List<String> actionList =  custActionMap.get(custId);
                actionList.add(actionCode);
            }
            custActionDAO.insertBatch("INS_CUST_ACTION", addExtraCustActionDbParamList);

            //记cust_origin_action
            List<Map<String, String>> custOriginActionDbParamList = new ArrayList<Map<String, String>>();
            for(int i = 0, size = addExtraCustActionList.size(); i < size; i++) {
                Map<String, String> custOriginActionDbParam = ConvertTool.toMap(addExtraCustActionList.getJSONObject(i));
                if(!custOriginActionDbParam.containsKey("FINISH_TIME")) {
                    custOriginActionDbParam.put("FINISH_TIME", sysdate);
                }
                custOriginActionDbParam.put("EMPLOYEE_ID", planEntity.getPlanExecutorId());
                custOriginActionDbParam.put("CREATE_USER_ID", userId);
                custOriginActionDbParam.put("CREATE_TIME", sysdate);
                custOriginActionDbParamList.add(custOriginActionDbParam);
            }
            custOriginalActionDAO.insertBatch("INS_CUST_ORIGINAL_ACTION", custOriginActionDbParamList);
        }

        if(ArrayTool.isNotEmpty(transToFinishList)) {
            List<Map<String, String>> custOriginActionDbParamList = new ArrayList<Map<String, String>>();
            for(int i = 0, size = transToFinishList.size(); i < size; i++) {
                parameter = ConvertTool.toMap(transToFinishList.getJSONObject(i));
                CustActionEntity custActionEntity = custActionDAO.queryByPk(CustActionEntity.class, "INS_CUST_ACTION", parameter);
                if(custActionEntity != null) {
                    custActionEntity.setFinishTime(sysdate);
                    custActionEntity.setUpdateTime(sysdate);
                    custActionEntity.setUpdateUserId(userId);
                    custActionDAO.update("INS_CUST_ACTION", custActionEntity.getContent());

                    Map<String, String> custOriginActionDbParam = new HashMap<String, String>();
                    custOriginActionDbParam.put("CUST_ID", custActionEntity.getCustId());
                    custOriginActionDbParam.put("ACTION_CODE", custActionEntity.getActionCode());
                    custOriginActionDbParam.put("FINISH_TIME", custActionEntity.getFinishTime());
                    custOriginActionDbParam.put("EMPLOYEE_ID", planEntity.getPlanExecutorId());
                    custOriginActionDbParam.put("CREATE_USER_ID", userId);
                    custOriginActionDbParam.put("CREATE_TIME", sysdate);
                    custOriginActionDbParamList.add(custOriginActionDbParam);
                }
//                parameter.put("UPDATE_USER_ID", userId);
//                parameter.put("UPDATE_TIME", sysdate);
//                parameter.put("FINISH_TIME", sysdate);
//                custActionDAO.save("INS_CUST_ACTION", parameter);
            }
            custOriginalActionDAO.insertBatch("INS_CUST_ORIGINAL_ACTION", custOriginActionDbParamList);
        }

        //更新INS_PLAN_CYCLE_FINISH_INFO
        JSONObject finishTargetLimitMap = new JSONObject();
        PlanCycleFinishInfoDAO cyclePlanFinishInfoDAO = new PlanCycleFinishInfoDAO("ins");
        List<PlanTargetLimitEntity> planTargetLimitEntityList = PlanTargetLimitCache.getPlanTargetLimitList();
        for(PlanTargetLimitEntity planTargetLimitEntity : planTargetLimitEntityList) {
            String targetCode = planTargetLimitEntity.getTargetCode();
            int timeInterval = Integer.parseInt(planTargetLimitEntity.getTimeInterval());
            int limitNum = Integer.parseInt(planTargetLimitEntity.getLimitNum());
            PlanCycleFinishInfoEntity cyclePlanFinishInfoEntity = cyclePlanFinishInfoDAO.getCyclePlanFinishInfoEntity(planEntity.getPlanExecutorId(), targetCode);
            if(cyclePlanFinishInfoEntity == null) {
                cyclePlanFinishInfoEntity = new PlanCycleFinishInfoEntity();
                String preCycleEndDate = TimeTool.addTime(planEntity.getPlanDate() + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0,10);
                cyclePlanFinishInfoEntity.setExecutorId(planEntity.getPlanExecutorId());
                cyclePlanFinishInfoEntity.setActionCode(targetCode);
                cyclePlanFinishInfoEntity.setPreCycleEndDate(preCycleEndDate);
                cyclePlanFinishInfoEntity.setUnfinishNum("0");
                cyclePlanFinishInfoEntity.setCurrCycleFinishNum("0");
                cyclePlanFinishInfoEntity.setCurrCycleImproperDays("0");

                String logId = String.valueOf(cyclePlanFinishInfoDAO.insertAutoIncrement("INS_PLAN_CYCLE_FINISH_INFO", cyclePlanFinishInfoEntity.getContent()));
                cyclePlanFinishInfoEntity.setLogId(logId);
            }

            int preTotalUnfinishNum = Integer.parseInt(cyclePlanFinishInfoEntity.getUnfinishNum());
            int currCycleFinishNum = Integer.parseInt(cyclePlanFinishInfoEntity.getCurrCycleFinishNum());
            int currCycleImproperDays = Integer.parseInt(cyclePlanFinishInfoEntity.getCurrCycleImproperDays());
            String preCycleEndDate = cyclePlanFinishInfoEntity.getPreCycleEndDate();
            String logId = cyclePlanFinishInfoEntity.getLogId();
            String planType = planEntity.getPlanType();

            int finishNum = custActionDAO.queryFinishActionCountByPlanId(planId, targetCode);
            currCycleFinishNum = currCycleFinishNum + finishNum;
            if(!"1".equals(planType)) {
                currCycleImproperDays++;
            }

            parameter.clear();
            parameter.put("LOG_ID", logId);

            int interval = (int)TimeTool.getAbsDateDiffDay(LocalDate.parse(preCycleEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    LocalDate.parse(planEntity.getPlanDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if(!"1".equals(planType) && interval == 1) {
                //如果总结的计划不是正常上班，且离上一个周期仅一天，则上一周期时间顺延一天
                //这一天完成的任务算到上一周期
                parameter.put("PRE_CYCLE_END_DATE", planEntity.getPlanDate());
                int unFinishNum = preTotalUnfinishNum - currCycleFinishNum > 0 ? preTotalUnfinishNum - currCycleFinishNum : 0;
                parameter.put("UNFINISH_NUM", String.valueOf(unFinishNum));
                cyclePlanFinishInfoDAO.save("INS_PLAN_CYCLE_FINISH_INFO", parameter);

                continue;
            }

            if(interval >= timeInterval + currCycleImproperDays){
                //周期的最后一天
                parameter.put("PRE_CYCLE_END_DATE", planEntity.getPlanDate());
                int unFinishNum = preTotalUnfinishNum + limitNum - currCycleFinishNum > 0 ? preTotalUnfinishNum + limitNum - currCycleFinishNum : 0;
                parameter.put("UNFINISH_NUM", String.valueOf(unFinishNum));
                parameter.put("CURR_CYCLE_FINISH_NUM", "0");//清0
                parameter.put("CURR_CYCLE_IMPROPER_DAYS", "0");//清0
                cyclePlanFinishInfoDAO.save("INS_PLAN_CYCLE_FINISH_INFO", parameter);

                //如果有累计未完成计划，则发消息给上级主管
                if(unFinishNum > 0) {
                    String targetName = ActionCache.getAction(targetCode).getActionName();
                    String employeeName = EmployeeBean.getEmployeeByEmployeeId(planEntity.getPlanExecutorId()).getName();

                    StringBuilder msgContent = new StringBuilder();
                    msgContent.append("截止到本周期结束【").append(planEntity.getPlanDate()).append("】，员工").append(employeeName).append("累计还有");
                    msgContent.append(String.valueOf(unFinishNum)).append("个【").append(targetName);
                    msgContent.append("】目标未完成");
                    String parentEmployeeId = EmployeeBean.queryParentEmployeeIdByEmployeeIdAndJobRoles(planEntity.getPlanExecutorId(), "3,4");
                    if(StringUtils.isNotBlank(parentEmployeeId)) {
                        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
                        UserEntity userEntity = userDAO.queryUserByEmployeeId(parentEmployeeId);
                        if(userEntity != null) {
                            MsgBean.sendMsg(userEntity.getUserId(),msgContent.toString(),"0",TimeTool.now(), MsgType.sys);
                        }
                    }
                }

                continue;
            }

            //非当前周期的最后一天
            if(!"1".equals(planType)) {
                //其他非正常上班情况，则本周期非正常上班天数+1
                parameter.put("CURR_CYCLE_IMPROPER_DAYS", String.valueOf(currCycleImproperDays));
            }
            parameter.put("CURR_CYCLE_FINISH_NUM", String.valueOf(currCycleFinishNum));
            cyclePlanFinishInfoDAO.save("INS_PLAN_CYCLE_FINISH_INFO", parameter);

//            String startTime = TimeTool.addTime(planEntity.getPlanDate() + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, (-timeInterval+1)).substring(0,10);
//            String endTime = TimeTool.addTime(planEntity.getPlanDate() + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, 1).substring(0,10);
//            if(interval == timeInterval) {
//                int totalFinishNum = custActionDAO.queryFinishActionCount(planEntity.getPlanExecutorId(), targetCode, startTime, endTime);
//                if(totalFinishNum > preTotalUnfinishNum + limitNum) {
//                    parameter.put("UNFINISH_NUM", "0");
//                } else {
//                    parameter.put("UNFINISH_NUM", String.valueOf(preTotalUnfinishNum + limitNum - totalFinishNum));
//                }
//                cyclePlanFinishInfoDAO.save("INS_PLAN_CYCLE_FINISH_INFO", parameter);
//            }
        }

        //更新统计信息
        List<CustActionEntity> custActionList = custActionDAO.queryCustFinishActionListByPlanId(planEntity.getPlanId());
        JSONObject jsonFinishInfo = new JSONObject();
        for(CustActionEntity custActionEntity : custActionList) {
            String actionCode = custActionEntity.getActionCode();
            if(!jsonFinishInfo.containsKey(actionCode)) {
                jsonFinishInfo.put(actionCode, 0);
            }
            jsonFinishInfo.put(actionCode, jsonFinishInfo.getIntValue(actionCode)+1);
        }
        PlanStatBean.statMonPlanFinishActionByPlanEntity(planEntity, jsonFinishInfo);

//        PlanDayEntity employeePlanDayEntity = new PlanDayEntity();
//        employeePlanDayEntity.setStatDay(planEntity.getPlanDate());
//        employeePlanDayEntity.setStatType("EMPLOYEE_FINISH");
//        employeePlanDayEntity.setObjectId(planEntity.getPlanExecutorId());
//        employeePlanDayEntity.setStatResult(jsonFinishInfo.toJSONString());
//        PlanStatBean.saveStatPlanDayEntityByEmployee(employeePlanDayEntity);
        PerformDueTaskEntity performDueTaskEntity = new PerformDueTaskEntity();
        performDueTaskEntity.setTaskType("PLAN_FINISH_STAT");
        performDueTaskEntity.setObjectId(planEntity.getPlanExecutorId());
        performDueTaskEntity.setObjectType("EMPLOYEE");
        performDueTaskEntity.setExecTime(sysdate);
        performDueTaskEntity.setDealTag("0");
        performDueTaskEntity.setCreateUserId(userId);
        performDueTaskEntity.setCreateTime(sysdate);
        JSONObject taskParam = new JSONObject();
        taskParam.put("EMPLOYEE_ID", planEntity.getPlanExecutorId());
        taskParam.put("STAT_DAY", planEntity.getPlanDate());
        taskParam.put("PLAN_FINISH_INFO", jsonFinishInfo.toJSONString());
        performDueTaskEntity.setParams(taskParam.toJSONString());
        PerformDueTaskBean.addPerformDueTask(performDueTaskEntity);

        return response;
    }

    public ServiceResponse queryNewCustList4Summary(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        JSONObject requestData = request.getBody().getData();
        String planExecutorId = requestData.getString("PLAN_EXECUTOR_ID");
        String planDate = requestData.getString("PLAN_DATE");

        PlanDAO planDAO = new PlanDAO("ins");
        PlanEntity entity = planDAO.getPlanEntityByEidAndPlanDate(planExecutorId, planDate);
        if(entity == null) {
            //TODO 报错
        }

        CustDAO custDAO = new CustDAO("ins");
        CustActionDAO custActionDAO = new CustActionDAO("ins");

        JSONArray custList = new JSONArray();
        List<CustomerEntity> customerEntityList = custDAO.queryNewCustListByPlanDate(planExecutorId);
        for(CustomerEntity customerEntity : customerEntityList) {
            JSONObject cust = customerEntity.toJSON(new String[] {"CUST_NAME", "WX_NICK", "CUST_ID"});

            //获取客户已完成的动作
            JSONArray jsonArrayCustActionList = new JSONArray();
            List<CustActionEntity> custActionEntityList = custActionDAO.queryCustFinishActionByCustIdAndEid(cust.getString("CUST_ID"), planExecutorId);
            for(CustActionEntity custActionEntity : custActionEntityList) {
                JSONObject jsonObjectCustAction = custActionEntity.toJSON(new String[] {"ACTION_CODE","FINISH_TIME"});
                jsonObjectCustAction.put("ACTION_NAME", ActionCache.getAction(custActionEntity.getActionCode()).getActionName());
            }

//            cust.put("ACTION_LIST", jsonArrayCustActionList);
            custList.add(cust);
        }

        /*
        List<CustActionEntity> custActionEntityList = custActionDAO.queryCustActionByPlanId(entity.getPlanId());
        JSONObject rtnCustMap = new JSONObject();
        if(ArrayTool.isNotEmpty(custActionEntityList)) {
            Map<String, CustomerEntity> custMap = new HashMap<String, CustomerEntity>();//保存已检索的客户,value为CustomerEntity
            for(CustActionEntity custActionEntity : custActionEntityList) {
//                if(StringUtils.isBlank(custActionEntity.getFinishTime())) {
//                    continue;
//                }
                String custId = custActionEntity.getCustId();
                CustomerEntity customerEntity = null;
                if(!custMap.containsKey(custId)) {
                    customerEntity = custDAO.getCustById(custId);
                } else {
                    customerEntity = custMap.get(custId);
                }

                //判断是否新客户
                if(!CustomerTool.isNewCust(customerEntity)) {
                    continue;
                }

//                JSONObject jsonCust = custActionEntity.toJSON(new String[] {"ACTION_CODE", "FINISH_TIME"});
//                jsonCust.put("ACTION_NAME", ActionCache.getAction(custActionEntity.getActionCode()).getActionName());

                JSONObject rtnCust = null;
                if(rtnCustMap.containsKey(custId)) {
                    continue;
//                    rtnCust = rtnCustMap.getJSONObject(custId);
//                    rtnCust.getJSONArray("ACTION_LIST").add(jsonCust);
                } else {
                    rtnCust = new JSONObject();
                    rtnCust.put("CUST_NAME", customerEntity.getCustName());
                    rtnCust.put("WX_NICK", customerEntity.getWxNick());
                    rtnCust.put("CUST_ID", custId);
                    rtnCustMap.put(custId, rtnCust);

//                    JSONArray actionList = new JSONArray();
//                    actionList.add(jsonCust);
//                    rtnCust.put("ACTION_LIST", jsonCust);
                }
            }
        }


        JSONArray custList = new JSONArray();
        Iterator custIter = rtnCustMap.keySet().iterator();
        while(custIter.hasNext()) {
            String key = (String)custIter.next();
            custList.add(rtnCustMap.getJSONObject(key));
        }
        */
        response.set("CUST_LIST", custList);

        return response;
    }

    //
    public ServiceResponse getCustFinishActionList(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        Map<String, String> parameter = new HashMap<String, String>();
        JSONArray custList = new JSONArray();
        CustOriginalActionDAO custOriginalActionDAO = DAOFactory.createDAO(CustOriginalActionDAO.class);

        JSONObject requestData = request.getBody().getData();
        String custId = requestData.getString("CUST_ID");

        List<CustOriginalActionEntity> custOriginalActionEntityList = custOriginalActionDAO.getCustOriginalActionEntityByCustId(custId);
        for(CustOriginalActionEntity custOriginalActionEntity : custOriginalActionEntityList) {
            JSONObject custAction = custOriginalActionEntity.toJSON(new String[] {"FINISH_TIME", "ACTION_CODE", "EMPLOYEE_ID"});
            custAction.put("ACTION_NAME", ActionCache.getAction(custOriginalActionEntity.getActionCode()).getActionName());
            custAction.put("EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(custOriginalActionEntity.getEmployeeId()));
            custList.add(custAction);
        }

        response.set("CUST_FINISH_ACTION_LIST", custList);

        return response;
    }

    public ServiceResponse queryEmployeeDailySheet(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        String planExecutorId = requestData.getString("PLAN_EXECUTOR_ID");
        String planDate = requestData.getString("PLAN_DATE");
        String yesterdayPlanDate = TimeTool.addTime(planDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0,10);
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("1");

        PlanDAO planDAO = new PlanDAO("ins");
        CustActionDAO custActionDAO = new CustActionDAO("ins");
        List<CustActionEntity> todayCustActionEntityList = new ArrayList<CustActionEntity>();
        List<CustActionEntity> yesterdayCustActionEntityList = new ArrayList<CustActionEntity>();
        PlanEntity todayPlanEntity = planDAO.getPlanEntityByEidAndPlanDate(planExecutorId, planDate);
        if(todayPlanEntity != null) {
            todayCustActionEntityList = custActionDAO.queryCustActionListByPlanId(todayPlanEntity.getPlanId());
        }
        PlanEntity yesterdayPlanEntity = planDAO.getPlanEntityByEidAndPlanDate(planExecutorId, yesterdayPlanDate);
        if(yesterdayPlanEntity != null) {
            yesterdayCustActionEntityList = custActionDAO.queryCustActionListByPlanId(yesterdayPlanEntity.getPlanId());
        }

        Map<String, Integer> mapTodayActionPlanNum = new HashMap<String, Integer>();
        for(ActionEntity actionEntity : actionEntityList) {
            String actionCode = actionEntity.getActionCode();
            if(!mapTodayActionPlanNum.containsKey(actionCode)) {
                mapTodayActionPlanNum.put(actionCode, 0);
            }
            for(CustActionEntity todayCustActionEntity : todayCustActionEntityList) {
                if(actionCode.equals(todayCustActionEntity.getActionCode())
                        && "0".equals(todayCustActionEntity.getActionStatus())) {
                    int num = mapTodayActionPlanNum.get(actionCode).intValue() + 1;
                    mapTodayActionPlanNum.put(actionCode, num);
                }
            }
        }

        Map<String, Integer> mapYesterdayActionPlanNum = new HashMap<String, Integer>();
        for(ActionEntity actionEntity : actionEntityList) {
            String actionCode = actionEntity.getActionCode();
            if(!mapYesterdayActionPlanNum.containsKey(actionCode)) {
                mapYesterdayActionPlanNum.put(actionCode, 0);
            }
            for(CustActionEntity yesterdayCustActionEntity : yesterdayCustActionEntityList) {
                if(actionCode.equals(yesterdayCustActionEntity.getActionCode())
                        && "0".equals(yesterdayCustActionEntity.getActionStatus())) {
                    int num = mapYesterdayActionPlanNum.get(actionCode).intValue() + 1;
                    mapYesterdayActionPlanNum.put(actionCode, num);
                }
            }
        }

        Map<String, Integer> mapYesterdayActionFinishNum = new HashMap<String, Integer>();
        for(ActionEntity actionEntity : actionEntityList) {
            String actionCode = actionEntity.getActionCode();
            if(!mapYesterdayActionFinishNum.containsKey(actionCode)) {
                mapYesterdayActionFinishNum.put(actionCode, 0);
            }
            for(CustActionEntity yesterdayCustActionEntity : yesterdayCustActionEntityList) {
                if(yesterdayCustActionEntity.getFinishTime() != null
                        && actionCode.equals(yesterdayCustActionEntity.getActionCode())) {
                    int num = mapYesterdayActionFinishNum.get(actionCode).intValue() + 1;
                    mapYesterdayActionFinishNum.put(actionCode, num);
                }
            }
        }

        JSONArray actionCountList = new JSONArray();
        for(ActionEntity actionEntity : actionEntityList) {
            JSONObject jsonActionCount = new JSONObject();
            String actionCode = actionEntity.getActionCode();
            jsonActionCount.put("ACTION_NAME", ActionCache.getAction(actionCode).getActionName());
            jsonActionCount.put("ACTION_CODE", actionCode);
            jsonActionCount.put("YESTERDAY_PLAN_NUM", mapYesterdayActionPlanNum.get(actionCode));
            jsonActionCount.put("YESTERDAY_FINISH_NUM", mapYesterdayActionFinishNum.get(actionCode));
            jsonActionCount.put("TODAY_PLAN_NUM", mapTodayActionPlanNum.get(actionCode));

            actionCountList.add(jsonActionCount);
        }

        response.set("ACTION_COUNT_LIST", actionCountList);

        return response;
    }

    public ServiceResponse getPlanActionAndCustList(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String planDate = requestData.getString("PLAN_DATE");
        String planExecutorId = requestData.getString("PLAN_EXECUTOR_ID");

        CustDAO custDAO = new CustDAO("ins");
        Map<String, String> dbParam = new HashMap<String, String>();
        JSONArray jsonActionAndCustList = new JSONArray();

        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("1");
        for(ActionEntity actionEntity : actionEntityList) {
            JSONObject jsonAction = new JSONObject();
            jsonAction.put("ACTION_CODE", actionEntity.getActionCode());
            jsonAction.put("ACTION_NAME", actionEntity.getActionName());

            if("JW".equals(actionEntity.getActionCode())) {
                jsonAction.put("CUST_LIST", new JSONArray());
            } else {
                dbParam.clear();
                dbParam.put("UNEXECUTED_ACTION", actionEntity.getActionCode());
                dbParam.put("HOUSE_COUNSELOR_ID", planExecutorId);
                List<CustomerEntity> customerEntityList = custDAO.queryCustList(dbParam);
                jsonAction.put("CUST_LIST", ConvertTool.toJSONArray(customerEntityList));
            }

            jsonActionAndCustList.add(jsonAction);
        }

        response.set("ACTION_LIST", jsonActionAndCustList);

        return response;
    }

    public ServiceResponse getTargetLowerLimit(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        JSONObject requestData = request.getBody().getData();
        JSONArray jsonTargetList = requestData.getJSONArray("TARGET_LIST");
        String planExecutorId = requestData.getString("PLAN_EXECUTOR_ID");
        jsonTargetList = jsonTargetList != null ? jsonTargetList : new JSONArray();

        for(int i = 0, size = jsonTargetList.size(); i < size; i++) {
            JSONObject jsonTarget = jsonTargetList.getJSONObject(i);
            String actionCode = jsonTarget.getString("ACTION_CODE");
            int actionLowerLimit = PlanBean.getTargetLowerLimit(planExecutorId, actionCode);
            jsonTarget.put("LOWER_LIMIT", String.valueOf(actionLowerLimit));
        }

        response.set("TARGET_LIST", jsonTargetList);
        return response;
    }

    public ServiceResponse planEntryInitCheck(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String executorId = requestData.getString("PLAN_EXECUTOR_ID");
        String planDate = requestData.getString("PLAN_DATE");
        PlanRuleProcess.planEntryInitCheck(executorId, planDate);
//        String errorMessage = PlanRuleProcess.planEntryInitCheck(executorId, planDate);
//        if(StringUtils.isNotBlank(errorMessage)) {
//            response.setError("-1", errorMessage);
//        }

        return response;
    }

    public ServiceResponse queryEmployeeDailySheetDetail(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String executorId = requestData.getString("EXECUTOR_ID");
        String planDate = requestData.getString("PLAN_DATE");
        String yesterdayPlanDate = TimeTool.addTime(planDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0,10);

        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);
        CustActionDAO custActionDAO = DAOFactory.createDAO(CustActionDAO.class);
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        Map<String, String> custMap = new HashMap<String, String>();//key:custId;value:custName

        JSONArray datalist = new JSONArray();
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("1");

        for(ActionEntity actionEntity : actionEntityList) {
            //先给个初始值
            JSONObject jsonActionMap = new JSONObject();
            String actionCode = actionEntity.getActionCode();
            JSONArray yesterdayPlanCustList = new JSONArray();
            JSONArray yesterdayPlanFinishCustList = new JSONArray();
            JSONArray yesterdayPlanUnFinishCustList = new JSONArray();
            JSONArray planCustList = new JSONArray();

            //查昨天的
            PlanEntity yesterdayPlanEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, yesterdayPlanDate);
            if(yesterdayPlanEntity == null) {

            } else {
                String yesterdayPlanId = yesterdayPlanEntity.getPlanId();
                List<CustActionEntity> custActionEntityList = custActionDAO.queryCustActionListByPlanIdAndActionCode(yesterdayPlanId, actionCode);
                for(CustActionEntity custActionEntity : custActionEntityList) {
                    String custId = custActionEntity.getCustId();
                    String finishTime = custActionEntity.getFinishTime();
                    String unFinishCauseDesc = custActionEntity.getUnfinishCauseDesc();
                    if(!custMap.containsKey(custId)) {
                        CustomerEntity customerEntity = custDAO.getCustById(custId);
                        custMap.put(custId, customerEntity.getCustName());
                    }
                    String custName = custMap.get(custId);

                    JSONObject yesterdayPlanCust = new JSONObject();
                    yesterdayPlanCust.put("CUST_NAME", custName);
                    yesterdayPlanCustList.add(yesterdayPlanCust);

                    if(StringUtils.isNotBlank(finishTime)) {
                        //完成的
                        JSONObject yesterdayPlanFinishCust = new JSONObject();
                        yesterdayPlanFinishCust.put("CUST_NAME", custName);
                        yesterdayPlanFinishCust.put("FINISH_TIME", finishTime);
                        yesterdayPlanFinishCustList.add(yesterdayPlanFinishCust);
                    } else {
                        JSONObject yesterdayPlanUnFinishCust = new JSONObject();
                        yesterdayPlanUnFinishCust.put("CUST_NAME", custName);
                        yesterdayPlanUnFinishCust.put("UNFINISH_CAUSE_DESC", unFinishCauseDesc);
                        yesterdayPlanUnFinishCustList.add(yesterdayPlanUnFinishCust);
                    }

                }
            }

            //查今天的
            PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, planDate);
            if(planEntity == null) {

            } else {
                String planId = planEntity.getPlanId();
                List<CustActionEntity> custActionEntityList = custActionDAO.queryCustActionListByPlanIdAndActionCode(planId, actionCode);
                for(CustActionEntity custActionEntity : custActionEntityList) {
                    String custId = custActionEntity.getCustId();
                    if(!custMap.containsKey(custId)) {
                        CustomerEntity customerEntity = custDAO.getCustById(custId);
                        custMap.put(custId, customerEntity.getCustName());
                    }
                    String custName = custMap.get(custId);
                    JSONObject planCust = new JSONObject();
                    planCust.put("CUST_NAME", custName);
                    planCustList.add(planCust);
                }
            }

            jsonActionMap.put("YESTERDAY_PLAN_CUST_LIST", yesterdayPlanCustList);
            jsonActionMap.put("YESTERDAY_PLAN_FINISH_CUST_LIST", yesterdayPlanFinishCustList);
            jsonActionMap.put("YESTERDAY_PLAN_UNFINISH_CUST_LIST", yesterdayPlanUnFinishCustList);
            jsonActionMap.put("PLAN_CUST_LIST", planCustList);
            jsonActionMap.put("ACTION_NAME",actionEntity.getActionName());
            datalist.add(jsonActionMap);
        }

        response.set("DATA_LIST", datalist);

        return response;
    }

    public ServiceResponse queryEmployeeMonStat(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String executorId = requestData.getString("PLAN_EXECUTOR_ID");
        String statMon = requestData.getString("STAT_MON");
        PlanFinishMonDAO planFinishMonDAO = DAOFactory.createDAO(PlanFinishMonDAO.class);
        PlanFinishMonEntity entity = planFinishMonDAO.getPlanFinishMonEntityByStatMonAndEid(statMon, executorId);
        JSONObject jsonStatResult;
        JSONArray arrayResult = new JSONArray();
        if(entity == null) {
            jsonStatResult = new JSONObject();
        } else {
            jsonStatResult = JSONObject.parseObject(entity.getStatResult());
        }
        List<ActionEntity> actionList = ActionCache.getActionListByType("1");
        for(ActionEntity actionEntity : actionList) {
            String actionCode = actionEntity.getActionCode();
            String actionName = actionEntity.getActionName();
            JSONObject jsonStatMon = new JSONObject();
            jsonStatMon.put("ACTION_CODE", actionCode);
            jsonStatMon.put("ACTION_NAME", actionName);
            if(jsonStatResult.containsKey(actionCode)) {
                jsonStatMon.put("TOTAL_NUM", jsonStatResult.getIntValue(actionCode));
            } else {
                jsonStatMon.put("TOTAL_NUM", 0);
            }

            arrayResult.add(jsonStatMon);
        }

        response.set("ACTION_COUNT_LIST", arrayResult);

        return response;
    }

    public ServiceResponse queryPlanDetail(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String executorId = requestData.getString("PLAN_EXECUTOR_ID");
        String planDate = requestData.getString("PLAN_DATE");

        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);
        CustActionDAO custActionDAO = DAOFactory.createDAO(CustActionDAO.class);
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        Map<String, String> custMap = new HashMap<String, String>();//key:custId;value:custName

        JSONArray datalist = new JSONArray();
        JSONObject baseInfo = new JSONObject();
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("1");

        PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, planDate);
        if(planEntity != null) {
            baseInfo.put("PLAN_DATE", planEntity.getPlanDate());
            baseInfo.put("PLAN_STATUS_DESC", PlanStatus.getNameByValue(planEntity.getPlanStatus()));
            baseInfo.put("PLAN_TYPE_DESC", PlanType.getNameByValue(planEntity.getPlanType()));

            //查询明细
            for(ActionEntity actionEntity : actionEntityList) {
                //先给个初始值
                JSONObject jsonActionMap = new JSONObject();
                String actionCode = actionEntity.getActionCode();
                JSONArray planFinishCustList = new JSONArray();
                JSONArray planUnFinishCustList = new JSONArray();
                JSONArray planCustList = new JSONArray();

                //查昨天的
                String planId = planEntity.getPlanId();
                List<CustActionEntity> custActionEntityList = custActionDAO.queryCustActionListByPlanIdAndActionCode(planId, actionCode);
                for(CustActionEntity custActionEntity : custActionEntityList) {
                    String custId = custActionEntity.getCustId();
                    String finishTime = custActionEntity.getFinishTime();
                    String unFinishCauseDesc = custActionEntity.getUnfinishCauseDesc();
                    if(!custMap.containsKey(custId)) {
                        CustomerEntity customerEntity = custDAO.getCustById(custId);
                        custMap.put(custId, customerEntity.getCustName());
                    }
                    String custName = custMap.get(custId);

                    JSONObject planCust = new JSONObject();
                    planCust.put("CUST_NAME", custName);
                    planCustList.add(planCust);

                    if(StringUtils.isNotBlank(finishTime)) {
                        //完成的
                        JSONObject planFinishCust = new JSONObject();
                        planFinishCust.put("CUST_NAME", custName);
                        planFinishCust.put("FINISH_TIME", finishTime);
                        planFinishCustList.add(planFinishCust);
                    } else {
                        JSONObject planUnFinishCust = new JSONObject();
                        planUnFinishCust.put("CUST_NAME", custName);
                        planUnFinishCust.put("UNFINISH_CAUSE_DESC", unFinishCauseDesc);
                        planUnFinishCustList.add(planUnFinishCust);
                    }
                }
                jsonActionMap.put("PLAN_CUST_LIST", planCustList);
                jsonActionMap.put("PLAN_FINISH_CUST_LIST", planFinishCustList);
                jsonActionMap.put("PLAN_UNFINISH_CUST_LIST", planUnFinishCustList);
                jsonActionMap.put("ACTION_NAME",actionEntity.getActionName());
                datalist.add(jsonActionMap);
            }
        } else {

        }

        response.set("CUST_ACTION_LIST", datalist);
        response.set("PLAN_INFO", baseInfo);

        return response;
    }

    public ServiceResponse queryCustUnFinishCause(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        Map<String, String> parameter = new HashMap<String, String>();
        JSONArray custList = new JSONArray();
        CustActionDAO custActionDAO = DAOFactory.createDAO(CustActionDAO.class);

        JSONObject requestData = request.getBody().getData();
        String custId = requestData.getString("CUST_ID");

        List<CustActionEntity> custActionEntityList = custActionDAO.queryCustUnFinishCauseByCustId(custId);
        for(CustActionEntity custActionEntity : custActionEntityList) {
            JSONObject custAction = custActionEntity.toJSON(new String[] {"UNFINISH_CAUSE_DESC"});
            custAction.put("ACTION_NAME", ActionCache.getAction(custActionEntity.getActionCode()).getActionName());
            custAction.put("EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(custActionEntity.getExecutorId()));
            custList.add(custAction);
        }

        response.set("CUST_UNFINISH_ACTION_CAUSE", custList);

        return response;
    }

    public ServiceResponse additionalAddCustAction(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        Map<String, String> parameter = new HashMap<String, String>();
        JSONArray custList = new JSONArray();
        CustActionDAO custActionDAO = DAOFactory.createDAO(CustActionDAO.class);
        CustOriginalActionDAO custOriginalActionDAO = DAOFactory.createDAO(CustOriginalActionDAO.class);

        JSONObject requestData = request.getBody().getData();
        String custId = requestData.getString("CUST_ID");
        String executorId = requestData.getString("EXECUTOR_ID");
        JSONArray actionList = requestData.getJSONArray("ACTION_LIST");

        String now = TimeTool.now();
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        for(int i = 0, size = actionList.size(); i < size; i++) {
            JSONObject action = actionList.getJSONObject(i);
            CustActionEntity custActionEntity = new CustActionEntity();
            custActionEntity.setCustId(custId);
            custActionEntity.setActionCode(action.getString("ACTION_CODE"));
            custActionEntity.setPlanId("0");
            custActionEntity.setActionStatus("1");
            custActionEntity.setFinishTime(now);
            custActionEntity.setExecutorId(executorId);
            custActionEntity.setCreateUserId(userId);
            custActionEntity.setCreateTime(now);
            custActionEntity.setUpdateUserId(userId);
            custActionEntity.setUpdateTime(now);
            custActionDAO.insert("INS_CUST_ACTION", custActionEntity.getContent());

            CustOriginalActionEntity custOriginalActionEntity = new CustOriginalActionEntity();
            custOriginalActionEntity.setCustId(custId);
            custOriginalActionEntity.setActionCode(action.getString("ACTION_CODE"));
            custOriginalActionEntity.setFinishTime(now);
            custOriginalActionEntity.setEmployeeId(executorId);
            custOriginalActionEntity.setCreateUserId(userId);
            custOriginalActionEntity.setCreateTime(now);
            custOriginalActionDAO.insert("INS_CUST_ORIGINAL_ACTION", custOriginalActionEntity.getContent());
        }

        return response;
    }

    public ServiceResponse setMonPlanTarget(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject target = request.getJSONObject("TARGET");
        String objType = request.getString("OBJ_TYPE");
        String[] objs = request.getString("OBJS").split(",");
        String targetTimeValue = request.getString("TARGET_TIME_VALUE");
        String targetTimeType = request.getString("TARGET_TIME_TYPE");

        MonPlanTargetBean.setMonPlanTarget(target, objType, objs, targetTimeType, targetTimeValue);

        return response;
    }

    public ServiceResponse initPlanEntry(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String executorId = request.getString("PLAN_EXECUTOR_ID");
        String planDate = request.getString("PLAN_DATE");
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);

        if(ArrayTool.isEmpty(planDAO.getMonNormalPlanListByEidAndPlanDate(executorId, planDate))) {
            //如果录计划的当月还没有一条正常工作的计划，则表示这可能是这个月第一天工作，
            // 重置当月目标数
            PlanBean.resetPlanCycleFinishInfo(executorId, planDate);
        }

        return response;
    }

}
