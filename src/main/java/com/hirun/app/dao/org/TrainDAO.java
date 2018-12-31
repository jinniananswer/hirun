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
        sql.append("select a.train_id, a.name train_name,a.train_desc, a.train_address, a.hotel_address, date_format(a.start_date, '%Y-%m-%d') start_date, date_format(a.end_date, '%Y-%m-%d') end_date,a.charge_employee_id,a.status, a.sign_status, c.course_id, c.name course_name,d.employee_id, d.name employee_name  ");
        sql.append("from ins_train a, ins_train_course_rel b, ins_course c, ins_employee d ");
        sql.append("where b.train_id = a.train_id ");
        sql.append("and c.course_id = b.course_id ");
        sql.append("and d.employee_id = a.charge_employee_id ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        sql.append("and c.status = '0' ");
        sql.append("and d.status = '0' ");
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

        sql.append("select a.train_id, a.name train_name,a.train_desc, a.train_address, a.hotel_address, date_format(a.start_date, '%Y-%m-%d') start_date, date_format(a.end_date, '%Y-%m-%d') end_date,a.charge_employee_id,a.status, a.sign_status, c.course_id, c.name course_name,d.employee_id, d.name employee_name,e.employee_id sign_employee_id,e.status employee_sign_status   ");
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
        sql.append("select a.train_id, a.employee_id,a.status, b.name, c.org_id, c.job_role, d.name org_name, e.name enterprise_name ");
        sql.append("from ins_train_sign a, ins_employee b, ins_employee_job_role c, ins_org d, ins_enterprise e ");
        sql.append("where b.employee_id = a.employee_id ");
        sql.append("and c.employee_id = a.employee_id ");
        sql.append("and d.org_id = c.org_id ");
        sql.append("and e.enterprise_id = d.enterprise_id ");
        sql.append("and b.status = '0' ");
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
}
