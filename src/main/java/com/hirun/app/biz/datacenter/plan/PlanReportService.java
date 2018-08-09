package com.hirun.app.biz.datacenter.plan;

import com.alibaba.fastjson.JSONArray;
import com.hirun.app.bean.plan.PlanStatBean;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;

/**
 * Created by awx on 2018/8/8/008.
 */
public class PlanReportService extends GenericService{

    public ServiceResponse queryEmployeeDailySheet(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String topEMployeeId = request.getString("TOP_EMPLOYEE_ID");
        String queryDate = request.getString("QUERY_DATE");

        JSONArray employeeDailySheetList = PlanStatBean.queryEmployeeDailySheetList("TOP", topEMployeeId, queryDate);
        response.set("EMPLOYEE_DAILYSHEET_LIST", employeeDailySheetList);

        return response;
    }
}
