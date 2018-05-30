package com.hirun.out;

import com.alibaba.fastjson.JSONObject;
import com.most.core.app.database.conn.ConnectionFactory;
import com.most.core.app.service.invoker.ServiceInvoker;
import com.most.core.app.service.register.ServiceRegister;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-21.
 */
public class DataImportTest {

    @Before
    public void setUp() throws Exception {
        ConnectionFactory.init();
        ServiceRegister.register();
    }

    @Test
    public void hirunplusDataImportTest() {
        try{
            Map parameter = new HashMap();
            JSONObject jsonObject = new JSONObject(parameter);
            ServiceRequest request = new ServiceRequest(jsonObject);
            ServiceResponse response = ServiceInvoker.invoke("OperationCenter.plan.DataImportTaskService.hirunplusDataImport", request);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void hirunplusStaffDataImportTest() {
        try{
            Map parameter = new HashMap();
            JSONObject jsonObject = new JSONObject(parameter);
            ServiceRequest request = new ServiceRequest(jsonObject);
            ServiceResponse response = ServiceInvoker.invoke("OperationCenter.plan.DataImportTaskService.hirunplusStaffDataImport", request);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
