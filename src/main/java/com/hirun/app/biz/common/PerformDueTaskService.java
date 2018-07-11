package com.hirun.app.biz.common;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.cust.CustBean;
import com.hirun.app.dao.common.HisPerformDueTaskDAO;
import com.hirun.app.dao.common.PerformDueTaskDAO;
import com.hirun.pub.domain.entity.common.PerformDueTaskEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;

import java.util.List;

/**
 * Created by awx on 2018/7/6/006.
 */
public class PerformDueTaskService extends GenericService{

    public ServiceResponse deal(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        PerformDueTaskDAO dao = DAOFactory.createDAO(PerformDueTaskDAO.class);
        HisPerformDueTaskDAO hisDao = DAOFactory.createDAO(HisPerformDueTaskDAO.class);
        List<PerformDueTaskEntity> list = dao.queryPerformDueTaskList();
        try{
            for(PerformDueTaskEntity entity : list) {
                try{
                    JSONObject params = JSONObject.parseObject(entity.getParams());
                    if(entity.getTaskType().equals("CUST_REMIND")) {
                        String custContactId = params.getString("CUST_CONTACT_ID");
                        CustBean.remind(custContactId);
                    } else {
                        String custId = params.getString("CUST_ID");
                        CustBean.restore(custId);
                    }

                    entity.setDealTag("1");
                    hisDao.insert("INS_HIS_PERFORM_DUE_TASK", entity.getContent());
                    dao.delete("INS_PERFORM_DUE_TASK", entity.getContent());
                } catch(Exception e) {
                    e.printStackTrace();
                    entity.setResultInfo(e.getMessage());
                    entity.setResultCode("-1");
                    dao.update("INS_PERFORM_DUE_TASK", entity.getContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
