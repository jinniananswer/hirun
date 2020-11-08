package com.hirun.app.bean.order;

import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.dao.order.OrderDAO;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: hirun
 * @description: 订单处理类
 * @author: jinnian
 * @create: 2020-02-05 21:11
 **/
public class OrderBean {

    /**
     * 创建咨询订单
     *
     * @param custId                客户编码
     * @param housesId              楼盘编码
     * @param decorateAddress       装修地址
     * @param houseLayout           户型
     * @param floorage              建筑面积
     * @param indoorArea            室内面积
     * @param reportEmployeeId      报备员工ID
     * @param custServiceEmployeeId 客户代表员工ID
     * @param counselorEmployeeId   家装顾问员工ID
     * @param designerEmployeeId    设计师员工ID
     * @throws Exception
     */
    public static void createConsultOrder(String custId, String housesId, String decorateAddress, String houseLayout, String floorage, String indoorArea,
                                          String reportEmployeeId, String custServiceEmployeeId, String counselorEmployeeId,
                                          String designerEmployeeId, String orderStatus, String type, String consultTime, String stage) throws Exception {
        Map<String, String> order = new HashMap<String, String>();
        order.put("CUST_ID", custId);
        order.put("TYPE", type);
        order.put("HOUSES_ID", housesId);
        order.put("DECORATE_ADDRESS", decorateAddress);
        order.put("HOUSE_LAYOUT", houseLayout);
        order.put("FLOORAGE", floorage);
        order.put("INDOOR_AREA", indoorArea);

        order.put("STATUS", orderStatus);
        order.put("STAGE", stage);

        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        String selfOrgId = OrgBean.getOrgId(session.getSessionEntity());
        OrgEntity orgEntity = OrgBean.getAssignTypeOrg(selfOrgId, "2");
        if (orgEntity != null) {
            order.put("SHOP_ID", orgEntity.getOrgId());
        }
        order.put("CREATE_USER_ID", userId);
        order.put("CREATE_TIME", session.getCreateTime());
        order.put("UPDATE_USER_ID", userId);
        order.put("UPDATE_TIME", session.getCreateTime());

        OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
        long orderId = orderDAO.insertAutoIncrement("order_base", order);
        String now = session.getCreateTime();

        List<Map<String, String>> orderWorkers = new ArrayList<Map<String, String>>();
        if (StringUtils.isNotBlank(reportEmployeeId)) {
            Map<String, String> reportWorker = new HashMap<String, String>();
            reportWorker.put("ORDER_ID", orderId + "");
            reportWorker.put("ROLE_ID", "555");//报备角色
            reportWorker.put("EMPLOYEE_ID", reportEmployeeId);
            reportWorker.put("START_DATE", now);
            reportWorker.put("END_DATE", "3000-12-31 23:59:59");
            reportWorker.put("CREATE_USER_ID", userId);
            reportWorker.put("CREATE_TIME", session.getCreateTime());
            reportWorker.put("UPDATE_USER_ID", userId);
            reportWorker.put("UPDATE_TIME", session.getCreateTime());

            orderWorkers.add(reportWorker);
        }

        if (StringUtils.isNotBlank(custServiceEmployeeId)) {
            Map<String, String> custServiceWorker = new HashMap<String, String>();
            custServiceWorker.put("ORDER_ID", orderId + "");
            custServiceWorker.put("ROLE_ID", "15");//客户代表
            custServiceWorker.put("EMPLOYEE_ID", custServiceEmployeeId);
            custServiceWorker.put("START_DATE", now);
            custServiceWorker.put("END_DATE", "3000-12-31 23:59:59");
            custServiceWorker.put("CREATE_USER_ID", userId);
            custServiceWorker.put("CREATE_TIME", now);
            custServiceWorker.put("UPDATE_USER_ID", userId);
            custServiceWorker.put("UPDATE_TIME", now);

            orderWorkers.add(custServiceWorker);
        }

        if (StringUtils.isNotBlank(counselorEmployeeId)) {
            Map<String, String> counselorWorker = new HashMap<String, String>();
            counselorWorker.put("ORDER_ID", orderId + "");
            counselorWorker.put("ROLE_ID", "3");//家装顾问
            counselorWorker.put("EMPLOYEE_ID", counselorEmployeeId);
            counselorWorker.put("START_DATE", now);
            counselorWorker.put("END_DATE", "3000-12-31 23:59:59");
            counselorWorker.put("CREATE_USER_ID", userId);
            counselorWorker.put("CREATE_TIME", now);
            counselorWorker.put("UPDATE_USER_ID", userId);
            counselorWorker.put("UPDATE_TIME", now);

            orderWorkers.add(counselorWorker);
        }

        if (StringUtils.isNotBlank(designerEmployeeId)) {
            Map<String, String> designerWorker = new HashMap<String, String>();
            designerWorker.put("ORDER_ID", orderId + "");
            designerWorker.put("ROLE_ID", "30");//设计师
            designerWorker.put("EMPLOYEE_ID", designerEmployeeId);
            designerWorker.put("START_DATE", now);
            designerWorker.put("END_DATE", "3000-12-31 23:59:59");
            designerWorker.put("CREATE_USER_ID", userId);
            designerWorker.put("CREATE_TIME", now);
            designerWorker.put("UPDATE_USER_ID", userId);
            designerWorker.put("UPDATE_TIME", now);

            orderWorkers.add(designerWorker);
        }

        if (ArrayTool.isNotEmpty(orderWorkers)) {
            orderDAO.insertBatch("order_worker", orderWorkers);
        }

        createOrderOperLog(orderId + "", order.get("STAGE"), order.get("STATUS"), "创建客户订单", session.getSessionEntity().get("EMPLOYEE_ID"));

        createOrderConsult(orderId + "", custServiceEmployeeId, designerEmployeeId, consultTime);
    }

    public static void createOrderOperLog(String orderId, String orderStage, String orderStatus, String content, String operEmployeeId) throws Exception {
        OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
        Map<String, String> operLog = new HashMap<String, String>();

        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        operLog.put("ORDER_ID", orderId);
        operLog.put("EMPLOYEE_ID", operEmployeeId);
        operLog.put("TYPE", "1");
        operLog.put("OPER_CONTENT", content);
        operLog.put("ORDER_STAGE", orderStage);
        operLog.put("ORDER_STATUS", orderStatus);
        operLog.put("CREATE_USER_ID", userId);
        operLog.put("CREATE_TIME", session.getCreateTime());
        operLog.put("UPDATE_USER_ID", userId);
        operLog.put("UPDATE_TIME", session.getCreateTime());

        orderDAO.insert("order_oper_log", operLog);

    }

    public static void createOrderConsult(String orderId, String customerServiceEmployeeId, String designEmployeeId, String consultTime) throws Exception {
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
        Map<String, String> orderConsult = new HashMap<String, String>();
        RecordSet recordSet=orderDAO.queryOrderConsult(orderId);
        if(recordSet.size()<=0) {
            orderConsult.put("ORDER_ID", orderId);
            orderConsult.put("CUST_SERVICE_EMPLOYEE_ID", customerServiceEmployeeId);
            orderConsult.put("DESIGN_EMPLOYEE_ID", designEmployeeId);
            orderConsult.put("CONSULT_TIME", consultTime);
            orderConsult.put("CREATE_USER_ID", userId);
            orderConsult.put("CREATE_TIME", session.getCreateTime());
            orderConsult.put("UPDATE_USER_ID", userId);
            orderConsult.put("UPDATE_TIME", session.getCreateTime());
            orderDAO.insert("order_consult", orderConsult);
        }
    }

    public static void updateConsultOrder(String custId, String housesId, String decorateAddress, String houseLayout, String floorage, String indoorArea,
                                          String reportEmployeeId, String custServiceEmployeeId, String counselorEmployeeId,
                                          String designerEmployeeId, String orderStatus, String type, String consultTime, String stage) throws Exception {

        Record orderRecord = queryOrderByCustId(custId);
        if (orderRecord == null) {
            new Exception("");
        }
        String orderId = orderRecord.get("ORDER_ID");
        Map<String, String> order = new HashMap<String, String>();
        order.put("CUST_ID", custId);
        order.put("TYPE", type);
        order.put("HOUSES_ID", housesId);
        order.put("DECORATE_ADDRESS", decorateAddress);
        order.put("HOUSE_LAYOUT", houseLayout);
        order.put("FLOORAGE", floorage);
        order.put("INDOOR_AREA", indoorArea);

        order.put("STATUS", orderStatus);
        order.put("STAGE", stage);

        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        order.put("UPDATE_USER_ID", userId);
        order.put("UPDATE_TIME", session.getCreateTime());
        order.put("ORDER_ID", orderId);

        OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
        orderDAO.save("order_base", new String[]{"ORDER_ID"}, order);
        String now = session.getCreateTime();

        List<Map<String, String>> orderWorkers = new ArrayList<Map<String, String>>();


        if (StringUtils.isNotBlank(custServiceEmployeeId)) {
            Map<String, String> custServiceWorker = new HashMap<String, String>();
            RecordSet recordSet = orderDAO.queryOrderWork(orderId, "15", custServiceEmployeeId);
            if (recordSet.size() <= 0) {
                custServiceWorker.put("ORDER_ID", orderId + "");
                custServiceWorker.put("ROLE_ID", "15");//客户代表
                custServiceWorker.put("EMPLOYEE_ID", custServiceEmployeeId);
                custServiceWorker.put("START_DATE", now);
                custServiceWorker.put("END_DATE", "3000-12-31 23:59:59");
                custServiceWorker.put("CREATE_USER_ID", userId);
                custServiceWorker.put("CREATE_TIME", now);
                custServiceWorker.put("UPDATE_USER_ID", userId);
                custServiceWorker.put("UPDATE_TIME", now);
                orderWorkers.add(custServiceWorker);
            }
        }


        if (ArrayTool.isNotEmpty(orderWorkers)) {
            orderDAO.insertBatch("order_worker", orderWorkers);
        }

        createOrderOperLog(orderId + "", order.get("STAGE"), order.get("STATUS"), "修改客户订单", session.getSessionEntity().get("EMPLOYEE_ID"));

        createOrderConsult(orderId + "", custServiceEmployeeId, designerEmployeeId, consultTime);
    }

    public static Record queryOrderByCustId(String custId) throws Exception {
        OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
        Map<String, String> param = new HashMap<>();
        param.put("CUST_ID", custId);
        RecordSet recordSet = orderDAO.query("order_base", param);
        if (recordSet.size() <= 0) {
            return null;
        }
        return recordSet.get(0);
    }

    public static void updateOrder(String custId, String address, String houseMode, String area) throws Exception {
        Record orderRecord = queryOrderByCustId(custId);
        if (orderRecord == null) {
            new Exception("");
        }
        String orderId = orderRecord.get("ORDER_ID");
        Map<String, String> order = new HashMap<String, String>();
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ORDER_BASE  ");
        sql.append(" SET DECORATE_ADDRESS = :DECORATE_ADDRESS,HOUSE_LAYOUT=:HOUSE_LAYOUT,FLOORAGE=:FLOORAGE,UPDATE_USER_ID=:UPDATE_USER_ID,UPDATE_TIME=:UPDATE_TIME ");
        sql.append(" WHERE ORDER_ID = :ORDER_ID ");

        order.put("ORDER_ID", orderId);
        order.put("DECORATE_ADDRESS", address);
        order.put("HOUSE_LAYOUT", houseMode);
        order.put("FLOORAGE", area);

        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        order.put("UPDATE_USER_ID", userId);
        order.put("UPDATE_TIME", session.getCreateTime());

        OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
        orderDAO.executeUpdate(sql.toString(), order);
    }

    public static void updateOrderWork(String custId, String roleId, String employeeId) throws Exception {
        Record orderRecord = queryOrderByCustId(custId);
        OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        RecordSet recordSet = orderDAO.queryOrderWork(orderRecord.get("ORDER_ID"), roleId, employeeId);
        Map<String, String> worker = new HashMap<String, String>();

        worker.put("ORDER_ID", orderRecord.get("ORDER_ID"));
        worker.put("ROLE_ID", roleId);
        worker.put("EMPLOYEE_ID", employeeId);
        worker.put("START_DATE", TimeTool.now());
        worker.put("END_DATE", "3000-12-31 23:59:59");
        worker.put("CREATE_USER_ID", userId);
        worker.put("CREATE_TIME", TimeTool.now());
        worker.put("UPDATE_USER_ID", userId);
        worker.put("UPDATE_TIME", TimeTool.now());

        if (recordSet.size() > 0) {
            Map<String, String> updateWorker = new HashMap<String, String>();
            StringBuilder updateSql = new StringBuilder();
            updateSql.append(" UPDATE ORDER_WORKER  " +
                    " SET END_DATE=:END_DATE , UPDATE_USER_ID=:UPDATE_USER_ID ,UPDATE_TIME=:UPDATE_TIME WHERE ID=:ID ");
            updateWorker.put("ID", recordSet.get(0).get("ID"));
            updateWorker.put("END_DATE", TimeTool.now());
            updateWorker.put("UPDATE_USER_ID", userId);
            updateWorker.put("UPDATE_TIME", TimeTool.now());
            orderDAO.executeUpdate(updateSql.toString(), updateWorker);
        }

        orderDAO.insertAutoIncrement("order_worker", worker);
    }

    public static void updateOrderConsult(String custId, String emplyeeId) throws Exception {
        Record orderRecord = queryOrderByCustId(custId);
        OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        StringBuilder updateSql = new StringBuilder();
        Map<String, String> param = new HashMap<>();
        updateSql.append("UPDATE ORDER_CONSULT SET DESIGN_EMPLOYEE_ID=:EMPLOYEE_ID,UPDATE_USER_ID=:UPDATE_USER_ID,UPDATE_TIME=:UPDATE_TIME WHERE ORDER_ID=:ORDER_ID");
        param.put("EMPLOYEE_ID", emplyeeId);
        param.put("ORDER_ID", orderRecord.get("ORDER_ID"));
        param.put("UPDATE_USER_ID", userId);
        param.put("UPDATE_TIME", TimeTool.now());
        orderDAO.executeUpdate(updateSql.toString(), param);
    }

}
