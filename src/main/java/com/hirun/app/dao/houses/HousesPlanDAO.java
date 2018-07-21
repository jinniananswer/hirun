package com.hirun.app.dao.houses;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/5/1 0:26
 * @Description:
 */
@DatabaseName("ins")
public class HousesPlanDAO extends StrongObjectDAO {

    public HousesPlanDAO(String databaseName){
        super(databaseName);
    }

    public RecordSet queryHousesPlan(Map<String, String> parameter) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.houses_id, a.name, a.city, a.area, a.org_id, a.nature, date_format(a.check_date, '%Y-%m-%d') check_date, a.house_num, date_format(a.plan_in_date, '%Y-%m-%d') plan_in_date, date_format(a.actual_in_date, '%Y-%m-%d') actual_in_date,date_format(a.destroy_date, '%Y-%m-%d') destroy_date,a.plan_counselor_num, a.status, b.employee_id, b.tower_no,b.house_num employee_house_num,   c.name employee_name,c.sex, d.name org_name ");
        sb.append("FROM ins_houses a ");
        sb.append("LEFT JOIN ins_houses_plan b ON (b.houses_id = a.houses_id and now() < b.end_date) ");
        sb.append("LEFT JOIN ins_employee c ON (c.employee_id = b.employee_id and c.destroy_date is null) ");
        sb.append("LEFT JOIN ins_org d ON d.org_id = a.org_id ");
        sb.append("WHERE 1=1 ");
        sb.append("AND now() < a.destroy_date ");
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

        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryHousesByEmployeeId(String employeeId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.houses_id, a.name, a.city, a.area, a.org_id, a.nature, date_format(a.check_date, '%Y-%m-%d') check_date, a.house_num, date_format(a.plan_in_date, '%Y-%m-%d') plan_in_date,date_format(a.actual_in_date, '%Y-%m-%d') actual_in_date, date_format(a.destroy_date, '%Y-%m-%d') destroy_date, a.status ");
        sb.append(" FROM INS_HOUSES a, INS_HOUSES_PLAN b ");
        sb.append(" WHERE b.houses_id = a.houses_id ");
        sb.append(" and (now() < a.destroy_date) ");
        sb.append(" and (now() < b.end_date) ");
        sb.append(" and a.status = '1' ");
        sb.append(" AND b.employee_id = :EMPLOYEE_ID ");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryHouses() throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT DISTINCT a.houses_id, a.name ");
        sb.append(" FROM INS_HOUSES a, INS_HOUSES_PLAN b ");
        sb.append(" WHERE b.houses_id = a.houses_id ");
        Map<String, String> parameter = new HashMap<String, String>();
        return this.queryBySql(sb.toString(), parameter);
    }

    public int getAllHousesNum() throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT count(1) houses_num ");
        sb.append(" FROM INS_HOUSES a ");
        RecordSet rst = this.queryBySql(sb.toString(), null);
        return rst.getInt(0, "HOUSES_NUM");
    }

    public RecordSet getGroupCountByHouseNature() throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT nature, count(1) houses_num ");
        sb.append(" FROM INS_HOUSES a ");
        sb.append(" GROUP BY nature ");
        return this.queryBySql(sb.toString(), null);
    }

    public int getCompanyHousesNum(String orgId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT count(1) houses_num ");
        sb.append(" FROM INS_HOUSES a ");
        sb.append(" WHERE a.org_id in ( ");
        sb.append(" select b.org_id from ins_org b ");
        sb.append(" where b.parent_org_id = :ORG_ID) ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", orgId);
        RecordSet rst = this.queryBySql(sb.toString(), parameter);
        return rst.getInt(0, "HOUSES_NUM");
    }

    public RecordSet getGroupCountByCompanyHouseNature(String orgId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT nature, count(1) houses_num ");
        sb.append(" FROM INS_HOUSES a ");
        sb.append(" WHERE a.org_id in ( ");
        sb.append(" select b.org_id from ins_org b ");
        sb.append(" where b.parent_org_id = :ORG_ID) ");
        sb.append(" GROUP BY nature ");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", orgId);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet getPlanCounselorNumByShop(String orgId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.ORG_ID,b.name, sum(a.PLAN_COUNSELOR_NUM) plan_num ");
        sb.append(" from ins_houses a, ins_org b,ins_org c ");
        sb.append(" where c.org_id = :ORG_ID ");
        sb.append(" and b.ORG_ID = a.ORG_ID ");
        sb.append(" and b.PARENT_ORG_ID = c.ORG_ID ");
        sb.append(" group by a.ORG_ID,b.name ");
        sb.append(" order by a.ORG_ID asc ");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", orgId);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet getActualCounselorNumByShop(String orgId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.ORG_ID,b.name, count(1) actual_num ");
        sb.append(" from ins_houses_plan a, ins_org b,ins_org c ");
        sb.append(" where c.ORG_ID = :ORG_ID ");
        sb.append(" and b.ORG_ID = a.ORG_ID ");
        sb.append(" and b.PARENT_ORG_ID = c.ORG_ID ");
        sb.append(" group by a.ORG_ID,b.name ");
        sb.append(" order by a.ORG_ID asc ");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", orgId);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet getPlanCounselorNumByCompany() throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" select c.ORG_ID,c.name, sum(a.PLAN_COUNSELOR_NUM) plan_num ");
        sb.append(" from ins_houses a, ins_org b,ins_org c ");
        sb.append(" where c.type = '2' ");
        sb.append(" and c.org_id = 17 ");
        sb.append(" and b.ORG_ID = a.ORG_ID ");
        sb.append(" and b.PARENT_ORG_ID = c.ORG_ID ");
        sb.append(" group by c.ORG_ID,c.name ");
        sb.append(" order by c.ORG_ID asc ");

        return this.queryBySql(sb.toString(), null);
    }

    public RecordSet getActualCounselorNumByCompany() throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" select c.ORG_ID,c.name, count(1) actual_num ");
        sb.append(" from ins_houses_plan a, ins_org b,ins_org c ");
        sb.append(" where c.type = '2' ");
        sb.append(" and c.org_id = 17 ");
        sb.append(" and b.ORG_ID = a.ORG_ID ");
        sb.append(" and b.PARENT_ORG_ID = c.ORG_ID ");
        sb.append(" group by c.ORG_ID,c.name ");
        sb.append(" order by c.ORG_ID asc ");

        return this.queryBySql(sb.toString(), null);
    }

    public RecordSet getPlanCounselorNumByShopAndNature(String orgId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.ORG_ID,b.name, a.nature, sum(a.PLAN_COUNSELOR_NUM) plan_num ");
        sb.append(" from ins_houses a, ins_org b,ins_org c ");
        sb.append(" where c.org_id = :ORG_ID ");
        sb.append(" and b.ORG_ID = a.ORG_ID ");
        sb.append(" and b.PARENT_ORG_ID = c.ORG_ID ");
        sb.append(" and a.nature in ('0','1') ");
        sb.append(" group by a.ORG_ID,b.name,a.nature ");
        sb.append(" order by a.ORG_ID asc ");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", orgId);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet getActualCounselorNumByShopAndNature(String orgId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.ORG_ID,b.name, d.nature, count(1) actual_num ");
        sb.append(" from ins_houses_plan a, ins_org b,ins_org c, ins_houses d ");
        sb.append(" where c.ORG_ID = :ORG_ID ");
        sb.append(" and b.ORG_ID = a.ORG_ID ");
        sb.append(" and b.PARENT_ORG_ID = c.ORG_ID ");
        sb.append(" and d.houses_id = a.houses_id ");
        sb.append(" and d.nature in ('0','1') ");
        sb.append(" group by a.ORG_ID,b.name,d.nature ");
        sb.append(" order by a.ORG_ID asc ");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", orgId);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet getPlanCounselorNumByCompanyAndNature() throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" select c.ORG_ID,c.name, a.nature, sum(a.PLAN_COUNSELOR_NUM) plan_num ");
        sb.append(" from ins_houses a, ins_org b,ins_org c ");
        sb.append(" where c.type = '2' ");
        sb.append(" and c.org_id = 17 ");
        sb.append(" and b.ORG_ID = a.ORG_ID ");
        sb.append(" and b.PARENT_ORG_ID = c.ORG_ID ");
        sb.append(" and a.nature in ('0','1') ");
        sb.append(" group by c.ORG_ID,c.name,a.nature ");
        sb.append(" order by c.ORG_ID asc ");

        return this.queryBySql(sb.toString(), null);
    }

    public RecordSet getActualCounselorNumByCompanyAndNature() throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" select c.ORG_ID,c.name, d.nature, count(1) actual_num ");
        sb.append(" from ins_houses_plan a, ins_org b,ins_org c,ins_houses d ");
        sb.append(" where c.type = '2' ");
        sb.append(" and c.org_id = 17 ");
        sb.append(" and b.ORG_ID = a.ORG_ID ");
        sb.append(" and b.PARENT_ORG_ID = c.ORG_ID ");
        sb.append(" and d.houses_id = a.houses_id ");
        sb.append(" and d.nature in ('0','1') ");
        sb.append(" group by c.ORG_ID,c.name,d.nature ");
        sb.append(" order by c.ORG_ID asc ");

        return this.queryBySql(sb.toString(), null);
    }

    public Record queryHousesByEmployeeIdHouseId(String employeeId, String houseId) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.houses_id, a.name, a.city, a.area, a.org_id, a.nature, date_format(a.check_date, '%Y-%m-%d') check_date, a.house_num, date_format(a.plan_in_date, '%Y-%m-%d') plan_in_date,date_format(a.actual_in_date, '%Y-%m-%d') actual_in_date, date_format(a.destroy_date, '%Y-%m-%d') destroy_date, a.status, b.tower_no, b.house_num employee_house_num, c.name org_name ");
        sb.append(" FROM INS_HOUSES a, INS_HOUSES_PLAN b, ins_org c ");
        sb.append(" WHERE b.houses_id = a.houses_id ");
        sb.append(" and c.org_id = a.org_id ");
        sb.append(" AND b.employee_id = :EMPLOYEE_ID ");
        sb.append(" and b.houses_id = :HOUSE_ID ");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);
        parameter.put("HOUSE_ID", houseId);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);

        if(recordSet == null || recordSet.size() <= 0)
            return null;

        return recordSet.get(0);
    }
}
