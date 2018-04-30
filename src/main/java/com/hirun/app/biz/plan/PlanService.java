package com.hirun.app.biz.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.PlanActionLimitCache;
import com.hirun.app.cache.PlanTargetLimitCache;
import com.hirun.app.cache.PlanUnFinishCauseCache;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.param.PlanActionLimitEntity;
import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.hirun.pub.domain.entity.param.PlanUnfinishCauseEntity;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.tool.PlanTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by pc on 2018-04-28.
 */
public class PlanService extends GenericService {

    public ServiceResponse addPlan(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();

        JSONObject planInfo = request.getBody().getData();
        PlanDAO planDao = new PlanDAO("ins");
        CustActionDAO custActionDAO = new CustActionDAO("ins");

        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        String planDate = planInfo.getString("PLAN_DATE");
        String planType = planInfo.getString("PLAN_TYPE");

        //TODO 需做校验

        Map<String, String> planEntityParameter = new HashMap<String, String>();
        planEntityParameter.put("PLAN_DATE", planDate);
        planEntityParameter.put("PLAN_STATUS", "0");
        planEntityParameter.put("PLAN_EXECUTOR_ID", userId);
        planEntityParameter.put("PLAN_TYPE", planType);
        String planId = String.valueOf(planDao.insert("INS_PLAN", planEntityParameter));

        if("1".equals(planType)) {
            JSONArray planList = planInfo.getJSONArray("PLANLIST");
            List<Map<String, String>> custActionList = new ArrayList<Map<String, String>>();
            for(int i = 0, sizeI = planList.size(); i < sizeI; i++) {
                JSONObject plan = planList.getJSONObject(i);
                String actionCode = plan.getString("ACTION_CODE");
                JSONArray custList = plan.getJSONArray("CUSTLIST");
                for(int j = 0, sizeJ = custList.size(); j < sizeJ; j++) {
                    String custId = custList.getJSONObject(j).getString("CUST_ID");
                    Map<String, String> custActionParameter = new HashMap<String, String>();
                    custActionParameter.put("PLAN_ID", planId);
                    custActionParameter.put("ACTION_CODE", actionCode);
                    custActionParameter.put("CUST_ID", custId);
                    custActionParameter.put("ACTION_STATUS", "0");
                    custActionParameter.put("PLAN_DEAL_DATE", planDate);
                    custActionParameter.put("EXECUTOR_ID", userId);
                    custActionList.add(custActionParameter);
                }
            }
            custActionDAO.insertBatch("INS_CUST_ACTION", custActionList);
        }

        return response;
    }

    public ServiceResponse checkPlanTarget(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();

        JSONObject requestData = request.getBody().getData();
        JSONArray targetList = requestData.getJSONArray("PLAN_TARGET_LIST");
        String planDate = requestData.getString("PLAN_DATE");

        JSONObject targetJSONObject = ConvertTool.toJSONObject(targetList, "ACTION_CODE");
        Iterator<String> iter = targetJSONObject.keySet().iterator();
        while(iter.hasNext()) {
            String actionCode = (String) iter.next();
            int num = targetJSONObject.getJSONObject(actionCode).getIntValue("NUM");

            String errorMessage = this.checkPlanAction(actionCode, num, planDate, targetJSONObject);
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
        JSONObject targetJSONObject = ConvertTool.toJSONObject(planTargetList, "ACTION_CODE");

        String errorMessage = this.checkPlanAction(actionCode, custNum, planDate, targetJSONObject);
        if (StringUtils.isNotBlank(errorMessage)) {
            response.setError("-1", errorMessage);
            return response;
        }

        return response;
    }

    /**
     *
     * @param actionCode
     * @param custNum
     * @param actionMap
     * @return
     * @throws Exception
     */
    private String checkPlanAction(String actionCode, int custNum, String planDate, JSONObject actionMap) throws Exception {
        StringBuilder errorMessage = new StringBuilder();

        String actionName = ActionCache.getAction(actionCode).getActionName();
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        //校验自身动作数量限制
        PlanTargetLimitEntity planTargetLimitEntity = PlanTargetLimitCache.getPlanTargetLimit(actionCode);
        if(planTargetLimitEntity != null) {
            int timeInterval = Integer.parseInt(planTargetLimitEntity.getTimeInterval());
            int unit = Integer.parseInt(planTargetLimitEntity.getUnit());
            int limitNum = Integer.parseInt(planTargetLimitEntity.getLimitNum());

            int totalNum = 0;

            //获取往日数量
            CustActionDAO custActionDAO = new CustActionDAO("ins");
            String startTime = TimeTool.addTime(planDate + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, (-timeInterval+1)).substring(0,10);
            totalNum = custActionDAO.queryFinishActionCount(userId, actionCode, startTime, planDate);

            if(limitNum - totalNum > custNum) {
                //报错
                errorMessage.append(actionName + "数需至少" + (limitNum - totalNum) + "个");
                return errorMessage.toString();
            }

        }

        //校验与其他动作的限制
        PlanActionLimitEntity planActionLimitEntity = PlanActionLimitCache.getPlanActionLimit(actionCode);
        if(planActionLimitEntity != null) {
            String relActionCode = planActionLimitEntity.getRelActionCode();
            int multipleNum = JSONObject.parseObject(planActionLimitEntity.getLimitParam()).getInteger("NUM");
            if (actionMap.containsKey(relActionCode)) {
                int relActionCustNum = actionMap.getJSONObject(relActionCode).getIntValue("NUM");
                if (custNum < relActionCustNum * multipleNum) {
                    errorMessage.append(actionName + "数需至少" + relActionCustNum * multipleNum + "个");
                    return errorMessage.toString();
                }
            }
        }

        return "";
    }

    public ServiceResponse getPlanFinishedInfo(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        String planExecutorId = userId;
        String planDate = PlanTool.getPlanDate4Summarize();

        //查询计划
        PlanDAO planDAO = new PlanDAO("ins");
        PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(planExecutorId, planDate);
        if(planEntity == null) {
            response.set("-1", "没有时间为" + planDate + "的计划");
            return response;
        }
        String planId = planEntity.getPlanId();
        response.set("PLAN_ID", planId);
        response.set("PLAN_DATE", planDate);

        //获取计划时间的所有客户动作，包括执行的和未执行的
        JSONArray planList = new JSONArray();
        JSONArray finishActionList = new JSONArray();
        JSONArray unFinishActionList = new JSONArray();
        CustActionDAO custActionDAO = new CustActionDAO("ins");
        CustDAO custDAO = new CustDAO("ins");
        List<CustActionEntity> custActionEntityList = custActionDAO.queryCustActionByEidAndPlanDate(planExecutorId, planDate);
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
                if(!custMap.containsKey(custId)) {
                    CustomerEntity customerEntity = custDAO.getCustById(custId);
                    if(customerEntity == null) {
                        //TODO 如果为NULL怎么处理？？？
                    }
                    custMap.put(custId, customerEntity.toJSON(new String[] {"CUST_ID", "CUST_NAME"}));
                }
                if(StringUtils.isNotBlank(custActionEntity.getPlanId())) {//这里面也有完成的
                    //计划中
                    if(planCustActionMap.containsKey(actionCode)) {
                        planCustActionMap.getJSONArray(actionCode).add(custMap.getJSONObject(custId));
                    } else {
                        JSONArray custList = new JSONArray();
                        custList.add(custMap.getJSONObject(custId));
                        planCustActionMap.put(actionCode, custList);
                    }

                    if(StringUtils.isNotBlank(custActionEntity.getFinishTime())) {
                        //计划中且完成的
                        if(finishCustActionMap.containsKey(actionCode)) {
                            finishCustActionMap.getJSONArray(actionCode).add(custMap.getJSONObject(custId));
                        } else {
                            JSONArray custList = new JSONArray();
                            custList.add(custMap.getJSONObject(custId));
                            finishCustActionMap.put(actionCode, custList);
                        }
                    } else {
                        //计划中未完成的
                        if(unFinishCustActionMap.containsKey(actionCode)) {
                            unFinishCustActionMap.getJSONArray(actionCode).add(custMap.getJSONObject(custId));
                        } else {
                            JSONArray custList = new JSONArray();
                            custList.add(custMap.getJSONObject(custId));
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
}
