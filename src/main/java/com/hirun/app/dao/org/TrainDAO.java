package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.context.jdbc.Sql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-12-13 18:08
 **/
@DatabaseName("ins")
public class TrainDAO extends GenericDAO {

    public TrainDAO(String databaseName) {
        super(databaseName);
    }

    public RecordSet queryTrains(String trainId, boolean isValid) throws SQLException {
        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        sql.append("select a.train_id, a.name train_name,a.type, a.train_desc, a.train_address, a.hotel_address, date_format(a.start_date, '%Y-%m-%d') start_date, date_format(a.end_date, '%Y-%m-%d') end_date,a.charge_employee_id,a.status, a.sign_status, c.course_id, c.name course_name,d.employee_id, d.name employee_name  ");
        sql.append("from ins_train a, ins_train_course_rel b, ins_course c, ins_employee d ");
        sql.append("where b.train_id = a.train_id ");
        sql.append("and c.course_id = b.course_id ");
        sql.append("and d.employee_id = a.charge_employee_id ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        sql.append("and c.status = '0' ");
        sql.append("and d.status = '0' ");
        sql.append("and a.type in ('2','3') ");
        if(isValid) {
            sql.append("and a.end_date >= now() ");
        }
        if (StringUtils.isNotBlank(trainId)) {
            sql.append("and a.train_id = :TRAIN_ID ");
            parameter.put("TRAIN_ID", trainId);
        }

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryPreWorks(String trainId, boolean isValid) throws SQLException {
        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        sql.append("select a.train_id, a.name train_name,a.type, a.train_desc, a.train_address, a.hotel_address, date_format(a.start_date, '%Y-%m-%d') start_date, date_format(a.end_date, '%Y-%m-%d') end_date,a.charge_employee_id,a.status, a.sign_status ");
        sql.append("from ins_train a ");
        sql.append("where a.type = '1' ");
        sql.append("and a.status = '0' ");
        if(isValid) {
            sql.append("and a.end_date >= now() ");
        }
        if (StringUtils.isNotBlank(trainId)) {
            sql.append("and a.train_id = :TRAIN_ID ");
            parameter.put("TRAIN_ID", trainId);
        }

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryTrainsBySign(String employeeId) throws SQLException {
        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);

        sql.append("select a.train_id, a.name train_name,a.type,a.train_desc, a.train_address, a.hotel_address, date_format(a.start_date, '%Y-%m-%d') start_date, date_format(a.end_date, '%Y-%m-%d') end_date,a.charge_employee_id,a.status, a.sign_status, c.course_id, c.name course_name,d.employee_id, d.name employee_name,e.employee_id sign_employee_id,e.status employee_sign_status   ");
        sql.append("from ins_train a ");
        sql.append("left join ins_train_sign e on (e.train_id = a.train_id and e.employee_id = :EMPLOYEE_ID) ");
        sql.append(", ins_train_course_rel b, ins_course c, ins_employee d ");
        sql.append("where b.train_id = a.train_id ");
        sql.append("and c.course_id = b.course_id ");
        sql.append("and d.employee_id = a.charge_employee_id ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        sql.append("and c.status = '0' ");
        sql.append("and d.status = '0' ");
        sql.append("and a.type in ('2','3') ");
        sql.append("and a.end_date >= now() ");

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet querySchedules(String trainId) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.train_id,a.course_id, a.nature, a.course_name, a.teacher_id, date_format(a.start_date, '%Y-%m-%d %H:%i:%S') start_date, date_format(a.end_date, '%Y-%m-%d %H:%i:%S') end_date, b.name teacher_name ");
        sql.append("from ins_schedule a ");
        sql.append("left join ins_teacher b on (b.teacher_id = a.teacher_id) ");
        sql.append("where a.train_id = :TRAIN_ID ");
        sql.append("and a.status = '0' ");
        sql.append("order by start_date asc ");

        return this.queryBySql(sql.toString(), parameter);
    }

    public void deleteTrainCourseRelByTrainId(String trainId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        StringBuilder sql = new StringBuilder();
        sql.append("update ins_train_course_rel set status = '1' ");
        sql.append("where train_id = :TRAIN_ID ");

        this.executeUpdate(sql.toString(), parameter);
    }

    public void deleteScheduleByTrainId(String trainId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        StringBuilder sql = new StringBuilder();
        sql.append("update ins_schedule set status = '1' ");
        sql.append("where train_id = :TRAIN_ID ");

        this.executeUpdate(sql.toString(), parameter);
    }

    public RecordSet querySignList(String trainId, String orgs) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.train_id, a.employee_id,a.status, b.name,b.identity_no, b.sex,b.major, b.school, b.education_level,date_format(b.in_date, '%Y-%m-%d') in_date, c.org_id, c.job_role, d.name org_name, e.name enterprise_name, f.mobile_no ");
        sql.append("from ins_train_sign a, ins_employee b, ins_employee_job_role c, ins_org d, ins_enterprise e, ins_user f ");
        sql.append("where b.employee_id = a.employee_id ");
        sql.append("and c.employee_id = a.employee_id ");
        sql.append("and d.org_id = c.org_id ");
        sql.append("and e.enterprise_id = d.enterprise_id ");
        sql.append("and f.user_id = b.user_id ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        sql.append("and (now() between c.start_date and c.end_date ) ");
        sql.append("and a.train_id = :TRAIN_ID ");

        if(StringUtils.isNotBlank(orgs)) {
            sql.append(" and c.org_id in ("+orgs+") ");
        }

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryPreworkSignList(String trainId, String orgs) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.train_id, a.employee_id,a.status, b.name, b.identity_no, b.sex,b.major, b.school, b.education_level,date_format(b.in_date, '%Y-%m-%d') in_date, c.org_id, c.job_role, d.name org_name, e.name enterprise_name, f.type, f.item, g.mobile_no ");
        sql.append("from ins_train_sign a, ins_employee b, ins_employee_job_role c, ins_org d, ins_enterprise e, ins_train_sign_item f, ins_user g ");
        sql.append("where b.employee_id = a.employee_id ");
        sql.append("and c.employee_id = a.employee_id ");
        sql.append("and d.org_id = c.org_id ");
        sql.append("and e.enterprise_id = d.enterprise_id ");
        sql.append("and f.sign_id = a.sign_id ");
        sql.append("and g.user_id = b.user_id ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        sql.append("and f.status = '0' ");
        sql.append("and (now() between c.start_date and c.end_date ) ");
        sql.append("and a.train_id = :TRAIN_ID ");

        if(StringUtils.isNotBlank(orgs)) {
            sql.append(" and c.org_id in ("+orgs+") ");
        }

        return this.queryBySql(sql.toString(), parameter);
    }

    public void updateSignStatus(String employeeIds, String trainId, String status, String userId, String time) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);
        parameter.put("STATUS", status);
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", time);

        StringBuilder sql = new StringBuilder();
        sql.append("update ins_train_sign ");
        sql.append("set status = :STATUS, ");
        sql.append("update_user_id = :UPDATE_USER_ID, ");
        sql.append("update_time = :UPDATE_TIME ");
        sql.append("where train_id = :TRAIN_ID ");
        sql.append("and employee_id in ("+employeeIds+") ");

        this.executeUpdate(sql.toString(), parameter);
    }

    public void deleteSign(String employeeIds, String trainId, String signEmployeeId) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);
        parameter.put("SIGN_EMPLOYEE_ID", signEmployeeId);

        StringBuilder sql = new StringBuilder();
        sql.append("delete from ins_train_sign ");
        sql.append("where train_id = :TRAIN_ID ");
        sql.append("and sign_employee_id = :SIGN_EMPLOYEE_ID ");
        sql.append("and employee_id in ("+employeeIds+") ");

        this.executeUpdate(sql.toString(), parameter);
    }

    public RecordSet queryEmployeeBySignEmployeeId(String trainId, String signEmployeeId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);
        parameter.put("SIGN_EMPLOYEE_ID", signEmployeeId);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.train_id, a.employee_id, b.name, b.sex ");
        sql.append("from ins_train_sign a, ins_employee b ");
        sql.append("where b.employee_id = a.employee_id ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        sql.append("and a.train_id = :TRAIN_ID ");
        sql.append("and a.sign_employee_id = :SIGN_EMPLOYEE_ID ");

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryPreworkEmployeeBySignEmployeeId(String trainId, String signEmployeeId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);
        parameter.put("SIGN_EMPLOYEE_ID", signEmployeeId);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.train_id, a.employee_id, b.name, b.sex, c.type, c.item ");
        sql.append("from ins_train_sign a, ins_employee b, ins_train_sign_item c ");
        sql.append("where b.employee_id = a.employee_id ");
        sql.append("and c.sign_id = a.sign_id ");
        sql.append("and c.status = '0' ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        sql.append("and a.train_id = :TRAIN_ID ");
        sql.append("and a.sign_employee_id = :SIGN_EMPLOYEE_ID ");

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryNeedSignPreTrainEmployee(String trainId, boolean needOverThirty, String orgId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,date_format(b.in_date,'%Y-%m-%d') in_date ,a.mobile_no contact_no, d.JOB_ROLE,e.org_id, e.name org_name ");
        sql.append("from ins_user a, ins_employee b, ins_employee_job_role d,ins_org e ");
        sql.append("where b.USER_ID = a.USER_ID ");
        sql.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' " );
        sql.append("and now() < d.end_date ");
        sql.append("and e.ORG_ID = d.ORG_ID ");
        sql.append("and b.regular_date > now() ");
        sql.append("and e.org_id in ("+orgId+") ");
        if(needOverThirty) {
            sql.append("and date_add(now(), interval -30 day) > b.in_date ");
        }
        sql.append("and not exists(select 1 from ins_train_sign f where f.employee_id = b.employee_id and f.train_id = :TRAIN_ID and f.status = '0') ");

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryNeedSignPreWorkEmployee(String trainId, boolean needOverSeventyFive, boolean needPassExam, String orgId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,date_format(b.in_date,'%Y-%m-%d') in_date,a.mobile_no contact_no, d.JOB_ROLE, e.org_id, e.name org_name ");
        sql.append("from ins_user a, ins_employee b, ins_employee_job_role d,ins_org e ");
        sql.append("where b.USER_ID = a.USER_ID ");
        sql.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' " );
        sql.append("and now() < d.end_date ");
        sql.append("and e.ORG_ID = d.ORG_ID ");
        sql.append("and b.regular_date > now() ");
        sql.append("and e.org_id in ("+orgId+") ");
        if (needOverSeventyFive) {
            sql.append("and date_add(now(), interval - 75 day) > b.in_date ");
        }
        sql.append("and not exists(select 1 from ins_train_sign f where f.employee_id = b.employee_id and f.train_id = :TRAIN_ID and f.status = '0') ");
        if (needPassExam) {
            sql.append("and exists(select 1 from ins_exam_score g where g.employee_id = b.employee_id and g.exam_id = 1 and f.score >= 80) ");
            sql.append("and exists(select 1 from ins_exam_score h where h.employee_id = b.employee_id and h.exam_id = 2 and g.score >= 80) ");
            sql.append("and exists(select 1 from ins_exam_score i where i.employee_id = b.employee_id and i.exam_id = 3 and h.score >= 80) ");
            sql.append("and exists(select 1 from ins_exam_score j where j.employee_id = b.employee_id and j.exam_id = 4 and i.score >= 80) ");
            sql.append("and exists(select 1 from ins_exam_score k where k.employee_id = b.employee_id and k.exam_id = 5 and j.score >= 80) ");
            sql.append("and exists(select 1 from ins_train l, ins_train_exam_score m where m.train_id = l.train_id and l.type = '2' and l.status = '0' and m.employee_id = b.employee_id and m.score >= 80 ) ");
        }

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryMyTrain(String employeeId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.train_id, a.name train_name, a.type train_type, date_format(a.start_date,'%Y-%m-%d') start_date, date_format(a.end_date,'%Y-%m-%d') end_date ");
        sql.append("from ins_train a, ins_train_sign b ");
        sql.append("where b.train_id = a.train_id ");
        sql.append("and a.status = '0' ");
        sql.append("and a.sign_status = '1' ");
        sql.append("and b.status = '0' ");
        sql.append("and a.end_date > now() ");
        sql.append("and b.employee_id = :EMPLOYEE_ID ");

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryCourseByTrainId(String trainId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        StringBuilder sql = new StringBuilder();
        sql.append("select c.name ");
        sql.append("from ins_train a, ins_train_course_rel b, ins_course c ");
        sql.append("where b.train_id = a.train_id ");
        sql.append("and c.course_id = b.course_id ");
        sql.append("and b.status = '0' ");
        sql.append("and c.status = '0' ");
        sql.append("and a.train_id = :TRAIN_ID ");

        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryOnlineScore(String employeeId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);

        StringBuilder sql = new StringBuilder();
        sql.append("select exam_id, max(score) score ");
        sql.append("from ins_exam_score ");
        sql.append("where employee_id = :EMPLOYEE_ID ");
        sql.append("and exam_id <= 5 ");
        sql.append("group by exam_id ");
        sql.append("order by exam_id asc ");

        return this.queryBySql(sql.toString(), parameter);
    }
}
