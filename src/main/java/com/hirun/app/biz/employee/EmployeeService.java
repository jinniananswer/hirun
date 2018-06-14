package com.hirun.app.biz.employee;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
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


    public ServiceResponse queryContacts(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        RecordSet recordSet = dao.queryContacts(request.getString("SEARCH_TEXT"));
        if(recordSet == null || recordSet.size() <= 0)
            return response;

        int size = recordSet.size();
        for(int i=0;i<size;i++){
            Record record = recordSet.get(i);
            record.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", record.get("JOB_ROLE")));
        }
        response.set("DATAS", ConvertTool.toJSONArray(recordSet));
        return response;
    }
}
