package com.hirun.app.biz.houses;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.authority.AuthorityJudgement;
import com.hirun.app.bean.houses.HousesBean;
import com.hirun.app.bean.housesplan.HousesPlanBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.houses.HouseDAO;
import com.hirun.app.dao.houses.HousesPlanDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.houses.HousesEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
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

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author jinnian
 * @Date 2018/4/26 10:21
 * @Description:
 */
public class HousesService extends GenericService {

    public ServiceResponse initCreatePlan(ServiceRequest request) throws Exception {
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        OrgEntity org = null;
        if (StringUtils.isNotBlank(orgId)) {
            OrgDAO dao = new OrgDAO("ins");
            org = dao.queryOrgById(orgId);
        }
        boolean needAllCity = AuthorityJudgement.hasAllCity();

        JSONArray citys = null;
        if (needAllCity) {
            citys = ConvertTool.toJSONArray(StaticDataTool.getCodeTypeDatas("BIZ_CITY"));
        } else {
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

        if (org != null) {
            response.set("DEFAULT_CITY_ID", org.getCity());
            response.set("DEFAULT_CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", org.getCity()));
        }

        return response;
    }

    public ServiceResponse initArea(ServiceRequest request) throws Exception {
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        OrgDAO dao = new OrgDAO("ins");
        OrgEntity org = dao.queryOrgById(orgId);
        OrgEntity parentOrg = null;
        boolean needAllShop = AuthorityJudgement.hasAllShop();

        String cityId = request.getString("CITY_ID");
        RecordSet areas = StaticDataTool.getRelCodeTypeDatas("BIZ_CITY", cityId);
        List<OrgEntity> orgs = null;
        if (needAllShop) {
            //查询某城市下的门店
            orgs = dao.queryOrgByCityAndType(cityId, "4");
        } else {
            orgs = new ArrayList<OrgEntity>();
            OrgEntity shop = OrgBean.getAssignTypeOrg(orgId, "2");
            if(shop != null)
                orgs.add(shop);
        }

        ServiceResponse response = new ServiceResponse();
        response.set("AREAS", ConvertTool.toJSONArray(areas));
        response.set("SHOPS", ConvertTool.toJSONArray(orgs));
        if (ArrayTool.isNotEmpty(orgs)) {
            response.set("DEFAULT_SHOP_NAME", orgs.get(0).getName());
            response.set("DEFAULT_SHOP_ID", orgs.get(0).getOrgId());
        }
        return response;
    }

    public ServiceResponse initCounselors(ServiceRequest request) throws Exception {
        String orgId = request.getString("ORG_ID");

        EmployeeDAO dao = new EmployeeDAO("ins");
        //查询家装顾问信息
        List<EmployeeEntity> employees = dao.queryEmployeeByParentOrgJobRole(orgId, "42,58");

        ServiceResponse response = new ServiceResponse();
        response.set("COUNSELORS", ConvertTool.toJSONArray(employees));
        return response;
    }

    public ServiceResponse createHousesPlan(ServiceRequest request) throws Exception {

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
        List<HousesEntity> houses = dao.queryHousesByName(request.getString("NAME"));
        if(ArrayTool.isNotEmpty(houses)){
            ServiceResponse response = new ServiceResponse();
            response.setError("HIRUN_CREATE_HOUSE_000001","该楼盘名称已经存在，请重新检查楼盘名称！");
            return response;
        }
        long houseId = dao.insertAutoIncrement("ins_houses", house);
        String employeeId = request.getString("EMPLOYEE_ID");
        if (StringUtils.isBlank(employeeId))
            return new ServiceResponse();

        String[] employees = employeeId.split(",");
        List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
        for (String employee : employees) {
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
            housesPlan.put("TOWER_NO", house.get(employee + "_TOWERNUM"));
            housesPlan.put("HOUSE_NUM", house.get(employee + "_HOUSENUM"));
            parameters.add(housesPlan);
        }

        HousesPlanDAO housesPlanDAO = new HousesPlanDAO("ins");
        housesPlanDAO.insertBatch("ins_houses_plan", parameters);

        return new ServiceResponse();
    }



    public ServiceResponse queryHousesPlan(ServiceRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.putAll(JSON.parseObject(request.getBody().getData().toJSONString(), Map.class));
        HousesPlanDAO dao = new HousesPlanDAO("ins");

        boolean needAllShop = AuthorityJudgement.hasAllShop();
        boolean needAllCity = Permission.hasAllCity();
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        OrgDAO orgDAO = new OrgDAO("ins");
        OrgEntity org = orgDAO.queryOrgById(orgId);
        OrgEntity parentOrg = null;


        if (needAllShop) {

        } else {
            OrgEntity shop = OrgBean.getAssignTypeOrg(orgId, "2");
            if(shop != null)
                parameter.put("SHOP", shop.getOrgId());
        }

        if(!needAllCity){
            parameter.put("CITY", org.getCity());
        }

        RecordSet recordset = dao.queryHousesPlan(parameter);
        if (recordset == null || recordset.size() <= 0) {
            return new ServiceResponse();
        }


        //一次性查询出来后合并数据,避免多次连数据库的效率问题
        int size = recordset.size();
        JSONArray housesPlans = new JSONArray();
        Map<String, JSONObject> cache = new HashMap<String, JSONObject>();
        for (int i = 0; i < size; i++) {
            Record record = recordset.get(i);
            JSONObject housesPlan = cache.get(record.get("HOUSES_ID"));
            JSONArray employees = null;
            if (housesPlan == null) {
                housesPlan = new JSONObject();
                housesPlan.put("NAME", record.get("NAME"));
                housesPlan.put("HOUSES_ID", record.get("HOUSES_ID"));
                housesPlan.put("CITY", record.get("CITY"));
                housesPlan.put("AREA", record.get("AREA"));
                housesPlan.put("CHECK_DATE", record.get("CHECK_DATE"));
                housesPlan.put("HOUSE_NUM", record.get("HOUSE_NUM"));
                housesPlan.put("PLAN_IN_DATE", record.get("PLAN_IN_DATE"));
                housesPlan.put("ACTUAL_IN_DATE", record.get("ACTUAL_IN_DATE"));
                housesPlan.put("DESTROY_DATE", record.get("DESTROY_DATE"));
                housesPlan.put("PLAN_COUNSELOR_NUM", record.get("PLAN_COUNSELOR_NUM"));
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate planInDate = LocalDate.parse(record.get("PLAN_IN_DATE"), dateTimeFormatter);
                LocalDate destroyDate = LocalDate.parse(record.get("DESTROY_DATE"), dateTimeFormatter);
                String checkDate = record.get("CHECK_DATE");
                String today = TimeTool.today();
                String responsibilityTime = TimeTool.addMonths(checkDate, TimeTool.DATE_FMT_3, 18);

                if(today.compareTo(checkDate) >= 0)
                    housesPlan.put("PAST_CHECK_DATE", "true");

                if(today.compareTo(responsibilityTime) >= 0)
                    housesPlan.put("PAST_RESPONSIBILITY", true);
                LocalDate now = LocalDate.now();
                long destroyMinusPlanInDate = TimeTool.getAbsDateDiffDay(destroyDate, planInDate);
                long planMinusNow = TimeTool.getAbsDateDiffDay(now, planInDate);
                DecimalFormat df = new DecimalFormat("0.00");
                double minus = Double.parseDouble(df.format((double) planMinusNow / (double) destroyMinusPlanInDate));
                double percent = Math.rint(minus * 100);
                housesPlan.put("CUR_PROGRESS", percent + "");
                housesPlan.put("ALL_DAYS", destroyMinusPlanInDate);
                housesPlan.put("PAST_DAYS", planMinusNow);
                housesPlan.put("STATUS", record.get("STATUS"));
                housesPlan.put("CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", record.get("CITY")));
                String areaName = StaticDataTool.getCodeName("BIZ_AREA", record.get("AREA"));
                if(StringUtils.isBlank(areaName))
                    areaName = " ";
                housesPlan.put("AREA_NAME", areaName);
                housesPlan.put("ORG_NAME", record.get("ORG_NAME"));
                housesPlan.put("STATUS_NAME", StaticDataTool.getCodeName("AUDIT_STATUS", record.get("STATUS")));
                String nature = record.get("NATURE");
                if (StringUtils.equals("0", nature)) {
                    housesPlan.put("NATURE_NAME", "期");
                } else if (StringUtils.equals("1", nature)) {
                    housesPlan.put("NATURE_NAME", "现");
                } else if (StringUtils.equals("2", nature)) {
                    housesPlan.put("NATURE_NAME", "责");
                }
                housesPlan.put("NATURE", record.get("NATURE"));
                employees = new JSONArray();
                housesPlan.put("COUNSELORS", employees);
                cache.put(record.get("HOUSES_ID"), housesPlan);
                housesPlans.add(housesPlan);
                String employeeId = record.get("EMPLOYEE_ID");
                if (StringUtils.isBlank(employeeId)) {
                    continue;
                }
            } else {
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

    public ServiceResponse queryMyHouses(ServiceRequest request) throws Exception{
        HousesPlanDAO dao = new HousesPlanDAO("ins");
        RecordSet recordset = dao.queryHousesByEmployeeId(request.getString("EMPLOYEE_ID"));
        if (recordset == null || recordset.size() <= 0) {
            return new ServiceResponse();
        }

        int size = recordset.size();
        for (int i = 0; i < size; i++) {
            Record record = recordset.get(i);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate planInDate = LocalDate.parse(record.get("PLAN_IN_DATE"), dateTimeFormatter);
            LocalDate destroyDate = LocalDate.parse(record.get("DESTROY_DATE"), dateTimeFormatter);
            String checkDate = record.get("CHECK_DATE");
            String today = TimeTool.today();
            String responsibilityTime = TimeTool.addMonths(checkDate, TimeTool.DATE_FMT_3, 18);

            if(today.compareTo(checkDate) >= 0)
                record.put("PAST_CHECK_DATE", "true");

            if(today.compareTo(responsibilityTime) >= 0)
                record.put("PAST_RESPONSIBILITY", "true");
            LocalDate now = LocalDate.now();
            long destroyMinusPlanInDate = TimeTool.getAbsDateDiffDay(destroyDate, planInDate);
            long planMinusNow = TimeTool.getAbsDateDiffDay(now, planInDate);
            DecimalFormat df = new DecimalFormat("0.00");
            double minus = Double.parseDouble(df.format((double) planMinusNow / (double) destroyMinusPlanInDate));
            double percent = Math.rint(minus * 100);
            record.put("CUR_PROGRESS", percent + "");
            record.put("ALL_DAYS", destroyMinusPlanInDate);
            record.put("PAST_DAYS", planMinusNow);
            record.put("CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", record.get("CITY")));
            record.put("AREA_NAME", StaticDataTool.getCodeName("BIZ_AREA", record.get("AREA")));
            record.put("ORG_NAME", record.get("ORG_NAME"));
            record.put("STATUS_NAME", StaticDataTool.getCodeName("AUDIT_STATUS", record.get("STATUS")));
            String nature = record.get("NATURE");

            if (StringUtils.equals("0", nature)) {
                record.put("NATURE_NAME", "期");
            } else if (StringUtils.equals("1", nature)) {
                record.put("NATURE_NAME", "现");
            } else if (StringUtils.equals("2", nature)) {
                record.put("NATURE_NAME", "责");
            }
            record.put("NATURE", record.get("NATURE"));
        }
        ServiceResponse response = new ServiceResponse();
        response.set("DATA", ConvertTool.toJSONArray(recordset));
        return response;
    }

    public ServiceResponse submitAudit(ServiceRequest request) throws Exception {
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

    public ServiceResponse queryHousesByEmployeeId(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String employeeId = requestData.getString("EMPLOYEE_ID");

        response.set("HOUSES_LIST", HousesPlanBean.queryHousesByEmployeeId(employeeId));

        return response;
    }

    public ServiceResponse initChangeHousesPlan(ServiceRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.putAll(JSON.parseObject(request.getBody().getData().toJSONString(), Map.class));
        HousesPlanDAO dao = new HousesPlanDAO("ins");
        RecordSet recordset = dao.queryHousesPlan(parameter);
        if (recordset == null || recordset.size() <= 0) {
            return new ServiceResponse();
        }

        //一次性查询出来后合并数据,避免多次连数据库的效率问题
        int size = recordset.size();
        JSONObject housesPlan = null;
        for (int i = 0; i < size; i++) {
            Record record = recordset.get(i);
            JSONArray employees = null;
            if (housesPlan == null) {
                housesPlan = new JSONObject();
                housesPlan.put("NAME", record.get("NAME"));
                housesPlan.put("HOUSES_ID", record.get("HOUSES_ID"));
                housesPlan.put("CITY", record.get("CITY"));
                housesPlan.put("AREA", record.get("AREA"));
                housesPlan.put("CHECK_DATE", record.get("CHECK_DATE"));
                housesPlan.put("HOUSE_NUM", record.get("HOUSE_NUM"));
                housesPlan.put("PLAN_IN_DATE", record.get("PLAN_IN_DATE"));
                housesPlan.put("ACTUAL_IN_DATE", record.get("ACTUAL_IN_DATE"));
                housesPlan.put("DESTROY_DATE", record.get("DESTROY_DATE"));
                housesPlan.put("PLAN_COUNSELOR_NUM", record.get("PLAN_COUNSELOR_NUM"));
                housesPlan.put("STATUS", record.get("STATUS"));
                housesPlan.put("CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", record.get("CITY")));
                String areaName = StaticDataTool.getCodeName("BIZ_AREA", record.get("AREA"));
                if(StringUtils.isBlank(areaName))
                    areaName = "";
                housesPlan.put("AREA_NAME", areaName);
                housesPlan.put("ORG_ID", record.get("ORG_ID"));
                housesPlan.put("ORG_NAME", record.get("ORG_NAME"));
                housesPlan.put("STATUS_NAME", StaticDataTool.getCodeName("AUDIT_STATUS", record.get("STATUS")));
                housesPlan.put("NATURE", record.get("NATURE"));
                housesPlan.put("NATURE_NAME", StaticDataTool.getCodeName("HOUSE_NATURE", record.get("NATURE")));
                employees = new JSONArray();
                housesPlan.put("COUNSELORS", employees);
                String employeeId = record.get("EMPLOYEE_ID");
                if (StringUtils.isBlank(employeeId)) {
                    continue;
                }
            } else {
                employees = housesPlan.getJSONArray("COUNSELORS");
            }
            JSONObject employee = new JSONObject();
            employee.put("EMPLOYEE_ID", record.get("EMPLOYEE_ID"));
            employee.put("EMPLOYEE_NAME", record.get("EMPLOYEE_NAME"));
            employee.put("ORG_ID", record.get("ORG_ID"));
            employee.put("ORG_NAME", record.get("ORG_NAME"));
            employee.put("TOWER_NO", record.get("TOWER_NO"));
            employee.put("EMPLOYEE_HOUSE_NUM", record.get("EMPLOYEE_HOUSE_NUM"));
            employees.add(employee);
        }

        if (housesPlan != null) {
            ServiceResponse response = new ServiceResponse();
            boolean needAllCity = AuthorityJudgement.hasAllCity();

            JSONArray citys = null;
            if (needAllCity) {
                citys = ConvertTool.toJSONArray(StaticDataTool.getCodeTypeDatas("BIZ_CITY"));
            } else {
                citys = new JSONArray();
                JSONObject city = new JSONObject();
                city.put("CODE_VALUE", housesPlan.getString("CITY"));
                city.put("CODE_NAME", StaticDataTool.getCodeName("BIZ_CITY", housesPlan.getString("CITY")));
                citys.add(city);
            }
            response.set("HOUSES_PLAN", housesPlan);
            response.set("CITYS", citys);
            return response;
        } else {
            return new ServiceResponse();
        }
    }

    public ServiceResponse changeHousePlan(ServiceRequest request) throws Exception {
        Map<String, String> house = new HashMap<String, String>();
        AppSession session = SessionManager.getSession();
        String now = TimeTool.now();

        house.putAll(JSON.parseObject(request.getBody().getData().toJSONString(), Map.class));
        String destroyDate = TimeTool.addMonths(house.get("CHECK_DATE"), "yyyy-MM-dd", 24);
        house.put("DESTROY_DATE", destroyDate);
        //house.put("STATUS", "0");
        String userId = session.getSessionEntity().getUserId();
        house.put("UPDATE_USER_ID", userId);
        house.put("ORG_ID", request.getString("SHOP"));
        house.put("UPDATE_TIME", session.getCreateTime());

        HouseDAO dao = new HouseDAO("ins");
        String houseId = request.getString("HOUSES_ID");
        dao.save("ins_houses", house);
        String employeeId = request.getString("EMPLOYEE_ID");
        String oldEmployeeId = request.getString("OLD_EMPLOYEE_ID");

        if (StringUtils.isBlank(employeeId) && StringUtils.isBlank(oldEmployeeId)) {
            return new ServiceResponse();
        }
        List<String> cancelEmployees = new ArrayList<String>();
        ;
        List<String> addEmployees = new ArrayList<String>();
        ;
        List<String> updateEmployees = new ArrayList<String>();
        ;
        if (StringUtils.isBlank(employeeId) && StringUtils.isNotBlank(oldEmployeeId)) {
            cancelEmployees = Arrays.asList(oldEmployeeId.split(","));
        } else if (StringUtils.isNotBlank(employeeId) && StringUtils.isBlank(oldEmployeeId)) {
            addEmployees = Arrays.asList(employeeId.split(","));
        } else {
            String[] employees = employeeId.split(",");
            String[] oldEmployees = oldEmployeeId.split(",");

            for (String oldEmployee : oldEmployees) {
                boolean find = false;
                for (String employee : employees) {
                    if (StringUtils.equals(oldEmployee, employee)) {
                        find = true;
                        break;
                    }
                }
                if (find) {
                    //新老都有，表示修改
                    updateEmployees.add(oldEmployee);
                } else {
                    //老有，新无，表示取消
                    cancelEmployees.add(oldEmployee);
                }
            }

            for (String employee : employees) {
                boolean find = false;
                for (String oldEmployee : oldEmployees) {
                    if (StringUtils.equals(employee, oldEmployee)) {
                        find = true;
                        break;
                    }
                }

                if (!find) {
                    //新有，老无，表示新增
                    addEmployees.add(employee);
                }
            }
        }

        HousesPlanDAO housesPlanDAO = new HousesPlanDAO("ins");
        if (ArrayTool.isNotEmpty(addEmployees)) {
            List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
            for (String employee : addEmployees) {
                Map<String, String> housesPlan = new HashMap<String, String>();
                housesPlan.put("HOUSES_ID", houseId);
                housesPlan.put("EMPLOYEE_ID", employee);
                housesPlan.put("ORG_ID", request.getString("SHOP"));
                housesPlan.put("START_DATE", session.getCreateTime());
                housesPlan.put("END_DATE", destroyDate);
                housesPlan.put("STATUS", "0");
                housesPlan.put("CREATE_USER_ID", userId);
                housesPlan.put("CREATE_DATE", session.getCreateTime());
                housesPlan.put("UPDATE_USER_ID", userId);
                housesPlan.put("UPDATE_TIME", session.getCreateTime());
                housesPlan.put("TOWER_NO", house.get(employee + "_TOWERNUM"));
                housesPlan.put("HOUSE_NUM", house.get(employee + "_HOUSENUM"));
                parameters.add(housesPlan);
            }
            housesPlanDAO.insertBatch("ins_houses_plan", parameters);
        }

        if (ArrayTool.isNotEmpty(updateEmployees)) {
            for (String employee : updateEmployees) {
                Map<String, String> housesPlan = new HashMap<String, String>();
                housesPlan.put("HOUSES_ID", houseId);
                housesPlan.put("EMPLOYEE_ID", employee);
                housesPlan.put("ORG_ID", request.getString("SHOP"));
                housesPlan.put("END_DATE", destroyDate);
                housesPlan.put("UPDATE_USER_ID", userId);
                housesPlan.put("UPDATE_TIME", session.getCreateTime());
                housesPlan.put("TOWER_NO", house.get(employee + "_TOWERNUM"));
                housesPlan.put("HOUSE_NUM", house.get(employee + "_HOUSENUM"));
                dao.save("ins_houses_plan", new String[]{"HOUSES_ID", "EMPLOYEE_ID"}, housesPlan);
            }
        }

        if (ArrayTool.isNotEmpty(cancelEmployees)) {
            for (String employee : cancelEmployees) {
                Map<String, String> housesPlan = new HashMap<String, String>();
                housesPlan.put("HOUSES_ID", houseId);
                housesPlan.put("EMPLOYEE_ID", employee);
                housesPlan.put("UPDATE_USER_ID", userId);
                housesPlan.put("UPDATE_TIME", session.getCreateTime());
                housesPlan.put("END_DATE", session.getCreateTime());
                dao.save("ins_houses_plan", new String[]{"HOUSES_ID", "EMPLOYEE_ID"}, housesPlan);
            }
        }
        return new ServiceResponse();
    }

    public ServiceResponse showHouseDetail(ServiceRequest request) throws Exception {
        ServiceResponse response = this.initChangeHousesPlan(request);
        response.set("HAS_CHANGE", Permission.hasChangeHouse()?"true":"false");
        response.set("HAS_AUDIT", Permission.hasAuditHouse()?"true":"false");
        return response;
    }

    public ServiceResponse queryHouses(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        response.set("HOUSES_LIST", HousesPlanBean.queryHouses());

        return response;
    }

    public ServiceResponse showMyHouseDetail(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        HousesPlanDAO dao = DAOFactory.createDAO(HousesPlanDAO.class);
        String employeeId = SessionManager.getSession().getSessionEntity().get("EMPLOYEE_ID");
        Record house = dao.queryHousesByEmployeeIdHouseId(employeeId, request.getString("HOUSE_ID"));
        if(house == null)
            return response;

        house.put("CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", house.get("CITY")));
        String areaName = StaticDataTool.getCodeName("BIZ_AREA", house.get("AREA"));
        if(StringUtils.isBlank(areaName))
            areaName = "";
        house.put("AREA_NAME", areaName);
        house.put("STATUS_NAME", StaticDataTool.getCodeName("AUDIT_STATUS", house.get("STATUS")));
        house.put("NATURE_NAME", StaticDataTool.getCodeName("HOUSE_NATURE", house.get("NATURE")));
        response.set("HOUSE", JSONObject.parseObject(JSON.toJSONString(house.getData())));

        CustDAO custDAO = DAOFactory.createDAO(CustDAO.class);
        List<CustomerEntity> customers = custDAO.getCustomersByEmployeeIdHouseId(employeeId, request.getString("HOUSE_ID"));
        response.set("CUSTOMERS", ConvertTool.toJSONArray(customers));
        return response;
    }

    public ServiceResponse queryHousesByName(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String housesName = request.getString("HOUSES_NAME");

        List<HousesEntity> housesEntityList = HousesBean.queryHousesEntityListByName(housesName);

        response.set("HOUSES_LIST", ConvertTool.toJSONArray(housesEntityList, new String[] {"HOUSES_ID","NAME"}));

        return response;
    }

    public ServiceResponse initCreateScatterHouses(ServiceRequest request) throws Exception{
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        OrgEntity org = null;
        if (StringUtils.isNotBlank(orgId)) {
            OrgDAO dao = new OrgDAO("ins");
            org = dao.queryOrgById(orgId);
        }
        boolean needAllCity = AuthorityJudgement.hasAllCity();

        JSONArray citys = null;
        if (needAllCity) {
            citys = ConvertTool.toJSONArray(StaticDataTool.getCodeTypeDatas("BIZ_CITY"));
        } else {
            citys = new JSONArray();
            JSONObject city = new JSONObject();
            city.put("CODE_VALUE", org.getCity());
            city.put("CODE_NAME", StaticDataTool.getCodeName("BIZ_CITY", org.getCity()));
            citys.add(city);
        }

        ServiceResponse response = new ServiceResponse();

        response.set("CITYS", citys);

        if (org != null) {
            response.set("DEFAULT_CITY_ID", org.getCity());
            response.set("DEFAULT_CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", org.getCity()));
        }
        return response;
    }

    public ServiceResponse submitScatterHouses(ServiceRequest request) throws Exception{
        long houseId = HousesBean.createScatterHouse(request.getString("NAME"), request.getString("CITY"));
        ServiceResponse response = new ServiceResponse();
        response.set("HOUSE_ID", houseId + "");
        return response;
    }

    public ServiceResponse autoUpdateNature(ServiceRequest request) throws Exception{
        HousesPlanDAO dao = DAOFactory.createDAO(HousesPlanDAO.class);
        dao.autoUpdateHouseNature();
        return new ServiceResponse();
    }

    public ServiceResponse queryScatterHouses(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        String housesName = request.getString("HOUSES_NAME");
        List<HousesEntity> housesEntityList = HousesBean.queryScatterHouses(housesName);

        response.set("HOUSES_LIST", ConvertTool.toJSONArray(housesEntityList));

        return response;
    }

    public ServiceResponse queryScatterHousesByCond(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        String name = request.getString("NAME");
        List<HousesEntity> houses = HousesBean.queryScatterHouses(name);
        if (ArrayTool.isEmpty(houses)) {
            return response;
        }

        JSONArray housesJsonArray = ConvertTool.toJSONArray(houses);
        for (Object temp : housesJsonArray) {
            JSONObject house = (JSONObject)temp;
            house.put("CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", house.getString("CITY")));
            house.put("NATURE_NAME", "散");
        }
        response.set("DATA", housesJsonArray);
        return response;
    }
}
