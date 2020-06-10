package com.hirun.app.biz.organization.score;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.dao.employee.EmployeeJobRoleDAO;
import com.hirun.app.dao.org.ScoreDAO;
import com.hirun.app.dao.org.TrainDAO;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;


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
        AppSession session = SessionManager.getSession();

        String orgId = request.getString("ORG_ID");
        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();

        if(StringUtils.isBlank(orgId)){
            orgId = OrgBean.getOrgId(session.getSessionEntity());
            OrgEntity rootOrg = OrgBean.findEmployeeRoot(orgId, allOrgs);
            if(StringUtils.equals("122", rootOrg.getParentOrgId())) {
                orgId = "122";
            }
            else {
                orgId = rootOrg.getOrgId();
            }
            orgId = OrgBean.getOrgLine(orgId, allOrgs);
        }
        else {
            orgId = OrgBean.getOrgLine(orgId);
        }
        RecordSet exams=dao.queryExamScore(request.getString("NAME"),orgId);
        exams=this.filterExamScore(exams);


        response.set("DATAS", ConvertTool.toJSONArray(exams));

        return response;
    }



    public static RecordSet filterExamScore(RecordSet exams) throws Exception {
        if(exams == null || exams.size() <= 0) {
            return exams;
        }

        int size = exams.size();
        RecordSet rst = new RecordSet();
        Map<String, Record> tempeExam = new HashMap<String, Record>();
        for(int i=0;i<size;i++) {
            Record exam = exams.get(i);
            String employeeId = exam.get("EMPLOYEE_ID");
            String examId = exam.get("EXAM_ID");
            String score = exam.get("SCORE");
            if(!tempeExam.containsKey(employeeId)) {
                //查询出的exam_id为空的情况
                if(StringUtils.isBlank(examId)){
                    tempeExam.put(employeeId, exam);
                }else{
                    exam.put("EXAM_ID_"+examId,score);//设置科目成绩
                    tempeExam.put(employeeId, exam);
                }
            }
            else {
                //employee_id已经有了
                //1、如果exam_id为空证明没考试，直接跳出本次循环
                //2、否则取exam_id对应的score进行比较，如果没有没有对于的EXAM_ID则新增成绩
                if(StringUtils.isBlank(examId)){
                    continue;
                }else {
                    String tempScore=tempeExam.get(employeeId).get("EXAM_ID_"+examId);
                    Record tempRecord=tempeExam.get(employeeId);
                    if(!tempRecord.containsKey("EXAM_ID_"+examId)){
                        tempRecord.put("EXAM_ID_"+examId,score);//设置科目成绩
                        tempeExam.put(employeeId, tempRecord);
                    }else{
                        if(StringUtils.isBlank(score)||score==null){
                            continue;
                        }else if(tempScore==null || StringUtils.isBlank(tempScore)){
                            tempRecord.put("EXAM_ID_"+examId,score);//设置科目成绩
                            tempeExam.put(employeeId, tempRecord);
                        }else{
                            if((Integer.parseInt(tempScore)-Integer.parseInt(score))<0){//已插入成绩小于新的成绩，将新成绩放入
                                tempRecord.put("EXAM_ID_"+examId,score);//设置科目成绩
                                tempeExam.put(employeeId, tempRecord);
                            }else {
                                continue;
                            }
                        }
                    }
                }

            }
        }
        Set<String> keys = tempeExam.keySet();
        for(String key : keys){
            rst.add(tempeExam.get(key));
        }
        return rst;
    }


    public ServiceResponse initScoreQuery(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        ScoreDAO dao=DAOFactory.createDAO(ScoreDAO.class);
        AppSession session = SessionManager.getSession();
        Map<String,String> parm=new HashMap<String,String>();

        String today = TimeTool.today();
        response.set("TODAY", today);
        JSONObject orgTree = OrgBean.getOrgTree();
        response.set("ORG_TREE", orgTree);

        String train_id = request.getString("TRAIN_ID");

        String orgId = request.getString("ORG_ID");

        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();

        if(StringUtils.isBlank(orgId)){
            orgId = OrgBean.getOrgId(session.getSessionEntity());
            OrgEntity rootOrg = OrgBean.findEmployeeRoot(orgId, allOrgs);
            if(StringUtils.equals("122", rootOrg.getParentOrgId())) {
                orgId = "122";
            }
            else {
                orgId = rootOrg.getOrgId();
            }

            orgId = OrgBean.getOrgLine(orgId, allOrgs);

        }
        else {
            orgId = OrgBean.getOrgLine(orgId);
        }

        RecordSet scores= new RecordSet();



        parm.put("TRAIN_ID",train_id);
        Record trainRecord=dao.queryByPk("INS_TRAIN",parm);
        String type=trainRecord.get("TYPE");


        if(StringUtils.equals(type,"1")){
            scores=dao.queryPreWorkScore(request.getString("NAME"),orgId,train_id);
            scores=this.filterTrainScore(scores);
            int size = scores.size();

            for (int i=0;i<size;i++){
                Record record =scores.get(i);
                String signItem=record.get("SIGN_ITEM");
                String[] itemArray = signItem.split("\\|");
                for (String key : itemArray) {
                    if (StringUtils.isBlank(key)) {
                        continue;
                    }
                    if (StringUtils.equals("-1", key)) {
                        record.put("NEED_COMM","TRUE");
                        record.put("NEED_PRO","TRUE");
                    }
                    else if(StringUtils.equals("0", key)) {
                        record.put("NEED_COMM","TRUE");

                    }
                    else if(StringUtils.equals("1", key)) {
                        record.put("NEED_PRO","TRUE");

                    }
                    else {
                        record.put("NEED_COMM","FALSE");
                        record.put("NEED_PRO","FALSE");
                    }
                }

                record.put("TYPE",type);
            }

        }else {
            scores=dao.queryPostJobScore(request.getString("NAME"),orgId,train_id);
            if(scores==null || scores.size() <=0){
                return response;
            }

            int size = scores.size();

            for (int i=0;i<size;i++){
                Record record =scores.get(i);
                record.put("TYPE",type);
            }

        }

        JSONObject company = new JSONObject();

        JSONObject companyPass = new JSONObject();

        if(ArrayTool.isNotEmpty(scores)) {
            int passComm = 0;
            int passPro = 0;
            int totalComm = 0;
            int totalPro = 0;
            int totalComposite = 0;
            int passComposite = 0;

            int size = scores.size();
            for(int i=0;i<size;i++) {
                Record score = scores.get(i);
                String employeeOrgId = score.get("ORG_ID");
                OrgEntity rootOrg = OrgBean.findEmployeeRoot(employeeOrgId, allOrgs);
                JSONArray companyScores = company.getJSONArray(rootOrg.getName());
                if(companyScores == null) {
                    companyScores = new JSONArray();
                    company.put(rootOrg.getName(), companyScores);
                }

                if (StringUtils.equals("1", type)) {

                    String needComm = score.get("NEED_COMM");
                    String needPro = score.get("NEED_PRO");

                    if (StringUtils.equals("TRUE", needComm)) {
                        String commStr = score.get("ITEM_0");
                        String companyTotalCommStr = companyPass.getString(rootOrg.getName()+"_COMM_TOTAL");
                        int companyTotalComm = 0;

                        if (StringUtils.isNotBlank(companyTotalCommStr)) {
                            companyTotalComm = Integer.parseInt(companyTotalCommStr);
                        }

                        companyTotalComm++;
                        companyPass.put(rootOrg.getName() + "_COMM_TOTAL", companyTotalComm);

                        totalComm++;
                        if (StringUtils.isNotBlank(commStr)) {
                            double commScore = Double.parseDouble(commStr);
                            if (commScore >= 80) {
                                passComm ++;
                                String companyPassCommStr = companyPass.getString(rootOrg.getName()+"_COMM_PASS");
                                int companyPassComm = 0;

                                if (StringUtils.isNotBlank(companyPassCommStr)) {
                                    companyPassComm = Integer.parseInt(companyPassCommStr);
                                }

                                companyPassComm++;
                                companyPass.put(rootOrg.getName()+"_COMM_PASS", companyPassComm);
                            }
                        }
                    }

                    if (StringUtils.equals("TRUE", needPro)) {
                        String proStr = score.get("ITEM_1");
                        String companyTotalProStr = companyPass.getString(rootOrg.getName()+"_PRO_TOTAL");
                        int companyTotalPro = 0;

                        if (StringUtils.isNotBlank(companyTotalProStr)) {
                            companyTotalPro = Integer.parseInt(companyTotalProStr);
                        }

                        companyTotalPro++;
                        companyPass.put(rootOrg.getName() + "_PRO_TOTAL", companyTotalPro);

                        totalPro++;
                        if (StringUtils.isNotBlank(proStr)) {
                            double proScore = Double.parseDouble(proStr);
                            if (proScore >= 80) {
                                passPro ++;
                                String companyPassProStr = companyPass.getString(rootOrg.getName()+"_PRO_PASS");
                                int companyPassPro = 0;

                                if (StringUtils.isNotBlank(companyPassProStr)) {
                                    companyPassPro = Integer.parseInt(companyPassProStr);
                                }

                                companyPassPro++;
                                companyPass.put(rootOrg.getName()+"_PRO_PASS", companyPassPro);
                            }
                        }
                    }
                } else {
                    String scoreStr = score.get("SCORE");
                    String companyTotalCompositeStr = companyPass.getString(rootOrg.getName()+"_COMPOSITE_TOTAL");
                    int companyTotalComposite = 0;

                    if (StringUtils.isNotBlank(companyTotalCompositeStr)) {
                        companyTotalComposite = Integer.parseInt(companyTotalCompositeStr);
                    }
                    companyTotalComposite++;
                    companyPass.put(rootOrg.getName() + "_COMPOSITE_TOTAL", companyTotalComposite);
                    totalComposite++;
                    if (StringUtils.isNotBlank(scoreStr)) {
                        double compositeScore = 0;
                        try {
                            compositeScore = Double.parseDouble(scoreStr);
                        } catch (Exception e) {

                        }

                        if (compositeScore >= 80) {
                            passComposite++;
                            String companyPassCompositeStr = companyPass.getString(rootOrg.getName()+"_COMPOSITE_PASS");
                            int companyPassComposite = 0;

                            if (StringUtils.isNotBlank(companyPassCompositeStr)) {
                                companyPassComposite = Integer.parseInt(companyPassCompositeStr);
                            }
                            companyPassComposite++;

                            companyPass.put(rootOrg.getName()+"_COMPOSITE_PASS", companyPassComposite + "");

                        }
                    }
                }

                companyScores.add(ConvertTool.toJSONObject(score));
            }

            response.set("COMPANY_PASS", companyPass);
            response.set("TOTAL_COMM", totalComm);
            response.set("TOTAL_PRO", totalPro);
            response.set("PASS_COMM", passComm);
            response.set("PASS_PRO", passPro);
            response.set("TOTAL_COMPOSITE", totalComposite);
            response.set("PASS_COMPOSITE", passComposite);
        }

        response.set("DATAS", ConvertTool.toJSONArray(scores));
        response.set("COMPANY_SCORES", company);

        return response;
    }

    public ServiceResponse queryPostJobScore(ServiceRequest request) throws Exception {
        ServiceResponse response=new ServiceResponse();
        ScoreDAO dao=DAOFactory.createDAO(ScoreDAO.class);
        String train_id = request.getString("TRAIN_ID");
        AppSession session = SessionManager.getSession();
        Map<String,String> parm=new HashMap<String,String>();


        String orgId = request.getString("ORG_ID");
        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();

        if(StringUtils.isBlank(orgId)){
            orgId = OrgBean.getOrgId(session.getSessionEntity());
            OrgEntity rootOrg = OrgBean.findEmployeeRoot(orgId, allOrgs);
            if(StringUtils.equals("122", rootOrg.getParentOrgId())) {
                orgId = "122";
            }
            else {
                orgId = rootOrg.getOrgId();
            }

            orgId = OrgBean.getOrgLine(orgId, allOrgs);

        }
        else {
            orgId = OrgBean.getOrgLine(orgId);
        }
        RecordSet scores=new RecordSet();


        parm.put("TRAIN_ID",train_id);
        Record trainRecord=dao.queryByPk("INS_TRAIN",parm);
        String type=trainRecord.get("TYPE");
        if(StringUtils.equals(type,"1")){
            scores=dao.queryPreWorkScore(request.getString("NAME"),orgId,train_id);
            scores=this.filterTrainScore(scores);
            int size = scores.size();

            for (int i=0;i<size;i++){
                Record record =scores.get(i);
                String signItem=record.get("SIGN_ITEM");
                String[] itemArray = signItem.split("\\|");
                for (String key : itemArray) {
                    if (StringUtils.isBlank(key)) {
                        continue;
                    }
                    if (StringUtils.equals("-1", key)) {
                        record.put("NEED_COMM","TRUE");
                        record.put("NEED_PRO","TRUE");

                    }
                    else if(StringUtils.equals("0", key)) {
                        record.put("NEED_COMM","TRUE");

                    }
                    else if(StringUtils.equals("1", key)) {
                        record.put("NEED_PRO","TRUE");

                    }
                    else {
                        record.put("NEED_COMM","FALSE");
                        record.put("NEED_PRO","FALSE");
                    }
                }

                record.put("TYPE",type);
            }
        }else{
            scores=dao.queryPostJobScore(request.getString("NAME"),orgId,train_id);
            if(scores==null || scores.size() <=0){
                return response;
            }

            int size = scores.size();

            for (int i=0;i<size;i++){
                Record record =scores.get(i);
                record.put("TYPE",type);
            }
        }

        JSONObject company = new JSONObject();

        if(ArrayTool.isNotEmpty(scores)) {
            int size = scores.size();
            List<OrgEntity> orgs = OrgBean.getAllOrgs();
            for(int i=0;i<size;i++) {
                Record score = scores.get(i);
                String employeeOrgId = score.get("ORG_ID");
                OrgEntity rootOrg = OrgBean.findEmployeeRoot(employeeOrgId, orgs);
                JSONArray companyScores = company.getJSONArray(rootOrg.getName());
                if(companyScores == null) {
                    companyScores = new JSONArray();
                    company.put(rootOrg.getName(), companyScores);
                }

                companyScores.add(ConvertTool.toJSONObject(score));
            }
        }
        response.set("DATAS", ConvertTool.toJSONArray(scores));
        response.set("COMPANY_SCORES", company);

        return response;
    }

    public static RecordSet filterTrainScore (RecordSet scores){
        if(scores==null || scores.size() <=0){
            return scores;
        }

        Map <String,Record> tempScore=new HashMap<String, Record>() ;
        int scoresSize =scores.size();
        for(int i=0;i<scoresSize;i++){
            Record score=scores.get(i);
            String employeeId=score.get("EMPLOYEE_ID");
            String item=score.get("ITEM");

            if(!tempScore.containsKey(employeeId)){
                score.put("ITEM_"+item,score.get("SCORE"));
                tempScore.put(employeeId,score);
            }else {
                tempScore.get(employeeId);
                tempScore.get(employeeId).put("ITEM_"+item,score.get("SCORE"));
                tempScore.put(employeeId,tempScore.get(employeeId));
            }


         }
        RecordSet rst=new RecordSet();
        Set <String> keys=tempScore.keySet();
        for(String key :keys){
            rst.add(tempScore.get(key));
        }
        return rst;
    }

    public ServiceResponse inputScore(ServiceRequest request) throws Exception {
        ServiceResponse response=new ServiceResponse();
        ScoreDAO dao=DAOFactory.createDAO(ScoreDAO.class);
        EmployeeJobRoleDAO employeeJobRoleDAO=DAOFactory.createDAO(EmployeeJobRoleDAO.class);
        TrainDAO trainDAO=DAOFactory.createDAO(TrainDAO.class);

        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        String train_id = request.getString("TRAIN_ID");

        RecordSet trainSet=trainDAO.queryPreWorks(train_id,false);


        List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();

        JSONObject obj=request.getBody().getData();
        Map<String, String> mobjec = ConvertTool.toMap(obj);
        String cols[]={"EMPLOYEE_ID","TRAIN_ID"};
        //取已经生成过转正时间待办的信息
        List<Map<String, String>> pendingList = new ArrayList<Map<String, String>>();
        Map<String, String> existRegularPending = new HashMap<>();
        //只有岗前培训才需要生成记录
        if(trainSet.size()>0) {
            RecordSet signEmployeeRecord = dao.queryPostJobScore(null, null, train_id);
            String signEmployeeIds = "";
            if (signEmployeeRecord.size() > 0) {
                for (int i = 0; i < signEmployeeRecord.size(); i++) {
                    Record signRecord = signEmployeeRecord.get(i);
                    signEmployeeIds = signEmployeeIds + signRecord.get("EMPLOYEE_ID") + ",";
                }
                RecordSet employeePendingRecordSet = dao.queryUpdateRegularPendingByEmployeeIds(signEmployeeIds.substring(0, signEmployeeIds.length() - 1));
                if (employeePendingRecordSet.size() > 0) {
                    for (int i = 0; i < employeePendingRecordSet.size(); i++) {
                        Record employeePendingRecord = employeePendingRecordSet.get(i);
                        existRegularPending.put(employeePendingRecord.get("EMPLOYEE_ID"), employeePendingRecord.get("ID"));
                    }
                }
            }
        }

        for (String key : mobjec.keySet()) {
            String value = mobjec.get(key);
            if (value == null || StringUtils.equals(key,"TRAIN_ID") || key.contains("late") || key.contains("money")) { // 过滤空的key
                continue;
            }
            Map<String, String> param = new HashMap<String, String>();
            Map<String, String> pending = new HashMap<String, String>();

            String[] arrys=key.split("\\_");

            param.put("EMPLOYEE_ID",arrys[0]);
            if(arrys.length>1) {
                param.put("ITEM", arrys[1]);
            }
            //只要员工录了成绩则给对应的人资发送一条需要修改转正时间的待办
/*            boolean isPass=false;
            if(StringUtils.isNotBlank(value)){
                BigDecimal data1 = new BigDecimal(value);
                BigDecimal data2 = new BigDecimal(80f);
                isPass=data1.compareTo(data2) < 0;
            }
            if(StringUtils.isBlank(value)|| isPass){*/
            if(trainSet.size()>0) {
                if (!existRegularPending.keySet().contains(arrys[0])) {
                    existRegularPending.put(arrys[0], "");
                    pending.put("EMPLOYEE_ID", arrys[0]);
                    pending.put("PENDING_TYPE", "6");
                    pending.put("START_TIME", session.getCreateTime());
                    pending.put("END_TIME", "3000-12-31 23:59:59");
                    pending.put("PENDING_STATUS", "1");
                    pending.put("PENDING_CREATE_ID", session.getSessionEntity().get("EMPLOYEE_ID"));
                    pending.put("REMARK", "员工考试成绩已出，请确认是否需要修改转正时间，如不需要则点击忽略按钮。");
                    List<EmployeeJobRoleEntity> list = employeeJobRoleDAO.queryJobRoleByEmployeeId(arrys[0]);
                    Record orgHrRel = dao.queryOrgHrRel(list.get(0).getOrgId());
                    if (orgHrRel == null) {
                        //找不到关系发给洪慧
                        pending.put("PENDING_EXECUTE_ID", "107");
                    } else {
                        pending.put("PENDING_EXECUTE_ID", orgHrRel.get("ARCHIVE_MANAGER_EMPLOYEE_ID"));
                    }
                    pending.put("CREATE_USER_ID", userId);
                    pending.put("CREATE_TIME", session.getCreateTime());
                    pending.put("UPDATE_USER_ID", userId);
                    pending.put("UPDATE_TIME", session.getCreateTime());

                    pendingList.add(pending);
                    //}
                }
            }

            param.put("TRAIN_ID",train_id);
            param.put("SCORE",value);
            param.put("CREATE_USER_ID",userId);
            param.put("CREATE_TIME",session.getCreateTime());
            param.put("UPDATE_USER_ID",userId);
            param.put("UPDATE_TIME",session.getCreateTime());
            parameters.add(param);
        }
        dao.deleteBatch("ins_train_exam_score",cols,parameters);
        dao.insertBatch("ins_train_exam_score",parameters);
        dao.insertBatch("ins_hr_pending",pendingList);

        Map<String, Map<String, String>> employeeExt = new HashMap<String, Map<String, String>>();

        for (String key : mobjec.keySet()) {
            if (!key.contains("late") && !key.contains("money")) {
                continue;
            }

            String[] arrys=key.split("\\_");
            String value = mobjec.get(key);

            if (StringUtils.isNotBlank(value)) {
                Map<String, String> ext = employeeExt.get(arrys[0]);

                if (ext == null) {
                    ext = new HashMap<String, String>();
                    ext.put("TRAIN_ID", train_id);
                    ext.put("EMPLOYEE_ID", arrys[0]);
                    ext.put("CREATE_USER_ID",userId);
                    ext.put("CREATE_TIME",session.getCreateTime());
                    ext.put("UPDATE_USER_ID",userId);
                    ext.put("UPDATE_TIME",session.getCreateTime());
                    employeeExt.put(arrys[0], ext);
                }

                if (key.contains("late")) {
                    ext.put("LATE_TIME", value);
                } else {
                    ext.put("MONEY", value);
                }

            }
        }

        if (!employeeExt.isEmpty()) {
            List<Map<String, String>> exts = new ArrayList<Map<String, String>>();

            Set<String> keySet = employeeExt.keySet();
            for (String key : keySet) {
                Map<String, String> ext = employeeExt.get(key);
                exts.add(ext);
            }

            if (ArrayTool.isNotEmpty(exts)) {
                dao.deleteBatch("ins_train_exam_score_ext",cols,exts);
                dao.insertBatch("ins_train_exam_score_ext",exts);
            }
        }

        return response;
    }
}