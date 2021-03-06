package com.hirun.app.dao.plan;

import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
@DatabaseName("ins")
public class PlanWorkDAO extends StrongObjectDAO {

    public PlanWorkDAO(String databaseName){
        super(databaseName);
    }
}
