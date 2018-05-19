package com.hirun.app.bean.cust;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-05-18.
 */
public class CustBean {

    public static Map<String, String> isCreateOrBindNewCust(String wxNick, String identifyCode, String planDate, String executorId) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        CustomerEntity customerEntity = custDAO.getCustomerEntityByIdentifyCode(identifyCode);
        if(customerEntity != null) {
            result.put("DO_CUST", "no");
            result.put("CUST_ID", customerEntity.getCustId());
            return result;
        }

        customerEntity = custDAO.getCustomerEntityByWxNick(identifyCode);
        if(customerEntity != null) {
            result.put("DO_CUST", "no");
            result.put("CUST_ID", customerEntity.getCustId());
            return result;
        }

        //找不到的情况
        //当天看有没有新客户
        List<CustomerEntity> customerEntityList = custDAO.queryNewVirtualCustListByPlanDate(executorId, planDate);
        if(ArrayTool.isNotEmpty(customerEntityList)) {
            //取第一条
            customerEntity = customerEntityList.get(0);
            result.put("DO_CUST", "BIND_VIRTUAL");
            result.put("CUST_ID", customerEntity.getCustId());
            return result;
        }

        //虚拟新客户也没有的情况，则新增一条
        result.put("DO_CUST", "CREATE");
        result.put("CUST_ID", null);
        return result;
    }
}
