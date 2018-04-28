package com.hirun.app.biz.cust;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.hirun.pub.util.MapUtil;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.security.Encryptor;
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
        Map<String, String> parameter = MapUtil.jsonToMap(custInfo);
        int i = dao.insert("INS_CUSTOMER", parameter);

        if(i > 0) {
//            throw new Exception("测试异常-----------------");
        }

        return response;
    }

    public ServiceResponse editCust(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject custInfo = request.getBody().getData();

        CustDAO dao = new CustDAO("ins");
        Map<String, String> parameter = MapUtil.jsonToMap(custInfo);
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
        JSONObject inData = request.getBody().getData();

        int newCustNew = inData.getInteger("NEW_CUSTNUM");
        String sysdate = "2018-05-01";
        List<Map<String, String>> listCust = new ArrayList<Map<String, String>>();
        for(int i = 0; i < newCustNew; i++) {
            Map<String, String> cust = new HashMap<String, String>();
            cust.put("HOUSE_COUNSELOR_ID", "123");
            cust.put("CUST_STATUS", "9");
            cust.put("CUST_NAME", sysdate + "新客户");
            cust.put("CUST_ID","1001");
            listCust.add(cust);
        }

        CustDAO dao = new CustDAO("ins");
        dao.insertBatch("INS_CUSTOMER", listCust);

        response.set("custList", JSONArray.toJSON(listCust));

        return response;
    }
}
