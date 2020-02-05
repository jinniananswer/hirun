package com.hirun.app.dao.order;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;

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
}
