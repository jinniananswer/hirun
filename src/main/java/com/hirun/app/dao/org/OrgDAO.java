package com.hirun.app.dao.org;

import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/23 22:21
 * @Description:
 */
public class OrgDAO extends StrongObjectDAO {

    public OrgDAO(String databaseName){
        super(databaseName);
    }

    public OrgEntity queryOrgById(String orgId) throws SQLException{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", orgId);
        return this.queryByPk(OrgEntity.class, "ins_org", parameter);
    }

    public List<OrgEntity> queryOrgByCity(String city) throws SQLException{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CITY", city);
        return this.query(OrgEntity.class, "ins_org", parameter);
    }

    public List<OrgEntity> queryOrgByCityAndType(String city, String type) throws SQLException{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CITY", city);
        parameter.put("TYPE", type);
        return this.query(OrgEntity.class, "ins_org", parameter);
    }
}
