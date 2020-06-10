package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


@DatabaseName("ins")
public class ScoreDAO extends GenericDAO {

    public ScoreDAO(String databaseName) {
        super(databaseName);
    }


    public RecordSet queryExamScore(String name, String orgId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();

        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, e.city , f.NAME parent_org_name ,g.exam_id,g.score ");
        sb.append("from ins_user a, ins_employee b left join ins_exam_score g on (b.EMPLOYEE_ID = g.EMPLOYEE_ID) , ");
        sb.append("ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' ");
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");

        if (StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if (StringUtils.isNotBlank(orgId)) {
            sb.append("and d.org_id in ( " + orgId + ") ");
        }

        return this.queryBySql(sb.toString(), parameter);
    }


    public RecordSet queryPostJobScore(String name, String orgId, String train_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();

        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE,e.org_id, e.name org_name, e.city , f.NAME parent_org_name ,h.TRAIN_ID ,g.score ,it.name train_name, g.item, x.late_time, x.money   ");
        sb.append("from ins_user a, ins_employee b ,  ");
        sb.append("ins_train_sign h  ");
        sb.append("left join ins_train_exam_score g on (h.EMPLOYEE_ID=g.EMPLOYEE_ID AND g.train_id = h.train_id) ");
        sb.append("left join ins_train_exam_score_ext x on (x.train_id = h.train_id and x.employee_id = h.employee_id), ");

        sb.append("ins_employee_job_role d, ins_train it , ins_org e  ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' ");
        sb.append("and h.STATUS ='0' ");
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and h.EMPLOYEE_ID=b.EMPLOYEE_ID ");
        sb.append("and it.train_id=h.train_id ");


        if (StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if (StringUtils.isNotBlank(orgId)) {
            sb.append("and d.org_id in ( " + orgId + ") ");
        }

        if (StringUtils.isNotBlank(train_id)) {
            sb.append("and h.train_id = " + train_id + " ");
        }

        return this.queryBySql(sb.toString(), parameter);
    }


    public RecordSet queryPreWorkScore(String name, String orgId, String train_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();

        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE,e.org_id, e.name org_name, e.city , f.NAME parent_org_name ,h.TRAIN_ID ,g.score ,it.name train_name, g.item , i.ITEM sign_item, x.late_time, x.money   ");
        sb.append("from ins_user a, ins_employee b ,  ins_train_sign_item i , ");
        sb.append("ins_train_sign h  ");
        sb.append("left join ins_train_exam_score g on (h.EMPLOYEE_ID=g.EMPLOYEE_ID AND g.train_id = h.train_id) ");
        sb.append("left join ins_train_exam_score_ext x on (x.train_id = h.train_id and x.employee_id = h.employee_id), ");

        sb.append("ins_employee_job_role d, ins_train it , ins_org e  ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' ");
        sb.append("and i.status = '0' ");
        sb.append("and h.STATUS ='0' ");
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and h.EMPLOYEE_ID=b.EMPLOYEE_ID ");
        sb.append("and it.train_id=h.train_id ");
        sb.append("and i.SIGN_ID=h.SIGN_ID ");


        if (StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if (StringUtils.isNotBlank(orgId)) {
            sb.append("and d.org_id in ( " + orgId + ") ");
        }

        if (StringUtils.isNotBlank(train_id)) {
            sb.append("and h.train_id = " + train_id + " ");
        }

        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryUpdateRegularPendingByEmployeeIds(String employeeIds) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from ins_hr_pending where pending_status='1' and pending_type='6' and employee_id in " +
                "(" + employeeIds + ") ");
        return this.queryBySql(sb.toString(), new HashMap<>());
    }

    public Record queryOrgHrRel(String orgId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from ins_org_hr_rel where  org_id in " +
                "(" + orgId + ") and end_time > now() ");
        RecordSet recordSet = this.queryBySql(sb.toString(), new HashMap<>());
        if (recordSet.size() > 0) {
            return recordSet.get(0);
        }
        return null;
    }
}