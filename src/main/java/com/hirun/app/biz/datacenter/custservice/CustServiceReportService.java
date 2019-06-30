package com.hirun.app.biz.datacenter.custservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.bean.plan.PlanStatBean;
import com.hirun.app.cache.EmployeeCache;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.vo.UnEntryPlanEmployeeListVO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.*;


public class CustServiceReportService extends GenericService{

    public ServiceResponse initQueryActionFinishInfo(ServiceRequest request) throws Exception {
        ServiceResponse response=new ServiceResponse();
        JSONObject orgTree = OrgBean.getOrgTree();
        response.set("ORG_TREE", orgTree);

        return response;
    }

    public ServiceResponse queryCustServiceByName(ServiceRequest request) throws Exception{
        ServiceResponse response=new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String name=request.getString("CUSTSERVICE_NAME");
        String employeeId=session.getSessionEntity().get("EMPLOYEE_ID");

        RecordSet  childEmployeeRecordSet=EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId,"0");
        if(childEmployeeRecordSet.size()<=0){
            return response;
        }
        String employeeIds="";
        for(int i=0;i<childEmployeeRecordSet.size();i++){
            Record childRecord=childEmployeeRecordSet.get(i);
            employeeIds +=childRecord.get("EMPLOYEE_ID")+",";
        }
        employeeIds=employeeIds+employeeId;

        RecordSet employeeRecord=EmployeeBean.queryEmployeeByEmpIdsAndName(employeeIds,name);
        if(employeeRecord.size() <=0){
            return response;
        }
        response.set("CUSTSERVICEINFO",ConvertTool.toJSONArray(employeeRecord));

        return response;
    }

    public ServiceResponse queryCustServFinishActionInfo(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao=DAOFactory.createDAO(CustomerServiceDAO.class);
        String startDate=request.getString("START_DATE");
        String endDate=request.getString("END_DATE");
        String custserviceempid=request.getString("CUSTSERVICEEMPID");//选择的客户代表ID
        String employeeId=session.getSessionEntity().get("EMPLOYEE_ID");

        String employeeIds="";

        if(StringUtils.isNotBlank(custserviceempid)){
            employeeIds=custserviceempid;
        }else{
            RecordSet  childEmployeeRecordSet=EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId,"0");
            for(int i=0;i<childEmployeeRecordSet.size();i++){
                Record childRecord=childEmployeeRecordSet.get(i);
                employeeIds +=childRecord.get("EMPLOYEE_ID")+",";
            }
            employeeIds=employeeIds+employeeId;
        }


        if(StringUtils.isNotBlank(startDate)){
            startDate=startDate+" 00:00:00";
        }
        if(StringUtils.isNotBlank(endDate)){
            endDate=endDate+" 23:59:59";
        }

        String orgId=request.getString("ORG_ID");
        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();

        if(StringUtils.isBlank(orgId)) {
            if(Permission.hasAllCity()) {
                orgId = "7";
            } else if(Permission.hasAllShop()) {
                orgId = EmployeeBean.queryOrgByEmployee(employeeId, "3").getOrgId();
            } else {
                orgId = EmployeeBean.queryOrgByEmployee(employeeId, "2").getOrgId();
            }
        }
        orgId=OrgBean.getOrgLine(orgId,allOrgs);

   /*     RecordSet employeeRecordSet=EmployeeBean.queryEmployeeByEmpIdsAndOrgId(employeeIds,orgId);
        if(employeeRecordSet.size()<=0){
            return response;
        }

        String queryEmployeeIds="";
        for(int i=0;i<employeeRecordSet.size();i++){
            Record empRecord=employeeRecordSet.get(i);
            queryEmployeeIds +=empRecord.get("EMPLOYEE_ID")+",";
        }
*/
        RecordSet custServFinishActionInfo=dao.queryCustServFinishActionInfo(startDate,endDate,employeeIds,orgId);
        if(custServFinishActionInfo.size()<=0 || custServFinishActionInfo==null){
            return response;
        }

        for(int i=0;i<custServFinishActionInfo.size();i++){
            Record record=custServFinishActionInfo.get(i);
            String linkemployeeid=record.get("LINK_EMPLOYEE_ID");
            String partyId=record.get("PARTY_ID");
            String projectId=record.get("PROJECT_ID");
            String openId=record.get("OPEN_ID");

            //RecordSet insScanCityRecordSet=dao.queryInsScanCityInfoByProIdAndPId(projectId,partyId);//查询带看城市木屋数据

            /*
            //获取需求蓝图二的内容
            if(StringUtils.isNotBlank(openId)){
                RecordSet xqlteInfo=dao.queryXQLTEByOpenIdAndActionCode(openId,"XQLTE",linkemployeeid);
                if(xqlteInfo.size() > 0 && xqlteInfo != null){
                    record.put("FUNCPRINTTIME",xqlteInfo.get(0).get("FUNCPRINT_CREATE_TIME"));
                    record.put("STYLEPRINTTIME",xqlteInfo.get(0).get("STYLEPRINT_CREATE_TIME"));
                }

            }
            */
            /*
            if(insScanCityRecordSet.size()>0){
                String cityCabinIds=insScanCityRecordSet.get(0).get("CITY_CABINS");
                String  experienceTime=insScanCityRecordSet.get(0).get("EXPERIENCE_TIME");
                String experience=insScanCityRecordSet.get(0).get("EXPERIENCE");

                record.put("EXPERIENCE_TIME",experienceTime);
                record.put("EXPERIENCE",experience);
            //转译城市木屋的名字
            if(StringUtils.isNotBlank(cityCabinIds)){
                String[] cityCabinId=cityCabinIds.split(",");
                String cityCabinName="";
                for(int j=0;j<cityCabinId.length;j++){
                    Record cityCabinRecord =dao.getCityCabinById(cityCabinId[j]);
                    cityCabinName +=cityCabinRecord.get("CITYCABIN_ADDRESS")+"、";
                }
                record.put("CITYCABINNAMES",cityCabinName.substring(0,cityCabinName.length()-1));

            }
            }
            */
            String cityCabinIds=record.get("CITY_CABINS");
            if(StringUtils.isNotBlank(cityCabinIds)){
                String[] cityCabinId=cityCabinIds.split(",");
                String cityCabinName="";
                for(int j=0;j<cityCabinId.length;j++){
                    Record cityCabinRecord =dao.getCityCabinById(cityCabinId[j]);
                    cityCabinName +=cityCabinRecord.get("CITYCABIN_ADDRESS")+"、";
                }
                record.put("CITYCABINNAMES",cityCabinName.substring(0,cityCabinName.length()-1));

            }
            //翻译客户代表名字
            record.put("CUSTSERVICENAME",EmployeeCache.getEmployeeNameEmployeeId(linkemployeeid));
        }
        response.set("CUSTSERVICEFINISHACTIONINFO", ConvertTool.toJSONArray(custServFinishActionInfo));
        return response;
    }


    public ServiceResponse queryCustServMonStatInfo(ServiceRequest request) throws Exception{
        ServiceResponse response=new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        EmployeeJobRoleEntity emproleEntity=EmployeeBean.queryEmployeeJobRoleByEmpId(employeeId);//获取当前登录员工的ROLE_JOB
        CustomerServiceDAO dao=DAOFactory.createDAO(CustomerServiceDAO.class);
        OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);
        String custServiceEmpId=request.getString("CUSTSERVICEEMPID");
        String orgId=request.getString("ORG_ID");
        String monDate=request.getString("MON_DATE");
        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();


        String employeeIds="";

        if(StringUtils.isNotBlank(custServiceEmpId)){
            employeeIds=custServiceEmpId;
        }else{
            RecordSet  childEmployeeRecordSet=EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId,"0");
            for(int i=0;i<childEmployeeRecordSet.size();i++){
                Record childRecord=childEmployeeRecordSet.get(i);
                employeeIds +=childRecord.get("EMPLOYEE_ID")+",";
            }
            employeeIds=employeeIds+employeeId;
        }

        if(StringUtils.isBlank(orgId)) {
            if(Permission.hasAllCity()) {
                orgId = "7";
            } else if(Permission.hasAllShop()) {
                orgId = EmployeeBean.queryOrgByEmployee(employeeId, "3").getOrgId();
            } else {
                orgId = EmployeeBean.queryOrgByEmployee(employeeId, "2").getOrgId();
            }
        }
        orgId=OrgBean.getOrgLine(orgId,allOrgs);

        /*
        //如果前台选择的客户代表不为空或者登录查询报表的员工为普通客户代表只能查询自己本身或者指定的数据
        if(StringUtils.isNotBlank(custServiceEmpId)){
            RecordSet custServiceStatSet=dao.queryCustServMonStatInfo(custServiceEmpId,monDate);
            JSONArray sheetList1 = new JSONArray();
            sheetList1.add(dealStatRecord(custServiceStatSet,custServiceEmpId));
            response.set("CUSTSERVICESTATINFO",sheetList1);
            return response;
        }
        if(StringUtils.equals("46",emproleEntity.getJobRole()) || StringUtils.equals("69",emproleEntity.getJobRole()) || StringUtils.equals("119",emproleEntity.getJobRole())){
            JSONArray sheetList2 = new JSONArray();
            RecordSet custServiceStatSet=dao.queryCustServMonStatInfo(employeeId,monDate);
            sheetList2.add(dealStatRecord(custServiceStatSet,employeeId));
            response.set("CUSTSERVICESTATINFO",sheetList2);
            return response;
        }
        //组长或者客户主管只能查询自己以及下面员工的数据
        if(StringUtils.equals("118",emproleEntity.getJobRole()) || StringUtils.equals("103",emproleEntity.getJobRole())){
            RecordSet childEmployeeRecordSet=EmployeeBean.recursiveAllSubordinatesReordSet(employeeId);
            Record record=new Record();
            record.put("EMPLOYEE_ID",employeeId);
            childEmployeeRecordSet.add(record);
            JSONArray sheetList3 = new JSONArray();

            for(int i=0;i<childEmployeeRecordSet.size();i++){
                Record childEmployeeRecord=childEmployeeRecordSet.get(i);
                RecordSet custServiceStatSet=dao.queryCustServMonStatInfo(childEmployeeRecord.get("EMPLOYEE_ID"),monDate);
                sheetList3.add(dealStatRecord(custServiceStatSet,childEmployeeRecord.get("EMPLOYEE_ID")));
            }

            response.set("CUSTSERVICESTATINFO",sheetList3);
            return response;
        }

        */
        //OrgEntity orgEntity=OrgBean.getAssignTypeOrg(orgId,"1");

        RecordSet employeeJobRoleEntities=EmployeeBean.queryEmployeeJobRoleByOrgId1(orgId,"",employeeIds);
        if(employeeJobRoleEntities.size()<=0){
            return response;
        }
        int size = employeeJobRoleEntities.size();

        for(int i=0;i<size;i++){
            Record counselor = employeeJobRoleEntities.get(i);
            String counselorOrgId = counselor.get("ORG_ID");
            OrgEntity shop = EmployeeBean.queryOrgByOrgId(counselorOrgId, "2", allOrgs);
            if(shop != null) {
                counselor.put("SHOP", shop.getOrgId());
                counselor.put("SHOP_NAME",shop.getName());
            }

            OrgEntity company = EmployeeBean.queryOrgByOrgId(counselorOrgId, "3", allOrgs);
            if(company != null) {
                counselor.put("COMPANY", company.getOrgId());
                counselor.put("COMPANY_NAME", company.getName());
            }
        }
        Map<String, List<String>> companyMap = new HashMap<String, List<String>>();
        Map<String, List<JSONObject>> shopMap = new HashMap<String, List<JSONObject>>();
        JSONArray sheetList = new JSONArray();

        for(int i = 0; i < size; i++) {
            Record record = employeeJobRoleEntities.get(i);
            String shop = record.get("SHOP");
            String company = record.get("COMPANY");

            JSONObject custservicestat=new JSONObject();

            RecordSet statSet=dao.queryCustServMonStatInfo(record.get("EMPLOYEE_ID"),monDate);
            if(statSet.size()<=0){
                custservicestat.put("STAT_MONTH", monDate);
                custservicestat.put("OBJECT_ID", record.get("EMPLOYEE_ID"));
                custservicestat.put("CONSULT_COUNT", "0");
                custservicestat.put("STYLE_COUNT", "0");
                custservicestat.put("FUNC_COUNT", "0");
                custservicestat.put("SCAN_COUNT", "0");
                custservicestat.put("SCANCITYHOUSE_COUNT", "0");
                custservicestat.put("STYLE_SCALE", "0.00%");
                custservicestat.put("FUNC_SCALE", "0.00%");
                custservicestat.put("XQLTE_SCALE", "0.00%");
                custservicestat.put("SCAN_SCALE", "0.00%");
                custservicestat.put("SCANCITYHOUSE_SCALE", "0.00%");
                custservicestat.put("EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(record.get("EMPLOYEE_ID")));
            }else{
                custservicestat.put("STAT_MONTH", statSet.get(0).get("STAT_MONTH"));
                custservicestat.put("OBJECT_ID", statSet.get(0).get("OBJECT_ID"));
                custservicestat.put("CONSULT_COUNT", statSet.get(0).get("CONSULT_COUNT"));
                custservicestat.put("STYLE_COUNT", statSet.get(0).get("STYLE_COUNT"));
                custservicestat.put("FUNC_COUNT", statSet.get(0).get("FUNC_COUNT"));
                custservicestat.put("SCAN_COUNT", statSet.get(0).get("SCAN_COUNT"));
                custservicestat.put("SCANCITYHOUSE_COUNT", statSet.get(0).get("SCANCITYHOUSE_COUNT"));
                custservicestat.put("STYLE_SCALE", statSet.get(0).get("STYLE_SCALE"));
                custservicestat.put("FUNC_SCALE", statSet.get(0).get("FUNC_SCALE"));
                custservicestat.put("XQLTE_SCALE", statSet.get(0).get("XQLTE_SCALE"));
                custservicestat.put("SCAN_SCALE", statSet.get(0).get("SCAN_SCALE"));
                custservicestat.put("SCANCITYHOUSE_SCALE", statSet.get(0).get("SCANCITYHOUSE_SCALE"));
                custservicestat.put("EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(record.get("EMPLOYEE_ID")));
            }

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
                shopMap.get(shop).add(custservicestat);
            } else {
                List<JSONObject> employeeList = new ArrayList<JSONObject>();
                employeeList.add(custservicestat);
                shopMap.put(shop, employeeList);
            }
        }

        Iterator<String> companyIter = companyMap.keySet().iterator();
        JSONObject buSheet = new JSONObject();
        while(companyIter.hasNext()){
            String companyKey = companyIter.next();
            List<String> shopList = companyMap.get(companyKey);
            JSONObject companySheet = new JSONObject();
            for(String shopId : shopList) {
                if(StringUtils.isBlank(shopId)){
                    continue;
                }
                List<JSONObject> employeeList = shopMap.get(shopId);
                JSONObject shopSheet = new JSONObject();
                for(JSONObject employeeSheet : employeeList) {
                    sheetList.add(employeeSheet);
                    shopSheet.put("CONSULT_COUNT",shopSheet.getIntValue("CONSULT_COUNT")+employeeSheet.getIntValue("CONSULT_COUNT"));
                    shopSheet.put("STYLE_COUNT",shopSheet.getIntValue("STYLE_COUNT")+employeeSheet.getIntValue("STYLE_COUNT"));
                    shopSheet.put("FUNC_COUNT",shopSheet.getIntValue("FUNC_COUNT")+employeeSheet.getIntValue("FUNC_COUNT"));
                    shopSheet.put("SCAN_COUNT",shopSheet.getIntValue("SCAN_COUNT")+employeeSheet.getIntValue("SCAN_COUNT"));
                    shopSheet.put("SCANCITYHOUSE_COUNT",shopSheet.getIntValue("SCANCITYHOUSE_COUNT")+employeeSheet.getIntValue("SCANCITYHOUSE_COUNT"));

                    companySheet.put("CONSULT_COUNT",companySheet.getIntValue("CONSULT_COUNT") + employeeSheet.getIntValue("CONSULT_COUNT"));
                    companySheet.put("STYLE_COUNT",companySheet.getIntValue("STYLE_COUNT") + employeeSheet.getIntValue("STYLE_COUNT"));
                    companySheet.put("FUNC_COUNT",companySheet.getIntValue("FUNC_COUNT") + employeeSheet.getIntValue("FUNC_COUNT"));
                    companySheet.put("SCAN_COUNT",companySheet.getIntValue("SCAN_COUNT") + employeeSheet.getIntValue("SCAN_COUNT"));
                    companySheet.put("SCANCITYHOUSE_COUNT",companySheet.getIntValue("SCANCITYHOUSE_COUNT") + employeeSheet.getIntValue("SCANCITYHOUSE_COUNT"));


                    buSheet.put("CONSULT_COUNT",buSheet.getIntValue("CONSULT_COUNT") + employeeSheet.getIntValue("CONSULT_COUNT"));
                    buSheet.put("STYLE_COUNT",buSheet.getIntValue("STYLE_COUNT") + employeeSheet.getIntValue("STYLE_COUNT"));
                    buSheet.put("FUNC_COUNT",buSheet.getIntValue("FUNC_COUNT") + employeeSheet.getIntValue("FUNC_COUNT"));
                    buSheet.put("SCAN_COUNT",buSheet.getIntValue("SCAN_COUNT") + employeeSheet.getIntValue("SCAN_COUNT"));
                    buSheet.put("SCANCITYHOUSE_COUNT",buSheet.getIntValue("SCANCITYHOUSE_COUNT") + employeeSheet.getIntValue("SCANCITYHOUSE_COUNT"));

                }
                shopSheet.put("EMPLOYEE_NAME", orgDAO.queryOrgById(shopId).getName() + "合计");
                shopSheet=reckonScale(shopSheet);

                sheetList.add(shopSheet);

            }
            if(Permission.hasAllShop()){//
                companySheet.put("EMPLOYEE_NAME", orgDAO.queryOrgById(companyKey).getName() + "合计");
                companySheet=reckonScale(companySheet);
                sheetList.add(companySheet);

            }
            }
            if(Permission.hasAllCity()){//Permission.hasAllCity()
                 buSheet.put("EMPLOYEE_NAME",  "事业部合计");
                 buSheet=reckonScale(buSheet);
                 sheetList.add(buSheet);
             }

        response.set("CUSTSERVICESTATINFO",sheetList);
        return response;
    }

    private JSONObject reckonScale(JSONObject jsonObject){
        int consultCount=jsonObject.getIntValue("CONSULT_COUNT");
        int styleCount=jsonObject.getIntValue("STYLE_COUNT");
        int funcCount=jsonObject.getIntValue("FUNC_COUNT");
        int scanCount=jsonObject.getIntValue("SCAN_COUNT");
        int scancityhouseCount=jsonObject.getIntValue("SCANCITYHOUSE_COUNT");
        String prcent="0.00%";
        DecimalFormat df = new DecimalFormat("0.00%");
        String styleScale=df.format(styleCount/(consultCount*1.0));
        String funcScale=df.format(funcCount/(consultCount*1.0));
        String scanScale=df.format(scanCount/(consultCount*1.0));
        String scancityHouseScale=df.format(scancityhouseCount/(consultCount*1.0));

        if(consultCount!=0) {
            jsonObject.put("STYLE_SCALE", styleScale);
            jsonObject.put("FUNC_SCALE", funcScale);
            jsonObject.put("XQLTE_SCALE", funcScale);
            jsonObject.put("SCAN_SCALE", scanScale);
            jsonObject.put("SCANCITYHOUSE_SCALE", scancityHouseScale);
        }else{
            jsonObject.put("STYLE_SCALE", prcent);
            jsonObject.put("FUNC_SCALE", prcent);
            jsonObject.put("XQLTE_SCALE", prcent);
            jsonObject.put("SCAN_SCALE", prcent);
            jsonObject.put("SCANCITYHOUSE_SCALE", prcent);
        }
        return jsonObject;
    }

    private JSONObject dealStatRecord(RecordSet recordSet,String employeeId) throws  Exception{
        JSONObject custservicestat=new JSONObject();
        if(recordSet.size()<=0){
            custservicestat.put("CONSULT_COUNT", "0");
            custservicestat.put("STYLE_COUNT", "0");
            custservicestat.put("FUNC_COUNT", "0");
            custservicestat.put("SCAN_COUNT", "0");
            custservicestat.put("SCANCITYHOUSE_COUNT", "0");
            custservicestat.put("STYLE_SCALE", "0.00%");
            custservicestat.put("FUNC_SCALE", "0.00%");
            custservicestat.put("XQLTE_SCALE", "0.00%");
            custservicestat.put("SCAN_SCALE", "0.00%");
            custservicestat.put("SCANCITYHOUSE_SCALE", "0.00%");
            custservicestat.put("EMPLOYEE_NAME",EmployeeCache.getEmployeeNameEmployeeId(employeeId));
            return custservicestat;
        }else{
            Record custServiceRecord=recordSet.get(0);
            custServiceRecord.put("EMPLOYEE_NAME",EmployeeCache.getEmployeeNameEmployeeId(custServiceRecord.get("OBJECT_ID")));
            return ConvertTool.toJSONObject(custServiceRecord);
        }
    }





}
