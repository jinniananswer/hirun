package com.hirun.app.dao.employee;

import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.most.core.app.database.annotation.DatabaseName;
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
@DatabaseName("ins")
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

    public EmployeeEntity queryEmployeeByEmployeeId(String employeeId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);

        List<EmployeeEntity> employees = this.query(EmployeeEntity.class, "ins_employee", parameter);
        if(ArrayTool.isEmpty(employees)){
            return null;
        }

        return employees.get(0);
    }

    public List<EmployeeEntity> queryEmployeeByParentOrgJobRole(String parentOrgId, String jobRole) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PARENT_ORG_ID", parentOrgId);
        parameter.put("JOB_ROLE", jobRole);

        StringBuilder sb = new StringBuilder();
        sb.append("select a.* from ins_employee a, ins_org b, ins_employee_job_role c ");
        sb.append("where b.parent_org_id = :PARENT_ORG_ID ");
        sb.append("and c.employee_id = a.employee_id ");
        sb.append("and c.org_id = b.org_id ");
        sb.append("and c.job_role = :JOB_ROLE ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), parameter);
        return employees;
    }
}
