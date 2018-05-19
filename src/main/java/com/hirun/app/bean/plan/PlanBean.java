package com.hirun.app.bean.plan;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.cust.CustBean;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.PlanTargetLimitCache;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.plan.PlanActionNumDAO;
import com.hirun.app.dao.plan.PlanCycleFinishInfoDAO;
import com.hirun.app.dao.plan.PlanDAO;
import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.hirun.pub.domain.entity.plan.PlanCycleFinishInfoEntity;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.domain.enums.cust.CustStatus;
import com.hirun.pub.domain.enums.plan.ActionStatus;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-16.
 */
public class PlanBean {

    public static int getTargetLowerLimit(String executorId, String actionCode) throws Exception {
        int lowerLimit = 0;

        PlanTargetLimitEntity planTargetLimitEntity = PlanTargetLimitCache.getPlanTargetLimit(actionCode);
        if(planTargetLimitEntity != null) {
            int limitNum = Integer.parseInt(planTargetLimitEntity.getLimitNum());

            PlanCycleFinishInfoDAO cyclePlanFinishInfoDAO = new PlanCycleFinishInfoDAO("ins");
            PlanCycleFinishInfoEntity cyclePlanFinishInfoEntity = cyclePlanFinishInfoDAO.getCyclePlanFinishInfoEntity(executorId, actionCode);
            int preTotalUnfinishNum = 0;
            int currCycleFinishNum = 0;
            if(cyclePlanFinishInfoEntity == null) {

            } else  {
                preTotalUnfinishNum = Integer.parseInt(cyclePlanFinishInfoEntity.getUnfinishNum());
                currCycleFinishNum = Integer.parseInt(cyclePlanFinishInfoEntity.getCurrCycleFinishNum());
            }

            int totalLimitNum = 0;
            totalLimitNum = preTotalUnfinishNum + limitNum;
            lowerLimit = totalLimitNum - currCycleFinishNum;
        }

        lowerLimit = lowerLimit > 0 ? lowerLimit : 0;
        return lowerLimit;
    }

    public static void actionBindPlan(String nickName, String identifyCode, String actionCode, String planDate, String executorId, String finishTime) throws Exception {
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        CustActionDAO custActionDAO = DAOFactory.createDAO(CustActionDAO.class);
        PlanDAO planDAO = DAOFactory.createDAO(PlanDAO.class);
        Map<String, String> doCustResult = CustBean.isCreateOrBindNewCust(nickName, identifyCode, planDate, executorId);
        String doCust = doCustResult.get("DO_CUST");
        String custId = doCustResult.get("CUST_ID");

        if("BIND_VIRTUAL".equals(doCust)) {
            //UPDATE
            Map<String, String > custDbParam = new HashMap<String, String>();
            custDbParam.put("CUST_ID", custId);
            custDbParam.put("WX_NICK", nickName);
            custDbParam.put("IDENTIFY_CODE", identifyCode);
            custDAO.save("INS_CUSTOMER", custDbParam);
        }
        if("CREATE".equals(doCust)) {
            //INSERT
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setIdentifyCode(identifyCode);
            customerEntity.setWxNick(nickName);
            customerEntity.setCustName(nickName);
            customerEntity.setCustStatus(CustStatus.normal.getValue());
            customerEntity.setFirstPlanDate(planDate);
            customerEntity.setHouseCounselorId(executorId);
            customerEntity.setCreateDate(TimeTool.now());
            customerEntity.setCreateUserId("0");
            customerEntity.setUpdateTime(TimeTool.now());
            customerEntity.setUpdateUserId("0");
            custId = String.valueOf(custDAO.insertAutoIncrement("INS_CUSTOMER", customerEntity.getContent()));
        }

        //TODO 这里应该判一下该客户之前是否已经触发过该动作，如果有，则不应该重复触发
        //update or insert cust_action
        PlanEntity planEntity = planDAO.getPlanEntityByEidAndPlanDate(executorId, planDate);
        CustActionEntity custActionEntity = custActionDAO.queryCustActionByCustIdAndActionCodeAndPlanId(custId, actionCode, planEntity.getPlanId());
        if(custActionEntity != null) {
            //update
            String actionId = custActionEntity.getActionId();
            custActionEntity.setFinishTime(finishTime);
            custActionEntity.setUpdateUserId("0");
            custActionEntity.setUpdateTime(TimeTool.now());
            custActionDAO.update("INS_CUST_ACTION", custActionEntity.getContent());
        } else {
            //insert
            custActionEntity = new CustActionEntity();
            custActionEntity.setCustId(custId);
            custActionEntity.setActionCode(actionCode);
            custActionEntity.setPlanId(planEntity.getPlanId());
            custActionEntity.setActionStatus(ActionStatus.outerPlan.getValue());
            custActionEntity.setPlanDealDate(planEntity.getPlanDate());
            custActionEntity.setFinishTime(finishTime);
            custActionEntity.setExecutorId(executorId);
            custActionEntity.setCreateDate(TimeTool.now());
            custActionEntity.setCreateUserId("0");
            custActionEntity.setUpdateTime(TimeTool.now());
            custActionEntity.setUpdateUserId("0");
            custActionDAO.insertAutoIncrement("INS_CUST_ACTION", custActionEntity.getContent());
        }
    }
}
