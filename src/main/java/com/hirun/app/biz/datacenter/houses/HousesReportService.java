package com.hirun.app.biz.datacenter.houses;

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
            OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);
            company = orgDAO.queryCompany();
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
}
