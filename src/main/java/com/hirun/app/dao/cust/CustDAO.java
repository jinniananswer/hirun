package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.RecordSet;
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
            sql.append(" AND B.`EXECUTOR_ID` = :HOUSE_COUNSELOR_ID");
            sql.append(" AND B.`FINISH_TIME` IS NOT NULL)");
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

    public void deleteByIdentifyIsNullAndFirstPlanDate(String houseCounselorId, String firstPlanDate) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSE_COUNSELOR_ID", houseCounselorId);
        parameter.put("FIRST_PLAN_DATE", firstPlanDate);

        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE A FROM INS_CUSTOMER A");
        sql.append(" WHERE 1=1 ");
//        sql.append(" WHERE A.WX_NICK IS NULL ");
        sql.append(" AND A.HOUSE_COUNSELOR_ID = :HOUSE_COUNSELOR_ID ");
        sql.append(" AND A.FIRST_PLAN_DATE = :FIRST_PLAN_DATE ");
        sql.append(" AND A.IDENTIFY_CODE IS NULL ");
        this.executeUpdate(sql.toString(), parameter);
    }

    public List<CustomerEntity> queryNewCustListByPlanDate(String houseCounselorId) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUSTOMER ");
        sql.append(" WHERE CUST_STATUS = '9' ");
        sql.append(" AND HOUSE_COUNSELOR_ID = :HOUSE_COUNSELOR_ID ");
//        sql.append(" AND WX_NICK IS NOT NULL ");
        sql.append(" AND IDENTIFY_CODE IS NOT NULL ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSE_COUNSELOR_ID", houseCounselorId);
        List<CustomerEntity> list = this.queryBySql(CustomerEntity.class, sql.toString(), parameter);
        return list;
    }

    public List<CustomerEntity> queryNewVirtualCustListByPlanDate(String houseCounselorId, String firstPlanDate) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUSTOMER ");
        sql.append(" WHERE CUST_STATUS = '9' ");
//        sql.append(" AND WX_NICK IS NULL ");
        sql.append(" AND HOUSE_COUNSELOR_ID = :HOUSE_COUNSELOR_ID ");
        sql.append(" AND FIRST_PLAN_DATE = :FIRST_PLAN_DATE ");
        sql.append(" AND IDENTIFY_CODE IS NULL ");

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
        sb.append("and IDENTIFY_CODE is not null ");
        sb.append("and cust_status not in (8,9) ");
        return this.queryBySql(CustomerEntity.class, sb.toString(), parameter);
    }

    public RecordSet queryCustIds4Action4HouseCounselor(String houseCounselorIds, String startDate, String endDate, String finishAction, String custName,String wxNick) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("START_DATE", startDate);
        parameter.put("END_DATE", endDate);
        parameter.put("FINISH_ACTION", finishAction);
        parameter.put("CUST_NAME", custName);
        parameter.put("WX_NICK", wxNick);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT customer.CUST_ID,CUST_NAME, HOUSE_COUNSELOR_ID, employee.NAME ,customer.WX_NICK,customer.first_plan_date,mpo.opencount FROM ins_customer customer ");
        sb.append("LEFT JOIN ins_employee employee ON (employee.EMPLOYEE_ID = customer.HOUSE_COUNSELOR_ID and employee.STATUS = '0' ) ");

        //20201106
        sb.append("left join (select count(1) opencount,mp.open_id,mp.employee_id from ins_midprod_open mp group by mp.open_id,mp.employee_id ) mpo ON (customer.identify_code=mpo.open_id and customer.house_counselor_id=mpo.employee_id), ");

        sb.append("(SELECT cust_id,employee_id, GROUP_CONCAT(DISTINCT action_code) finish_actions FROM ins_cust_original_action where 1=1 ");

        if(StringUtils.isNotBlank(startDate)) {
            sb.append("AND finish_time >= :START_DATE ");
        }
        if(StringUtils.isNotBlank(endDate)) {
            sb.append("AND finish_time <= :END_DATE ");
        }

        sb.append("GROUP BY cust_id,employee_id) tmp_actions ,");
        sb.append("(SELECT cust_id, MIN(finish_time) finish_time FROM ins_cust_original_action " +
                //"WHERE action_code = 'JW' " +
                "GROUP BY cust_id) tmp_time ");

        sb.append("WHERE customer.CUST_ID = tmp_actions.CUST_ID ");
        sb.append("AND tmp_actions.CUST_ID = tmp_time.CUST_ID ");
        sb.append("AND customer.`CUST_STATUS` != '9' ");
        //2020/03/15新增
        sb.append("AND tmp_actions.EMPLOYEE_ID=customer.HOUSE_COUNSELOR_ID ");

        if(StringUtils.isNotBlank(houseCounselorIds)) {
            sb.append("AND customer.HOUSE_COUNSELOR_ID IN (").append(houseCounselorIds).append(") ");
        }
/*        if(StringUtils.isNotBlank(startDate)) {
            sb.append("AND tmp_time.finish_time >= :START_DATE ");
        }
        if(StringUtils.isNotBlank(endDate)) {
            sb.append("AND tmp_time.finish_time <= :END_DATE ");
        }*/
        if(StringUtils.isNotBlank(finishAction)) {
            sb.append("AND finish_actions like CONCAT('%', :FINISH_ACTION, '%') ");
        }
        if(StringUtils.isNotBlank(custName)) {
            sb.append("AND customer.cust_name like CONCAT('%', :CUST_NAME, '%') ");
        }
        //2020/03/15新增
        if(StringUtils.isNotBlank(wxNick)) {
            sb.append("AND customer.wx_nick like CONCAT('%', :WX_NICK, '%') ");
        }

        sb.append(" ORDER BY NAME ");
        sb.append(" LIMIT 300");

        return this.queryBySql(sb.toString(), parameter);
    }


    public RecordSet queryBluePrintByOpenId(String openid, String relEmployeeId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_blueprint_action ");
        sb.append("where OPEN_ID=:OPEN_ID  ");
        sb.append(" and action_code in ('XQLTY','XQLTY_A','XQLTY_B','XQLTY_C')");
        sb.append(" and rel_employee_id=:REL_EMPLOYEE_ID");
        sb.append(" order by create_time ");
        parameter.put("OPEN_ID", openid);
        parameter.put("REL_EMPLOYEE_ID", relEmployeeId);
        RecordSet recordSet = queryBySql(sb.toString(), parameter);

        if (recordSet.size() < 0) {
            return null;
        }
        return recordSet;
    }

}
