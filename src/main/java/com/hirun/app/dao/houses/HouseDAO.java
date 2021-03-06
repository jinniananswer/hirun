package com.hirun.app.dao.houses;

import com.hirun.pub.domain.entity.houses.HousesEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/30 1:42
 * @Description:
 */
@DatabaseName("ins")
public class HouseDAO extends StrongObjectDAO {

    public HouseDAO(String databaseName){
        super(databaseName);
    }

    public HousesEntity getHousesEntityById(String housesId) throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("HOUSES_ID", housesId);
        return this.queryByPk(HousesEntity.class, "INS_HOUSES", param);
    }

    public List<HousesEntity> queryHousesEntityListByName(String housesName) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT HOUSES_ID, NAME ,CITY ,NATURE ");
        sql.append(" FROM INS_HOUSES ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND now() < DESTROY_DATE ");
        sql.append(" AND NAME LIKE CONCAT('%', :NAME, '%') ");

        Map<String, String> param = new HashMap<String, String>();
        param.put("NAME", housesName);

        return this.queryBySql(HousesEntity.class, sql.toString(), param);
    }

    public List<HousesEntity> queryHousesByName(String housesName, boolean noScatter) throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT HOUSES_ID, NAME ");
        sql.append(" FROM INS_HOUSES ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND now() < DESTROY_DATE ");
        sql.append(" AND NAME = :NAME ");
        if (noScatter) {
            sql.append(" AND NATURE <> '3' ");
        }

        Map<String, String> param = new HashMap<String, String>();
        param.put("NAME", housesName);

        return this.queryBySql(HousesEntity.class, sql.toString(), param);
    }

    public List<HousesEntity> queryScatterHouses(String housesName, String city) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM INS_HOUSES ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND now() < DESTROY_DATE ");
        sql.append(" AND nature = '3' ");

        if (StringUtils.isNotBlank(city)) {
            sql.append(" AND CITY = :CITY ");
        }

        if (StringUtils.isNotBlank(housesName)) {
            sql.append(" AND NAME LIKE CONCAT('%', :NAME, '%') ");
        }

        Map<String, String> param = new HashMap<String, String>();
        param.put("NAME", housesName);
        param.put("CITY", city);

        return this.queryBySql(HousesEntity.class, sql.toString(), param);
    }

    public List<HousesEntity> queryMyScatterHouses(String housesName, String city, String employeeId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.* ");
        sql.append(" FROM INS_HOUSES a ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND now() < a.DESTROY_DATE ");
        sql.append(" AND a.nature = '3' ");

        if (StringUtils.isNotBlank(city)) {
            sql.append(" AND a.CITY = :CITY ");
        }

        if (StringUtils.isNotBlank(housesName)) {
            sql.append(" AND a.NAME LIKE CONCAT('%', :NAME, '%') ");
        }
        sql.append(" and exists (select 1 from ins_customer c where c.house_id = a.houses_id and house_counselor_id = :EMPLOYEE_ID and IDENTIFY_CODE is not null and cust_status not in (8,9) ) ");

        Map<String, String> param = new HashMap<String, String>();
        param.put("NAME", housesName);
        param.put("CITY", city);
        param.put("EMPLOYEE_ID", employeeId);

        return this.queryBySql(HousesEntity.class, sql.toString(), param);
    }
}
