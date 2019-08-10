package com.hirun.app.biz.task.out;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.custservice.PartyBean;
import com.hirun.app.bean.out.hirunplusdata.*;
import com.hirun.app.bean.plan.PlanBean;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.app.dao.out.DataGetTimeDAO;
import com.hirun.pub.domain.out.DataGetTimeEntity;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransCustServiceDataToInsService extends GenericService {

    public ServiceResponse transCustServiceDataToIns(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        GenericDAO dao = new GenericDAO("out");
        StringBuilder sql = new StringBuilder();
        String now = TimeTool.now();

        sql.append(" SELECT * ");
        sql.append(" FROM out_hirunplus_custservice_scan ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY ADD_TIME ");
        JSONArray jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for(int i = 0, size = jsonProjectList.size(); i < size; i++) {
            Map<String, String> dbParam = new HashMap<String, String>();
            Map<String, String> updateParam = new HashMap<String, String>();

            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id=jsonProject.getString("ID");
            updateParam.put("ID",id);
            boolean isSuccess= PartyBean.transScanDataFromTableToIns(jsonProject);
            if(isSuccess){
                dbParam.put("PROJECT_ID", jsonProject.getString("PROJECT_ID"));
                dbParam.put("UID", jsonProject.getString("UID"));
                dbParam.put("HEAD_URL", jsonProject.getString("HEAD_URL"));
                dbParam.put("NICKNAME", jsonProject.getString("NICKNAME"));
                dbParam.put("SCAN_ID", jsonProject.getString("SCAN_ID"));
                dbParam.put("ROLE_ID", jsonProject.getString("ROLE_ID"));
                dbParam.put("CID", jsonProject.getString("CID"));
                dbParam.put("ADD_TIME", jsonProject.getString("ADD_TIME"));
                dbParam.put("NAME", jsonProject.getString("NAME"));
                dbParam.put("PHONE", jsonProject.getString("PHONE"));
                dbParam.put("ADDRESS", jsonProject.getString("ADDRESS"));
                dbParam.put("MODE_ID", jsonProject.getString("MODE_ID"));
                dbParam.put("MODE_TIME", jsonProject.getString("MODE_TIME"));
                dbParam.put("LOUPAN", jsonProject.getString("LOUPAN"));
                dbParam.put("LNUMBER", jsonProject.getString("LNUMBER"));
                dbParam.put("STAFF_ID", jsonProject.getString("STAFF_ID"));
                dbParam.put("OPENID", jsonProject.getString("OPENID"));
                dbParam.put("INDB_TIME", TimeTool.now());
                dbParam.put("DEAL_TAG", "1");
                dbParam.put("DEAL_TIME", TimeTool.now());

                dao.insertAutoIncrement("out_his_hirunplus_custservice_scan",dbParam);
                dao.executeUpdate("delete from out_hirunplus_custservice_scan where ID = :ID ",updateParam);
            }
        }

        sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM out_hirunplus_xqlte ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY LT2_TIME ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for(int i = 0, size = jsonProjectList.size(); i < size; i++) {
            Map<String, String> dbParam = new HashMap<String, String>();
            Map<String, String> updateParam = new HashMap<String, String>();

            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id=jsonProject.getString("ID");
            updateParam.put("ID",id);

            boolean isSuccess= PartyBean.transXQLTEDataFromTableToIns(jsonProject);
            if(isSuccess){
                dbParam.put("UID", jsonProject.getString("UID"));
                dbParam.put("NICKNAME", jsonProject.getString("NICKNAME"));
                dbParam.put("HEADIMGURL", jsonProject.getString("HEADIMGURL"));
                dbParam.put("ADD_TIME", jsonProject.getString("ADD_TIME"));
                dbParam.put("STAT", jsonProject.getString("STAT"));
                dbParam.put("MSG", jsonProject.getString("MSG"));
                dbParam.put("NAME", jsonProject.getString("NAME"));
                dbParam.put("PHONE", jsonProject.getString("PHONE"));
                dbParam.put("ADDRESS", jsonProject.getString("ADDRESS"));
                dbParam.put("ISHIDE", jsonProject.getString("ISHIDE"));
                dbParam.put("MODE_ID", jsonProject.getString("MODE_ID"));
                dbParam.put("MODE_TIME", jsonProject.getString("MODE_TIME"));
                dbParam.put("LOUPAN", jsonProject.getString("LOUPAN"));
                dbParam.put("LNUMBER", jsonProject.getString("LNUMBER"));
                dbParam.put("CUST_FROM", jsonProject.getString("CUST_FROM"));
                dbParam.put("STAFF_ID", jsonProject.getString("STAFF_ID"));
                dbParam.put("IS_COMMEND", jsonProject.getString("IS_COMMEND"));
                dbParam.put("COMM_TIME", jsonProject.getString("COMM_TIME"));
                dbParam.put("IS_INPROCESS", jsonProject.getString("IS_INPROCESS"));
                dbParam.put("IN_TIME", jsonProject.getString("IN_TIME"));
                dbParam.put("METHOD", jsonProject.getString("METHOD"));
                dbParam.put("ISCCMW", jsonProject.getString("ISCCMW"));
                dbParam.put("ISZJGD", jsonProject.getString("ISZJGD"));
                dbParam.put("TMPINT", jsonProject.getString("TMPINT"));
                dbParam.put("URL", jsonProject.getString("URL"));
                dbParam.put("LT2_TIME", jsonProject.getString("GNLT_UPDATE_TIME"));
                dbParam.put("LT3_TIME", jsonProject.getString("LT3_TIME"));
                dbParam.put("LT2_UPDATE_TIME", jsonProject.getString("GNLT_UPDATE_TIME"));
                dbParam.put("LT3_UPDATE_TIME", jsonProject.getString("LT3_UPDATE_TIME"));
                //新增返回功能蓝图保存时间
                dbParam.put("GNLT_CREATE_TIME", jsonProject.getString("GNLT_UPDATE_TIME"));
                dbParam.put("GNLT_UPDATE_TIME", jsonProject.getString("GNLT_UPDATE_TIME"));

                dbParam.put("IS_CHANGE", jsonProject.getString("IS_CHANGE"));
                dbParam.put("OPEN_ID", jsonProject.getString("OPEN_ID"));
                dbParam.put("SJS_STAFF_ID", jsonProject.getString("SJS_STAFF_ID"));
                dbParam.put("SJS_ROLE_ID", jsonProject.getString("SJS_ROLE_ID"));
                dbParam.put("FUNC", jsonProject.getString("FUNC"));
                //缺少风格蓝图内容
                dbParam.put("INDB_TIME", TimeTool.now());
                dbParam.put("DEAL_TAG", "1");
                dbParam.put("DEAL_TIME",TimeTool.now());
                dao.insertAutoIncrement("out_his_hirunplus_xqlte",dbParam);
                dao.executeUpdate("delete from out_hirunplus_xqlte where ID = :ID",updateParam);
            }
        }
        return response;
    }


    private void signToDone(String id, String dealTime, String tableName, String hisTableName) throws Exception {
        GenericDAO dao = new GenericDAO("out");
        Map<String, String> dbParam = new HashMap<String, String>();
        dbParam.put("ID", id);
        dbParam.put("DEAL_TIME", dealTime);

        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ").append(tableName).append(" ");
        sql.append(" SET DEAL_TAG = '1',DEAL_TIME = :DEAL_TIME ");
        sql.append(" WHERE ID = :ID ");
        dao.executeUpdate(sql.toString(), dbParam);

        //移历史表
        sql = new StringBuilder();
        sql.append(" INSERT INTO ").append(hisTableName).append(" ");
        sql.append(" SELECT * FROM ").append(tableName).append(" WHERE ID = :ID");
        dao.executeUpdate(sql.toString(), dbParam);

        //删除在线表
        sql = new StringBuilder();
        sql.append(" DELETE FROM ").append(tableName).append(" ");
        sql.append(" WHERE ID = :ID");
        dao.executeUpdate(sql.toString(), dbParam);
    }
}