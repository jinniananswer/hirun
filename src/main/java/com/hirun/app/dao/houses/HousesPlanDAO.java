package com.hirun.app.dao.houses;

import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/5/1 0:26
 * @Description:
 */
public class HousesPlanDAO extends StrongObjectDAO {

    public HousesPlanDAO(String databaseName){
        super(databaseName);
    }

    public RecordSet queryHousesPlan(Map<String, String> parameter) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.houses_id, a.name, a.city, a.area, a.org_id, a.nature, date_format(a.check_date, '%Y-%m-%d') check_date, a.house_num, date_format(a.plan_in_date, '%Y-%m-%d') plan_in_date, date_format(a.destroy_date, '%Y-%m-%d') destroy_date,a.plan_counselor_num, a.status, b.employee_id, b.tower_no,b.house_num employee_house_num,   c.name employee_name, d.name org_name ");
        sb.append("FROM ins_houses a ");
        sb.append("LEFT JOIN ins_houses_plan b ON b.houses_id = a.houses_id ");
        sb.append("LEFT JOIN ins_employee c ON c.employee_id = b.employee_id ");
        sb.append("LEFT JOIN ins_org d ON d.org_id = a.org_id ");
        sb.append("WHERE 1=1 ");
        if(StringUtils.isNotBlank(parameter.get("HOUSES_ID")))
            sb.append("AND a.houses_id = :HOUSES_ID ");
        if(StringUtils.isNotBlank(parameter.get("NAME")))
            sb.append("AND a.name like CONCAT('%',:NAME,'%') ");
        if(StringUtils.isNotBlank(parameter.get("NATURE")))
            sb.append("AND a.nature = :NATURE ");
        if(StringUtils.isNotBlank(parameter.get("CITY")))
            sb.append("AND a.city = :CITY ");
        if(StringUtils.isNotBlank(parameter.get("AREA")))
            sb.append("AND a.area = :AREA ");
        if(StringUtils.isNotBlank(parameter.get("SHOP")))
            sb.append("AND a.org_id = :SHOP ");
        if(StringUtils.isNotBlank(parameter.get("CHECK_DATE")))
            sb.append("AND a.check_date > :CHECK_DATE ");
        if(StringUtils.isNotBlank(parameter.get("HOUSE_NUM")))
            sb.append("AND a.house_num > :HOUSE_NUM ");
        if(StringUtils.isNotBlank(parameter.get("PLAN_COUNSELOR_NUM")))
            sb.append("AND a.PLAN_COUNSELOR_NUM > :PLAN_COUNSELOR_NUM ");
        if(StringUtils.isNotBlank(parameter.get("PLAN_IN_DATE")))
            sb.append("AND a.plan_in_date > :PLAN_IN_DATE ");
        if(StringUtils.isNotBlank(parameter.get("STATUS")))
            sb.append("AND a.status = :STATUS ");
        if(StringUtils.isNotBlank(parameter.get("EMPLOYEE_ID"))){
            sb.append("AND a.houses_id in (select e.houses_id from ins_houses_plan e, ins_employee f where f.employee_id = e.employee_id and f.employee_id in ("+parameter.get("EMPLOYEE_ID")+"))");
        }

        return this.queryBySql(sb.toString(), parameter, 0, 10);
    }

    public RecordSet queryHousesByEmployeeId(String employeeId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.houses_id, a.name, a.city, a.area, a.org_id, a.nature, date_format(a.check_date, '%Y-%m-%d') check_date, a.house_num, date_format(a.plan_in_date, '%Y-%m-%d') plan_in_date, date_format(a.destroy_date, '%Y-%m-%d') destroy_date, a.status ");
        sb.append(" FROM INS_HOUSES a, INS_HOUSES_PLAN b ");
        sb.append(" WHERE b.houses_id = a.houses_id ");
        sb.append(" AND b.employee_id = :EMPLOYEE_ID ");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);
        return this.queryBySql(sb.toString(), parameter);
    }
}
