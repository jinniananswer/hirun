package com.hirun.app.dao.houses;

import com.hirun.pub.domain.entity.houses.HousesEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;

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
        sql.append(" SELECT HOUSES_ID, NAME ");
        sql.append(" FROM INS_HOUSES ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND now() < DESTROY_DATE ");
        sql.append(" AND NAME LIKE CONCAT('%', :NAME, '%') ");

        Map<String, String> param = new HashMap<String, String>();
        param.put("NAME", housesName);

        return this.queryBySql(HousesEntity.class, sql.toString(), param);
    }

    public List<HousesEntity> queryHousesByName(String housesName) throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT HOUSES_ID, NAME ");
        sql.append(" FROM INS_HOUSES ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND now() < DESTROY_DATE ");
        sql.append(" AND NAME = :NAME ");

        Map<String, String> param = new HashMap<String, String>();
        param.put("NAME", housesName);

        return this.queryBySql(HousesEntity.class, sql.toString(), param);
    }
}
