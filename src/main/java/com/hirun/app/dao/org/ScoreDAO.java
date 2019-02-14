package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@DatabaseName("ins")
public class ScoreDAO extends GenericDAO {

    public ScoreDAO(String databaseName) {
        super(databaseName);
    }


    public RecordSet queryExamScore(String name,String orgId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();

        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, e.city , f.NAME parent_org_name ,g.exam_id,g.score ");
        sb.append("from ins_user a, ins_employee b, ins_employee_job_role d,ins_exam_score g ,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' " );
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and b.EMPLOYEE_ID = g.EMPLOYEE_ID ");

        if(StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if(StringUtils.isNotBlank(orgId)){
            sb.append("and d.org_id in ( "+orgId+") ");
        }

        return this.queryBySql(sb.toString(),parameter);
    }


    public RecordSet queryPostJobScore(String name,String orgId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();

        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, e.city , f.NAME parent_org_name ,g.TRAIN_ID,g.score ");
        sb.append("from ins_user a, ins_employee b ");
        sb.append("left join ins_train_exam_score g on(b.EMPLOYEE_ID=g.EMPLOYEE_ID) , ");
        sb.append("ins_employee_job_role d, ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' " );
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and b.EMPLOYEE_ID = g.EMPLOYEE_ID ");

        if(StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if(StringUtils.isNotBlank(orgId)){
            sb.append("and d.org_id in ( "+orgId+") ");
        }

        return this.queryBySql(sb.toString(),parameter);
    }
}