package com.hirun.app.dao.employee;

import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;
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

    public List<EmployeeEntity> querySubordinatesByParentEmployee(String parentEmployeeId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PARENT_EMPLOYEE_ID", parentEmployeeId);

        StringBuilder sb = new StringBuilder();
        sb.append("select a.* from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.parent_employee_id = :PARENT_EMPLOYEE_ID ");
        sb.append("and b.employee_id = a.employee_id ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), parameter);
        return employees;
    }

    public List<EmployeeEntity> querySubordinatesInParentEmployee(String parentEmployeeIds) throws Exception{

        StringBuilder sb = new StringBuilder();
        sb.append("select a.* from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.parent_employee_id in ("+parentEmployeeIds+") ");
        sb.append("and b.employee_id = a.employee_id ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), new HashMap<String, String>());
        return employees;
    }

    @Deprecated
    public List<EmployeeEntity> querySubordinatesByParentEmployeeJobRole(String parentEmployeeId, String jobRole) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PARENT_EMPLOYEE_ID", parentEmployeeId);
        parameter.put("JOB_ROLE", jobRole);

        StringBuilder sb = new StringBuilder();
        sb.append("select a.* from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.parent_employee_id = :PARENT_EMPLOYEE_ID ");
        sb.append("and b.employee_id = a.employee_id ");
        sb.append("and b.job_role = :JOB_ROLE ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), parameter);
        return employees;
    }

    @Deprecated
    public List<EmployeeEntity> querySubordinatesInParentEmployeeJobRole(String parentEmployeeIds, String jobRole) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("JOB_ROLE", jobRole);

        StringBuilder sb = new StringBuilder();
        sb.append("select a.* from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.parent_employee_id in ("+parentEmployeeIds+") ");
        sb.append("and b.employee_id = a.employee_id ");
        sb.append("and b.job_role = :JOB_ROLE ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), parameter);
        return employees;
    }

    @Deprecated
    public List<EmployeeEntity> querySubordinatesInParentEmployeeAndJobRoles(String parentEmployeeIds, String jobRoles) throws Exception{

        StringBuilder sb = new StringBuilder();
        sb.append("select a.* from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.parent_employee_id in ("+parentEmployeeIds+") ");
        sb.append("and b.employee_id = a.employee_id ");
        sb.append("and b.job_role in ("+jobRoles+") ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), new HashMap<String, String>());
        return employees;
    }

    public RecordSet querySubordinatesEmployeeByParentEmployee(String parentEmployeeId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PARENT_EMPLOYEE_ID", parentEmployeeId);

        StringBuilder sb = new StringBuilder();
        sb.append("select a.*,b.job_role from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.parent_employee_id = :PARENT_EMPLOYEE_ID ");
        sb.append("and b.employee_id = a.employee_id ");

        RecordSet employees = this.queryBySql(sb.toString(), parameter);
        return employees;
    }

    public RecordSet querySubordinatesEmployeeInParentEmployee(String parentEmployeeIds) throws Exception{

        StringBuilder sb = new StringBuilder();
        sb.append("select a.*,b.job_role from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.parent_employee_id in ("+parentEmployeeIds+") ");
        sb.append("and b.employee_id = a.employee_id ");

        RecordSet employees = this.queryBySql(sb.toString(), new HashMap<String, String>());
        return employees;
    }


    public RecordSet queryContacts(String searchText) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("SEARCH_TEXT", searchText);
        StringBuilder sb = new StringBuilder();
        sb.append("select a.USER_ID,b.NAME,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, f.NAME parent_org_name ");
        sb.append("from ins_user a, ins_employee b, ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and b.name like concat('%',:SEARCH_TEXT,'%') ");
        sb.append("order by user_id ");
        return this.queryBySql(sb.toString(), parameter);
    }
}
