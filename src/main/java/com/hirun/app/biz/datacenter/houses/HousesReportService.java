package com.hirun.app.biz.datacenter.houses;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.houses.HousesPlanDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class HousesReportService extends GenericService {

    public ServiceResponse reportAllHousesNature(ServiceRequest request) throws Exception{
        HousesPlanDAO dao = DAOFactory.createDAO(HousesPlanDAO.class);

        int housesNum = dao.getAllHousesNum();
        RecordSet natureGroups = dao.getGroupCountByHouseNature();

        this.translateNature(natureGroups);

        OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);
        RecordSet companys = new RecordSet();
        Record comany = new Record();
        comany.put("ORG_ID","-1");
        comany.put("NAME","所有分公司");
        companys.add(comany);

        RecordSet orgs = orgDAO.queryCompany();
        companys.addAll(orgs);

        ServiceResponse response = new ServiceResponse();
        response.set("HOUSE_NUM", housesNum);
        response.set("NATURE_GROUP", ConvertTool.toJSONArray(natureGroups));
        response.set("COMPANYS", ConvertTool.toJSONArray(companys));
        return response;
    }

    public ServiceResponse reportCompanyHousesNature(ServiceRequest request) throws Exception{
        HousesPlanDAO dao = DAOFactory.createDAO(HousesPlanDAO.class);

        String orgId = request.getString("ORG_ID");
        int housesNum = -1;
        RecordSet natureGroups = null;
        if(StringUtils.equals("-1", orgId)){
            housesNum = dao.getAllHousesNum();
            natureGroups = dao.getGroupCountByHouseNature();
        }
        else{
            housesNum = dao.getCompanyHousesNum(orgId);
            natureGroups = dao.getGroupCountByCompanyHouseNature(orgId);
        }

        this.translateNature(natureGroups);
        ServiceResponse response = new ServiceResponse();
        response.set("HOUSE_NUM", housesNum);
        response.set("NATURE_GROUP", ConvertTool.toJSONArray(natureGroups));
        return response;
    }

    private void translateNature(RecordSet natureGroups) throws Exception{
        if(natureGroups != null && natureGroups.size() > 0){
            int size = natureGroups.size();
            for(int i=0;i<size;i++){
                Record natureGroup = natureGroups.get(i);
                String natureName = StaticDataTool.getCodeName("HOUSE_NATURE", natureGroup.get("NATURE"));
                natureGroup.put("NATURE_NAME", natureName);
            }
        }
    }

    public ServiceResponse reportCounselorPlanActual(ServiceRequest request) throws Exception{
        String type = request.getString("QUERY_TYPE");
        RecordSet plan = null;
        RecordSet actual = null;
        RecordSet company = null;
        HousesPlanDAO dao = DAOFactory.createDAO(HousesPlanDAO.class);

        if(type == null){
//            OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);
//            company = orgDAO.queryCompany();
            company = new RecordSet();
            Record cs = new Record();
            cs.put("NAME","长沙鸿扬");
            cs.put("ORG_ID","17");
            company.add(cs);
            type = "0";
        }


        if(StringUtils.equals("0", type)){
            plan = dao.getPlanCounselorNumByCompany();
            actual = dao.getActualCounselorNumByCompany();
        }
        else{
            String orgId = request.getString("ORG_ID");
            plan = dao.getPlanCounselorNumByShop(orgId);
            actual = dao.getActualCounselorNumByShop(orgId);
        }

        ServiceResponse response = new ServiceResponse();
        response.set("COUNSELOR_PLAN", ConvertTool.toJSONArray(plan));
        response.set("COUNSELOR_ACTUAL", ConvertTool.toJSONArray(actual));
        if(company != null)
            response.set("COMPANYS", ConvertTool.toJSONArray(company));

        return response;
    }

    public ServiceResponse reportCounselorLoophole(ServiceRequest request) throws Exception{
        String orgId = request.getString("ORG_ID");
        RecordSet plan = null;
        RecordSet actual = null;
        HousesPlanDAO dao = DAOFactory.createDAO(HousesPlanDAO.class);

        RecordSet company = new RecordSet();
        Record record = new Record();
        record.put("NAME","所有分公司");
        record.put("ORG_ID","-1");

        Record cs = new Record();
        cs.put("NAME","长沙鸿扬");
        cs.put("ORG_ID","17");

        company.add(record);
        company.add(cs);
//        if(orgId == null){
//            OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);
//            RecordSet rst = orgDAO.queryCompany();
//            company.addAll(rst);
//        }


        if(orgId == null || StringUtils.equals("-1", orgId)){
            plan = dao.getPlanCounselorNumByCompanyAndNature();
            actual = dao.getActualCounselorNumByCompanyAndNature();
        }
        else{
            plan = dao.getPlanCounselorNumByShopAndNature(orgId);
            actual = dao.getActualCounselorNumByShopAndNature(orgId);
        }

        ServiceResponse response = new ServiceResponse();
        JSONArray jsonArray = new JSONArray();
        int num = 0;
        Map<String, String> temp = new HashMap<String, String>();
        Set<String> orgTemp = new TreeSet<String>();
        Map<String, String> orgNameTemp = new HashMap<String, String>();
        if(plan != null && plan.size() > 0){
            int size = plan.size();
            for(int i=0;i<size;i++){
                Record planRecord = plan.get(i);
                int planNum = planRecord.getInt("PLAN_NUM");
                String planNature = planRecord.get("NATURE");
                String planOrgId = planRecord.get("ORG_ID");
                temp.put(planOrgId+"_PLAN_"+planNature, planNum+"");
                orgTemp.add(planOrgId);
                orgNameTemp.put(planOrgId, planRecord.get("NAME"));
                int actualNum = 0;
                if(actual !=null && actual.size() > 0){
                    int actualSize = actual.size();
                    for(int j=0;j<actualSize;j++){
                        Record actualRecord = actual.get(j);
                        String actualOrgId = actualRecord.get("ORG_ID");
                        String actualNature = actualRecord.get("NATURE");

                        if(StringUtils.equals(planOrgId, actualOrgId) && StringUtils.equals(planNature, actualNature)){
                            actualNum = actualRecord.getInt("ACTUAL_NUM");
                            break;
                        }
                    }
                }
                temp.put(planOrgId+"_ACTUAL_"+planNature, actualNum+"");
            }

            for(String tempOrgId : orgTemp){
                JSONObject json = new JSONObject();
                json.put("ORG_ID", tempOrgId);
                json.put("NAME", orgNameTemp.get(tempOrgId));
                int planKeyNoPassCheck = Integer.parseInt(temp.get(tempOrgId+"_PLAN_0"));
                int actualKeyNoPassCheck = Integer.parseInt(temp.get(tempOrgId+"_ACTUAL_0"));
                int planKeyPassCheck = Integer.parseInt(temp.get(tempOrgId+"_PLAN_1"));
                int actualKeyPassCheck = Integer.parseInt(temp.get(tempOrgId+"_ACTUAL_1"));

                int loopholeNoPassCheck = planKeyNoPassCheck - actualKeyNoPassCheck;
                int loopholePassCheck = planKeyPassCheck - actualKeyPassCheck;

                if(loopholeNoPassCheck > loopholePassCheck) {
                    json.put("LOOPHOLE", loopholeNoPassCheck);
                    num = num + (loopholeNoPassCheck);
                }
                else{
                    json.put("LOOPHOLE", loopholePassCheck);
                    num = num + (loopholePassCheck);
                }
                jsonArray.add(json);
            }
        }

        response.set("LOOPHOLE", jsonArray);
        response.set("NUM", num);
        if(company != null)
            response.set("COMPANYS", ConvertTool.toJSONArray(company));

        return response;
    }
}
