package com.hirun.web.biz.datacenter.plan;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
        ServiceResponse response = ServiceClient.call("DataCenter.plan.PlanReportService.queryEmployeeDailySheet2", paramter);
        return response.toJsonString();
    }

    @RequestMapping("/datacenter/plan/queryUnEntryPlanList")
    public @ResponseBody String queryUnEntryPlanList(@RequestParam Map paramter) throws Exception{
        paramter.put("PLAN_DATE", TimeTool.today());
        ServiceResponse response = ServiceClient.call("DataCenter.plan.PlanReportService.queryUnEntryPlanList", paramter);
        return response.toJsonString();
    }
}
