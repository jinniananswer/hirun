package com.hirun.app.bean.order;

import com.hirun.app.dao.order.OrderDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
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
     * @param custId 客户编码
     * @param housesId 楼盘编码
     * @param decorateAddress 装修地址
     * @param houseLayout 户型
     * @param floorage 建筑面积
     * @param indoorArea 室内面积
     * @param reportEmployeeId 报备员工ID
     * @param custServiceEmployeeId 客户代表员工ID
     * @param counselorEmployeeId 家装顾问员工ID
     * @param designerEmployeeId 设计师员工ID
     * @throws Exception
     */
    public static void createConsultOrder(String custId, String housesId, String decorateAddress, String houseLayout, String floorage, String indoorArea, String reportEmployeeId, String custServiceEmployeeId, String counselorEmployeeId, String designerEmployeeId) throws Exception {
        Map<String, String> order = new HashMap<String, String>();
        order.put("CUST_ID", custId);
        order.put("TYPE", "1");
        order.put("HOUSES_ID", housesId);
        order.put("DECORATE_ADDRESS", decorateAddress);
        order.put("HOUSE_LAYOUT", houseLayout);
        order.put("FLOORAGE", floorage);
        order.put("INDOOR_AREA", indoorArea);

        order.put("STATUS", "2");
        order.put("STAGE", "10");

        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

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
            reportWorker.put("ROLE_ID", "-1");//报备角色
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
            custServiceWorker.put("EMPLOYEE_ID", reportEmployeeId);
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
            counselorWorker.put("EMPLOYEE_ID", reportEmployeeId);
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
            designerWorker.put("EMPLOYEE_ID", reportEmployeeId);
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

        createOrderOperLog(orderId+"", order.get("STAGE"), order.get("STATUS"), "创建客户订单", session.getSessionEntity().get("EMPLOYEE_ID"));
    }

    public static void createOrderOperLog(String orderId, String orderStage, String orderStatus, String content, String operEmployeeId) throws Exception {
        OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
        Map<String, String> operLog = new HashMap<String, String>();

        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        operLog.put("ORDER_ID", orderId);
        operLog.put("EMPLOYEE_ID", operEmployeeId);
        operLog.put("OPER_CONTENT", content);
        operLog.put("ORDER_STAGE", orderStage);
        operLog.put("ORDER_STATUS", orderStatus);
        operLog.put("CREATE_USER_ID", userId);
        operLog.put("CREATE_TIME", session.getCreateTime());
        operLog.put("UPDATE_USER_ID", userId);
        operLog.put("UPDATE_TIME", session.getCreateTime());

        orderDAO.insert("order_oper_log", operLog);

    }
}
