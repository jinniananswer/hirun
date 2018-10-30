package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-10-27 17:45
 **/
@DatabaseName("ins")
public class CourseFileDAO extends GenericDAO {

    public CourseFileDAO(String databaseName) {
        super(databaseName);
    }

    public RecordSet queryCourseFilesByCourseType(String type) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TYPE", type);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.*, b.name course_name from ins_course_file a, ins_course b ");
        sql.append("where a.course_id = b.course_id ");
        sql.append("and b.type = :TYPE ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryCourseFilesByCourseId(String courseId) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("COURSE_ID", courseId);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.*, b.name course_name from ins_course_file a, ins_course b ");
        sql.append("where a.course_id = b.course_id ");
        sql.append("and b.course_id = :COURSE_ID ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        return this.queryBySql(sql.toString(), parameter);
    }
}