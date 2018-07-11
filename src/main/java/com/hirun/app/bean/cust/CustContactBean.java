package com.hirun.app.bean.cust;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.common.PerformDueTaskBean;
import com.hirun.app.dao.cust.CustContactDAO;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.task.PerformDueTask;
import com.hirun.pub.domain.entity.common.PerformDueTaskEntity;
import com.hirun.pub.domain.entity.cust.CustContactEntity;
import com.hirun.pub.domain.enums.cust.CustStatus;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by awx on 2018/6/20/020.
 */
public class CustContactBean {

    public static void addCustContact(CustContactEntity custContactEntity) throws Exception {
        CustContactDAO custContactDAO = DAOFactory.createDAO(CustContactDAO.class);
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();
        String now = TimeTool.now();

        //插入ins_cust_contact
        custContactEntity.setCreateDate(now);
        custContactEntity.setCreateUserId(userId);
        long custContactId = custContactDAO.insertAutoIncrement("INS_CUST_CONTACT", custContactEntity.getContent());

        //更新ins_customer
        if(StringUtils.isNotBlank(custContactEntity.getRestoreDate())) {
            String custId = custContactEntity.getCustId();
            Map<String, String> dbParam = new HashMap<String, String>();
            dbParam.put("CUST_ID", custId);
            dbParam.put("CUST_STATUS", CustStatus.pause.getValue());
            dbParam.put("RESTORE_DATE", custContactEntity.getRestoreDate());
            custDAO.save("INS_CUSTOMER", dbParam);
        }

        //记到期执行
        PerformDueTaskEntity performDueTaskEntity = new PerformDueTaskEntity();
        performDueTaskEntity.setObjectId(custContactEntity.getCustId());
        performDueTaskEntity.setObjectType("CUST");
        performDueTaskEntity.setCreateDate(now);
        performDueTaskEntity.setCreateUserId(userId);
        performDueTaskEntity.setDealTag("0");
        if(StringUtils.isNotBlank(custContactEntity.getRestoreDate())) {
            performDueTaskEntity.setTaskType("CUST_RESTORE");
            performDueTaskEntity.setExecTime(custContactEntity.getRestoreDate());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CUST_ID", custContactEntity.getCustId());
            performDueTaskEntity.setParams(jsonObject.toJSONString());
            PerformDueTaskBean.addPerformDueTask(performDueTaskEntity);
        } else if(StringUtils.isNotBlank(custContactEntity.getRemindDate())) {
            performDueTaskEntity.setTaskType("CUST_REMIND");
            performDueTaskEntity.setExecTime(custContactEntity.getRemindDate());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CUST_CONTACT_ID", custContactId);
            performDueTaskEntity.setParams(jsonObject.toJSONString());
            PerformDueTaskBean.addPerformDueTask(performDueTaskEntity);
        }

    }
}
