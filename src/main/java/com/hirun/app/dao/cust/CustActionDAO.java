package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 9:56
 * @Description:
 */
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
    public List<CustActionEntity> queryFinishActionList(String executorId, String actionCode, String startTime, String endTime) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSE_COUNSELOR_ID", "123");

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
}
