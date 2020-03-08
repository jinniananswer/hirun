package com.hirun.app.dao.employee;

import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

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
        sb.append("and a.status = '0' ");
        sb.append("and now() < c.end_date ");
        sb.append("and c.job_role in ("+jobRole+") ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), parameter);
        return employees;
    }

    public List<EmployeeEntity> queryEmployeeByParentOrgJobRoleAndMarket(String parentOrgId, String jobRole) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PARENT_ORG_ID", parentOrgId);
        parameter.put("JOB_ROLE", jobRole);

        StringBuilder sb = new StringBuilder();
        sb.append("select a.* from ins_employee a, ins_org b, ins_employee_job_role c ");
        sb.append("where b.parent_org_id = :PARENT_ORG_ID ");
        sb.append("and c.employee_id = a.employee_id ");
        sb.append("and c.org_id = b.org_id ");
        sb.append("and a.status = '0' ");
        sb.append("and now() < c.end_date ");
        sb.append("and b.name = '市场部' ");
        sb.append("and c.job_role in ("+jobRole+") ");

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
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), parameter);
        return employees;
    }

    public List<EmployeeEntity> querySubordinatesInParentEmployee(String parentEmployeeIds) throws Exception{

        StringBuilder sb = new StringBuilder();
        sb.append("select a.* from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.parent_employee_id in ("+parentEmployeeIds+") ");
        sb.append("and b.employee_id = a.employee_id ");
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");

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
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");

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
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");

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
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");

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
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");

        RecordSet employees = this.queryBySql(sb.toString(), parameter);
        return employees;
    }

    public RecordSet querySubordinatesEmployeeInParentEmployee(String parentEmployeeIds) throws Exception{

        StringBuilder sb = new StringBuilder();
        sb.append("select a.*,b.job_role,c.nature from ins_employee a, ins_employee_job_role b,ins_org c ");
        sb.append("where b.parent_employee_id in ("+parentEmployeeIds+") ");
        sb.append("and b.employee_id = a.employee_id ");
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");
        sb.append("and b.org_id=c.org_id ");


        RecordSet employees = this.queryBySql(sb.toString(), new HashMap<String, String>());
        return employees;
    }

    public RecordSet querySubordinatesEmployeeBypEmpIdAndVaild(String parentEmployeeIds,String vaild) throws Exception{

        StringBuilder sb = new StringBuilder();
        sb.append("select a.*,b.job_role from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.parent_employee_id in ("+parentEmployeeIds+") ");
        sb.append("and b.employee_id = a.employee_id ");



        if(StringUtils.equals("0",vaild)){
            sb.append("and a.status = '0' ");
            sb.append("and now() < b.end_date ");
        }
        if(StringUtils.equals("1",vaild)){
            sb.append("and a.status != '0' ");
            sb.append("and now() > b.end_date ");
        }
        RecordSet employees = this.queryBySql(sb.toString(), new HashMap<String,String>());
        return employees;
    }

    public RecordSet queryContacts(String searchText) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("SEARCH_TEXT", searchText);
        StringBuilder sb = new StringBuilder();
        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, f.NAME parent_org_name ");
        sb.append("from ins_user a, ins_employee b, ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and b.name like concat('%',:SEARCH_TEXT,'%') ");
        sb.append("order by user_id ");
        return this.queryBySql(sb.toString(), parameter);
    }

    public String queryParentEmployeeIdByEmployeeIdAndJobRoles(String employeeId, String jobRoles) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT b.PARENT_EMPLOYEE_ID FROM ins_employee a, ins_employee_job_role b ");
        sb.append("WHERE a.employee_id = b.employee_id ");
        sb.append("and b.job_role in ("+jobRoles+") ");
        sb.append("AND a.employee_id = :EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");
//        sb.append("and c.job_role in ('"+jobRole+"') ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("JOB_ROLES", jobRoles);
        parameter.put("EMPLOYEE_ID", employeeId);

        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        if(recordSet != null && recordSet.size() > 0) {
            return recordSet.get(0, "PARENT_EMPLOYEE_ID");
        }

        return null;
    }

    public int changeEmployeeParent(String originalParent, String newParent) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append(" update ins_employee_job_role set parent_employee_id = "+newParent);
        sb.append(" where parent_employee_id = "+originalParent);
        int num = this.executeUpdate(sb.toString(), null);
        return num;
    }

    public RecordSet queryCounselorsByOrgIds(String orgIds) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("select a.*, b.org_id ");
        sb.append("from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.employee_id = a.employee_id ");
        sb.append("and b.job_role in ('42','58') ");
        sb.append("and b.org_id in ("+orgIds+") ");
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");
        return this.queryBySql(sb.toString(), null);
    }

    public RecordSet queryCounselorsByOrgIds(String orgIds, String name) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("select a.*, b.org_id ");
        sb.append("from ins_employee a, ins_employee_job_role b ");
        sb.append("where b.employee_id = a.employee_id ");
        sb.append("and b.job_role in ('42','58') ");
        sb.append("and b.org_id in ("+orgIds+") ");
        sb.append("and a.name like concat('%',:NAME,'%') ");
        sb.append("and a.status = '0' ");
        sb.append("and now() < b.end_date ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("NAME", name);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryEmployees(String name, String sex, String city, String mobileNo, String identityNo, String orgId, String jobRole, String parentEmployeeId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();
        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, date_format(b.in_date,'%Y-%m-%d') in_date,d.JOB_ROLE,e.org_id, e.name org_name, f.NAME parent_org_name ");
        sb.append("from ins_user a, ins_employee b, ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' " );
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        if(StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }
        if(StringUtils.isNotBlank(city)){
            sb.append("and e.city = :CITY ");
            parameter.put("CITY", city);
        }
        if(StringUtils.isNotBlank(mobileNo)){
            sb.append("and a.mobile_no like concat('%',:MOBILE_NO,'%') ");
            parameter.put("MOBILE_NO", mobileNo);
        }
        if(StringUtils.isNotBlank(sex)){
            sb.append("and b.sex = :SEX ");
            parameter.put("SEX", sex);
        }
        if(StringUtils.isNotBlank(identityNo)){
            sb.append("and b.IDENTITY_NO like concat('%',:IDENTITY_NO,'%') ");
            parameter.put("IDENTITY_NO", identityNo);
        }
        if(StringUtils.isNotBlank(orgId)){
            sb.append("and d.org_id in ( "+orgId+") ");
        }
        if(StringUtils.isNotBlank(jobRole)){
            sb.append("and d.job_role = :JOB_ROLE ");
            parameter.put("JOB_ROLE", jobRole);
        }
        if(StringUtils.isNotBlank(parentEmployeeId)){
            sb.append("and d.parent_employee_id = :PARENT_EMPLOYEE_ID ");
            parameter.put("PARENT_EMPLOYEE_ID", parentEmployeeId);
        }
        return this.queryBySql(sb.toString(), parameter);
    }

    public Record queryEmployee(String employeeId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);

        StringBuilder sb = new StringBuilder();
        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, f.NAME parent_org_name ");
        sb.append("from ins_user a, ins_employee b, ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' " );
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and b.employee_id = :EMPLOYEE_ID ");

        RecordSet employees = this.queryBySql(sb.toString(), parameter);
        if(employees == null || employees.size() <= 0)
            return null;
        return employees.get(0);
    }

    public List<EmployeeEntity> queryUnEntryPlanEmployeeList(String planDate) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_DATE", planDate);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.name,a.employee_id FROM ins_employee a, ins_employee_job_role b ");
        sb.append("WHERE a.`EMPLOYEE_ID` = b.`EMPLOYEE_ID` ");
        sb.append("AND b.`JOB_ROLE` IN ('42','58') ");
        sb.append("AND NOW() BETWEEN b.`START_DATE` AND b.`END_DATE` ");
        sb.append("AND a.status = '0' ");
        sb.append("AND a.`EMPLOYEE_ID` NOT IN (SELECT c.`PLAN_EXECUTOR_ID` FROM ins_plan c WHERE c.`PLAN_DATE` = :PLAN_DATE) ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), parameter);
        return employees;
    }
    public List<EmployeeEntity> queryEmployeeListByPlanType(String planDate, String planType) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_DATE", planDate);
        parameter.put("PLAN_TYPE", planType);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.name,a.employee_id FROM ins_employee a, ins_employee_job_role b ");
        sb.append("WHERE a.`EMPLOYEE_ID` = b.`EMPLOYEE_ID` ");
        sb.append("AND b.`JOB_ROLE` IN ('42','58') ");
        sb.append("AND NOW() BETWEEN b.`START_DATE` AND b.`END_DATE` ");
        sb.append("AND a.status = '0' ");
        sb.append("AND EXISTS (SELECT 1 FROM ins_plan c WHERE c.`PLAN_DATE` = :PLAN_DATE AND c.plan_type = :PLAN_TYPE " +
                                " AND a.EMPLOYEE_ID = c.PLAN_EXECUTOR_ID)");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), parameter);
        return employees;
    }

    public EmployeeEntity getEmployeeByMobileNo(String mobileNo) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("MOBILE_NO", mobileNo);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.* FROM ins_employee a, ins_user b ");
        sb.append("WHERE a.user_id = b.user_id ");
        sb.append("AND a.`STATUS` = '0' ");
        sb.append("AND b.`STATUS` = '0' ");
        sb.append("AND b.`MOBILE_NO` = :MOBILE_NO ");

        List<EmployeeEntity> employees = this.queryBySql(EmployeeEntity.class, sb.toString(), parameter);
        if(ArrayTool.isEmpty(employees)) {
            return null;
        }
        return employees.get(0);
    }

    public RecordSet queryEmployeeByEnterpriseIdAndName(String enterpriseId, String name) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ENTERPRISE_ID", enterpriseId);
        parameter.put("NAME", name);

        StringBuilder sb = new StringBuilder();
        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, f.NAME parent_org_name ");
        sb.append("from ins_user a, ins_employee b, ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and b.name like concat('%',:NAME,'%') ");
        sb.append("and e.enterprise_id = :ENTERPRISE_ID ");
        sb.append("order by user_id ");

        return this.queryBySql(sb.toString(), parameter);
    }


    public RecordSet queryEmployeeJobRoleByOrgId(String orgId,String name) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ins_employee a, ins_employee_job_role b ");
        sb.append("WHERE a.`EMPLOYEE_ID` = b.`EMPLOYEE_ID` ");
        sb.append("AND b.`JOB_ROLE` IN ('46','118','103','0','45','69','119') ");
        sb.append("AND NOW() BETWEEN b.`START_DATE` AND b.`END_DATE` ");
        sb.append("AND a.status = '0' ");

        if(StringUtils.isNotBlank(orgId)){
            sb.append("and b.org_id in ( "+orgId+") ");
        }

        if (StringUtils.isNotBlank(name)) {
            sb.append("and a.NAME like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        RecordSet employeeJobRoleEntities = this.queryBySql(sb.toString(), parameter);
        return employeeJobRoleEntities;
    }

    public RecordSet queryEmployeeJobRoleByOrgId1(String orgId,String name,String employeeIDs) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ins_employee a, ins_employee_job_role b , ins_org e ");
        sb.append("WHERE a.`EMPLOYEE_ID` = b.`EMPLOYEE_ID` ");
        sb.append("AND b.`JOB_ROLE` IN ('46','118','103','45','69','119') ");
        sb.append("AND NOW() BETWEEN b.`START_DATE` AND b.`END_DATE` ");
        sb.append("AND a.status = '0' ");
        sb.append("AND b.org_id = e.org_id ");
        sb.append("AND e.name ='客户部' ");



        if(StringUtils.isNotBlank(orgId)){
            sb.append("and b.org_id in ( "+orgId+") ");
        }

        if (StringUtils.isNotBlank(name)) {
            sb.append("and a.NAME like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if(StringUtils.isNotBlank(employeeIDs)){
            sb.append("and a.EMPLOYEE_ID in ( "+employeeIDs+") ");
        }

        RecordSet employeeJobRoleEntities = this.queryBySql(sb.toString(), parameter);
        return employeeJobRoleEntities;
    }


    public RecordSet queryEmployeeByEmpIdsAndOrgId(String empIds,String orgId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ins_employee a, ins_employee_job_role b ");
        sb.append("WHERE a.`EMPLOYEE_ID` = b.`EMPLOYEE_ID` ");
        sb.append("AND b.`JOB_ROLE` IN ('46','118','103','0','45','69','119') ");
        sb.append("AND NOW() BETWEEN b.`START_DATE` AND b.`END_DATE` ");
        sb.append("AND a.status = '0' ");

        if(StringUtils.isNotBlank(orgId)){
            sb.append("and b.org_id in ( "+orgId+") ");
        }

        if(StringUtils.isNotBlank(empIds)){
            sb.append("and a.EMPLOYEE_ID in ( "+empIds+") ");
        }

        RecordSet employeeJobRoleEntities = this.queryBySql(sb.toString(), parameter);
        return employeeJobRoleEntities;
    }

    public RecordSet queryEmployeeByEmpIdsAndName(String employeeIds,String name) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();
        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, e.city, f.NAME parent_org_name ");
        sb.append("from ins_user a, ins_employee b , ");
        sb.append("ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' " );
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("AND d.`JOB_ROLE` IN ('46','118','103','45','69','119') ");
        sb.append("and e.name='客户部' ");

        if(StringUtils.isNotBlank(employeeIds)){
            sb.append("and b.EMPLOYEE_ID in ( "+employeeIds+") ");
        }

        if (StringUtils.isNotBlank(name)) {
            sb.append("and b.NAME like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        RecordSet employeeJobRoleEntities = this.queryBySql(sb.toString(), parameter);
        return employeeJobRoleEntities;
    }

    public EmployeeJobRoleEntity queryEmployeeJobRoleByEmpId(String employeeId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT b.* FROM ins_employee a, ins_employee_job_role b ");
        sb.append("WHERE a.`EMPLOYEE_ID` = b.`EMPLOYEE_ID` ");
        sb.append("AND NOW() BETWEEN b.`START_DATE` AND b.`END_DATE` ");
        sb.append("AND a.status = '0' ");
        sb.append("AND a.employee_id = :EMPLOYEE_ID");
        parameter.put("EMPLOYEE_ID", employeeId);


        List<EmployeeJobRoleEntity> employeeJobRoleEntities = this.query(EmployeeJobRoleEntity.class, "ins_employee_job_role", parameter);
        if(ArrayTool.isEmpty(employeeJobRoleEntities)){
            return null;
        }

        return employeeJobRoleEntities.get(0);
    }
}
