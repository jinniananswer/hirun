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
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id=jsonProject.getString("ID");
            boolean isSuccess= PartyBean.transScanDataFromTableToIns(jsonProject);
            if(isSuccess){
                signToDone(id,now,"out_hirunplus_custservice_scan","out_his_hirunplus_custservice_scan");
            }
        }

        sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM out_hirunplus_xqlte ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY LT2_TIME ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for(int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id=jsonProject.getString("ID");
            boolean isSuccess= PartyBean.transXQLTEDataFromTableToIns(jsonProject);
            if(isSuccess){
                signToDone(id,now,"out_hirunplus_xqlte","out_his_hirunplus_xqlte");
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