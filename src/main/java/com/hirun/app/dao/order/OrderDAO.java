package com.hirun.app.dao.order;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: hirun
 * @description: 订单数据操作类
 * @author: jinnian
 * @create: 2020-02-05 21:12
 **/
@DatabaseName("ins")
public class OrderDAO extends StrongObjectDAO {

    public OrderDAO(String databaseName) {
        super(databaseName);
    }

    public RecordSet queryOrderWork(String orderId,String roleId,String employeeId) throws Exception{
        StringBuilder sql = new StringBuilder();
        Map<String,String> param=new HashMap<>();
        sql.append(" SELECT * FROM ORDER_WORKER  " +
                " WHERE ORDER_ID=:ORDER_ID AND ROLE_ID=:ROLE_ID AND EMPLOYEE_ID=:EMPLOYEE_ID AND (NOW() BETWEEN START_DATE AND END_DATE) ");
        param.put("ORDER_ID",orderId);
        param.put("ROLE_ID",roleId);
        param.put("EMPLOYEE_ID",employeeId);
        return this.queryBySql(sql.toString(),param);
    }

    public RecordSet queryOrderConsult(String orderId) throws Exception{
        StringBuilder sql = new StringBuilder();
        Map<String,String> param=new HashMap<>();
        sql.append(" SELECT * FROM order_consult  " +
                " WHERE ORDER_ID=:ORDER_ID  ");
        param.put("ORDER_ID",orderId);
        return this.queryBySql(sql.toString(),param);
    }
}
