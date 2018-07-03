package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.cust.CustOriginalActionEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 9:56
 * @Description:
 */
@DatabaseName("ins")
public class CustOriginalActionDAO extends StrongObjectDAO {

    public CustOriginalActionDAO(String databaseName){
        super(databaseName);
    }

    public CustOriginalActionEntity getCustOriginalActionEntityByOutIdAndActionCodeAndCid(String custId, String outId, String actionCode) throws Exception {
        Map<String, String> dbParam = new HashMap<String, String>();
        dbParam.put("OUT_ID", outId);
        dbParam.put("ACTION_CODE", actionCode);
        dbParam.put("CUST_ID", custId);

        List<CustOriginalActionEntity> custOriginalActionEntityList = this.query(CustOriginalActionEntity.class, "INS_CUST_ORIGINAL_ACTION", dbParam);
        if(ArrayTool.isNotEmpty(custOriginalActionEntityList)) {
            return custOriginalActionEntityList.get(0);
        }

        return null;
    }

    public List<CustOriginalActionEntity> getCustOriginalActionEntityByActionCodeAndCidAndEid(String custId, String actionCode, String employeeId) throws Exception {
        Map<String, String> dbParam = new HashMap<String, String>();
        dbParam.put("ACTION_CODE", actionCode);
        dbParam.put("CUST_ID", custId);
        dbParam.put("EMPLOYEE_ID", employeeId);

        return this.query(CustOriginalActionEntity.class, "INS_CUST_ORIGINAL_ACTION", dbParam);
    }

    public List<CustOriginalActionEntity> getCustOriginalActionEntityByCustId(String custId) throws Exception {
        Map<String, String> dbParam = new HashMap<String, String>();
        dbParam.put("CUST_ID", custId);

        return this.query(CustOriginalActionEntity.class, "INS_CUST_ORIGINAL_ACTION", dbParam);
    }
}
