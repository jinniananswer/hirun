package com.hirun.app.dao.cust;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;

/**
 * Created by awx on 2018/6/20/020.
 */
@DatabaseName("ins")
public class CustContactDAO extends StrongObjectDAO{

    public CustContactDAO(String databaseName) {
        super(databaseName);
    }
}
