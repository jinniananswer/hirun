package com.hirun.web.biz.plan;

import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.domain.entity.session.BizSessionEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.hirun.pub.tool.PlanTool;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.MapTool;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
@Controller
@ResponseBody
public class PlanController {

    @RequestMapping(value = "/plan/addPlan", method = RequestMethod.POST)
    public String addPlan(@RequestParam Map paramter) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        BizSessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        paramter.put("PLAN_EXECUTOR_ID", sessionEntity.getEmployeeId());

        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.addPlan", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/checkPlanTarget")
    public String checkPlanTarget(@RequestParam Map paramter) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        BizSessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        paramter.put("EXECUTOR_ID", sessionEntity.getEmployeeId());

        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.checkPlanTarget", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/checkPlanAction")
    public String checkPlanAction(@RequestParam Map paramter) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        BizSessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        paramter.put("EXECUTOR_ID", sessionEntity.getEmployeeId());

        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.checkPlanAction", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getPlanInitData")
    public String getPlanInitData(@RequestParam Map pageData) throws Exception {
        ServiceResponse response = new ServiceResponse();
        response.setHeader("RESULT_CODE", "0");
        response.set("PLAN_DATE", PlanTool.getPlanDate());

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        BizSessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        Map<String, String> paramter = new HashMap<String, String>();
        paramter.put("PLAN_EXECUTOR_ID", sessionEntity.getEmployeeId());
        paramter.put("PLAN_DATE", PlanTool.getPlanDate());
        ServiceResponse planInfoResponse = ServiceClient.call("OperationCenter.plan.PlanService.getPlanBaseInfo", paramter);
        if(StringUtils.isNotBlank(planInfoResponse.getString("PLAN_ID"))) {
            response.setError("-1", "您已经录过【" + PlanTool.getPlanDate() + "】的计划了");
        }

        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getSummarizeInitData")
    public String getSummarizeInitData(@RequestParam Map paramter) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        BizSessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        paramter.put("PLAN_EXECUTOR_ID", sessionEntity.getEmployeeId());
        paramter.put("PLAN_DATE", PlanTool.getPlanDate4Summarize());

        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.getPlanBaseInfo", paramter);
        if(StringUtils.isNotBlank(response.getString("PLAN_ID"))) {
            ServiceResponse newCustListResponse = ServiceClient.call("OperationCenter.plan.PlanService.queryNewCustList4Summary", paramter);
            response.set("CUST_LIST", newCustListResponse.getJSONArray("CUST_LIST"));
        } else {
            response.setError("-1", "没有【" + PlanTool.getPlanDate4Summarize() + "】的计划");
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getCauseListByActionCode")
    public String getCauseListByActionCode(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.getCauseListByActionCode", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/summarizePlan")
    public String summarizePlan(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.summarizePlan", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getNewCustList")
    public String getNewCustList(@RequestParam Map paramter) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        BizSessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        paramter.put("PLAN_EXECUTOR_ID", sessionEntity.getEmployeeId());
        paramter.put("PLAN_DATE", PlanTool.getPlanDate4Summarize());

        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.queryNewCustList4Summary", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getCustFinishActionList")
    public String getCustFinishAction(@RequestParam Map paramter) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.getCustFinishActionList", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getPlanFinishedInfo")
    public String getPlanFinishedInfo(@RequestParam Map paramter) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.getPlanFinishedInfo", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/queryEmployeeDailySheet")
    public String queryEmployeeDailySheet(@RequestParam Map paramter) throws Exception{
        String planDate = TimeTool.today();
//        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
//        BizSessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
//        paramter.put("PLAN_EXECUTOR_ID", sessionEntity.getEmployeeId());
        paramter.put("PLAN_DATE", planDate);
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.queryEmployeeDailySheet", paramter);
        return response.toJsonString();
    }
}
