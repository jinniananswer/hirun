package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;

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

    public List<CustActionEntity> queryCustActionListByPlanId(String planId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_ID", planId);
        List<CustActionEntity> list = this.query(CustActionEntity.class, "INS_CUST_ACTION", parameter);
        return list;
    }

    public List<CustActionEntity> queryCustFinishActionListByPlanId(String planId) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUST_ACTION ");
        sql.append(" WHERE PLAN_ID = :PLAN_ID ");
        sql.append(" AND FINISH_TIME IS NOT NULL ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_ID", planId);
        List<CustActionEntity> list = this.queryBySql(CustActionEntity.class, sql.toString(), parameter);
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

    public List<CustActionEntity> queryCustFinishActionByCustIdaa(String custId) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUST_ACTION ");
        sql.append(" WHERE CUST_ID = :CUST_ID ");
        sql.append(" AND FINISH_TIME IS NOT NULL ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);
        List<CustActionEntity> list = this.queryBySql(CustActionEntity.class, sql.toString(), parameter);
        return list;
    }

    public List<CustActionEntity> queryCustFinishActionByCustIdAndEid(String custId, String executorId) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUST_ACTION ");
        sql.append(" WHERE CUST_ID = :CUST_ID ");
        sql.append(" AND EXECUTOR_ID = :EXECUTOR_ID ");
        sql.append(" AND FINISH_TIME IS NOT NULL ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);
        parameter.put("EXECUTOR_ID", executorId);
        List<CustActionEntity> list = this.queryBySql(CustActionEntity.class, sql.toString(), parameter);
        return list;
    }

    public List<CustActionEntity> queryCustFinishActionByCustIdAndActionCodeAndEid(String custId, String actionCode, String executorId) throws Exception {
        StringBuilder sql = new StringBuilder(200);
        sql.append(" SELECT * FROM INS_CUST_ACTION ");
        sql.append(" WHERE CUST_ID = :CUST_ID ");
        sql.append(" AND ACTION_CODE = :ACTION_CODE ");
        sql.append(" AND EXECUTOR_ID = :EXECUTOR_ID");
        sql.append(" AND FINISH_TIME IS NOT NULL ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);
        parameter.put("ACTION_CODE", actionCode);
        parameter.put("EXECUTOR_ID", executorId);
        List<CustActionEntity> list = this.queryBySql(CustActionEntity.class, sql.toString(), parameter);
        return list;
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

    public List<CustActionEntity> queryCustActionListByPlanIdAndActionCode(String planId, String actionCode) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PLAN_ID", planId);
        parameter.put("ACTION_CODE", actionCode);
        List<CustActionEntity> list = this.query(CustActionEntity.class, "INS_CUST_ACTION", parameter);
        return list;
    }

    public List<CustActionEntity> queryCustUnFinishCauseByCustId(String custId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ins_cust_action a ");
        sql.append(" WHERE a.cust_id = :CUST_ID ");
        sql.append(" AND NOT EXISTS (SELECT 1 FROM ins_cust_action b ");
        sql.append("                 WHERE a.CUST_ID = b.CUST_ID ");
        sql.append("                 AND b.finish_time IS NOT NULL ");
        sql.append("                 AND a.action_code = b.action_code) ");
        sql.append(" AND action_id IN (SELECT MAX(action_id) FROM ins_cust_action c ");
        sql.append("                   WHERE c.cust_id = a.cust_id ");
        sql.append("                   GROUP BY c.action_code) ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);

        return this.queryBySql(CustActionEntity.class, sql.toString(), parameter);
    }
}
