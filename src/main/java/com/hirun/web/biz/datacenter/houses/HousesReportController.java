package com.hirun.web.biz.datacenter.houses;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HousesReportController extends RootController {

    @RequestMapping("/initHousesNatureReport")
    public @ResponseBody String initHousesNatureReport() throws Exception{
        ServiceResponse response = ServiceClient.call("DataCenter.houses.HousesReportService.reportAllHousesNature", new HashMap<String, String>());
        return response.toJsonString();
    }

    @RequestMapping("/queryHousesNatureReport")
    public @ResponseBody String queryHousesNatureReport(HttpServletRequest request) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", request.getParameter("ORG_ID"));
        ServiceResponse response = ServiceClient.call("DataCenter.houses.HousesReportService.reportCompanyHousesNature", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryCounselorPlanActualReport")
    public @ResponseBody String queryCounselorPlanActualReport(HttpServletRequest request) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        String type = request.getParameter("QUERY_TYPE");
        String orgId = request.getParameter("ORG_ID");
        parameter.put("QUERY_TYPE",type);
        parameter.put("ORG_ID", orgId);
        ServiceResponse response = ServiceClient.call("DataCenter.houses.HousesReportService.reportCounselorPlanActual", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryCounselorLoopholeReport")
    public @ResponseBody String queryCounselorLoopholeReport(HttpServletRequest request) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", request.getParameter("ORG_ID"));
        ServiceResponse response = ServiceClient.call("DataCenter.houses.HousesReportService.reportCounselorLoophole", parameter);
        return response.toJsonString();
    }
}
