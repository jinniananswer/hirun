package com.hirun.app.dao.employee;

import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/23 22:56
 * @Description:
 */
@DatabaseName("ins")
public class EmployeeJobRoleDAO extends StrongObjectDAO {

    public EmployeeJobRoleDAO(String databaseName){
        super(databaseName);
    }

    public List<EmployeeJobRoleEntity> queryJobRoleByEmployeeId(String employeeId) throws SQLException{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_employee_job_role ");
        sb.append("where now() < end_date ");
        sb.append("and employee_id = :EMPLOYEE_ID ");
        return this.queryBySql(EmployeeJobRoleEntity.class, sb.toString(), parameter);
    }

}
