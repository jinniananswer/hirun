package com.hirun.app.biz.plan;

import com.alibaba.fastjson.JSONObject;
import com.most.core.app.database.conn.ConnectionFactory;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.service.invoker.ServiceInvoker;
import com.most.core.app.service.register.ServiceRegister;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.client.ServiceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-17.
 */
public class TransOriginalDataToActionTest {

    @Before
    public void setUp() throws Exception {
        ConnectionFactory.init();
        ServiceRegister.register();
    }

    @Test
    public void ltzdstsActionTest() {
        try{
            Map parameter = new HashMap();
            JSONObject jsonObject = new JSONObject(parameter);
            ServiceRequest request = new ServiceRequest(jsonObject);
            ServiceResponse response = ServiceInvoker.invoke("OperationCenter.plan.PlanTaskService.transOriginalDataToAction", request);
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
