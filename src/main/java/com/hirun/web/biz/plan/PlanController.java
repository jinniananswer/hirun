package com.hirun.web.biz.plan;

import com.hirun.pub.domain.entity.session.BizSessionEntity;
import com.hirun.pub.tool.PlanTool;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
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
import java.time.temporal.ChronoUnit;
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
//        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
//        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
//        paramter.put("PLAN_EXECUTOR_ID", sessionEntity.get("EMPLOYEE_ID"));

        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.addPlan", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/checkPlanTarget")
    public String checkPlanTarget(@RequestParam Map paramter) throws Exception {
//        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
//        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
//        paramter.put("EXECUTOR_ID", sessionEntity.get("EMPLOYEE_ID"));

        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.checkPlanTarget", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/checkPlanAction")
    public String checkPlanAction(@RequestParam Map paramter) throws Exception {
//        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
//        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
//        paramter.put("EXECUTOR_ID", sessionEntity.get("EMPLOYEE_ID"));

        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.checkPlanAction", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getPlanInitData")
    public String getPlanInitData(@RequestParam Map pageData) throws Exception {
        Map<String, String> paramter = new HashMap<String, String>();
        String planDate = (String)pageData.get("PLAN_DATE");
        if(StringUtils.isBlank(planDate)) {
            planDate = PlanTool.getPlanDate();
        }

        String executorId = (String)pageData.get("EXECUTOR_ID");
        if(StringUtils.isBlank(executorId)) {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
            executorId = sessionEntity.get("EMPLOYEE_ID");
        }

        paramter.put("PLAN_EXECUTOR_ID", executorId);
        paramter.put("PLAN_DATE", planDate);
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.planEntryInitCheck", paramter);
        if("0".equals(response.getResultCode())) {
            response.set("PLAN_DATE", planDate);
            response.set("EXECUTOR_ID", executorId);
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getSummarizeInitData")
    public String getSummarizeInitData(@RequestParam Map paramter) throws Exception {
        String summarizePlanDate = (String)paramter.get("PLAN_DATE");
        String planExecutorId = (String)paramter.get("PLAN_EXECUTOR_ID");

        if(StringUtils.isBlank(summarizePlanDate)) {
            summarizePlanDate = PlanTool.getPlanDate4Summarize();
        }
        if(StringUtils.isBlank(planExecutorId)) {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
            planExecutorId = sessionEntity.get("EMPLOYEE_ID");
        }


        paramter.put("PLAN_EXECUTOR_ID", planExecutorId);
        paramter.put("PLAN_DATE", summarizePlanDate);

        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.getPlanBaseInfo", paramter);
        if(StringUtils.isNotBlank(response.getString("PLAN_ID"))) {
            String planStatus = response.getString("PLAN_STATUS");
            String planDate = response.getString("PLAN_DATE");
            if("2".equals(planStatus)) {
                response.setError("-1", planDate + "的计划已经总结过了，不能再次总结");
            }
        } else {
            response.setError("-1", "没有【" + summarizePlanDate + "】的计划");
            return response.toJsonString();
        }

        ServiceResponse newCustListResponse = ServiceClient.call("OperationCenter.plan.PlanService.queryNewCustList4Summary", paramter);
        response.set("CUST_LIST", newCustListResponse.getJSONArray("CUST_LIST"));

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

    @RequestMapping(value = "/plan/getPlanActionAndCustList")
    public String getPlanActionAndCustList(@RequestParam Map paramter) throws Exception{
//        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
//        BizSessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
//        paramter.put("PLAN_EXECUTOR_ID", sessionEntity.getEmployeeId());
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.getPlanActionAndCustList", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getTargetLowerLimit")
    public String getTargetLowerLimit(@RequestParam Map paramter) throws Exception{
//        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
//        BizSessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
//        paramter.put("PLAN_EXECUTOR_ID", sessionEntity.getEmployeeId());
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.getTargetLowerLimit", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/queryEmployeeDailySheetDetail")
    public String queryEmployeeDailySheetDetail(@RequestParam Map parameter) throws Exception{
        parameter.put("PLAN_DATE", TimeTool.today());
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.queryEmployeeDailySheetDetail", parameter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/queryEmployeeMonStat")
    public String queryEmployeeMonStat(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.queryEmployeeMonStat", parameter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/queryPlanDetail")
    public String queryPlanDetail(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.plan.PlanService.queryPlanDetail", parameter);
        return response.toJsonString();
    }
}
