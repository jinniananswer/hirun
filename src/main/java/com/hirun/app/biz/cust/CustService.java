package com.hirun.app.biz.cust;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.houses.HousesBean;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.enums.cust.CustStatus;
import com.hirun.pub.domain.enums.cust.Sex;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Body;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
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

        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        String now = TimeTool.now();

        CustDAO dao = new CustDAO("ins");
        Map<String, String> cust = ConvertTool.toMap(custInfo);
        cust.put("CUST_STATUS", "1");
        cust.put("CREATE_USER_ID", userId);
        cust.put("CREATE_DATE", now);
        cust.put("UPDATE_USER_ID", userId);
        cust.put("UPDATE_TIME", now);
        long custId = dao.insertAutoIncrement("INS_CUSTOMER", cust);
        response.set("CUST_ID", custId);

        return response;
    }

    public ServiceResponse editCust(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject custInfo = request.getBody().getData();

        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();
        String sysdate = TimeTool.now();

        CustDAO dao = new CustDAO("ins");
        Map<String, String> parameter = ConvertTool.toMap(custInfo);
        parameter.put("UPDATE_USER_ID",userId);
        parameter.put("UPDATE_TIME",sysdate);
        parameter.put("CUST_STATUS", "1");
        int i = dao.save("INS_CUSTOMER", new String[] {"CUST_ID"}, parameter);

        return response;
    }

    public ServiceResponse queryCustList(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        Map<String, String> parameter = ConvertTool.toMap(requestData);

        CustDAO dao = new CustDAO("ins");
        List<CustomerEntity> customerList = dao.queryCustList(parameter);

        response.set("CUSTOMERLIST", ConvertTool.toJSONArray(customerList));

        return response;
    }

    public ServiceResponse addCustByNum(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        int newCustNew = requestData.getInteger("NEW_CUSTNUM");
        String houseCounselorId = requestData.getString("HOUSE_COUNSELOR_ID");
        String firstPlanDate = requestData.getString("FIRST_PLAN_DATE");

        //从SESSION里取userId
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        String now = TimeTool.now();//获取系统时间
        CustDAO dao = new CustDAO("ins");

        //删除今天添加的新客户
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("FIRST_PLAN_DATE", firstPlanDate);
        dao.deleteByWxNickisNullAndFirstPlanDate(houseCounselorId, firstPlanDate);

        List<Map<String, String>> listCust = new ArrayList<Map<String, String>>();
        for(int i = 0; i < newCustNew; i++) {
            Map<String, String> cust = new HashMap<String, String>();
            cust.put("HOUSE_COUNSELOR_ID", houseCounselorId);
            cust.put("CUST_STATUS", "9");
            cust.put("CUST_NAME", "新客户" + (i+1));
            cust.put("CREATE_USER_ID", userId);
            cust.put("CREATE_DATE", now);
            cust.put("UPDATE_USER_ID", userId);
            cust.put("UPDATE_TIME", now);
            cust.put("FIRST_PLAN_DATE", firstPlanDate);
            long custId = dao.insertAutoIncrement("INS_CUSTOMER", cust);
            cust.put("CUST_ID", String.valueOf(custId));
            listCust.add(cust);
        }

        response.set("custList", JSONArray.toJSON(listCust));

        return response;
    }

    public ServiceResponse getCustById(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String custId = requestData.getString("CUST_ID");

        CustDAO custDAO = new CustDAO("ins");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);
        CustomerEntity customerEntity = custDAO.queryByPk(CustomerEntity.class, "INS_CUSTOMER", parameter);
        if(customerEntity == null) {
            response.setError("-1", "客户资料不存在");
            return response;
        }

        JSONObject jsonCust = customerEntity.toJson();
        //根据编码查中文描述
        jsonCust.put("SEX_DESC", Sex.getNameByValue(jsonCust.getString("SEX")));
        if(StringUtils.isNotBlank(jsonCust.getString("HOUSE_ID"))) {
            jsonCust.put("HOUSE_DESC", HousesBean.getHousesEntityById(jsonCust.getString("HOUSE_ID")).getName());
        }
        if(StringUtils.isNotBlank(jsonCust.getString("HOUSE_MODE"))) {
            jsonCust.put("HOUSE_MODE_DESC", StaticDataTool.getCodeName("HOUSE_MODE", jsonCust.getString("HOUSE_MODE")));
        }

        response.setBody(new Body(jsonCust));

        return response;
    }

    public ServiceResponse deleteCustById(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String custId = requestData.getString("CUST_ID");

        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        Map<String, String> dbParam = new HashMap<String, String>();
        dbParam.put("CUST_ID", custId);
        dbParam.put("CUST_STATUS", CustStatus.del.getValue());
        custDAO.save("INS_CUSTOMER", dbParam);

        return response;
    }
}
