package com.hirun.app.biz.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.PlanActionLimitCache;
import com.hirun.app.cache.PlanTargetLimitCache;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.pub.domain.entity.param.PlanActionLimitEntity;
import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
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

        String planDate = planInfo.getString("PLAN_DATE");
        String planType = planInfo.getString("PLAN_TYPE");

        //TODO 需做校验

        Map<String, String> planEntityParameter = new HashMap<String, String>();
        planEntityParameter.put("PLAN_DATE", planDate);
        planEntityParameter.put("PLAN_STATUS", "0");
        planEntityParameter.put("PLAN_EXECUTOR_ID", "123");
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
                    custActionParameter.put("EXECUTOR_ID", "123");
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

            /*
            //校验自身的数量限制
            PlanTargetLimitEntity planTargetLimitEntity = PlanTargetLimitCache.getPlanTargetLimit(actionCode);
            if(planTargetLimitEntity != null) {
                int timeInterval = Integer.parseInt(planTargetLimitEntity.getTimeInterval());
                int unit = Integer.parseInt(planTargetLimitEntity.getUnit());
                int limitNum = Integer.parseInt(planTargetLimitEntity.getLimitNum());

                int totalNum = 0;
                String startTime = "2018-04-28";
                if("ZX".equals(actionCode)) {
                    //获取往日数量
                    totalNum = custActionDAO.queryFinishActionCount("123", actionCode, startTime, planDate);
                    if(limitNum - totalNum > num) {
                        //报错
                        response.setError("-1", "今日目标[咨询数]必须大于" + (limitNum - totalNum));
                        return response;
                    }
                } else if("SMJRQLC".equals(actionCode)) {
                    if(limitNum - totalNum > num) {
                        //报错
                        response.setError("-1", "今日目标[扫码进入全流程数]必须大于" + (limitNum - totalNum));
                        return response;
                    }
                }
            }

            //校验与其他动作的数量限制
            //TODO 这段代码以后需变成公共代码
            String errorMessage = checkPlanAction(actionCode, num, targetJSONObject);
            if(StringUtils.isNotBlank(errorMessage)) {
                response.setError("-1", errorMessage);
                return response;
            }
        }


        List<PlanTargetLimitEntity> planTargetLimitEntityList = PlanTargetLimitCache.getPlanTargetLimitList();
        for(PlanTargetLimitEntity planTargetLimitEntity : planTargetLimitEntityList) {
            String targetCode = planTargetLimitEntity.getTargetCode();
            int timeInterval = Integer.parseInt(planTargetLimitEntity.getTimeInterval());
            int unit = Integer.parseInt(planTargetLimitEntity.getUnit());
            int limitNum = Integer.parseInt(planTargetLimitEntity.getLimitNum());

            int totalNum = 0;

            //获取过往累计数量
            if("ZX".equals(targetCode)) {
                if(limitNum - totalNum > adviceNum) {
                    //报错
                    response.setError("-1", "今日目标[咨询数]必须大于" + (limitNum - totalNum));
                    break;
                }
            } else if("SMJRQLC".equals(targetCode)) {
                if(limitNum - totalNum > scanHouseCounselorNum) {
                    //报错
                    response.setError("-1", "今日目标[扫码进入全流程数]必须大于" + (limitNum - totalNum));
                    break;
                }
            }
        }*/

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

        //TODO 这一段需公用
        /*
        PlanActionLimitEntity planActionLimitEntity = PlanActionLimitCache.getPlanActionLimit(actionCode);
        if(planActionLimitEntity != null) {
            String relActionCode = planActionLimitEntity.getRelActionCode();
            int multipleNum = JSONObject.parseObject(planActionLimitEntity.getLimitParam()).getInteger("NUM");
            for(int i = 0; i < planTargetList.size(); i++) {
                JSONObject planTarget = planTargetList.getJSONObject(i);
                String targetActionCode = planTarget.getString("ACTION_CODE");
                if(relActionCode.equals(targetActionCode)) {
                    int num = planTarget.getInteger("NUM");
                    if(custNum < num*multipleNum) {
                        String actionName = ActionCache.getAction(actionCode).getActionName();
                        response.setError("-1", actionName + "数需至少" + num*multipleNum + "个");
                        break;
                    }
                }
            }
        }
        */

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
            totalNum = custActionDAO.queryFinishActionCount("123", actionCode, startTime, planDate);

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
}
