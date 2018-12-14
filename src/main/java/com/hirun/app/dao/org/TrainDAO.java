package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;

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

}
