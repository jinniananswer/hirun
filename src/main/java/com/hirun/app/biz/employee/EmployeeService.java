package com.hirun.app.biz.employee;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.transform.ConvertTool;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by pc on 2018-06-12.
 */
public class EmployeeService extends GenericService {

    public ServiceResponse getAllSubordinatesCounselors(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String employeeIds = requestData.getString("EMPLOYEE_IDS");
        String columns = requestData.getString("COLUMNS");

        List<EmployeeEntity> list = EmployeeBean.getAllSubordinatesCounselors(employeeIds);
        if(StringUtils.isBlank(columns)) {
            response.set("EMPLOYEE_LIST", ConvertTool.toJSONArray(list));
        } else {
            response.set("EMPLOYEE_LIST", ConvertTool.toJSONArray(list, columns.split(",")));
        }

        return response;
    }
}
