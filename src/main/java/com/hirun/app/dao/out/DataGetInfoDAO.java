package com.hirun.app.dao.out;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;

/**
 * Created by pc on 2018-05-21.
 */
@DatabaseName("out")
public class DataGetInfoDAO extends StrongObjectDAO {
    public DataGetInfoDAO(String databaseName) {
        super(databaseName);
    }
}
