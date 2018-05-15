package com.hirun.app.biz.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.plan.ActionCheckRuleProcess;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.PlanActionLimitCache;
import com.hirun.app.cache.PlanTargetLimitCache;
import com.hirun.app.cache.PlanUnFinishCauseCache;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.plan.PlanActionNumDAO;
import com.hirun.app.dao.plan.PlanCycleFinishInfoDAO;
import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.hirun.pub.domain.entity.param.PlanActionLimitEntity;
import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.hirun.pub.domain.entity.param.PlanUnfinishCauseEntity;
import com.hirun.pub.domain.entity.plan.PlanCycleFinishInfoEntity;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.tool.CustomerTool;
import com.hirun.pub.tool.PlanTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.*;
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

        String planDate = requestData.getString("PLAN_DATE");
        String planType = requestData.getString("PLAN_TYPE");
        String planExecutorId = requestData.getString("PLAN_EXECUTOR_ID");
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
                String errorMessage = ActionCheckRuleProcess.checkPlanAction(planExecutorId,actionEntity.getActionCode(),actionMap.getIntValue(actionEntity.getActionCode()),planDate,actionMap);
                if(StringUtils.isNotBlank(errorMessage)) {
                    response.setError("-1", errorMessage);
                    return response;
                }
            }
        }

        Map<String, String> planEntityParameter = new HashMap<String, String>();
        planEntityParameter.put("PLAN_DATE", planDate);
        planEntityParameter.put("PLAN_STATUS", "0");
        planEntityParameter.put("PLAN_EXECUTOR_ID", planExecutorId);
        planEntityParameter.put("PLAN_TYPE", planType);
        planEntityParameter.put("CREATE_USER_ID", userId);
        planEntityParameter.put("CREATE_DATE", now);
        planEntityParameter.put("UPDATE_USER_ID", userId);
        planEntityParameter.put("UPDATE_TIME", now);
        String planId = String.valueOf(planDao.insertAutoIncrement("INS_PLAN", planEntityParameter));

        //将今天的计划动作数记到INS_PLAN_ACTION_NUM
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

        if("1".equals(planType)) {
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
                    custActionParameter.put("CREATE_DATE", now);
                    custActionParameter.put("UPDATE_USER_ID", userId);
                    custActionParameter.put("UPDATE_TIME", now);
                    custActionList.add(custActionParameter);
                }
            }
            if(ArrayTool.isNotEmpty(custActionList)) {
                custActionDAO.insertBatch("INS_CUST_ACTION", custActionList);
            }
        }

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
                String errorMessage = ActionCheckRuleProcess.checkPlanAction(executorId, actionCode, num, planDate, targetJSONObject);
                if (StringUtils.isNotBlank(errorMessage)) {
                    response.setError("-1", errorMessage);
                return response;
            }
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

        String errorMessage = ActionCheckRuleProcess.checkPlanAction(executorId, actionCode, custNum, planDate, targetJSONObject);
        if (StringUtils.isNotBlank(errorMessage)) {
            response.setError("-1", errorMessage);
            return response;
        }

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
            response.setError("-1", "没有找到编码为【" + planId + "】的计划");
            return response;
        }

        //获取计划时间的所有客户动作，包括执行的和未执行的
        JSONArray planList = new JSONArray();
        JSONArray finishActionList = new JSONArray();
        JSONArray unFinishActionList = new JSONArray();
        CustActionDAO custActionDAO = new CustActionDAO("ins");
        CustDAO custDAO = new CustDAO("ins");
        List<CustActionEntity> custActionEntityList = custActionDAO.queryCustActionByEidAndPlanDate(planEntity.getPlanExecutorId(), planEntity.getPlanDate());
        if(ArrayTool.isNotEmpty(custActionEntityList)) {
            //存计划action对应的custList，格式为{actionCode, custList}，后续再遍历成list
            JSONObject planCustActionMap = new JSONObject();
            //存完成action对应的custList，格式为{actionCode, custList}，后续再遍历成list
            JSONObject finishCustActionMap = new JSONObject();
            //存未完成action对应的custList，格式为{actionCode, custList}，后续再遍历成list
            JSONObject unFinishCustActionMap = new JSONObject();
            //存客户信息
            JSONObject custMap = new JSONObject();//{custId, cust}

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
                    custMap.put(custId, customerEntity.toJSON(new String[] {"CUST_ID", "CUST_NAME"}));
                }

                JSONObject jsonAction = custActionEntity.toJSON(new String[] {"ACTION_ID","CUST_ID"});
                jsonAction.putAll(custMap.getJSONObject(custId));

                if(StringUtils.isNotBlank(custActionEntity.getPlanId())) {//这里面也有完成的
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
                        if(unFinishCustActionMap.containsKey(actionCode)) {
                            unFinishCustActionMap.getJSONArray(actionCode).add(jsonAction);
                        } else {
                            JSONArray custList = new JSONArray();
                            custList.add(jsonAction);
                            unFinishCustActionMap.put(actionCode, custList);
                        }
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
            while(planIter.hasNext()) {
                String actionCode = (String)planIter.next();
                JSONObject plan = new JSONObject();
                plan.put("ACTION_CODE", actionCode);
                plan.put("CUSTLIST", planCustActionMap.getJSONArray(actionCode));
                planList.add(plan);
            }
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
        JSONArray unFinishSummaryList = requestData.getJSONArray("UNFINISH_SUMMARY_LIST");
        JSONArray addExtraCustActionList = requestData.getJSONArray("ADD_EXTRA_CUST_ACTION_LIST");
        JSONArray transToFinishList = requestData.getJSONArray("TRANS_TO_FINISH_LIST");

        String userId = SessionManager.getSession().getSessionEntity().getUserId();

        String sysdate = TimeTool.now();
        CustActionDAO custActionDAO = new CustActionDAO("ins");
        PlanDAO planDAO = new PlanDAO("ins");
        Map<String, List<String>> custActionMap = new HashMap<String, List<String>>();

        Map<String, String> parameter  = new HashMap<String, String>();
        parameter.put("PLAN_ID", planId);
        List<PlanEntity> list = planDAO.query(PlanEntity.class, "INS_PLAN", parameter);
        if(ArrayTool.isEmpty(list)) {
            //TODO 报错
            response.setError("-1","找不到计划");
        }
        PlanEntity planEntity = list.get(0);

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
                addExtraCustActionDbParam.put("CREATE_DATE", sysdate);
                addExtraCustActionDbParam.put("UPDATE_USER_ID", userId);
                addExtraCustActionDbParam.put("UPDATE_TIME", sysdate);
                addExtraCustActionDbParam.put("ACTION_STATUS", "1");
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
        }

        if(ArrayTool.isNotEmpty(transToFinishList)) {
            for(int i = 0, size = transToFinishList.size(); i < size; i++) {
                parameter = ConvertTool.toMap(transToFinishList.getJSONObject(i));
                parameter.put("UPDATE_USER_ID", userId);
                parameter.put("UPDATE_TIME", sysdate);
                parameter.put("FINISH_TIME", sysdate);
                custActionDAO.save("INS_CUST_ACTION", parameter);
            }
        }

        //更新INS_CYCLE_PLAN_FINISH_INFO
        JSONObject finishTargetLimitMap = new JSONObject();
        PlanCycleFinishInfoDAO cyclePlanFinishInfoDAO = new PlanCycleFinishInfoDAO("ins");
        List<PlanTargetLimitEntity> planTargetLimitEntityList = PlanTargetLimitCache.getPlanTargetLimitList();
        for(PlanTargetLimitEntity planTargetLimitEntity : planTargetLimitEntityList) {
            String targetCode = planTargetLimitEntity.getTargetCode();
            int timeInterval = Integer.parseInt(planTargetLimitEntity.getTimeInterval());
            int limitNum = Integer.parseInt(planTargetLimitEntity.getLimitNum());

            PlanCycleFinishInfoEntity cyclePlanFinishInfoEntity = cyclePlanFinishInfoDAO.getCyclePlanFinishInfoEntity(planEntity.getPlanExecutorId(), targetCode);
            int preTotalUnfinishNum = Integer.parseInt(cyclePlanFinishInfoEntity.getUnfinishNum());
            String preCycleEndDate = cyclePlanFinishInfoEntity.getPreCycleEndDate();
            int interval = (int)TimeTool.getAbsDateDiffDay(LocalDate.parse(preCycleEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    LocalDate.parse(planEntity.getPlanDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            String startTime = TimeTool.addTime(planEntity.getPlanDate() + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, (-timeInterval+1)).substring(0,10);
            String endTime = TimeTool.addTime(planEntity.getPlanDate() + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, 1).substring(0,10);
            if(interval == timeInterval) {
                int totalFinishNum = custActionDAO.queryFinishActionCount(planEntity.getPlanExecutorId(), targetCode, startTime, endTime);

                parameter.clear();
                parameter.put("LOG_ID", cyclePlanFinishInfoEntity.getLogId());
                parameter.put("PRE_CYCLE_END_DATE", planEntity.getPlanDate());
                if(totalFinishNum > preTotalUnfinishNum + limitNum) {
                    parameter.put("UNFINISH_NUM", "0");
                } else {
                    parameter.put("UNFINISH_NUM", String.valueOf(preTotalUnfinishNum + limitNum - totalFinishNum));
                }
                cyclePlanFinishInfoDAO.save("INS_CYCLE_PLAN_FINISH_INFO", parameter);
            }
        }

        //更新客户的LAST_ACTION、LAST_ACTION_DATE

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
        List<CustomerEntity> customerEntityList = custDAO.queryNewCustListByPlanDate(planExecutorId, planDate);
        for(CustomerEntity customerEntity : customerEntityList) {
            JSONObject cust = customerEntity.toJSON(new String[] {"CUST_NAME", "WX_NICK", "CUST_ID"});

            //获取客户已完成的动作
            JSONArray jsonArrayCustActionList = new JSONArray();
            List<CustActionEntity> custActionEntityList = custActionDAO.queryCustFinishActionByCustId(cust.getString("CUST_ID"));
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

        JSONObject requestData = request.getBody().getData();
        String custId = requestData.getString("CUST_ID");

        CustActionDAO custActionDAO = new CustActionDAO("ins");

        parameter.put("CUST_ID", custId);
        List<CustActionEntity> custActionEntityList = custActionDAO.query(CustActionEntity.class, "INS_CUST_ACTION", parameter);
        if(ArrayTool.isNotEmpty(custActionEntityList)) {
            for(CustActionEntity custActionEntity : custActionEntityList) {
                if(StringUtils.isBlank(custActionEntity.getFinishTime())) {
                    //只要完成的
                    continue;
                }
                JSONObject custAction = custActionEntity.toJSON(new String[] {"FINISH_TIME"});
                custAction.put("ACTION_NAME", ActionCache.getAction(custActionEntity.getActionCode()).getActionName());
                custList.add(custAction);
            }
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
            todayCustActionEntityList = custActionDAO.queryCustActionByPlanId(todayPlanEntity.getPlanId());
        }
        PlanEntity yesterdayPlanEntity = planDAO.getPlanEntityByEidAndPlanDate(planExecutorId, yesterdayPlanDate);
        if(yesterdayPlanEntity != null) {
            yesterdayCustActionEntityList = custActionDAO.queryCustActionByPlanId(yesterdayPlanEntity.getPlanId());
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
}
