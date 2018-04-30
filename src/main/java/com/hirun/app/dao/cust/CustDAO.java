package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 9:56
 * @Description:
 */
public class CustDAO extends StrongObjectDAO {

    public CustDAO(String databaseName){
        super(databaseName);
    }

    public List<CustomerEntity> queryCustList(Map<String, String> parameter) throws SQLException {
        StringBuilder sql = new StringBuilder(400);
        sql.append(" SELECT * FROM INS_CUSTOMER A");
        sql.append(" WHERE 1=1 ");
        if(parameter.containsKey("CUST_NAME")) {
            sql.append(" AND A.CUST_NAME LIKE CONCAT('%', :CUST_NAME, '%') ");
        }
        if(parameter.containsKey("MOBILE_NO")) {
            sql.append(" AND A.MOBILE_NO LIKE CONCAT('%', :MOBILE_NO, '%') ");
        }
        if(parameter.containsKey("WX_NICK")) {
            sql.append(" AND A.WX_NICK LIKE CONCAT('%', :WX_NICK, '%') ");
        }
        if(parameter.containsKey("HOUSE_ID")) {
            sql.append(" AND A.HOUSE_ID = :HOUSE_ID ");
        }
        if(parameter.containsKey("LAST_ACTION")) {
            sql.append(" AND A.LAST_ACTION = :LAST_ACTION ");
        }
        if(parameter.containsKey("HOUSE_COUNSELOR_ID")) {
            sql.append(" AND A.HOUSE_COUNSELOR_ID = :HOUSE_COUNSELOR_ID ");
        }
        //TODO 这里以后不要去查表，考虑将客户触发过的动作放到缓存里,当然还需要一个缓存的互备表作为容灾的
        if(parameter.containsKey("UNEXECUTED_ACTION")) {
            sql.append(" AND NOT EXISTS(SELECT * FROM INS_CUST_ACTION B " +
                    " WHERE A.`CUST_ID` = B.`CUST_ID`" +
                    " AND B.`ACTION_CODE` = :UNEXECUTED_ACTION)");
        }

        List<CustomerEntity> customerList = this.queryBySql(CustomerEntity.class, sql.toString(), parameter);

        return customerList;
    }

    public CustomerEntity getCustById(String custId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);

        return this.queryByPk(CustomerEntity.class, "INS_CUSTOMER", parameter);
    }
}
