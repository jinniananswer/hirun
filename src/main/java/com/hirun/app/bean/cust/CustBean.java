package com.hirun.app.bean.cust;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.common.MsgBean;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.dao.cust.CustChangeRelaEmployeeLogDAO;
import com.hirun.app.dao.cust.CustContactDAO;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.pub.domain.entity.cust.CustChangeRelaEmployeeLogEntity;
import com.hirun.pub.domain.entity.cust.CustContactEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.enums.common.MsgType;
import com.hirun.pub.domain.enums.cust.CustStatus;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.exception.GenericException;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-05-18.
 */
public class CustBean {

    public static Map<String, String> isCreateOrBindNewCust(String identifyCode, String planDate, String executorId) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        CustomerEntity customerEntity = custDAO.getCustomerEntityByIdentifyCode(identifyCode);
        if(customerEntity != null) {
            result.put("DO_CUST", "no");
            result.put("CUST_ID", customerEntity.getCustId());
            return result;
        }

        //不根据微信昵称查了
//        customerEntity = custDAO.getCustomerEntityByWxNick(wxNick);
//        if(customerEntity != null) {
//            result.put("DO_CUST", "no");
//            result.put("CUST_ID", customerEntity.getCustId());
//            return result;
//        }

        //找不到的情况
        //当天看有没有新客户
        List<CustomerEntity> customerEntityList = custDAO.queryNewVirtualCustListByPlanDate(executorId, planDate);
        if(ArrayTool.isNotEmpty(customerEntityList)) {
            //取第一条
            customerEntity = customerEntityList.get(0);
            result.put("DO_CUST", "BIND_VIRTUAL");
            result.put("CUST_ID", customerEntity.getCustId());
            return result;
        }

        //虚拟新客户也没有的情况，则新增一条
        result.put("DO_CUST", "CREATE");
        result.put("CUST_ID", null);
        return result;
    }

    public static void changeCounselor(CustChangeRelaEmployeeLogEntity entity) throws Exception {
        CustChangeRelaEmployeeLogDAO custChangeRelaEmployeeLogDAO = DAOFactory.createDAO(CustChangeRelaEmployeeLogDAO.class);
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();
        String now = TimeTool.now();//获取系统时间

        CustomerEntity customerEntity = custDAO.getCustById(entity.getCustId());
        if(customerEntity == null) throw new GenericException("-1", "根据CUST_ID【" + entity.getCustId() + "】找不到客户信息");

        String oldEmployeeId = customerEntity.getHouseCounselorId();

        customerEntity.setHouseCounselorId(entity.getNewEmployeeId());
        customerEntity.setUpdateTime(now);
        customerEntity.setUpdateUserId(userId);
        custDAO.update("INS_CUSTOMER", customerEntity.getContent());

        entity.setJobRole("42");//TODO 暂时写死42
        //根据custId得到employeeId
        entity.setOldEmployeeId(oldEmployeeId);
        entity.setCreateUserId(userId);
        entity.setCreateDate(now);

        custChangeRelaEmployeeLogDAO.insert("INS_CUST_CHANGE_RELA_EMPLOYEE_LOG", entity.getContent());
    }

    public static void restore(String custId) throws Exception {
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        CustomerEntity customerEntity = custDAO.getCustById(custId);
        String today = TimeTool.today();
        String now = TimeTool.now();
        if(customerEntity == null || !customerEntity.getCustStatus().equals(CustStatus.pause.getValue())){
            return;
        }

        customerEntity.setCustStatus(CustStatus.normal.getValue());
        custDAO.update("INS_CUSTOMER", customerEntity.getContent());

        //提醒家装顾问
        String employeeId = customerEntity.getHouseCounselorId();
        EmployeeEntity employeeEntity = EmployeeBean.getEmployeeByEmployeeId(employeeId);
        StringBuilder msgContent = new StringBuilder();
        msgContent.append("您的客户【").append(customerEntity.getCustName()).append("】");
        msgContent.append("已于").append(today).append("恢复，可以继续跟踪");
        MsgBean.sendMsg(employeeEntity.getUserId(), msgContent.toString(), "0", now, MsgType.sys);
    }

    public static void remind(String custContactId) throws Exception {
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        CustContactDAO custContactDAO = DAOFactory.createDAO(CustContactDAO.class);
        CustContactEntity custContactEntity = custContactDAO.getCustContactEntityByCustContactId(custContactId);
        String today = TimeTool.today();
        String now = TimeTool.now();
        if(custContactEntity == null) {

        }
        String custId = custContactEntity.getCustId();

        CustomerEntity customerEntity = custDAO.getCustById(custId);
        if(customerEntity == null){

        }

        String custStatus = customerEntity.getCustStatus();
        if(CustStatus.normal.getValue().equals(custStatus)) {
            //提醒家装顾问
            String actionName = ActionCache.getAction(custContactEntity.getRemindActionCode()).getActionName();
            StringBuilder msgContent = new StringBuilder();
            msgContent.append(today).append("你要对客户【").append(customerEntity.getCustName()).append("】");
            msgContent.append("做【").append(actionName).append("】").append("的动作，");
            msgContent.append("请联系客户");
            EmployeeEntity employeeEntity = EmployeeBean.getEmployeeByEmployeeId(custContactEntity.getEmployeeId());
            MsgBean.sendMsg(employeeEntity.getUserId(), msgContent.toString(), "0", now, MsgType.sys);
        }
    }
}
