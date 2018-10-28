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
}
