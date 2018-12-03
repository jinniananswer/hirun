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
 * @create: 2018-12-03 17:40
 **/
@DatabaseName("ins")
public class TeacherDAO extends GenericDAO {

    public TeacherDAO(String databaseName) {
        super(databaseName);
    }


    public RecordSet queryTeachers(String teacherName, String courseId, String teacherId) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.teacher_id, t.id, t.type, t.level, t.qq_no, t.wechat_no, t.pic, e.name teacher_name, e.employee_id, o.name org_name, j.job_role, c.name course_name, c.course_id  ");
        sql.append(" from ins_teacher t, ins_employee e, ins_org o, ins_employee_job_role j, ins_teacher_course_rel r, ins_course c");
        sql.append(" where e.employee_id = t.id ");
        sql.append(" and t.type = '0' ");
        sql.append(" and e.status = '0' ");
        sql.append(" and j.employee_id = e.employee_id ");
        sql.append(" and now() < j.end_date ");
        sql.append(" and o.org_id = j.org_id ");
        sql.append(" and r.teacher_id = t.teacher_id ");
        sql.append(" and c.course_id = r.course_id ");
        sql.append(" and c.status = '0' ");
        sql.append(" and r.status = '0' ");
        if(StringUtils.isNotBlank(teacherName)) {
            sql.append(" and e.name like CONCAT('%',:NAME,'%') ");
            parameter.put("NAME", teacherName);
        }

        if(StringUtils.isNotBlank(courseId)) {
            sql.append(" and exists (select 1 from ins_teacher_course_rel r1, ins_course c1 where c1.course_id = r1.course_id and r1.status = '0' and c1.status = '0' and r1.teacher_id = t.teacher_id and r1.course_id = :COURSE_ID  ) ");
            parameter.put("COURSE_ID", courseId);
        }

        if(StringUtils.isNotBlank(teacherId)) {
            sql.append(" and t.teacher_id = :TEACHER_ID ");
            parameter.put("TEACHER_ID", teacherId);
        }

        return this.queryBySql(sql.toString(), parameter);
    }

    public void deleteTeacherCourseRel(String teacherId) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TEACHER_ID", teacherId);
        StringBuilder sql = new StringBuilder();
        sql.append("update ins_teacher_course_rel t set t.status = '1' ");
        sql.append("where t.teacher_id = :TEACHER_ID ");
        this.executeUpdate(sql.toString(), parameter);
    }
}
