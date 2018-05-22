package com.hirun.app.biz.task.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.plan.ActionCheckRuleProcess;
import com.hirun.app.bean.plan.PlanBean;
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

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ID,NICKNAME,DATE_FORMAT(FROM_UNIXTIME(ADD_TIME), '%Y-%m-%d %H:%i:%s') ADD_TIME,STAFF_ID,OPENID ");
        sql.append(" FROM OUT_HIRUNPLUS_PROJECTS ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY ADD_TIME ");
        JSONArray jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for(int i = 0, size = jsonProjectList.size(); i < size; i++) {
            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String nickName = jsonProject.getString("NICKNAME");
            String addTime = jsonProject.getString("ADD_TIME");
            String staffId = jsonProject.getString("STAFF_ID");
            String openId = jsonProject.getString("OPENID");
            String id = jsonProject.getString("ID");

            if(StringUtils.isBlank(staffId)) {
                continue;
            }

            if(ActionCheckRuleProcess.isActionBindYesterdayPlan(addTime, today, staffId)) {
                //归到昨日计划里
                String yesterday = TimeTool.addTime(today + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1).substring(0,10);
                PlanBean.actionBindPlan(nickName, openId, "LTZDSTS", yesterday, staffId, addTime);
                signToDone(id, now);
                continue;
            }

            if(ActionCheckRuleProcess.isActionBindTodayPlan(addTime, today, staffId)) {
                //归到今日计划里
                PlanBean.actionBindPlan(nickName, openId, "LTZDSTS", today, staffId, addTime);
                signToDone(id,now);
                continue;
            }

            if(ActionCheckRuleProcess.isActionBindTomorrowPlan(addTime, today, staffId)) {
                //归到明日计划里
                String tomorrow = TimeTool.addTime(today + " 00:00:00", TimeTool.TIME_PATTERN, ChronoUnit.DAYS, 1).substring(0,10);
                PlanBean.actionBindPlan(nickName, openId, "LTZDSTS", tomorrow, staffId, addTime);
                signToDone(id,now);
                continue;
            }

            //无法归到任意计划里时，跳过
            continue;
        }

        return response;
    }

    private void signToDone(String id, String dealTime) throws Exception {
        GenericDAO dao = new GenericDAO("ins");
        Map<String, String> dbParam = new HashMap<String, String>();
        dbParam.put("ID", id);
        dbParam.put("DEAL_TIME", dealTime);

        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE OUT_HIRUNPLUS_PROJECTS ");
        sql.append(" SET DEAL_TAG = '1',DEAL_TIME = :DEAL_TIME ");
        sql.append(" WHERE ID = :ID ");
        dao.executeUpdate(sql.toString(), dbParam);
    }


}
