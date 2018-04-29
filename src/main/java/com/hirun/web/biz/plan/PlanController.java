package com.hirun.web.biz.plan;

import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.tool.PlanTool;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
@Controller
@ResponseBody
public class PlanController {

    @RequestMapping(value = "/plan/addPlan", method = RequestMethod.POST)
    public String addPlan(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("MarketCenter.plan.PlanService.addPlan", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/checkPlanTarget")
    public String checkPlanTarget(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("MarketCenter.plan.PlanService.checkPlanTarget", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/checkPlanAction")
    public String checkPlanAction(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("MarketCenter.plan.PlanService.checkPlanAction", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getPlanInitData")
    public String getPlanInitData(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = new ServiceResponse();
        response.setHeader("RESULT_CODE", "0");
        response.set("PLAN_DATE", PlanTool.getPlanDate());
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/getSummarizeInitData")
    public String getSummarizeInitData(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("MarketCenter.plan.PlanService.getPlanFinishedInfo", paramter);
        return response.toJsonString();
    }
}
