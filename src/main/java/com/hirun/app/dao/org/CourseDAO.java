package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.test.context.jdbc.Sql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-10-27 16:44
 **/
@DatabaseName("ins")
public class CourseDAO extends GenericDAO {

    public CourseDAO(String databaseName) {
        super(databaseName);
    }

    public RecordSet queryCoursesByType(String type) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TYPE", type);
        parameter.put("STATUS", "0");
        return this.query("ins_course", parameter);
    }

    public RecordSet queryAllValidCourse() throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("STATUS", "0");
        return this.query("ins_course", parameter);
    }

    public void deleteCoursesById(String courseIds) throws SQLException {

        StringBuilder sb = new StringBuilder();
        sb.append("delete from ins_course ");
        sb.append("where course_id in ("+courseIds+") ");

        this.executeUpdate(sb.toString(), null);
    }
}
