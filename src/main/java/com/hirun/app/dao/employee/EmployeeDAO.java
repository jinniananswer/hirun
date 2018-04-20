package com.hirun.app.dao.employee;

import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/19 20:24
 * @Description:
 */
public class EmployeeDAO extends StrongObjectDAO{

    public EmployeeDAO(String databaseName){
        super(databaseName);
    }

    public EmployeeEntity queryEmployeeByUserId(String userId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USER_ID", userId);

        List<EmployeeEntity> employees = this.query(EmployeeEntity.class, "ins_employee", parameter);
        if(ArrayTool.isEmpty(employees)){
            return null;
        }

        return employees.get(0);
    }
}
