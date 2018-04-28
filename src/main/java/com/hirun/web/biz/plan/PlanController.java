package com.hirun.web.biz.plan;

import com.alibaba.fastjson.JSONObject;
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
    public String addPlan(String planInfoString) throws Exception {
        ServiceResponse response = ServiceClient.call("PlanCenter.plan.PlanService.addPlan", JSONObject.parseObject(planInfoString));
        return response.toJsonString();
    }

    @RequestMapping(value = "/plan/checkPlanTarget")
    public String checkPlanTarget(String scanHouseCounselorNum, String adviceNum) throws Exception {
        JSONObject planTarget = new JSONObject();
        planTarget.put("SCAN_HOUSE_COUNSELOR_NUM", scanHouseCounselorNum);
        planTarget.put("ADVICE_NUM", adviceNum);
        ServiceResponse response = ServiceClient.call("PlanCenter.plan.PlanService.checkPlanTarget", planTarget);
        return response.toJsonString();
    }
}
