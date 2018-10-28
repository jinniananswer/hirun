package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;

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
}
