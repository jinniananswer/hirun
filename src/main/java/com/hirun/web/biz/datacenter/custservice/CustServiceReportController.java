package com.hirun.web.biz.datacenter.custservice;

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

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class CustServiceReportController extends RootController {


    @RequestMapping(value = "queryCustServFinishActionInfo")
    public @ResponseBody String queryCustServFinishActionInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryCustServFinishActionInfo", paramter);
        return response.toJsonString();
    }



    @RequestMapping(value = "initQueryActionFinishInfo")
    public @ResponseBody String initQueryActionFinishInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.initQueryActionFinishInfo", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryCustServiceByName")
    public @ResponseBody String queryCustServiceByName(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryCustServiceByName", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryCustServMonStatInfo")
    public @ResponseBody String queryCustServMonStatInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryCustServMonStatInfo", paramter);
        return response.toJsonString();
    }
}
