package com.hirun.app.dao.common;

import com.hirun.pub.domain.entity.common.PerformDueTaskEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-06-09.
 */
@DatabaseName("ins")
public class HisPerformDueTaskDAO extends StrongObjectDAO{

    public HisPerformDueTaskDAO(String databaseName) {
        super(databaseName);
    }
}
