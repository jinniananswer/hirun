package com.hirun.app.biz.cust;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author awx
 * @Date 2018/4/17 21:11
 * @Description:
 */
public class CustService extends GenericService{

    public ServiceResponse addCust(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject custInfo = request.getBody().getData();

        CustDAO dao = new CustDAO("ins");
        custInfo.put("HOUSE_COUNSELOR_ID", "123");
        Map<String, String> parameter = ConvertTool.toMap(custInfo);
        int custId = dao.insert("INS_CUSTOMER", parameter);
        response.set("CUST_ID", custId);

        return response;
    }

    public ServiceResponse editCust(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject custInfo = request.getBody().getData();

        CustDAO dao = new CustDAO("ins");
        Map<String, String> parameter = ConvertTool.toMap(custInfo);
        int i = dao.save("INS_CUSTOMER", new String[] {"CUST_ID"}, parameter);

        return response;
    }

    public ServiceResponse queryCustList(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSE_COUNSELOR_ID","123");

        CustDAO dao = new CustDAO("ins");
        List<CustomerEntity> customerList = dao.query(CustomerEntity.class, "INS_CUSTOMER", parameter);

        response.set("CUSTOMERLIST", ConvertTool.toJSONArray(customerList));

        return response;
    }

    public ServiceResponse addCustByNum(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        int newCustNew = requestData.getInteger("NEW_CUSTNUM");
        String custNamePrefix = requestData.getString("CUST_NAME_PREFIX");
        if(StringUtils.isBlank(custNamePrefix)) {
            custNamePrefix = TimeTool.today();
        }
        List<Map<String, String>> listCust = new ArrayList<Map<String, String>>();
        CustDAO dao = new CustDAO("ins");
        for(int i = 0; i < newCustNew; i++) {
            Map<String, String> cust = new HashMap<String, String>();
            cust.put("HOUSE_COUNSELOR_ID", "123");
            cust.put("CUST_STATUS", "9");
            cust.put("CUST_NAME", custNamePrefix + "新客户");
            int custId = dao.insert("INS_CUSTOMER", cust);
            cust.put("CUST_ID", String.valueOf(custId));
            listCust.add(cust);
        }


//        dao.insertBatch("INS_CUSTOMER", listCust);

        response.set("custList", JSONArray.toJSON(listCust));

        return response;
    }
}
