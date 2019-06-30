package com.hirun.app.biz.custservice.citycabin;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.custservice.CustServiceStatBean;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.app.dao.custservice.citycabin.CityCabinDAO;
import com.hirun.pub.consts.CustomerServiceConst;
import com.hirun.pub.domain.entity.custservice.PartyEntity;
import com.hirun.pub.domain.entity.custservice.ProjectEntity;
import com.hirun.pub.domain.entity.custservice.ProjectIntentionEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.hirun.pub.tool.HouseParamTool;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.*;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CityCabinService extends GenericService {
    /**
     * 初始化城市木屋管理
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse initManagerCityCabin(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CityCabinDAO dao=DAOFactory.createDAO(CityCabinDAO.class);

        String org_id=OrgBean.getOrgId(session.getSessionEntity());
        OrgEntity orgEntity =OrgBean.getAssignTypeOrg(org_id,"1");
        RecordSet recordSet=dao.getCityCabinByCityId(orgEntity.getCity());

        response.set("CITYCABININFO",ConvertTool.toJSONArray(recordSet));

        return response;
    }

    public ServiceResponse queryCityCabinInfo(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CityCabinDAO dao=DAOFactory.createDAO(CityCabinDAO.class);

        String org_id=OrgBean.getOrgId(session.getSessionEntity());
        OrgEntity orgEntity =OrgBean.getAssignTypeOrg(org_id,"1");
        String houseAddress=request.getString("HOUSEADDRESS");
        String shop=request.getString("SHOP");
        String style=request.getString("STYLE");
        String custName=request.getString("CUST_NAME");
        String isVaild=request.getString("ISVAILD");
        String city=orgEntity.getCity();
        RecordSet recordSet=dao.queryCityCabinInfo(city,houseAddress,shop,style,custName,isVaild);

        if(recordSet.size()<0){
            return response;
        }

        response.set("CITYCABININFOLIST",ConvertTool.toJSONArray(recordSet));
        return response;
    }

    /**
     * 新增城市木屋
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse createCityCabin(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CityCabinDAO dao=DAOFactory.createDAO(CityCabinDAO.class);
        String org_id=OrgBean.getOrgId(session.getSessionEntity());
        OrgEntity orgEntity =OrgBean.getAssignTypeOrg(org_id,"1");

        Map<String,String> cityCabinInfo=new HashMap<String,String>();
        cityCabinInfo.put("BIZ_CITY",orgEntity.getCity());
        cityCabinInfo.put("SHOP",request.getString("SHOP"));
        cityCabinInfo.put("CUST_NAME",request.getString("CUSTNAME"));
        cityCabinInfo.put("CITYCABIN_ADDRESS",request.getString("HOUSE_ADDRESS"));
        cityCabinInfo.put("CITYCABIN_BUILDING",request.getString("HOUSE_BUILDING"));
        cityCabinInfo.put("CITYCABIN_ROOM",request.getString("HOUSE_ROOM"));
        cityCabinInfo.put("CITYCABIN_TITLE",request.getString("STYLE"));
        cityCabinInfo.put("AREA",request.getString("HOUSE_AREA"));
        cityCabinInfo.put("DESIGNER",request.getString("DESINGER"));
        cityCabinInfo.put("CUSTSERVICE",request.getString("CUSTOMERSERVICE"));
        cityCabinInfo.put("PROJECT_MANAGER",request.getString("PROJECT_MANAGER"));
        cityCabinInfo.put("COSTS",request.getString("COSTS"));
        cityCabinInfo.put("CONTACT",request.getString("CONTACT"));
        cityCabinInfo.put("SCAN_START_TIME",request.getString("COND_START_DATE"));
        cityCabinInfo.put("SCAN_END_TIME",request.getString("COND_END_DATE"));
        cityCabinInfo.put("IS_HAVE_PPT",request.getString("IS_PPT"));
        cityCabinInfo.put("REMARK",request.getString("REMARK"));
        cityCabinInfo.put("CREATE_USER_ID",session.getSessionEntity().getUserId());
        cityCabinInfo.put("CREATE_DATE",TimeTool.now());

        dao.insertAutoIncrement("ins_citycabin",cityCabinInfo);

        return response;
    }

    public ServiceResponse initChangeCityCabin(ServiceRequest request) throws Exception{
        ServiceResponse response=new ServiceResponse();
        CityCabinDAO dao=DAOFactory.createDAO(CityCabinDAO.class);
        Map<String,String> param=new HashMap<String,String>();
        param.put("CITY_CABIN_ID",request.getString("CITY_CABIN_ID"));
        Record record=dao.queryByPk("ins_citycabin",param);
        response.set("CITYCABININFO",ConvertTool.toJSONObject(record));
        return response;
    }

    public ServiceResponse changeCityCabin(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CityCabinDAO dao=DAOFactory.createDAO(CityCabinDAO.class);
        String org_id=OrgBean.getOrgId(session.getSessionEntity());
        OrgEntity orgEntity =OrgBean.getAssignTypeOrg(org_id,"1");

        Map<String,String> cityCabinInfo=new HashMap<String,String>();
        cityCabinInfo.put("CITY_CABIN_ID",request.getString("CITY_CABIN_ID"));
        cityCabinInfo.put("SHOP",request.getString("SHOP"));
        cityCabinInfo.put("CUST_NAME",request.getString("CUSTNAME"));
        cityCabinInfo.put("CITYCABIN_ADDRESS",request.getString("HOUSE_ADDRESS"));
        cityCabinInfo.put("CITYCABIN_BUILDING",request.getString("HOUSE_BUILDING"));
        cityCabinInfo.put("CITYCABIN_ROOM",request.getString("HOUSE_ROOM"));
        cityCabinInfo.put("CITYCABIN_TITLE",request.getString("STYLE"));
        cityCabinInfo.put("AREA",request.getString("HOUSE_AREA"));
        cityCabinInfo.put("DESIGNER",request.getString("DESINGER"));
        cityCabinInfo.put("CUSTSERVICE",request.getString("CUSTOMERSERVICE"));
        cityCabinInfo.put("PROJECT_MANAGER",request.getString("PROJECT_MANAGER"));
        cityCabinInfo.put("COSTS",request.getString("COSTS"));
        cityCabinInfo.put("CONTACT",request.getString("CONTACT"));
        cityCabinInfo.put("SCAN_START_TIME",request.getString("COND_START_DATE"));
        cityCabinInfo.put("SCAN_END_TIME",request.getString("COND_END_DATE"));
        cityCabinInfo.put("IS_HAVE_PPT",request.getString("IS_PPT"));
        cityCabinInfo.put("REMARK",request.getString("REMARK"));
        cityCabinInfo.put("UPDATE_USER_ID",session.getSessionEntity().getUserId());
        cityCabinInfo.put("UPDATE_TIME",TimeTool.now());
        dao.save("ins_citycabin",new String[]{"CITY_CABIN_ID"},cityCabinInfo);

        return response;
    }

    public ServiceResponse deleteCityCabin(ServiceRequest request) throws Exception{
        ServiceResponse response=new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CityCabinDAO dao=DAOFactory.createDAO(CityCabinDAO.class);
        Map<String,String> cityCabinInfo=new HashMap<String,String>();

        cityCabinInfo.put("CITY_CABIN_ID",request.getString("CITY_CABIN_ID"));
        cityCabinInfo.put("SCAN_END_TIME",TimeTool.now());
        cityCabinInfo.put("UPDATE_USER_ID",session.getSessionEntity().getUserId());
        cityCabinInfo.put("UPDATE_TIME",TimeTool.now());
        dao.save("ins_citycabin",new String[]{"CITY_CABIN_ID"},cityCabinInfo);

        return response;
    }


}
