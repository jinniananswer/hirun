package com.hirun.app.biz.common;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.cust.CustBean;
import com.hirun.app.bean.plan.PlanStatBean;
import com.hirun.app.dao.common.HisPerformDueTaskDAO;
import com.hirun.app.dao.common.PerformDueTaskDAO;
import com.hirun.pub.domain.entity.common.PerformDueTaskEntity;
import com.hirun.pub.domain.entity.plan.PlanDayEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.SessionManager;
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
                    } else if(entity.getTaskType().equals("CUST_RESTORE")){
                        String custId = params.getString("CUST_ID");
                        CustBean.restore(custId);
                    } else if(entity.getTaskType().equals("PLAN_FINISH_STAT")) {
                        PlanDayEntity employeePlanDayEntity = new PlanDayEntity();
                        employeePlanDayEntity.setStatDay(params.getString("STAT_DAY"));
                        employeePlanDayEntity.setStatType("EMPLOYEE_FINISH");
                        employeePlanDayEntity.setObjectId(params.getString("EMPLOYEE_ID"));
                        employeePlanDayEntity.setStatResult(params.getString("PLAN_FINISH_INFO"));
                        PlanStatBean.saveStatPlanDayEntityByEmployee(employeePlanDayEntity);
                    }

                    entity.setDealTag("1");
                    hisDao.insert("INS_HIS_PERFORM_DUE_TASK", entity.getContent());
                    dao.delete("INS_PERFORM_DUE_TASK", entity.getContent());

                    SessionManager.getSession().commit();
                } catch(Exception e) {
                    SessionManager.getSession().rollback();
                    e.printStackTrace();
                    String errorMessage = e.getMessage();
                    if(errorMessage != null) {
                        if(errorMessage.length() < 2000) {
                            entity.setResultInfo(errorMessage);
                        } else {
                            entity.setResultInfo(errorMessage.substring(0,1999));
                        }
                    }
                    entity.setResultCode("-1");
                    entity.setDealTag("-1");
                    dao.update("INS_PERFORM_DUE_TASK", entity.getContent());
                    SessionManager.getSession().commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
