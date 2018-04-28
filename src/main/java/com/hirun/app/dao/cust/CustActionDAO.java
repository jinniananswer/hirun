package com.hirun.app.dao.cust;

import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 9:56
 * @Description:
 */
public class CustActionDAO extends StrongObjectDAO {

    public CustActionDAO(String databaseName){
        super(databaseName);
    }

    public List<CustomerEntity> queryCustList() throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSE_COUNSELOR_ID", "123");
        List<CustomerEntity> customerList = this.query(CustomerEntity.class, "INS_CUSTOMER", parameter);

        return customerList;
    }
}
