package com.hirun.web.biz.datacenter.plan;

import com.hirun.pub.domain.enums.plan.PlanType;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PlanReportController extends RootController {

    @RequestMapping("/datacenter/plan/queryEmployeeDaillySheet")
    public @ResponseBody String queryEmployeeDaillySheet(@RequestParam Map paramter) throws Exception{
        ServiceResponse response = ServiceClient.call("DataCenter.plan.PlanReportService.queryEmployeeDailySheet", paramter);
        return response.toJsonString();
    }

    @RequestMapping("/datacenter/plan/queryEmployeeDaillySheet2")
    public @ResponseBody String queryEmployeeDaillySheet2(@RequestParam Map paramter) throws Exception{
        String employeeId = (String)paramter.get("EMPLOYEE_ID");
        if(StringUtils.isBlank(employeeId)) {
            logger.info("/datacenter/plan/queryEmployeeDaillySheet2没有取到EMPLOYEE_ID");
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
            employeeId = sessionEntity.get("EMPLOYEE_ID");
            logger.info("/datacenter/plan/queryEmployeeDaillySheet2从session里取EMPLOYEE_ID，值为" + employeeId);
            paramter.put("EMPLOYEE_ID", employeeId);
        }
        ServiceResponse response = ServiceClient.call("DataCenter.plan.PlanReportService.queryEmployeeDailySheet2", paramter);
        return response.toJsonString();
    }

    @RequestMapping("/datacenter/plan/queryUnEntryPlanList")
    public @ResponseBody String queryUnEntryPlanList(@RequestParam Map paramter) throws Exception{
        paramter.put("PLAN_DATE", TimeTool.today());
        ServiceResponse response = ServiceClient.call("DataCenter.plan.PlanReportService.queryUnEntryPlanList", paramter);
        return response.toJsonString();
    }

    @RequestMapping("/datacenter/plan/queryHolidayPlanList")
    public @ResponseBody String queryHolidayPlanList(@RequestParam Map paramter) throws Exception{
        paramter.put("PLAN_DATE", TimeTool.today());
        paramter.put("PLAN_TYPE", PlanType.holiday.getValue());
        ServiceResponse response = ServiceClient.call("DataCenter.plan.PlanReportService.queryEmployeeListByPlanType", paramter);
        return response.toJsonString();
    }

    @RequestMapping("/datacenter/plan/queryActivitiPlanList")
    public @ResponseBody String queryActivitiPlanList(@RequestParam Map paramter) throws Exception{
        paramter.put("PLAN_DATE", TimeTool.today());
        paramter.put("PLAN_TYPE", PlanType.active.getValue());
        ServiceResponse response = ServiceClient.call("DataCenter.plan.PlanReportService.queryEmployeeListByPlanType", paramter);
        return response.toJsonString();
    }

    @RequestMapping("/datacenter/plan/queryEmployeeMonthSheet2")
    public @ResponseBody String queryEmployeeMonthSheet2(@RequestParam Map paramter) throws Exception{
        String employeeId = (String)paramter.get("EMPLOYEE_ID");
        if(StringUtils.isBlank(employeeId)) {
            logger.info("/datacenter/plan/queryEmployeeMonthSheet2没有取到EMPLOYEE_ID");
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
            employeeId = sessionEntity.get("EMPLOYEE_ID");
            logger.info("/datacenter/plan/queryEmployeeMonthSheet2从session里取EMPLOYEE_ID，值为" + employeeId);
            paramter.put("EMPLOYEE_ID", employeeId);
        }
        ServiceResponse response = ServiceClient.call("DataCenter.plan.PlanReportService.queryEmployeeMonthSheet2", paramter);
        return response.toJsonString();
    }
}
