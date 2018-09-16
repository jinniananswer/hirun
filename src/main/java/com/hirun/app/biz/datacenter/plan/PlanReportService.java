package com.hirun.app.biz.datacenter.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.bean.plan.PlanStatBean;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.vo.UnEntryPlanEmployeeListVO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.transform.ConvertTool;

import java.util.*;

/**
 * Created by awx on 2018/8/8/008.
 */
public class PlanReportService extends GenericService{

    public ServiceResponse queryEmployeeDailySheet(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String topEMployeeId = request.getString("TOP_EMPLOYEE_ID");
        String queryDate = request.getString("QUERY_DATE");

        JSONArray employeeDailySheetList = PlanStatBean.queryEmployeeDailySheetList("TOP", topEMployeeId, queryDate);
        response.set("EMPLOYEE_DAILYSHEET_LIST", employeeDailySheetList);

        return response;
    }

    public ServiceResponse queryEmployeeDailySheet2(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        String startDate = request.getString("START_DATE");
        String endDate = request.getString("END_DATE");
        String employeeId = request.getString("EMPLOYEE_ID");

        String orgId = "";
        if(Permission.hasAllCity()) {
            orgId = "7";
        } else if(Permission.hasAllShop()) {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "3").getOrgId();
        } else {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "2").getOrgId();
        }

        OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);

        List<OrgEntity> orgEntityList = OrgBean.getAllOrgs();
        RecordSet recordSet = EmployeeBean.queryAllCounselors(orgId, orgEntityList);
        Map<String, List<String>> companyMap = new HashMap<String, List<String>>();
        Map<String, List<JSONObject>> shopMap = new HashMap<String, List<JSONObject>>();
//        JSONArray array = ConvertTool.toJSONArray(list, new String[]{"EMPLOYEE_ID", "NAME"});
        JSONArray sheetList = new JSONArray();
        for(int i = 0, size = recordSet.size(); i < size; i++) {
            Record record = recordSet.get(i);
            String company = record.get("COMPANY");
            String shop = record.get("SHOP");
            JSONObject employeeSheet = PlanStatBean.queryEmployeeSheetByEmployeeId(record.get("EMPLOYEE_ID"), startDate, endDate);
//            sheetList.add(employeeSheet);

            if(companyMap.containsKey(company)) {
                if(!shopMap.containsKey(shop)) {
                    companyMap.get(company).add(shop);
                }
            } else {
                List<String> shopList = new ArrayList<String>();
                shopList.add(shop);
                companyMap.put(company, shopList);
            }

            if(shopMap.containsKey(shop)) {
                shopMap.get(shop).add(employeeSheet);
            } else {
                List<JSONObject> employeeList = new ArrayList<JSONObject>();
                employeeList.add(employeeSheet);
                shopMap.put(shop, employeeList);
            }
        }

        Iterator<String> companyIter = companyMap.keySet().iterator();
        JSONObject buSheet = new JSONObject();
        while(companyIter.hasNext()) {
            String companyKey = companyIter.next();
            List<String> shopList = companyMap.get(companyKey);
            JSONObject companySheet = new JSONObject();
            for(String shopId : shopList) {
                List<JSONObject> employeeList = shopMap.get(shopId);
                JSONObject shopSheet = new JSONObject();
                for(JSONObject employeeSheet : employeeList) {
                    sheetList.add(employeeSheet);
                    shopSheet.put("PLAN_JW",shopSheet.getIntValue("PLAN_JW") + employeeSheet.getIntValue("PLAN_JW"));
                    shopSheet.put("PLAN_LTZDSTS",shopSheet.getIntValue("PLAN_LTZDSTS") + employeeSheet.getIntValue("PLAN_LTZDSTS"));
                    shopSheet.put("PLAN_GZHGZ",shopSheet.getIntValue("PLAN_GZHGZ") + employeeSheet.getIntValue("PLAN_GZHGZ"));
                    shopSheet.put("PLAN_HXJC",shopSheet.getIntValue("PLAN_HXJC") + employeeSheet.getIntValue("PLAN_HXJC"));
                    shopSheet.put("PLAN_SMJRQLC",shopSheet.getIntValue("PLAN_SMJRQLC") + employeeSheet.getIntValue("PLAN_SMJRQLC"));
                    shopSheet.put("PLAN_XQLTYTS",shopSheet.getIntValue("PLAN_XQLTYTS") + employeeSheet.getIntValue("PLAN_XQLTYTS"));
                    shopSheet.put("PLAN_ZX",shopSheet.getIntValue("PLAN_ZX") + employeeSheet.getIntValue("PLAN_ZX"));
                    shopSheet.put("PLAN_YJALTS",shopSheet.getIntValue("PLAN_YJALTS") + employeeSheet.getIntValue("PLAN_YJALTS"));
                    shopSheet.put("PLAN_DKCSMU",shopSheet.getIntValue("PLAN_DKCSMU") + employeeSheet.getIntValue("PLAN_DKCSMU"));
                    shopSheet.put("FINISH_JW",shopSheet.getIntValue("FINISH_JW") + employeeSheet.getIntValue("FINISH_JW"));
                    shopSheet.put("FINISH_LTZDSTS",shopSheet.getIntValue("FINISH_LTZDSTS") + employeeSheet.getIntValue("FINISH_LTZDSTS"));
                    shopSheet.put("FINISH_GZHGZ",shopSheet.getIntValue("FINISH_GZHGZ") + employeeSheet.getIntValue("FINISH_GZHGZ"));
                    shopSheet.put("FINISH_HXJC",shopSheet.getIntValue("FINISH_HXJC") + employeeSheet.getIntValue("FINISH_HXJC"));
                    shopSheet.put("FINISH_SMJRQLC",shopSheet.getIntValue("FINISH_SMJRQLC") + employeeSheet.getIntValue("FINISH_SMJRQLC"));
                    shopSheet.put("FINISH_XQLTYTS",shopSheet.getIntValue("FINISH_XQLTYTS") + employeeSheet.getIntValue("FINISH_XQLTYTS"));
                    shopSheet.put("FINISH_ZX",shopSheet.getIntValue("FINISH_ZX") + employeeSheet.getIntValue("FINISH_ZX"));
                    shopSheet.put("FINISH_YJALTS",shopSheet.getIntValue("FINISH_YJALTS") + employeeSheet.getIntValue("FINISH_YJALTS"));
                    shopSheet.put("FINISH_DKCSMU",shopSheet.getIntValue("FINISH_DKCSMU") + employeeSheet.getIntValue("FINISH_DKCSMU"));

                    companySheet.put("PLAN_JW",companySheet.getIntValue("PLAN_JW") + employeeSheet.getIntValue("PLAN_JW"));
                    companySheet.put("PLAN_LTZDSTS",companySheet.getIntValue("PLAN_LTZDSTS") + employeeSheet.getIntValue("PLAN_LTZDSTS"));
                    companySheet.put("PLAN_GZHGZ",companySheet.getIntValue("PLAN_GZHGZ") + employeeSheet.getIntValue("PLAN_GZHGZ"));
                    companySheet.put("PLAN_HXJC",companySheet.getIntValue("PLAN_HXJC") + employeeSheet.getIntValue("PLAN_HXJC"));
                    companySheet.put("PLAN_SMJRQLC",companySheet.getIntValue("PLAN_SMJRQLC") + employeeSheet.getIntValue("PLAN_SMJRQLC"));
                    companySheet.put("PLAN_XQLTYTS",companySheet.getIntValue("PLAN_XQLTYTS") + employeeSheet.getIntValue("PLAN_XQLTYTS"));
                    companySheet.put("PLAN_ZX",companySheet.getIntValue("PLAN_ZX") + employeeSheet.getIntValue("PLAN_ZX"));
                    companySheet.put("PLAN_YJALTS",companySheet.getIntValue("PLAN_YJALTS") + employeeSheet.getIntValue("PLAN_YJALTS"));
                    companySheet.put("PLAN_DKCSMU",companySheet.getIntValue("PLAN_DKCSMU") + employeeSheet.getIntValue("PLAN_DKCSMU"));
                    companySheet.put("FINISH_JW",companySheet.getIntValue("FINISH_JW") + employeeSheet.getIntValue("FINISH_JW"));
                    companySheet.put("FINISH_LTZDSTS",companySheet.getIntValue("FINISH_LTZDSTS") + employeeSheet.getIntValue("FINISH_LTZDSTS"));
                    companySheet.put("FINISH_GZHGZ",companySheet.getIntValue("FINISH_GZHGZ") + employeeSheet.getIntValue("FINISH_GZHGZ"));
                    companySheet.put("FINISH_HXJC",companySheet.getIntValue("FINISH_HXJC") + employeeSheet.getIntValue("FINISH_HXJC"));
                    companySheet.put("FINISH_SMJRQLC",companySheet.getIntValue("FINISH_SMJRQLC") + employeeSheet.getIntValue("FINISH_SMJRQLC"));
                    companySheet.put("FINISH_XQLTYTS",companySheet.getIntValue("FINISH_XQLTYTS") + employeeSheet.getIntValue("FINISH_XQLTYTS"));
                    companySheet.put("FINISH_ZX",companySheet.getIntValue("FINISH_ZX") + employeeSheet.getIntValue("FINISH_ZX"));
                    companySheet.put("FINISH_YJALTS",companySheet.getIntValue("FINISH_YJALTS") + employeeSheet.getIntValue("FINISH_YJALTS"));
                    companySheet.put("FINISH_DKCSMU",companySheet.getIntValue("FINISH_DKCSMU") + employeeSheet.getIntValue("FINISH_DKCSMU"));

                    buSheet.put("PLAN_JW",buSheet.getIntValue("PLAN_JW") + employeeSheet.getIntValue("PLAN_JW"));
                    buSheet.put("PLAN_LTZDSTS",buSheet.getIntValue("PLAN_LTZDSTS") + employeeSheet.getIntValue("PLAN_LTZDSTS"));
                    buSheet.put("PLAN_GZHGZ",buSheet.getIntValue("PLAN_GZHGZ") + employeeSheet.getIntValue("PLAN_GZHGZ"));
                    buSheet.put("PLAN_HXJC",buSheet.getIntValue("PLAN_HXJC") + employeeSheet.getIntValue("PLAN_HXJC"));
                    buSheet.put("PLAN_SMJRQLC",buSheet.getIntValue("PLAN_SMJRQLC") + employeeSheet.getIntValue("PLAN_SMJRQLC"));
                    buSheet.put("PLAN_XQLTYTS",buSheet.getIntValue("PLAN_XQLTYTS") + employeeSheet.getIntValue("PLAN_XQLTYTS"));
                    buSheet.put("PLAN_ZX",buSheet.getIntValue("PLAN_ZX") + employeeSheet.getIntValue("PLAN_ZX"));
                    buSheet.put("PLAN_YJALTS",buSheet.getIntValue("PLAN_YJALTS") + employeeSheet.getIntValue("PLAN_YJALTS"));
                    buSheet.put("PLAN_DKCSMU",buSheet.getIntValue("PLAN_DKCSMU") + employeeSheet.getIntValue("PLAN_DKCSMU"));
                    buSheet.put("FINISH_JW",buSheet.getIntValue("FINISH_JW") + employeeSheet.getIntValue("FINISH_JW"));
                    buSheet.put("FINISH_LTZDSTS",buSheet.getIntValue("FINISH_LTZDSTS") + employeeSheet.getIntValue("FINISH_LTZDSTS"));
                    buSheet.put("FINISH_GZHGZ",buSheet.getIntValue("FINISH_GZHGZ") + employeeSheet.getIntValue("FINISH_GZHGZ"));
                    buSheet.put("FINISH_HXJC",buSheet.getIntValue("FINISH_HXJC") + employeeSheet.getIntValue("FINISH_HXJC"));
                    buSheet.put("FINISH_SMJRQLC",buSheet.getIntValue("FINISH_SMJRQLC") + employeeSheet.getIntValue("FINISH_SMJRQLC"));
                    buSheet.put("FINISH_XQLTYTS",buSheet.getIntValue("FINISH_XQLTYTS") + employeeSheet.getIntValue("FINISH_XQLTYTS"));
                    buSheet.put("FINISH_ZX",buSheet.getIntValue("FINISH_ZX") + employeeSheet.getIntValue("FINISH_ZX"));
                    buSheet.put("FINISH_YJALTS",buSheet.getIntValue("FINISH_YJALTS") + employeeSheet.getIntValue("FINISH_YJALTS"));
                    buSheet.put("FINISH_DKCSMU",buSheet.getIntValue("FINISH_DKCSMU") + employeeSheet.getIntValue("FINISH_DKCSMU"));
                }
                shopSheet.put("EMPLOYEE_NAME", orgDAO.queryOrgById(shopId).getName() + "合计");
                sheetList.add(shopSheet);
            }
            if(Permission.hasAllShop()) {
                companySheet.put("EMPLOYEE_NAME", orgDAO.queryOrgById(companyKey).getName() + "合计");
                sheetList.add(companySheet);
            }
        }
        if(Permission.hasAllCity()) {
            buSheet.put("EMPLOYEE_NAME", "家装事业部合计");
            sheetList.add(buSheet);
        }

        response.set("EMPLOYEE_DAILYSHEET_LIST", sheetList);

        return response;
    }

    public ServiceResponse queryUnEntryPlanList(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String planDate = request.getString("PLAN_DATE");

        UnEntryPlanEmployeeListVO unEntryPlanEmployeeListVO = new UnEntryPlanEmployeeListVO();
        List<EmployeeEntity> employeeList = EmployeeBean.queryUnEntryPlanEmployeeList(planDate);
        for(EmployeeEntity employee : employeeList) {
            OrgEntity org = EmployeeBean.queryOrgByEmployee(employee.getEmployeeId(), "3");
            unEntryPlanEmployeeListVO.addEmployeeName(org.getName(), employee.getName());
        }

        response.set("COMPANY_LIST", unEntryPlanEmployeeListVO.toJSONArray());

        return response;
    }

    public ServiceResponse queryEmployeeMonthSheet2(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        String month = request.getString("MONTH");
        String employeeId = request.getString("EMPLOYEE_ID");

        String orgId = "";
        if(Permission.hasAllCity()) {
            orgId = "7";
        } else if(Permission.hasAllShop()) {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "3").getOrgId();
        } else {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "2").getOrgId();
        }

        OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);

        List<OrgEntity> orgEntityList = OrgBean.getAllOrgs();
        RecordSet recordSet = EmployeeBean.queryAllCounselors(orgId, orgEntityList);
        Map<String, List<String>> companyMap = new HashMap<String, List<String>>();
        Map<String, List<JSONObject>> shopMap = new HashMap<String, List<JSONObject>>();
        JSONArray sheetList = new JSONArray();
        for(int i = 0, size = recordSet.size(); i < size; i++) {
            Record record = recordSet.get(i);
            String company = record.get("COMPANY");
            String shop = record.get("SHOP");
            JSONObject employeeSheet = PlanStatBean.queryEmployeeMonSheetByEmployeeId2(record.get("EMPLOYEE_ID"), month);
//            sheetList.add(employeeSheet);

            if(companyMap.containsKey(company)) {
                if(!shopMap.containsKey(shop)) {
                    companyMap.get(company).add(shop);
                }
            } else {
                List<String> shopList = new ArrayList<String>();
                shopList.add(shop);
                companyMap.put(company, shopList);
            }

            if(shopMap.containsKey(shop)) {
                shopMap.get(shop).add(employeeSheet);
            } else {
                List<JSONObject> employeeList = new ArrayList<JSONObject>();
                employeeList.add(employeeSheet);
                shopMap.put(shop, employeeList);
            }
        }

        Iterator<String> companyIter = companyMap.keySet().iterator();
        JSONObject buSheet = new JSONObject();
        while(companyIter.hasNext()) {
            String companyKey = companyIter.next();
            List<String> shopList = companyMap.get(companyKey);
            JSONObject companySheet = new JSONObject();
            for(String shopId : shopList) {
                List<JSONObject> employeeList = shopMap.get(shopId);
                JSONObject shopSheet = new JSONObject();
                for(JSONObject employeeSheet : employeeList) {
                    sheetList.add(employeeSheet);
                    shopSheet.put("PLAN_JW",shopSheet.getIntValue("PLAN_JW") + employeeSheet.getIntValue("PLAN_JW"));
                    shopSheet.put("PLAN_LTZDSTS",shopSheet.getIntValue("PLAN_LTZDSTS") + employeeSheet.getIntValue("PLAN_LTZDSTS"));
                    shopSheet.put("PLAN_GZHGZ",shopSheet.getIntValue("PLAN_GZHGZ") + employeeSheet.getIntValue("PLAN_GZHGZ"));
                    shopSheet.put("PLAN_HXJC",shopSheet.getIntValue("PLAN_HXJC") + employeeSheet.getIntValue("PLAN_HXJC"));
                    shopSheet.put("PLAN_SMJRQLC",shopSheet.getIntValue("PLAN_SMJRQLC") + employeeSheet.getIntValue("PLAN_SMJRQLC"));
                    shopSheet.put("PLAN_XQLTYTS",shopSheet.getIntValue("PLAN_XQLTYTS") + employeeSheet.getIntValue("PLAN_XQLTYTS"));
                    shopSheet.put("PLAN_ZX",shopSheet.getIntValue("PLAN_ZX") + employeeSheet.getIntValue("PLAN_ZX"));
                    shopSheet.put("PLAN_YJALTS",shopSheet.getIntValue("PLAN_YJALTS") + employeeSheet.getIntValue("PLAN_YJALTS"));
                    shopSheet.put("PLAN_DKCSMU",shopSheet.getIntValue("PLAN_DKCSMU") + employeeSheet.getIntValue("PLAN_DKCSMU"));
                    shopSheet.put("FINISH_JW",shopSheet.getIntValue("FINISH_JW") + employeeSheet.getIntValue("FINISH_JW"));
                    shopSheet.put("FINISH_LTZDSTS",shopSheet.getIntValue("FINISH_LTZDSTS") + employeeSheet.getIntValue("FINISH_LTZDSTS"));
                    shopSheet.put("FINISH_GZHGZ",shopSheet.getIntValue("FINISH_GZHGZ") + employeeSheet.getIntValue("FINISH_GZHGZ"));
                    shopSheet.put("FINISH_HXJC",shopSheet.getIntValue("FINISH_HXJC") + employeeSheet.getIntValue("FINISH_HXJC"));
                    shopSheet.put("FINISH_SMJRQLC",shopSheet.getIntValue("FINISH_SMJRQLC") + employeeSheet.getIntValue("FINISH_SMJRQLC"));
                    shopSheet.put("FINISH_XQLTYTS",shopSheet.getIntValue("FINISH_XQLTYTS") + employeeSheet.getIntValue("FINISH_XQLTYTS"));
                    shopSheet.put("FINISH_ZX",shopSheet.getIntValue("FINISH_ZX") + employeeSheet.getIntValue("FINISH_ZX"));
                    shopSheet.put("FINISH_YJALTS",shopSheet.getIntValue("FINISH_YJALTS") + employeeSheet.getIntValue("FINISH_YJALTS"));
                    shopSheet.put("FINISH_DKCSMU",shopSheet.getIntValue("FINISH_DKCSMU") + employeeSheet.getIntValue("FINISH_DKCSMU"));

                    companySheet.put("PLAN_JW",companySheet.getIntValue("PLAN_JW") + employeeSheet.getIntValue("PLAN_JW"));
                    companySheet.put("PLAN_LTZDSTS",companySheet.getIntValue("PLAN_LTZDSTS") + employeeSheet.getIntValue("PLAN_LTZDSTS"));
                    companySheet.put("PLAN_GZHGZ",companySheet.getIntValue("PLAN_GZHGZ") + employeeSheet.getIntValue("PLAN_GZHGZ"));
                    companySheet.put("PLAN_HXJC",companySheet.getIntValue("PLAN_HXJC") + employeeSheet.getIntValue("PLAN_HXJC"));
                    companySheet.put("PLAN_SMJRQLC",companySheet.getIntValue("PLAN_SMJRQLC") + employeeSheet.getIntValue("PLAN_SMJRQLC"));
                    companySheet.put("PLAN_XQLTYTS",companySheet.getIntValue("PLAN_XQLTYTS") + employeeSheet.getIntValue("PLAN_XQLTYTS"));
                    companySheet.put("PLAN_ZX",companySheet.getIntValue("PLAN_ZX") + employeeSheet.getIntValue("PLAN_ZX"));
                    companySheet.put("PLAN_YJALTS",companySheet.getIntValue("PLAN_YJALTS") + employeeSheet.getIntValue("PLAN_YJALTS"));
                    companySheet.put("PLAN_DKCSMU",companySheet.getIntValue("PLAN_DKCSMU") + employeeSheet.getIntValue("PLAN_DKCSMU"));
                    companySheet.put("FINISH_JW",companySheet.getIntValue("FINISH_JW") + employeeSheet.getIntValue("FINISH_JW"));
                    companySheet.put("FINISH_LTZDSTS",companySheet.getIntValue("FINISH_LTZDSTS") + employeeSheet.getIntValue("FINISH_LTZDSTS"));
                    companySheet.put("FINISH_GZHGZ",companySheet.getIntValue("FINISH_GZHGZ") + employeeSheet.getIntValue("FINISH_GZHGZ"));
                    companySheet.put("FINISH_HXJC",companySheet.getIntValue("FINISH_HXJC") + employeeSheet.getIntValue("FINISH_HXJC"));
                    companySheet.put("FINISH_SMJRQLC",companySheet.getIntValue("FINISH_SMJRQLC") + employeeSheet.getIntValue("FINISH_SMJRQLC"));
                    companySheet.put("FINISH_XQLTYTS",companySheet.getIntValue("FINISH_XQLTYTS") + employeeSheet.getIntValue("FINISH_XQLTYTS"));
                    companySheet.put("FINISH_ZX",companySheet.getIntValue("FINISH_ZX") + employeeSheet.getIntValue("FINISH_ZX"));
                    companySheet.put("FINISH_YJALTS",companySheet.getIntValue("FINISH_YJALTS") + employeeSheet.getIntValue("FINISH_YJALTS"));
                    companySheet.put("FINISH_DKCSMU",companySheet.getIntValue("FINISH_DKCSMU") + employeeSheet.getIntValue("FINISH_DKCSMU"));

                    buSheet.put("PLAN_JW",buSheet.getIntValue("PLAN_JW") + employeeSheet.getIntValue("PLAN_JW"));
                    buSheet.put("PLAN_LTZDSTS",buSheet.getIntValue("PLAN_LTZDSTS") + employeeSheet.getIntValue("PLAN_LTZDSTS"));
                    buSheet.put("PLAN_GZHGZ",buSheet.getIntValue("PLAN_GZHGZ") + employeeSheet.getIntValue("PLAN_GZHGZ"));
                    buSheet.put("PLAN_HXJC",buSheet.getIntValue("PLAN_HXJC") + employeeSheet.getIntValue("PLAN_HXJC"));
                    buSheet.put("PLAN_SMJRQLC",buSheet.getIntValue("PLAN_SMJRQLC") + employeeSheet.getIntValue("PLAN_SMJRQLC"));
                    buSheet.put("PLAN_XQLTYTS",buSheet.getIntValue("PLAN_XQLTYTS") + employeeSheet.getIntValue("PLAN_XQLTYTS"));
                    buSheet.put("PLAN_ZX",buSheet.getIntValue("PLAN_ZX") + employeeSheet.getIntValue("PLAN_ZX"));
                    buSheet.put("PLAN_YJALTS",buSheet.getIntValue("PLAN_YJALTS") + employeeSheet.getIntValue("PLAN_YJALTS"));
                    buSheet.put("PLAN_DKCSMU",buSheet.getIntValue("PLAN_DKCSMU") + employeeSheet.getIntValue("PLAN_DKCSMU"));
                    buSheet.put("FINISH_JW",buSheet.getIntValue("FINISH_JW") + employeeSheet.getIntValue("FINISH_JW"));
                    buSheet.put("FINISH_LTZDSTS",buSheet.getIntValue("FINISH_LTZDSTS") + employeeSheet.getIntValue("FINISH_LTZDSTS"));
                    buSheet.put("FINISH_GZHGZ",buSheet.getIntValue("FINISH_GZHGZ") + employeeSheet.getIntValue("FINISH_GZHGZ"));
                    buSheet.put("FINISH_HXJC",buSheet.getIntValue("FINISH_HXJC") + employeeSheet.getIntValue("FINISH_HXJC"));
                    buSheet.put("FINISH_SMJRQLC",buSheet.getIntValue("FINISH_SMJRQLC") + employeeSheet.getIntValue("FINISH_SMJRQLC"));
                    buSheet.put("FINISH_XQLTYTS",buSheet.getIntValue("FINISH_XQLTYTS") + employeeSheet.getIntValue("FINISH_XQLTYTS"));
                    buSheet.put("FINISH_ZX",buSheet.getIntValue("FINISH_ZX") + employeeSheet.getIntValue("FINISH_ZX"));
                    buSheet.put("FINISH_YJALTS",buSheet.getIntValue("FINISH_YJALTS") + employeeSheet.getIntValue("FINISH_YJALTS"));
                    buSheet.put("FINISH_DKCSMU",buSheet.getIntValue("FINISH_DKCSMU") + employeeSheet.getIntValue("FINISH_DKCSMU"));
                }
                shopSheet.put("EMPLOYEE_NAME", orgDAO.queryOrgById(shopId).getName() + "合计");
                sheetList.add(shopSheet);
            }
            if(Permission.hasAllShop()) {
                companySheet.put("EMPLOYEE_NAME", orgDAO.queryOrgById(companyKey).getName() + "合计");
                sheetList.add(companySheet);
            }
        }
        if(Permission.hasAllCity()) {
            buSheet.put("EMPLOYEE_NAME", "家装事业部合计");
            sheetList.add(buSheet);
        }

        response.set("EMPLOYEE_DAILYSHEET_LIST", sheetList);

        return response;
    }
}
