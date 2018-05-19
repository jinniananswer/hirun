package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 9:56
 * @Description:
 */
@DatabaseName("ins")
public class CustActionDAO extends StrongObjectDAO {

    public CustActionDAO(String databaseName){
        super(databaseName);
    }

    /**
     * 获取某一个时间段，员工动作的完成情况
     * @param executorId
     * @param actionCode
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public List<CustActionEntity> queryFinishActionList(String executorId, String actionCode, String startTime, String endTime, String houseCounSelorId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSE_COUNSELOR_ID", houseCounSelorId);

        StringBuilder sql = new StringBuilder(200);
        List<CustActionEntity> list = this.queryBySql(CustActionEntity.class, sql.toString(), parameter);

        return list;
    }

    /**
     * 获取某一个时间段，员工动作的完成数
     * @param executorId
     * @param actionCode
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public int queryFinishActionCount(String executorId, String actionCode, String startTime, String endTime) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EXECUTOR_ID", executorId);
        parameter.put("ACTION_CODE", actionCode);
        parameter.put("START_TIME", startTime);
        parameter.put("END_TIME", endTime);

        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT COUNT(*) NUM FROM INS_CUST_ACTION ");
        sql.append(" WHERE EXECUTOR_ID = :EXECUTOR_ID ");
        sql.append(" AND ACTION_CODE = :ACTION_CODE ");
        sql.append(" AND FINISH_TIME >= :START_TIME ");
        sql.append(" AND FINISH_TIME < :END_TIME ");
        RecordSet recordSet = this.queryBySql(sql.toString(), parameter);
        int num = recordSet.getInt(0, "NUM");

        return num;
    }

    public int queryFinishActionCountByEndTime(String executorId, String actionCode, String endTime) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EXECUTOR_ID", executorId);
        parameter.put("ACTION_CODE", actionCode);
        parameter.put("END_TIME", endTime);

        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT COUNT(*) NUM FROM INS_CUST_ACTION ");
        sql.append(" WHERE EXECUTOR_ID = :EXECUTOR_ID ");
        sql.append(" AND ACTION_CODE = :ACTION_CODE ");
        sql.append(" AND FINISH_TIME >= :START_TIME ");
        sql.append(" AND FINISH_TIME < :END_TIME ");
        RecordSet recordSet = this.queryBySql(sql.toString(), parameter);
        int num = recordSet.getInt(0, "NUM");

        return num;
    }

    public List<CustActionEntity> queryCustActionByEidAndPlanDate(String executorId, String planDate) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUST_ACTION ");
        sql.append(" WHERE EXECUTOR_ID = :EXECUTOR_ID ");
        sql.append(" AND (DATE_FORMAT(FINISH_TIME, '%Y-%m-%d') = :PLAN_DATE OR PLAN_DEAL_DATE = :PLAN_DATE) ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EXECUTOR_ID", executorId);
        parameter.put("PLAN_DATE", planDate);
        List<CustActionEntity> list = this.queryBySql(CustActionEntity.class, sql.toString(), parameter);
        return list;
    }

    public List<CustActionEntity> queryCustActionByPlanId(String planId) throws Exception {
//        StringBuilder sql = new StringBuilder(200);
//        sql.append(" SELECT * FROM INS_CUST_ACTION ");
//        sql.append(" WHERE EXECUTOR_ID = :EXECUTOR_ID ");
//        sql.append(" AND (DATE_FORMAT(FINISH_TIME, '%Y-%m-%d') = :PLAN_DATE OR PLAN_DEAL_DATE = :PLAN_DATE) ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_ID", planId);
        List<CustActionEntity> list = this.query(CustActionEntity.class, "INS_CUST_ACTION", parameter);
        return list;
    }

    public CustActionEntity queryCustActionByCustIdAndActionCodeAndPlanId(String custId, String actionCode, String planId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);
        parameter.put("ACTION_CODE", actionCode);
        parameter.put("PLAN_ID", planId);
        List<CustActionEntity> list = this.query(CustActionEntity.class, "INS_CUST_ACTION", parameter);
        if(ArrayTool.isNotEmpty(list)) {
            return list.get(0);
        }

        return null;
    }

    public List<CustActionEntity> queryCustFinishActionByCustId(String custId) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUST_ACTION ");
        sql.append(" WHERE CUST_ID = :CUST_ID ");
        sql.append(" AND FINISH_TIME IS NOT NULL ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);
        List<CustActionEntity> list = this.queryBySql(CustActionEntity.class, sql.toString(), parameter);
//        if(ArrayTool.isEmpty(list)) {
//            return new ArrayList<CustActionEntity>();
//        } else {
        return list;
//        }
    }

    public int queryFinishActionCountByPlanId(String planId, String actionCode) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_ID", planId);
        parameter.put("ACTION_CODE", actionCode);

        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT COUNT(*) NUM FROM INS_CUST_ACTION ");
        sql.append(" WHERE PLAN_ID = :PLAN_ID ");
        sql.append(" AND ACTION_CODE = :ACTION_CODE ");
        sql.append(" AND FINISH_TIME IS NOT NULL ");
        RecordSet recordSet = this.queryBySql(sql.toString(), parameter);
        int num = recordSet.getInt(0, "NUM");

        return num;
    }
}
