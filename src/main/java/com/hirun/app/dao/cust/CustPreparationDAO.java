package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.Record;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author liuhui7
 * @Date 2020/2/27 9:56
 * @Description:
 */
@DatabaseName("ins")
public class CustPreparationDAO extends StrongObjectDAO {

    public CustPreparationDAO(String databaseName){
        super(databaseName);
    }

    public Record getCustomerPrepare(String prepareId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ID", prepareId);
        return this.queryByPk("cust_preparation",parameter);
    }

}
