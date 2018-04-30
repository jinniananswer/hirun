package com.hirun.app.biz.houses;

import com.alibaba.fastjson.JSON;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.houses.HouseDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/26 10:21
 * @Description:
 */
public class HousesService extends GenericService {

    public ServiceResponse initCreatePlan(ServiceRequest request) throws Exception{
        RecordSet citys = StaticDataTool.getCodeTypeDatas("BIZ_CITY");
        String today = TimeTool.today();

        ServiceResponse response = new ServiceResponse();

        response.set("CITYS", ConvertTool.toJSONArray(citys));
        response.set("TODAY", today);
        return response;
    }

    public ServiceResponse initArea(ServiceRequest request) throws Exception{
        String cityId = request.getString("CITY_ID");
        RecordSet areas = StaticDataTool.getRelCodeTypeDatas("BIZ_CITY", cityId);

        OrgDAO dao = new OrgDAO("ins");
        List<OrgEntity> orgs = dao.queryOrgByCity(cityId);

        ServiceResponse response = new ServiceResponse();
        response.set("AREAS", ConvertTool.toJSONArray(areas));
        response.set("SHOPS", ConvertTool.toJSONArray(orgs));
        return response;
    }

    public ServiceResponse initCounselors(ServiceRequest request) throws Exception{
        String orgId = request.getString("ORG_ID");

        EmployeeDAO dao = new EmployeeDAO("ins");
        //查询家装顾问信息
        List<EmployeeEntity> employees = dao.queryEmployeeByParentOrgJobRole(orgId, "42");

        ServiceResponse response = new ServiceResponse();
        response.set("COUNSELORS", ConvertTool.toJSONArray(employees));
        return response;
    }

    public ServiceResponse createHousesPlan(ServiceRequest request) throws Exception{

        Map<String, String> house = new HashMap<String, String>();
        Map<String, String> housePlan = new HashMap<String, String>();
        AppSession session = SessionManager.getSession();
        String now = TimeTool.now();

        house.putAll(JSON.parseObject(request.getBody().getData().toJSONString(), Map.class));
        house.put("DESTROY_DATE", TimeTool.addMonths(house.get("CHECK_DATE"), "yyyy-MM-dd", 24));
        house.put("STATUS", "0");
        String userId = SessionManager.getSession().getSessionEntity().getUserId();
        house.put("CREATE_USER_ID", userId);
        house.put("UPDATE_USER_ID", userId);
        house.put("UPDATE_TIME", now);
        house.put("CREATE_DATE", now);

        HouseDAO dao = new HouseDAO("ins");
        long houseId = dao.insertAutoIncrement("ins_houses", house);
        String employeeId = request.getString("EMPLOYEE_ID");
        

        return new ServiceResponse();
    }
}
