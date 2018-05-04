package com.hirun.app.biz.houses;

import com.alibaba.fastjson.JSON;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.houses.HouseDAO;
import com.hirun.app.dao.houses.HousesPlanDAO;
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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
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
        AppSession session = SessionManager.getSession();
        String now = TimeTool.now();

        house.putAll(JSON.parseObject(request.getBody().getData().toJSONString(), Map.class));
        String destroyDate = TimeTool.addMonths(house.get("CHECK_DATE"), "yyyy-MM-dd", 24);
        house.put("DESTROY_DATE", destroyDate);
        house.put("STATUS", "0");
        String userId = session.getSessionEntity().getUserId();
        house.put("CREATE_USER_ID", userId);
        house.put("UPDATE_USER_ID", userId);
        house.put("UPDATE_TIME", session.getCreateTime());
        house.put("CREATE_DATE", session.getCreateTime());

        HouseDAO dao = new HouseDAO("ins");
        long houseId = dao.insertAutoIncrement("ins_houses", house);
        String employeeId = request.getString("EMPLOYEE_ID");
        if(StringUtils.isBlank(employeeId))
            return new ServiceResponse();

        String[] employees = employeeId.split(",");
        List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
        for(String employee : employees){
            Map<String, String> housesPlan = new HashMap<String, String>();
            housesPlan.put("HOUSES_ID", String.valueOf(houseId));
            housesPlan.put("EMPLOYEE_ID", employee);
            housesPlan.put("ORG_ID", request.getString("SHOP"));
            housesPlan.put("START_DATE", session.getCreateTime());
            housesPlan.put("END_DATE", destroyDate);
            housesPlan.put("STATUS", "0");
            housesPlan.put("CREATE_USER_ID", userId);
            housesPlan.put("CREATE_DATE", session.getCreateTime());
            housesPlan.put("UPDATE_USER_ID", userId);
            housesPlan.put("UPDATE_TIME", session.getCreateTime());
            housesPlan.put("TOWER_NO", house.get(employee+"_TOWERNUM"));
            parameters.add(housesPlan);
        }

        HousesPlanDAO housesPlanDAO = new HousesPlanDAO("ins");
        housesPlanDAO.insertBatch("ins_houses_plan", parameters);

        return new ServiceResponse();
    }
}
