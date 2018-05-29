package com.hirun.app.biz.plan;

import com.alibaba.fastjson.JSONObject;
import com.most.core.app.database.conn.ConnectionFactory;
import com.most.core.app.service.invoker.ServiceInvoker;
import com.most.core.app.service.register.ServiceRegister;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.web.client.ServiceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-26.
 */
public class PlanServiceTest {

    @Before
    public void setUp() throws Exception {
        ConnectionFactory.init();
        ServiceRegister.register();
    }

    @Test
    public void queryEmployeeDailySheetDetailTest() {
        JSONObject parameter = new JSONObject();
        parameter.put("EXECUTOR_ID", "252");
        parameter.put("PLAN_DATE", TimeTool.today());
        ServiceRequest request = new ServiceRequest(parameter);
        try {
            ServiceResponse response = ServiceInvoker.invoke("OperationCenter.plan.PlanService.queryEmployeeDailySheetDetail", request);
            System.out.println(response.toJsonString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
