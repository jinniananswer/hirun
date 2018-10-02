package com.hirun.app.biz.cust;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.common.MsgBean;
import com.hirun.app.bean.cust.CustBean;
import com.hirun.app.bean.cust.CustContactBean;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.houses.HousesBean;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.EmployeeCache;
import com.hirun.app.dao.cust.CustContactDAO;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.pub.domain.entity.cust.CustChangeRelaEmployeeLogEntity;
import com.hirun.pub.domain.entity.cust.CustContactEntity;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.hirun.pub.domain.enums.common.MsgType;
import com.hirun.pub.domain.enums.cust.CustStatus;
import com.hirun.pub.domain.enums.cust.Sex;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.*;
import com.most.core.pub.exception.GenericException;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author awx
 * @Date 2018/4/17 21:11
 * @Description:
 */
public class CustService extends GenericService{

    public ServiceResponse addCust(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject custInfo = request.getBody().getData();

        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        String now = TimeTool.now();

        CustDAO dao = new CustDAO("ins");
        Map<String, String> cust = ConvertTool.toMap(custInfo);
        cust.put("CUST_STATUS", "1");
        cust.put("CREATE_USER_ID", userId);
        cust.put("CREATE_DATE", now);
        cust.put("UPDATE_USER_ID", userId);
        cust.put("UPDATE_TIME", now);
        long custId = dao.insertAutoIncrement("INS_CUSTOMER", cust);
        response.set("CUST_ID", custId);

        return response;
    }

    public ServiceResponse editCust(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject custInfo = request.getBody().getData();

        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();
        String sysdate = TimeTool.now();

        CustDAO dao = new CustDAO("ins");
        Map<String, String> parameter = ConvertTool.toMap(custInfo);
        parameter.put("UPDATE_USER_ID",userId);
        parameter.put("UPDATE_TIME",sysdate);
        parameter.put("CUST_STATUS", "1");
        int i = dao.save("INS_CUSTOMER", new String[] {"CUST_ID"}, parameter);

        return response;
    }

    public ServiceResponse queryCustList(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        Map<String, String> parameter = ConvertTool.toMap(requestData);
        String topEmployeeId = requestData.getString("TOP_EMPLOYEE_ID");
        if(StringUtils.isNotBlank(topEmployeeId)) {
            StringBuilder houseCounselorIds = new StringBuilder();
            List<EmployeeEntity> employeeEntityList = EmployeeBean.getAllSubordinatesCounselors(topEmployeeId);
            if(ArrayTool.isNotEmpty(employeeEntityList)) {
                for(EmployeeEntity employeeEntity : employeeEntityList) {
                    houseCounselorIds.append(employeeEntity.getEmployeeId()).append(",");
                }
            }
            houseCounselorIds.append(topEmployeeId);
            parameter.put("HOUSE_COUNSELOR_IDS", houseCounselorIds.toString());
        }

        CustDAO dao = new CustDAO("ins");
        List<CustomerEntity> customerList = dao.queryCustList(parameter);
        JSONArray arrayCustList = ConvertTool.toJSONArray(customerList);
        for(int i = 0, size = arrayCustList.size(); i < size; i++) {
            JSONObject jsonCust = arrayCustList.getJSONObject(i);
            jsonCust.put("HOUSE_COUNSELOR_NAME", EmployeeCache.getEmployeeNameEmployeeId(jsonCust.getString("HOUSE_COUNSELOR_ID")));
        }

        response.set("CUSTOMERLIST", arrayCustList);

        return response;
    }

    public ServiceResponse addCustByNum(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        int newCustNew = requestData.getInteger("NEW_CUSTNUM");
        String houseCounselorId = requestData.getString("HOUSE_COUNSELOR_ID");
        String firstPlanDate = requestData.getString("FIRST_PLAN_DATE");

        //从SESSION里取userId
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        String now = TimeTool.now();//获取系统时间
        CustDAO dao = new CustDAO("ins");

        //删除今天添加的新客户
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("FIRST_PLAN_DATE", firstPlanDate);
        dao.deleteByWxNickisNullAndFirstPlanDate(houseCounselorId, firstPlanDate);

        List<Map<String, String>> listCust = new ArrayList<Map<String, String>>();
        for(int i = 0; i < newCustNew; i++) {
            Map<String, String> cust = new HashMap<String, String>();
            cust.put("HOUSE_COUNSELOR_ID", houseCounselorId);
            cust.put("CUST_STATUS", "9");
            cust.put("CUST_NAME", "新客户" + (i+1));
            cust.put("CREATE_USER_ID", userId);
            cust.put("CREATE_DATE", now);
            cust.put("UPDATE_USER_ID", userId);
            cust.put("UPDATE_TIME", now);
            cust.put("FIRST_PLAN_DATE", firstPlanDate);
            long custId = dao.insertAutoIncrement("INS_CUSTOMER", cust);
            cust.put("CUST_ID", String.valueOf(custId));
            listCust.add(cust);
        }

        response.set("custList", JSONArray.toJSON(listCust));

        return response;
    }

    public ServiceResponse getCustById(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String custId = requestData.getString("CUST_ID");
        String userId = SessionManager.getSession().getSessionEntity().getUserId();

        CustDAO custDAO = new CustDAO("ins");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUST_ID", custId);
        CustomerEntity customerEntity = custDAO.queryByPk(CustomerEntity.class, "INS_CUSTOMER", parameter);
        if(customerEntity == null) {
            throw new GenericException("-1", "客户资料不存在");
//            response.setError("-1", "客户资料不存在");
//            return response;
        }

        JSONObject jsonCust = customerEntity.toJson();
        //根据编码查中文描述
        jsonCust.put("SEX_DESC", Sex.getNameByValue(jsonCust.getString("SEX")));
        if(StringUtils.isNotBlank(jsonCust.getString("HOUSE_ID"))) {
            jsonCust.put("HOUSE_DESC", HousesBean.getHousesEntityById(jsonCust.getString("HOUSE_ID")).getName());
        }
        if(StringUtils.isNotBlank(jsonCust.getString("HOUSE_MODE"))) {
            jsonCust.put("HOUSE_MODE_DESC", StaticDataTool.getCodeName("HOUSE_MODE", jsonCust.getString("HOUSE_MODE")));
        }
        jsonCust.put("EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(jsonCust.getString("HOUSE_COUNSELOR_ID")));

        //mobile_no模糊化
        String custRelaUserId = EmployeeBean.getEmployeeByEmployeeId(jsonCust.getString("HOUSE_COUNSELOR_ID")).getUserId();
        if(!userId.equals(custRelaUserId)) {
            String mobileNo = jsonCust.getString("MOBILE_NO");
            if(StringUtils.isNotBlank(mobileNo) && mobileNo.length() > 3)
//            String mobileNo = jsonCust.getString("MOBILE_NO");
            jsonCust.put("MOBILE_NO", mobileNo.substring(0,3) + "***");
        }

        response.setBody(new Body(jsonCust));

        return response;
    }

    public ServiceResponse deleteCustById(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String custId = requestData.getString("CUST_ID");

        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        Map<String, String> dbParam = new HashMap<String, String>();
        dbParam.put("CUST_ID", custId);
        dbParam.put("CUST_STATUS", CustStatus.del.getValue());
        custDAO.save("INS_CUSTOMER", dbParam);

        return response;
    }

    public ServiceResponse addCustContact(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        CustContactEntity custContactEntity = new CustContactEntity();
        custContactEntity.setCustId(requestData.getString("CUST_ID"));
        custContactEntity.setContactDate(requestData.getString("CONTACT_DATE"));
        custContactEntity.setContactNote(requestData.getString("CONTACT_NOTE"));
        custContactEntity.setRestoreDate(requestData.getString("RESTORE_DATE"));
        custContactEntity.setRemindDate(requestData.getString("REMIND_DATE"));
        custContactEntity.setRemindActionCode(requestData.getString("REMIND_ACTION_CODE"));
        custContactEntity.setEmployeeId(requestData.getString("EMPLOYEE_ID"));
        CustContactBean.addCustContact(custContactEntity);

        return response;
    }

    public ServiceResponse changeCounselor(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();

        CustChangeRelaEmployeeLogEntity entity = new CustChangeRelaEmployeeLogEntity();
        entity.setCustId(requestData.getString("CUST_ID"));
        entity.setNewEmployeeId(requestData.getString("NEW_EMPLOYEE_ID"));
        entity.setChangeReason(requestData.getString("CHANGE_REASON"));
        CustBean.changeCounselor(entity);

        return response;
    }

    public ServiceResponse queryCustContact(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String custId = request.getString("CUST_ID");
        CustContactDAO custContactDAO = DAOFactory.createDAO(CustContactDAO.class);

        List<CustContactEntity> list = custContactDAO.queryCustContactEntityListByCustId(custId);
        JSONArray array = ConvertTool.toJSONArray(list);

        for(int i = 0; i <array.size(); i++) {
            JSONObject custContact = array.getJSONObject(i);
            custContact.put("EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(custContact.getString("EMPLOYEE_ID")));
        }

        response.set("CUST_CONTACT_LIST", array);
        return response;
    }

    /**
     * 暂停的客户恢复
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse restore(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String custId = request.getString("CUST_ID");
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        CustomerEntity customerEntity = custDAO.getCustById(custId);
        String today = TimeTool.today();
        String now = TimeTool.now();
        if(customerEntity == null || !customerEntity.getCustStatus().equals(CustStatus.pause.getValue())){
            return response;
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

        return response;
    }

    public ServiceResponse remind(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String custContactId = request.getString("CUST_CONTACT_ID");
        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        CustContactDAO custContactDAO = DAOFactory.createDAO(CustContactDAO.class);
        CustContactEntity custContactEntity = custContactDAO.getCustContactEntityByCustContactId(custContactId);
        String today = TimeTool.today();
        String now = TimeTool.now();
        if(custContactEntity == null) {
            return response;
        }
        String custId = custContactEntity.getCustId();

        CustomerEntity customerEntity = custDAO.getCustById(custId);
        if(customerEntity == null){
            return response;
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

        return response;
    }

    /**
     * 删除的客户恢复
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse restoreCustById(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String custId = requestData.getString("CUST_ID");

        String userId = SessionManager.getSession().getSessionEntity().getUserId();
        String now = TimeTool.now();

        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        Map<String, String> dbParam = new HashMap<String, String>();
        dbParam.put("CUST_ID", custId);
        dbParam.put("CUST_STATUS", CustStatus.normal.getValue());
        dbParam.put("UPDATE_USER_ID", userId);
        dbParam.put("UPDATE_TIME", now);
        custDAO.save("INS_CUSTOMER", dbParam);

        return response;
    }

    public ServiceResponse queryCustAction4HouseCounselor(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String houseCounselorIds = requestData.getString("HOUSE_COUNSELOR_IDS");
        String topEmployeeId = requestData.getString("TOP_EMPLOYEE_ID");

        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        if(StringUtils.isNotBlank(houseCounselorIds)) {
//            custList = custDAO.queryCustIds4Action4HouseCounselor(houseCounselorIds, requestData.getString("START_DATE"), requestData.getString("END_DATE"), requestData.getString("FINISH_ACTION"));
        } else if(StringUtils.isNotBlank(topEmployeeId)) {
            StringBuilder tmpHouseCounselorIds = new StringBuilder();
            List<EmployeeEntity> employeeEntityList = EmployeeBean.getAllSubordinatesCounselors(topEmployeeId);
            if(ArrayTool.isNotEmpty(employeeEntityList)) {
                for(EmployeeEntity employeeEntity : employeeEntityList) {
                    tmpHouseCounselorIds.append(employeeEntity.getEmployeeId()).append(",");
                }
            }
//            tmpHouseCounselorIds.append(topEmployeeId);
            if(tmpHouseCounselorIds.length() > 0) {
                houseCounselorIds = tmpHouseCounselorIds.substring(0, tmpHouseCounselorIds.length() - 1);
            }
        }

        List<CustomerEntity> custList = custDAO.queryCustIds4Action4HouseCounselor(houseCounselorIds, requestData.getString("START_DATE"), requestData.getString("END_DATE"), requestData.getString("FINISH_ACTION"), requestData.getString("CUST_NAME"));
        JSONArray result = new JSONArray();
        GenericDAO insDao = new GenericDAO("ins");
        for(CustomerEntity customerEntity : custList) {
            JSONObject object = customerEntity.toJSON(new String[] {"CUST_ID", "CUST_NAME"});

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT cust_id, action_code, COUNT(action_code) action_num, DATE_FORMAT(MAX(finish_time), '%Y-%m-%d %H:%i:%s') last_finish_time ");
            sql.append("FROM ins_cust_original_action ");
            sql.append("WHERE cust_id = :CUST_ID ");
            sql.append("GROUP BY action_code ");
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("CUST_ID", customerEntity.getCustId());

            RecordSet recordSet = insDao.queryBySql(sql.toString(), parameter);

            for(int i = 0, size = recordSet.size(); i <size; i++) {
                Record record = recordSet.get(i);
                String actionCode = record.get("ACTION_CODE");
                String actionNum = record.get("ACTION_NUM");
                String lastFinishTime = record.get("LAST_FINISH_TIME");
                object.put(actionCode + "_NUM",actionNum);
                object.put(actionCode + "_LAST_TIME",lastFinishTime);
            }

            List<ActionEntity> actionEntityList = ActionCache.getActionListByType("1");
            for(ActionEntity actionEntity : actionEntityList) {
                if(!object.containsKey(actionEntity.getActionCode() + "_LAST_TIME")) {
                    object.put(actionEntity.getActionCode() + "_NUM", 0);
                }
            }

            result.add(object);
        }

        response.set("RESULT", result);
        return response;
    }
}
