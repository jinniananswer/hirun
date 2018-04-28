package com.hirun.app.biz.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.cache.PlanTargetLimitCache;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
public class PlanService extends GenericService {

    public ServiceResponse addPlan(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();

        JSONObject planInfo = request.getBody().getData();
        PlanDAO planDao = new PlanDAO("ins");
        CustActionDAO custActionDAO = new CustActionDAO("ins");

        String planDate = TimeTool.now("yyyy-MM-dd");
        String planId = "1";
        Map<String, String> planEntityParameter = new HashMap<String, String>();
        planEntityParameter.put("PLAN_ID", planId);
        planEntityParameter.put("PLAN_DATE", planDate);
        planEntityParameter.put("PLAN_STATUS", "0");
        planEntityParameter.put("PLAN_EXECUTOR_ID", "123");
        planDao.insert("INS_PLAN", planEntityParameter);

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
                custActionList.add(custActionParameter);
            }
        }
        custActionDAO.insertBatch("INS_CUST_ACTION", custActionList);

        return response;
    }

    public ServiceResponse checkPlanTarget(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();

        JSONObject planTarget = request.getBody().getData();
        int scanHouseCounselorNum = planTarget.getInteger("SCAN_HOUSE_COUNSELOR_NUM");
        int adviceNum = planTarget.getInteger("ADVICE_NUM");

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
        }

        return response;
    }
}
