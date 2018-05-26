package com.hirun.app.biz.task.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.plan.ActionCheckRuleProcess;
import com.hirun.app.bean.plan.PlanBean;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-17.
 */
public class PlanTaskService extends GenericService {

    public ServiceResponse transOriginalDataToAction(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String today = TimeTool.today();
        String now = TimeTool.now();
        GenericDAO dao = new GenericDAO("out");

        //蓝图指导书推送
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ID,NICKNAME,DATE_FORMAT(FROM_UNIXTIME(ADD_TIME), '%Y-%m-%d %H:%i:%s') ADD_TIME,STAFF_ID,OPENID ");
        sql.append(" FROM OUT_HIRUNPLUS_PROJECTS ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY ADD_TIME ");
        JSONArray jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for(int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            boolean isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "LTZDSTS");
            if(isTrans) {
                signToDone(id, now, "OUT_HIRUNPLUS_PROJECTS", "OUT_HIS_HIRUNPLUS_PROJECTS");
            }
        }

        //关注公众号
        sql = new StringBuilder();
        sql.append(" SELECT ID,NICKNAME,DATE_FORMAT(FROM_UNIXTIME(SUBSCRIBE_TIME), '%Y-%m-%d %H:%i:%s') ADD_TIME,STAFF_ID,OPENID ");
        sql.append(" FROM out_hirunplus_reg ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY ADD_TIME ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for(int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            boolean isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "GZHGZ");
            if(isTrans) {
                signToDone(id, now, "out_hirunplus_reg", "out_his_hirunplus_reg");
            }
        }

        //扫码
        sql = new StringBuilder();
        sql.append(" SELECT ID,NICKNAME,DATE_FORMAT(FROM_UNIXTIME(ADD_TIME), '%Y-%m-%d %H:%i:%s') ADD_TIME,STAFF_ID,OPENID,ROLE_ID ");
        sql.append(" FROM out_hirunplus_scan ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY ADD_TIME ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for(int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            String openId = jsonProject.getString("OPENID");
            String roleId = jsonProject.getString("ROLE_ID");//11:客户代表；19:家装顾问
            boolean isTrans = false;
            if("19".equals(roleId)) {
                //扫码
                isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "XQLTYTS");
            } else if("11".equals(roleId)) {
                //需要处理
                CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
                CustomerEntity customerEntity = custDAO.getCustomerEntityByIdentifyCode(openId);
                //TODO 暂时不根据WX_NICK查了
                if(customerEntity != null && StringUtils.isNotBlank(customerEntity.getHouseCounselorId())) {
                    jsonProject.put("STAFF_ID", customerEntity.getHouseCounselorId());
                    isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "XQLTYTS");
                } else {
                    continue;
                }

            }
            if(isTrans) {
                signToDone(id, now, "out_hirunplus_scan", "out_his_hirunplus_scan");
            }
        }

        //需求蓝图一推送
        sql = new StringBuilder();
        sql.append(" SELECT ID,NICKNAME,DATE_FORMAT(FROM_UNIXTIME(MODE_TIME), '%Y-%m-%d %H:%i:%s') ADD_TIME,STAFF_ID,OPENID ");
        sql.append(" FROM out_hirunplus_commends ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY ADD_TIME ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for(int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            boolean isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "XQLTYTS");
        }

        return response;
    }

    private void signToDone(String id, String dealTime, String tableName, String hisTableName) throws Exception {
        GenericDAO dao = new GenericDAO("ins");
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
