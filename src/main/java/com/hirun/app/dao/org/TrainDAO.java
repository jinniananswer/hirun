package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;
import org.apache.commons.lang3.StringUtils;

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

    public RecordSet queryTrains(String trainId) throws SQLException {
        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        sql.append("select a.train_id, a.name train_name,a.train_desc, a.train_address, a.hotel_address, date_format(a.start_date, '%Y-%m-%d') start_date, date_format(a.end_date, '%Y-%m-%d') end_date,a.charge_employee_id,c.course_id, c.name course_name,d.employee_id, d.name employee_name  ");
        sql.append("from ins_train a, ins_train_course_rel b, ins_course c, ins_employee d ");
        sql.append("where b.train_id = a.train_id ");
        sql.append("and c.course_id = b.course_id ");
        sql.append("and d.employee_id = a.charge_employee_id ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        sql.append("and c.status = '0' ");
        sql.append("and d.status = '0' ");
        sql.append("and a.end_date >= now() ");
        if (StringUtils.isNotBlank(trainId)) {
            sql.append("and a.train_id = :TRAIN_ID ");
            parameter.put("TRAIN_ID", trainId);
        }

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
}
