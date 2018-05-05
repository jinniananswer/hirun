package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

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
        sql.append(" AND A.CUST_STATUS = '1' ");
        if(StringUtils.isNotBlank(parameter.get("CUST_NAME"))) {
            sql.append(" AND A.CUST_NAME LIKE CONCAT('%', :CUST_NAME, '%') ");
        }
        if(StringUtils.isNotBlank(parameter.get("MOBILE_NO"))) {
            sql.append(" AND A.MOBILE_NO LIKE CONCAT('%', :MOBILE_NO, '%') ");
        }
        if(StringUtils.isNotBlank(parameter.get("WX_NICK"))) {
            sql.append(" AND A.WX_NICK LIKE CONCAT('%', :WX_NICK, '%') ");
        }
        if(StringUtils.isNotBlank(parameter.get("HOUSE_ID"))) {
            sql.append(" AND A.HOUSE_ID = :HOUSE_ID ");
        }
//        if(StringUtils.isNotBlank(parameter.get("LAST_ACTION"))) {
//            sql.append(" AND A.LAST_ACTION = :LAST_ACTION ");
//        }
        if(StringUtils.isNotBlank(parameter.get("HOUSE_COUNSELOR_ID"))) {
            sql.append(" AND A.HOUSE_COUNSELOR_ID = :HOUSE_COUNSELOR_ID ");
        }
        //TODO 这里以后不要去查表，考虑将客户触发过的动作放到缓存里,当然还需要一个缓存的互备表作为容灾的
        if(StringUtils.isNotBlank(parameter.get("UNEXECUTED_ACTION"))) {
            sql.append(" AND NOT EXISTS(SELECT * FROM INS_CUST_ACTION B " +
                    " WHERE A.`CUST_ID` = B.`CUST_ID`" +
                    " AND B.`ACTION_CODE` = :UNEXECUTED_ACTION ");
            sql.append(" AND B.`FINISH_TIME` IS NOT NULL)");
        }

        List<CustomerEntity> customerList = this.queryBySql(CustomerEntity.class, sql.toString(), parameter);

        return customerList;
    }

    public CustomerEntity getCustById(String custId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);

        return this.queryByPk(CustomerEntity.class, "INS_CUSTOMER", parameter);
    }

    public void deleteByWxNickisNullAndFirstPlanDate(String houseCounselorId, String firstPlanDate) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSE_COUNSELOR_ID", houseCounselorId);
        parameter.put("FIRST_PLAN_DATE", firstPlanDate);

        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE A FROM INS_CUSTOMER A");
        sql.append(" WHERE A.WX_NICK IS NULL ");
        sql.append(" AND A.HOUSE_COUNSELOR_ID = :HOUSE_COUNSELOR_ID ");
        sql.append(" AND A.FIRST_PLAN_DATE = :FIRST_PLAN_DATE ");
        this.executeUpdate(sql.toString(), parameter);
    }

    public List<CustomerEntity> queryNewCustListByPlanDate(String houseCounselorId, String firstPlanDate) throws Exception {
        /*
        *   SELECT * FROM INS_CUSTOMER
  WHERE CUST_STATUS = '9'
  AND HOUSE_COUNSELOR_ID = 123
  AND (WX_NICK IS NOT NULL OR FIRST_PLAN_DATE = '2018-05-04');
        * */
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUSTOMER ");
        sql.append(" WHERE CUST_STATUS = '9' ");
        sql.append(" AND HOUSE_COUNSELOR_ID = :HOUSE_COUNSELOR_ID ");
        sql.append(" AND (WX_NICK IS NOT NULL OR FIRST_PLAN_DATE = :FIRST_PLAN_DATE) ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("FIRST_PLAN_DATE", firstPlanDate);
        parameter.put("HOUSE_COUNSELOR_ID", houseCounselorId);
        List<CustomerEntity> list = this.queryBySql(CustomerEntity.class, sql.toString(), parameter);
        return list;
    }
}
