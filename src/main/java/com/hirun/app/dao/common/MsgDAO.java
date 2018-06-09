package com.hirun.app.dao.common;

import com.hirun.pub.domain.entity.common.MsgEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-06-09.
 */
@DatabaseName("ins")
public class MsgDAO extends StrongObjectDAO{

    public MsgDAO(String databaseName) {
        super(databaseName);
    }

    public List<MsgEntity> queryMsgListByRecvId(String recvId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT RECV_ID,MSG_STATUS,MSG_CONTENT,MSG_TYPE,SEND_ID,date_format(SEND_TIME, '%Y-%m-%d %T') SEND_TIME");
        sql.append(" FROM INS_MSG");
        sql.append(" WHERE RECV_ID = :RECV_ID");
        sql.append(" ORDER BY SEND_TIME DESC");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("RECV_ID", recvId);

        return this.queryBySql(MsgEntity.class, sql.toString(), parameter);
    }

    public int queryUnReadMsgCountByRecvId(String recvId, String msgStatus) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT count(*) NUM");
        sql.append(" FROM INS_MSG");
        sql.append(" WHERE RECV_ID = :RECV_ID");
        sql.append(" AND MSG_STATUS = 0");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("RECV_ID", recvId);
        parameter.put("MSG_STATUS", msgStatus);

        RecordSet recordSet = this.queryBySql(sql.toString(), parameter);
        return recordSet.getInt(0, "NUM");
    }

    public void updateMsgStatusByRecvId(String recvId, String msgStatus, String oldMsgStatus) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE INS_MSG");
        sql.append(" SET MSG_STATUS = :MSG_STATUS");
        sql.append(" WHERE RECV_ID = :RECV_ID");
        sql.append(" AND MSG_STATUS = :OLD_MSG_STATUS");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("RECV_ID", recvId);
        parameter.put("MSG_STATUS", msgStatus);
        parameter.put("OLD_MSG_STATUS", oldMsgStatus);

        this.executeUpdate(sql.toString(), parameter);
    }
}
