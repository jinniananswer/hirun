package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustContactEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by awx on 2018/6/20/020.
 */
@DatabaseName("ins")
public class CustContactDAO extends StrongObjectDAO{

    public CustContactDAO(String databaseName) {
        super(databaseName);
    }

    public List<CustContactEntity> queryCustContactEntityListByCustId(String custId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);

        return this.query(CustContactEntity.class, "INS_CUST_CONTACT", parameter);
    }

    public CustContactEntity getCustContactEntityByCustContactId(String custContactId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_CONTACT_ID", custContactId);

        List<CustContactEntity> list = this.query(CustContactEntity.class, "INS_CUST_CONTACT", parameter);
        if(ArrayTool.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
