package com.hirun.app.biz.houses;

import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;

import java.util.List;

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
}
