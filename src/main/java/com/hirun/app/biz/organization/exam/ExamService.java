package com.hirun.app.biz.organization.exam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.dao.org.ExamDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.Utility;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2019-01-03 22:43
 **/
public class ExamService extends GenericService {

    public ServiceResponse initPreworkExam(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");

        EmployeeEntity employee = EmployeeBean.getEmployeeByEmployeeId(employeeId);
//        if(StringUtils.isBlank(employee.getRegularDate()) || TimeTool.compareTwoTime(session.getCreateTime(), employee.getRegularDate()+" 00:00:00") > 0) {
//            response.set("NEED_EXAM", "false");
//            response.set("DESC","您已经是正式员工，无需参加岗前测试");
//            return response;
//        }

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);
        parameter.put("EXAM_ID", request.getString("EXAM_ID"));

        ExamDAO dao = DAOFactory.createDAO(ExamDAO.class);
        Record exam = dao.queryByPk("ins_exam", parameter);
        int passScore = Integer.parseInt(exam.get("PASS_SCORE"));
        int timeLimit = Integer.parseInt(exam.get("TIMES"));
        RecordSet examScores = dao.query("ins_exam_score", parameter);



        if(examScores == null) {
            response.set("NEED_EXAM", "true");
            response.set("DESC","这是您第1次参加"+exam.get("NAME")+"，本考试"+exam.get("PASS_SCORE")+"分为及格，祝您考出好成绩");
            return response;
        }

        int times = examScores.size();
        if(times >= timeLimit && timeLimit != -1) {
            response.set("NEED_EXAM", "false");
            response.set("DESC","您的考试次数已达上限，不能再参与在线测试");
            return response;
        }
        else {
            response.set("NEED_EXAM", "true");
            response.set("DESC","这是您第"+(times+1)+"次参加"+exam.get("NAME")+"，本考试"+passScore+"分为及格，祝您考出好成绩");
            return response;
        }
    }

    public ServiceResponse startPreworkExam(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        ExamDAO dao = DAOFactory.createDAO(ExamDAO.class);

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EXAM_ID", request.getString("EXAM_ID"));

        RecordSet topics = dao.query("ins_exam_topic", parameter);
        if(topics == null) {
            return response;
        }
        //先取出全部的
        int size = topics.size();
        RecordSet singleTopics = new RecordSet();
        RecordSet multiTopics = new RecordSet();
        RecordSet judgeTopics = new RecordSet();
        for(int i=0;i<size;i++) {
            Record record = topics.get(i);
            String type = record.get("TYPE");
            if(StringUtils.equals("1", type)) {
                singleTopics.add(record);
            }
            else if(StringUtils.equals("2", type)) {
                multiTopics.add(record);
            }
            else if(StringUtils.equals("3", type)) {
                judgeTopics.add(record);
            }
        }

        //取20个单选
        int[] singleNums = Utility.randomArray(0, singleTopics.size() -1, 20);
        //取10个多选
        int[] multiNums = Utility.randomArray(0, multiTopics.size() -1, 10);
        //取10个判断
        int[] judgeNums = Utility.randomArray(0, judgeTopics.size() -1, 10);

        RecordSet allSelectedTopics = new RecordSet();

        for(int singleNum : singleNums) {
            Record singleTopic = singleTopics.get(singleNum);
            allSelectedTopics.add(singleTopic);
        }

        for(int multiNum : multiNums) {
            Record multiTopic = multiTopics.get(multiNum);
            allSelectedTopics.add(multiTopic);
        }

        for(int judgeNum : judgeNums) {
            Record judgeTopic = judgeTopics.get(judgeNum);
            allSelectedTopics.add(judgeTopic);
        }

        size = allSelectedTopics.size();
        String selectedTopicIds = "";
        for(int i=0;i<size;i++) {
            Record selectedTopic = allSelectedTopics.get(i);
            if(i!=size-1){
                selectedTopicIds += selectedTopic.get("TOPIC_ID")+",";
            }
            else{
                selectedTopicIds += selectedTopic.get("TOPIC_ID");
            }
        }

        RecordSet options = dao.queryTopicOptions(selectedTopicIds);
        JSONArray examTopics = new JSONArray();

        for(int i=0;i<size;i++) {
            JSONObject topic = ConvertTool.toJSONObject(allSelectedTopics.get(i));
            String topicId = topic.getString("TOPIC_ID");
            JSONArray topicOptions = new JSONArray();

            int optionSize = options.size();
            for(int j=0;j<optionSize;j++) {
                Record option = options.get(j);
                if(StringUtils.equals(topicId, option.get("TOPIC_ID"))) {
                    topicOptions.add(ConvertTool.toJSONObject(option));
                }
            }
            topic.put("OPTION", topicOptions);
            examTopics.add(topic);
        }

        response.set("EXAM_TOPIC", examTopics);
        return response;
    }

    public ServiceResponse submitExam(ServiceRequest request) throws Exception {
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        String userId = session.getSessionEntity().getUserId();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);
        parameter.put("EXAM_ID", request.getString("EXAM_ID"));
        parameter.put("SCORE", request.getString("ANSWER_SCORE"));
        parameter.put("EXAM_TIME", session.getCreateTime());
        parameter.put("CREATE_USER_ID", userId);
        parameter.put("CREATE_TIME", session.getCreateTime());
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", session.getCreateTime());

        ExamDAO dao = DAOFactory.createDAO(ExamDAO.class);
        dao.insertAutoIncrement("ins_exam_score", parameter);

        return new ServiceResponse();
    }
}
