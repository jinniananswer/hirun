package com.hirun.app.biz.cust;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.hirun.pub.util.MapUtil;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.security.Encryptor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/17 21:11
 * @Description:
 */
public class CustService extends GenericService{

    public ServiceResponse addCust(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject custInfo = request.getBody().getData();

        CustDAO dao = new CustDAO("ins");
        Map<String, String> parameter = MapUtil.jsonToMap(custInfo);
        int i = dao.insert("INS_CUSTOMER", parameter);

        return response;
    }
}
