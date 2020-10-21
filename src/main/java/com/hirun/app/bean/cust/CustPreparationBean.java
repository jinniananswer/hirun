package com.hirun.app.bean.cust;

import com.hirun.app.dao.cust.CustPreparationDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.Record;
import com.most.core.pub.tools.time.TimeTool;

import java.util.HashMap;
import java.util.Map;

public class CustPreparationBean {
       public static Record getCustomerPrepare(String prepareId) throws Exception{
           CustPreparationDAO dao= DAOFactory.createDAO(CustPreparationDAO.class);
           return dao.getCustomerPrepare(prepareId);
       }

       public static void updateCustomerPrepare(String prepareId,String status,String updateUserId) throws  Exception{
           CustPreparationDAO dao= DAOFactory.createDAO(CustPreparationDAO.class);
            Map<String,String> param=new HashMap<>();
            param.put("ID",prepareId);
            param.put("STATUS",status);
            param.put("UPDATE_USER_ID",updateUserId);
            param.put("UPDATE_TIME", TimeTool.now());
            dao.save("cust_preparation",new String[]{"ID"},param);
       }
}
