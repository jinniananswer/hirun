package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.annotation.DatabaseName;
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
@DatabaseName("ins")
public class CustDAO extends StrongObjectDAO {

    public CustDAO(String databaseName){
        super(databaseName);
    }

    public List<CustomerEntity> queryCustList(Map<String, String> parameter) throws SQLException {
        StringBuilder sql = new StringBuilder(400);
        sql.append(" SELECT * FROM INS_CUSTOMER A");
        sql.append(" WHERE 1=1 ");

        if(StringUtils.isNotBlank(parameter.get("CUST_STATUS"))) {
            sql.append(" AND A.CUST_STATUS in (").append(parameter.get("CUST_STATUS")).append(") ");
        } else {
            sql.append(" AND A.CUST_STATUS = '1' ");
        }

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
            sql.append(" AND B.`EXECUTOR_ID` = :HOUSE_COUNSELOR_ID)");
        }

        if(StringUtils.isNotBlank(parameter.get("HOUSE_COUNSELOR_IDS"))) {
            sql.append(" AND A.HOUSE_COUNSELOR_ID in (").append(parameter.get("HOUSE_COUNSELOR_IDS")).append(") ");
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

    public List<CustomerEntity> queryNewCustListByPlanDate(String houseCounselorId) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUSTOMER ");
        sql.append(" WHERE CUST_STATUS = '9' ");
        sql.append(" AND HOUSE_COUNSELOR_ID = :HOUSE_COUNSELOR_ID ");
//        sql.append(" AND (WX_NICK IS NOT NULL OR FIRST_PLAN_DATE = :FIRST_PLAN_DATE) ");
        sql.append(" AND WX_NICK IS NOT NULL ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSE_COUNSELOR_ID", houseCounselorId);
        List<CustomerEntity> list = this.queryBySql(CustomerEntity.class, sql.toString(), parameter);
        return list;
    }

    public List<CustomerEntity> queryNewVirtualCustListByPlanDate(String houseCounselorId, String firstPlanDate) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUSTOMER ");
        sql.append(" WHERE CUST_STATUS = '9' ");
        sql.append(" AND WX_NICK IS NULL ");
        sql.append(" AND HOUSE_COUNSELOR_ID = :HOUSE_COUNSELOR_ID ");
        sql.append(" AND FIRST_PLAN_DATE = :FIRST_PLAN_DATE ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("FIRST_PLAN_DATE", firstPlanDate);
        parameter.put("HOUSE_COUNSELOR_ID", houseCounselorId);
        List<CustomerEntity> list = this.queryBySql(CustomerEntity.class, sql.toString(), parameter);
        return list;
    }

    public CustomerEntity getCustomerEntityByIdentifyCode(String identifyCode) throws Exception {
        Map<String, String > parameter = new HashMap<String, String>();
        parameter.put("IDENTIFY_CODE", identifyCode);
        List<CustomerEntity> customerEntityList = this.query(CustomerEntity.class, "INS_CUSTOMER", parameter);
        if(ArrayTool.isNotEmpty(customerEntityList)) {
            return customerEntityList.get(0);
        } else {
            return null;
        }
    }

    public CustomerEntity getCustomerEntityByWxNick(String wxNick) throws Exception {
        Map<String, String > parameter = new HashMap<String, String>();
            parameter.put("WX_NICK", wxNick);
        List<CustomerEntity> customerEntityList = this.query(CustomerEntity.class, "INS_CUSTOMER", parameter);
        if(ArrayTool.isNotEmpty(customerEntityList)) {
            return customerEntityList.get(0);
        } else {
            return null;
        }
    }

    public List<CustomerEntity> getCustomersByEmployeeIdHouseId(String employeeId, String houseId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);
        parameter.put("HOUSE_ID", houseId);

        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_customer ");
        sb.append("where house_counselor_id = :EMPLOYEE_ID ");
        sb.append("and house_id = :HOUSE_ID ");
        sb.append("and (cust_status = 1 or (cust_status = 9 and wx_nick is null)) ");
        return this.queryBySql(CustomerEntity.class, sb.toString(), parameter);
    }
}
