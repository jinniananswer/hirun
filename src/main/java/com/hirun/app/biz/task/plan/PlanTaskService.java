package com.hirun.app.biz.task.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.out.hirunplusdata.DataImportUtil;
import com.hirun.app.bean.out.hirunplusdata.YJALDataImport;
import com.hirun.app.bean.plan.ActionCheckRuleProcess;
import com.hirun.app.bean.plan.PlanBean;
import com.hirun.app.biz.common.PerformDueTaskService;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.custservice.PartyEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
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
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);

        //蓝图指导书推送
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ID,NICKNAME,DATE_FORMAT(FROM_UNIXTIME(COMM_TIME), '%Y-%m-%d %H:%i:%s') OPER_TIME,STAFF_ID,OPENID ");
        sql.append(" FROM OUT_HIRUNPLUS_PROJECTS ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY COMM_TIME ");
        JSONArray jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for (int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            String employeeId = PlanBean.getEmployeeIdByHirunPlusStaffId(jsonProject.getString("STAFF_ID"));
            if (StringUtils.isBlank(employeeId)) {
                continue;
            }
            jsonProject.put("STAFF_ID", employeeId);
            boolean isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "LTZDSTS");
            if (isTrans) {
                signToDone(id, now, "OUT_HIRUNPLUS_PROJECTS", "OUT_HIS_HIRUNPLUS_PROJECTS");
            }
        }

        //关注公众号
        sql = new StringBuilder();
        sql.append(" SELECT ID,NICKNAME,DATE_FORMAT(FROM_UNIXTIME(SUBSCRIBE_TIME), '%Y-%m-%d %H:%i:%s') OPER_TIME,OPENID ");
        sql.append(" FROM out_hirunplus_reg ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY SUBSCRIBE_TIME ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for (int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            String openId = jsonProject.getString("OPENID");
            CustomerEntity customerEntity = custDAO.getCustomerEntityByIdentifyCode(openId);
            boolean isTrans = false;
            if (customerEntity != null && StringUtils.isNotBlank(customerEntity.getHouseCounselorId())) {
                jsonProject.put("STAFF_ID", customerEntity.getHouseCounselorId());
                isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "GZHGZ");
            } else {
                continue;
            }
            if (isTrans) {
                signToDone(id, now, "out_hirunplus_reg", "out_his_hirunplus_reg");
            }
        }

        //扫码
        sql = new StringBuilder();
        sql.append(" SELECT ID,NICKNAME,DATE_FORMAT(FROM_UNIXTIME(ADD_TIME), '%Y-%m-%d %H:%i:%s') OPER_TIME,STAFF_ID,OPENID,ROLE_ID ");
        sql.append(" FROM out_hirunplus_scan ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY ADD_TIME ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for (int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            String openId = jsonProject.getString("OPENID");
            String roleId = jsonProject.getString("ROLE_ID");//11:客户代表；19:家装顾问
            boolean isTrans = false;
            if ("19".equals(roleId)) {
                //扫码
                String employeeId = PlanBean.getEmployeeIdByHirunPlusStaffId(jsonProject.getString("STAFF_ID"));
                if (StringUtils.isBlank(employeeId)) {
                    continue;
                }
                jsonProject.put("STAFF_ID", employeeId);
                isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "SMJRQLC");
            } else if ("11".equals(roleId)) {
                //需要处理
                CustomerEntity customerEntity = custDAO.getCustomerEntityByIdentifyCode(openId);
                if (customerEntity != null && StringUtils.isNotBlank(customerEntity.getHouseCounselorId())) {
                    jsonProject.put("STAFF_ID", customerEntity.getHouseCounselorId());
                    isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "ZX");
                } else {
                    continue;
                }
            } else {
                isTrans = true;
            }

            if (isTrans) {
                signToDone(id, now, "out_hirunplus_scan", "out_his_hirunplus_scan");
            }
        }

        //需求蓝图一推送
        sql = new StringBuilder();
        sql.append(" SELECT ID,NICKNAME,DATE_FORMAT(FROM_UNIXTIME(MODE_TIME), '%Y-%m-%d %H:%i:%s') OPER_TIME,STAFF_ID,OPENID ");
        sql.append(" FROM out_hirunplus_commends ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY MODE_TIME ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for (int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            String employeeId = PlanBean.getEmployeeIdByHirunPlusStaffId(jsonProject.getString("STAFF_ID"));
            if (StringUtils.isBlank(employeeId)) {
                continue;
            }
            jsonProject.put("STAFF_ID", employeeId);
            boolean isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "XQLTYTS");

            if (isTrans) {
                signToDone(id, now, "out_hirunplus_commends", "out_his_hirunplus_commends");
            }
        }

        //一键案例推送
        sql = new StringBuilder();
        sql.append(" SELECT ID,NICK_NAME NICKNAME,DATE_FORMAT(FROM_UNIXTIME(CREATE_TIME), '%Y-%m-%d %H:%i:%s') OPER_TIME,STAFF_ID,ROLE_ID,OPENID ");
        sql.append(" FROM out_hirunplus_yjal ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY CREATE_TIME ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for (int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            String staffId = jsonProject.getString("STAFF_ID");
            String roleId = jsonProject.getString("ROLE_ID");
            boolean isTrans = false;
            if ("19".equals(roleId)) {
                String employeeId = PlanBean.getEmployeeIdByHirunPlusStaffId(staffId);
                if (StringUtils.isBlank(employeeId)) {
                    continue;
                }
                jsonProject.put("STAFF_ID", employeeId);

                isTrans = PlanBean.transOriginalDataToAction(jsonProject, today, "YJALTS");
            } else {
                isTrans = true;
            }

            if (isTrans) {
                signToDone(id, now, "out_hirunplus_yjal", "out_his_hirunplus_yjal");
            }
        }
        //mode数据转换
        transModeToAction();

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

    private void transModeToAction() {
        try {
            GenericDAO dao = new GenericDAO("out");
            CustomerServiceDAO customerServiceDAO = DAOFactory.createDAO(CustomerServiceDAO.class);

            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ID,MODE_ID,DATE_FORMAT(FROM_UNIXTIME(MODE_TIME), '%Y-%m-%d %H:%i:%s') MODE_TIME,STAFF_ID,OPENID,STYLE,FUNC,NAME,AGE,MIANJI,HUXING,YONGTU ");
            sql.append(" FROM out_hirunplus_commends_mode ");
            sql.append(" WHERE DEAL_TAG = '0' ");
            sql.append(" ORDER BY INDB_TIME ");
            JSONArray jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

            if (jsonProjectList.size() < 0) {
                return;
            }

            for (int i = 0, size = jsonProjectList.size(); i < size; i++) {
                JSONObject jsonProject = jsonProjectList.getJSONObject(i);
                Map<String, String> param = new HashMap<String, String>();
                // Map<String,String> partyActionParam=new HashMap<String, String>();

                String id = jsonProject.getString("ID");
                String mode_id = jsonProject.getString("MODE_ID");
                String mode_time = jsonProject.getString("MODE_TIME");
                String style = jsonProject.getString("STYLE");
                String func = jsonProject.getString("FUNC");
                String staff_id = jsonProject.getString("STAFF_ID");
                String openid = jsonProject.getString("OPENID");
                String employeeId = PlanBean.getEmployeeIdByHirunPlusStaffId(staff_id);


                param.put("OPEN_ID", openid);
                param.put("ACTION_CODE", "XQLTY");
                param.put("MODE_ID", mode_id);
                param.put("MODE_TIME", mode_time);
                param.put("NAME", jsonProject.getString("NAME"));
                param.put("AGE", jsonProject.getString("AGE"));
                param.put("HOUSE_KIND", jsonProject.getString("HUXING"));
                param.put("HOUSE_AREA", jsonProject.getString("MIANJI"));
                param.put("APPLICATION", jsonProject.getString("YONGTU"));
                param.put("STAFF_ID", staff_id);
                param.put("STYLE", style);
                param.put("FUNC", func);
                param.put("REL_EMPLOYEE_ID", employeeId);
                param.put("CREATE_DATE", TimeTool.now());

                customerServiceDAO.insertAutoIncrement("ins_blueprint_action", param);//将需求蓝图一的内容转换成ins数据

                signToDone(id, TimeTool.now(), "out_hirunplus_commends_mode", "out_his_hirunplus_commends_mode");//搬历史表

            }

        } catch (Exception e) {
            log.error("mode数据转换异常：", e);
        }

    }

    public ServiceResponse test(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        String host = "www.hi-run.net";
        String path = "/api/subscribe";
        JSONArray jsonArray = DataImportUtil.getDataByApi(host, path, "2018-09-29 16:25:00", "2018-09-29 17:00:00");
//        YJALDataImport.dataImport("2018-09-29 16:25:00", "2018-09-29 17:00:00");

        return response;
    }
}
