package com.hirun.app.biz.houses;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.org.OrgBean;
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
import com.most.core.pub.data.*;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        boolean needAllCity = false;
        OrgEntity org = null;
        if(StringUtils.isNotBlank(orgId)){
            //这段逻辑要替换成根据权限来判断
            OrgDAO dao = new OrgDAO("ins");
            org = dao.queryOrgById(orgId);
            String parentOrgId = org.getParentOrgId();
            if(StringUtils.isBlank(parentOrgId)){
                //表示是集团公司的员工
                needAllCity = true;
            }
        }
        else{
            needAllCity = true;
        }

        JSONArray citys = null;
        if(needAllCity){
            citys= ConvertTool.toJSONArray(StaticDataTool.getCodeTypeDatas("BIZ_CITY"));
        }
        else{
            citys = new JSONArray();
            JSONObject city = new JSONObject();
            city.put("CODE_VALUE", org.getCity());
            city.put("CODE_NAME", StaticDataTool.getCodeName("BIZ_CITY", org.getCity()));
            citys.add(city);
        }

        ServiceResponse response = new ServiceResponse();

        response.set("CITYS", citys);
        String today = TimeTool.today();
        response.set("TODAY", today);

        if(org != null){
            response.set("DEFAULT_CITY_ID", org.getCity());
            response.set("DEFAULT_CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", org.getCity()));
        }
        return response;
    }

    public ServiceResponse initArea(ServiceRequest request) throws Exception{
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        OrgDAO dao = new OrgDAO("ins");
        boolean needAllShop = true;
        OrgEntity org = null;
        OrgEntity parentOrg = null;
        if(StringUtils.isNotBlank(orgId)){
            //以后要换成权限判断
            org = dao.queryOrgById(orgId);
            String type = org.getType();
            if(StringUtils.equals("4", type)){
                needAllShop = false;
            }
            else if(StringUtils.equals("3", type)){
                String parentOrgId = org.getParentOrgId();
                if(StringUtils.isNotBlank(parentOrgId)){
                    parentOrg = dao.queryOrgById(parentOrgId);
                    String parentType = parentOrg.getType();
                    if(StringUtils.equals("4", parentType)){
                        needAllShop = false;
                    }
                }
            }
        }

        String cityId = request.getString("CITY_ID");
        RecordSet areas = StaticDataTool.getRelCodeTypeDatas("BIZ_CITY", cityId);
        List<OrgEntity> orgs = null;
        if(needAllShop) {
            //查询某城市下的门店
            orgs = dao.queryOrgByCityAndType(cityId, "4");
        }
        else{
            orgs = new ArrayList<OrgEntity>();
            if(StringUtils.equals("4", org.getType())){
                orgs.add(org);
            }
            else if(StringUtils.equals("3", org.getType())){
                if(StringUtils.isNotBlank(org.getParentOrgId()) && StringUtils.equals("4", parentOrg.getType())){
                    orgs.add(parentOrg);
                }
            }
        }

        ServiceResponse response = new ServiceResponse();
        response.set("AREAS", ConvertTool.toJSONArray(areas));
        response.set("SHOPS", ConvertTool.toJSONArray(orgs));
        if(ArrayTool.isNotEmpty(orgs)){
            response.set("DEFAULT_SHOP_NAME", orgs.get(0).getName());
            response.set("DEFAULT_SHOP_ID", orgs.get(0).getOrgId());
        }
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
        house.put("ORG_ID", request.getString("SHOP"));
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
            housesPlan.put("HOUSE_NUM", house.get(employee+"_HOUSENUM"));
            parameters.add(housesPlan);
        }

        HousesPlanDAO housesPlanDAO = new HousesPlanDAO("ins");
        housesPlanDAO.insertBatch("ins_houses_plan", parameters);

        return new ServiceResponse();
    }

    public ServiceResponse queryHousesPlan(ServiceRequest request) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.putAll(JSON.parseObject(request.getBody().getData().toJSONString(), Map.class));
        HousesPlanDAO dao = new HousesPlanDAO("ins");
        RecordSet recordset = dao.queryHousesPlan(parameter);
        if(recordset == null || recordset.size() <= 0){
            return new ServiceResponse();
        }


        //一次性查询出来后合并数据,避免多次连数据库的效率问题
        int size = recordset.size();
        JSONArray housesPlans = new JSONArray();
        Map<String, JSONObject> cache = new HashMap<String, JSONObject>();
        for(int i=0;i<size;i++){
            Record record = recordset.get(i);
            JSONObject housesPlan = cache.get(record.get("HOUSES_ID"));
            JSONArray employees = null;
            if(housesPlan == null){
                housesPlan = new JSONObject();
                housesPlan.put("NAME", record.get("NAME"));
                housesPlan.put("HOUSES_ID", record.get("HOUSES_ID"));
                housesPlan.put("CITY", record.get("CITY"));
                housesPlan.put("AREA", record.get("AREA"));
                housesPlan.put("CHECK_DATE", record.get("CHECK_DATE"));
                housesPlan.put("HOUSE_NUM", record.get("HOUSE_NUM"));
                housesPlan.put("PLAN_IN_DATE", record.get("PLAN_IN_DATE"));
                housesPlan.put("DESTROY_DATE", record.get("DESTROY_DATE"));
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate planInDate = LocalDate.parse(record.get("PLAN_IN_DATE"),dateTimeFormatter);
                LocalDate destroyDate = LocalDate.parse(record.get("DESTROY_DATE"), dateTimeFormatter);
                LocalDate now = LocalDate.now();
                long destroyMinusPlanInDate = TimeTool.getAbsDateDiffDay(destroyDate, planInDate);
                long planMinusNow = TimeTool.getAbsDateDiffDay(now, planInDate);
                DecimalFormat df = new DecimalFormat("0.00");
                double minus = Double.parseDouble(df.format((double)planMinusNow/(double)destroyMinusPlanInDate));
                double percent = Math.rint(minus*100);
                housesPlan.put("CUR_PROGRESS", percent+"");
                housesPlan.put("ALL_DAYS", destroyMinusPlanInDate);
                housesPlan.put("STATUS", record.get("STATUS"));
                housesPlan.put("CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", record.get("CITY")));
                housesPlan.put("AREA_NAME", StaticDataTool.getCodeName("BIZ_AREA", record.get("AREA")));
                housesPlan.put("ORG_NAME", record.get("ORG_NAME"));
                housesPlan.put("STATUS_NAME",StaticDataTool.getCodeName("AUDIT_STATUS", record.get("STATUS")));
                String nature = record.get("NATURE");
                if(StringUtils.equals("0", nature)){
                    housesPlan.put("NATURE_NAME", "期");
                }
                else if(StringUtils.equals("1", nature)){
                    housesPlan.put("NATURE_NAME", "现");
                }
                else if(StringUtils.equals("2", nature)){
                    housesPlan.put("NATURE_NAME", "责");
                }
                housesPlan.put("NATURE", record.get("NATURE"));
                employees = new JSONArray();
                housesPlan.put("COUNSELORS", employees);
                cache.put(record.get("HOUSES_ID"), housesPlan);
                housesPlans.add(housesPlan);
                String employeeId = record.get("EMPLOYEE_ID");
                if(StringUtils.isBlank(employeeId)){
                    continue;
                }
            }
            else{
                employees = housesPlan.getJSONArray("COUNSELORS");
            }
            JSONObject employee = new JSONObject();
            employee.put("EMPLOYEE_ID", record.get("EMPLOYEE_ID"));
            employee.put("EMPLOYEE_NAME", record.get("EMPLOYEE_NAME"));
            employee.put("ORG_ID", record.get("ORG_ID"));
            employee.put("ORG_NAME", record.get("ORG_NAME"));
            employee.put("TOWER_NO", record.get("TOWER_NO"));
            employees.add(employee);
        }
        ServiceResponse response = new ServiceResponse();
        response.set("DATA", housesPlans);
        return response;
    }

    public ServiceResponse submitAudit(ServiceRequest request) throws Exception{
        Map<String, String> updateParameter = new HashMap<String, String>();
        String auditStatus = request.getString("AUDIT_OPTION");
        String housesId = request.getString("AUDIT_HOUSES_ID");
        updateParameter.put("STATUS", auditStatus);
        updateParameter.put("HOUSES_ID", housesId);

        HouseDAO dao = new HouseDAO("ins");
        dao.save("ins_houses", updateParameter);

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSES_ID", housesId);
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String employeeId = sessionEntity.get("EMPLOYEE_ID");
        parameter.put("EMPLOYEE_ID", employeeId);
        parameter.put("AUDIT_STATUS", auditStatus);
        parameter.put("AUDIT_DATE", session.getCreateTime());
        parameter.put("AUDIT_OPINION", request.getString("AUDIT_OPINION"));
        parameter.put("CREATE_USER_ID", sessionEntity.getUserId());
        parameter.put("CREATE_DATE", session.getCreateTime());
        parameter.put("UPDATE_USER_ID", sessionEntity.getUserId());
        parameter.put("UPDATE_TIME", session.getCreateTime());
        dao.insertAutoIncrement("ins_houses_audit", parameter);
        return new ServiceResponse();
    }
}
