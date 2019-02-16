package com.hirun.app.biz.organization.score;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.org.OrgBean;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.hirun.app.dao.org.ScoreDAO;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.*;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScoreService extends GenericService {


    /**
     * @program: hirun
     * @description: ${初始化考试查询条件}
     * @author:
     * @create:
     **/
    public ServiceResponse initExamQuery(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String today = TimeTool.today();
        response.set("TODAY", today);
        JSONObject orgTree = OrgBean.getOrgTree();
        response.set("ORG_TREE", orgTree);
        return response;
    }

    public ServiceResponse queryExamScore(ServiceRequest request) throws Exception {
        ServiceResponse response=new ServiceResponse();
        ScoreDAO dao=DAOFactory.createDAO(ScoreDAO.class);

        String orgId = request.getString("ORG_ID");
        if(StringUtils.isNotBlank(orgId))
            orgId = OrgBean.getOrgLine(orgId);

        RecordSet exams=dao.queryExamScore(request.getString("NAME"),orgId);
        RecordSet newexams=new RecordSet();

        if(exams.size()==0 || exams==null)
            return  response;

        int size = exams.size();
        for(int i=0;i<size;i++) {

            Record record = exams.get(i);
            String flag=record.get("FLAG");
            //处理过的数据不再处理
            if(StringUtils.equals("1",flag))
                continue;
            String employee_id_i = record.get("EMPLOYEE_ID");
            String score = record.get("SCORE");
            String examid = record.get("EXAM_ID");
            for (int j = 1; j < size; j++) {
                /*取数据与后面的数据比较
                 *1、判断employee_id是否一致，*/
                Record recordA = exams.get(j);
                String employee_id_j = recordA.get("EMPLOYEE_ID");
                String scorej = recordA.get("SCORE");
                String examidj = recordA.get("EXAM_ID");
                if (StringUtils.equals(employee_id_i, employee_id_j)) {
                    if (StringUtils.equals(examid, examidj)) {
                        //有问题，应该取数据与新拼装数据比较
                        String score_k=record.get("EXAM_ID_"+examidj);
                        if(StringUtils.isNotBlank(score_k)|| score_k!=null){
                            if((Integer.parseInt(score_k)-Integer.parseInt(scorej))<0){
                                record.put("EXAM_ID_" + examidj, scorej);
                            }
                        }
                        else if ((Integer.parseInt(score) - Integer.parseInt(scorej)) < 0) {
                            record.put("EXAM_ID_" + examidj, scorej);
                        }
                        else {
                            record.put("EXAM_ID_" + examid, score);
                        }
                        recordA.put("FLAG","1");
                    }
                    else{
                        record.put("EXAM_ID_" + examidj, scorej);
                        recordA.put("FLAG","1");
                    }
                }

            }
            record.put("CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", record.get("CITY")));
            newexams.add(record);
        }
        response.set("DATAS", ConvertTool.toJSONArray(newexams));

        return response;
    }

    public ServiceResponse initScoreQuery(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        ScoreDAO dao=DAOFactory.createDAO(ScoreDAO.class);

        String today = TimeTool.today();
        response.set("TODAY", today);
        JSONObject orgTree = OrgBean.getOrgTree();
        response.set("ORG_TREE", orgTree);

        String train_id = request.getString("TRAIN_ID_QUERY");

        String orgId = request.getString("ORG_ID");
        if(StringUtils.isNotBlank(orgId))
            orgId = OrgBean.getOrgLine(orgId);

        RecordSet exams=dao.queryPostJobScore(request.getString("NAME"),orgId,train_id);

        if(exams.size()==0 || exams==null)
            return  response;


        response.set("DATAS", ConvertTool.toJSONArray(exams));
        return response;
    }

    public ServiceResponse queryPostJobScore(ServiceRequest request) throws Exception {
        ServiceResponse response=new ServiceResponse();
        ScoreDAO dao=DAOFactory.createDAO(ScoreDAO.class);
        String train_id = request.getString("TRAIN_ID_QUERY");

        String orgId = request.getString("ORG_ID");
        if(StringUtils.isNotBlank(orgId))
            orgId = OrgBean.getOrgLine(orgId);

        RecordSet exams=dao.queryPostJobScore(request.getString("NAME"),orgId,train_id);

        if(exams.size()==0 || exams==null)
            return  response;


        response.set("DATAS", ConvertTool.toJSONArray(exams));

        return response;
    }

    public ServiceResponse inputScore(ServiceRequest request) throws Exception {
        ServiceResponse response=new ServiceResponse();
        ScoreDAO dao=DAOFactory.createDAO(ScoreDAO.class);
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        String train_id = request.getString("TRAIN_ID");

        List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();

        JSONObject obj=request.getBody().getData();
        Map<String, String> mobjec = ConvertTool.toMap(obj);
        String cols[]={"EMPLOYEE_ID","TRAIN_ID"};

        for (String key : mobjec.keySet()) {
            String value = mobjec.get(key);
            if (value == null || StringUtils.equals(key,"TRAIN_ID") ) { // 过滤空的key
                continue;
            }
            Map<String, String> param = new HashMap<String, String>();
            param.put("EMPLOYEE_ID",key);
            param.put("TRAIN_ID",train_id);
            param.put("SCORE",value);
            param.put("CREATE_USER_ID",userId);
            param.put("CREATE_DATE",session.getCreateTime());
            param.put("UPDATE_USER_ID",userId);
            param.put("UPDATE_TIME",session.getCreateTime());
            parameters.add(param);
        }
            dao.deleteBatch("ins_train_exam_score",cols,parameters);
            dao.insertBatch("ins_train_exam_score",parameters);

        return response;
    }
}