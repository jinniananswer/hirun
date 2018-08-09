package com.hirun.app.bean.plan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.cache.EmployeeCache;
import com.hirun.app.dao.cust.CustActionDAO;
import com.hirun.app.dao.plan.PlanDayDAO;
import com.hirun.app.dao.stat.PlanFinishMonDAO;
import com.hirun.pub.domain.entity.cust.CustActionEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.plan.PlanDayEntity;
import com.hirun.pub.domain.entity.plan.PlanEntity;
import com.hirun.pub.domain.entity.plan.PlanFinishMonEntity;
import com.most.core.app.database.dao.factory.DAOFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pc on 2018-06-06.
 */
public class PlanStatBean {

    public static void statMonPlanFinishActionByPlanEntity(PlanEntity planEntity,JSONObject jsonFinishInfo)  throws Exception {
        String planId = planEntity.getPlanId();
        String employeeId = planEntity.getPlanExecutorId();
        String statMon = planEntity.getPlanDate().substring(0, 4) + planEntity.getPlanDate().substring(5,7);
        PlanFinishMonDAO planFinishMonDAO = DAOFactory.createDAO(PlanFinishMonDAO.class);

        PlanFinishMonEntity planFinishMonEntity = planFinishMonDAO.getPlanFinishMonEntityByStatMonAndEid(statMon, employeeId);
        if(planFinishMonEntity == null) {
            planFinishMonEntity = new PlanFinishMonEntity();
            planFinishMonEntity.setStatMon(statMon);
            planFinishMonEntity.setEmployeeId(employeeId);
            planFinishMonEntity.setStatResult(jsonFinishInfo.toJSONString());
            planFinishMonDAO.insert("stat_plan_finish_mon", planFinishMonEntity.getContent());
        } else {
            JSONObject jsonStatResult = JSONObject.parseObject(planFinishMonEntity.getStatResult());
            Iterator<String> iter = jsonFinishInfo.keySet().iterator();
            while(iter.hasNext()) {
                String action = iter.next();
                int num = jsonStatResult.getIntValue(action);
                int finishNum = jsonFinishInfo.getIntValue(action);
                jsonStatResult.put(action, num+finishNum);
            }
            planFinishMonEntity.setStatResult(jsonStatResult.toJSONString());
            planFinishMonDAO.update("stat_plan_finish_mon", new String[]{"STAT_MON", "EMPLOYEE_ID"}, planFinishMonEntity.getContent());
        }
    }

    public static void saveStatPlanDayEntity(PlanDayEntity planDayEntity) throws Exception {
        PlanDayDAO planDayDAO = DAOFactory.createDAO(PlanDayDAO.class);

        PlanDayEntity oldPlanDayEntity = planDayDAO.getPlanDayByStatDayAndStatTypeAndOid(planDayEntity.getStatDay(), planDayEntity.getStatType(), planDayEntity.getObjectId());
        if(oldPlanDayEntity == null) {
            planDayDAO.insert("STAT_PLAN_DAY", planDayEntity.getContent());
        } else {
            JSONObject jsonStatResult = JSONObject.parseObject(oldPlanDayEntity.getStatResult());
            JSONObject jsonNewStatResult = JSONObject.parseObject(planDayEntity.getStatResult());
            Iterator<String> iter = jsonNewStatResult.keySet().iterator();
            while(iter.hasNext()) {
                String key = iter.next();
                if(jsonStatResult.containsKey(key)) {
                    int oldValue = jsonStatResult.getIntValue(key);
                    int newValue = oldValue + jsonNewStatResult.getIntValue(key);
                    jsonStatResult.put(key, newValue);
                } else {
                    jsonStatResult.put(key, jsonNewStatResult.getIntValue(key));
                }
            }
            oldPlanDayEntity.setStatResult(jsonStatResult.toJSONString());
            planDayDAO.update("STAT_PLAN_DAY", oldPlanDayEntity.getContent());
        }
    }

    public static void saveStatPlanDayEntityByEmployee(PlanDayEntity employeePlanDayEntity) throws Exception {
        //员工天
        employeePlanDayEntity.setStatType("EMPLOYEE_FINISH");
        saveStatPlanDayEntity(employeePlanDayEntity);

        String employeeId = employeePlanDayEntity.getObjectId();

        //分公司
        OrgEntity shopOrgEntity = EmployeeBean.queryOrgByEmployee(employeeId, "2");
        if(shopOrgEntity != null) {
            PlanDayEntity shopPlanDayEntity = new PlanDayEntity();
            shopPlanDayEntity.setStatDay(employeePlanDayEntity.getStatDay());
            shopPlanDayEntity.setStatResult(employeePlanDayEntity.getStatResult());
            shopPlanDayEntity.setStatType("SHOP_FINISH");
            shopPlanDayEntity.setObjectId(shopOrgEntity.getOrgId());
            saveStatPlanDayEntity(shopPlanDayEntity);
        }

        //分公司
        OrgEntity filialeOrgEntity = EmployeeBean.queryOrgByEmployee(employeeId, "3");
        if(filialeOrgEntity != null) {
            PlanDayEntity filialePlanDayEntity = new PlanDayEntity();
            filialePlanDayEntity.setStatDay(employeePlanDayEntity.getStatDay());
            filialePlanDayEntity.setStatResult(employeePlanDayEntity.getStatResult());
            filialePlanDayEntity.setStatType("FILIALE_FINISH");
            filialePlanDayEntity.setObjectId(filialeOrgEntity.getOrgId());
            saveStatPlanDayEntity(filialePlanDayEntity);
        }

        //家装事业部
        PlanDayEntity buPlanDayEntity = new PlanDayEntity();
        buPlanDayEntity.setStatDay(employeePlanDayEntity.getStatDay());
        buPlanDayEntity.setStatResult(employeePlanDayEntity.getStatResult());
        buPlanDayEntity.setStatType("BU_FINISH");
        buPlanDayEntity.setObjectId("7");
        saveStatPlanDayEntity(buPlanDayEntity);
    }

    public static JSONArray queryEmployeeDailySheetList(String queryType, String queryId, String date) throws Exception {
        List<EmployeeEntity> employeeList = EmployeeBean.getAllSubordinatesCounselors(queryId);
        JSONArray dailySheetList = new JSONArray();
        for(EmployeeEntity employeeEntity : employeeList) {
            JSONObject dailySheet = queryEmployeeDailySheetByEmployeeId(employeeEntity.getEmployeeId(), date);
            dailySheetList.add(dailySheet);
        }

        return dailySheetList;
    }

    public static JSONObject queryEmployeeDailySheetByEmployeeId(String employeeId, String date) throws Exception {
        PlanDayDAO planDayDAO = DAOFactory.createDAO(PlanDayDAO.class);

        PlanDayEntity employeePlan = planDayDAO.getPlanDayByStatDayAndStatTypeAndOid(date, "EMPLOYEE_PLAN", employeeId);
        PlanDayEntity employeeFinish = planDayDAO.getPlanDayByStatDayAndStatTypeAndOid(date, "EMPLOYEE_FINISH", employeeId);

        JSONObject jsonEmployeePlan = new JSONObject();
        if(employeePlan == null) {
            jsonEmployeePlan.put("PLAN_JW",0);
            jsonEmployeePlan.put("PLAN_LTZDSTS",0);
            jsonEmployeePlan.put("PLAN_GZHGZ",0);
            jsonEmployeePlan.put("PLAN_HXJC",0);
            jsonEmployeePlan.put("PLAN_SMJRQLC",0);
            jsonEmployeePlan.put("PLAN_XQLTYTS",0);
            jsonEmployeePlan.put("PLAN_ZX",0);
            jsonEmployeePlan.put("PLAN_YJALTS",0);
            jsonEmployeePlan.put("PLAN_DKCSMU",0);
        } else {
            JSONObject tmp = JSONObject.parseObject(employeePlan.getStatResult());
            jsonEmployeePlan.put("PLAN_JW",tmp.getIntValue("JW"));
            jsonEmployeePlan.put("PLAN_LTZDSTS",tmp.getIntValue("LTZDSTS"));
            jsonEmployeePlan.put("PLAN_GZHGZ",tmp.getIntValue("GZHGZ"));
            jsonEmployeePlan.put("PLAN_HXJC",tmp.getIntValue("HXJC"));
            jsonEmployeePlan.put("PLAN_SMJRQLC",tmp.getIntValue("SMJRQLC"));
            jsonEmployeePlan.put("PLAN_XQLTYTS",tmp.getIntValue("XQLTYTS"));
            jsonEmployeePlan.put("PLAN_ZX",tmp.getIntValue("ZX"));
            jsonEmployeePlan.put("PLAN_YJALTS",tmp.getIntValue("YJALTS"));
            jsonEmployeePlan.put("PLAN_DKCSMU",tmp.getIntValue("DKCSMU"));
        }

        JSONObject jsonEmployeeFinish = new JSONObject();
        if(employeeFinish == null) {
            jsonEmployeeFinish.put("FINISH_JW",0);
            jsonEmployeeFinish.put("FINISH_LTZDSTS",0);
            jsonEmployeeFinish.put("FINISH_GZHGZ",0);
            jsonEmployeeFinish.put("FINISH_HXJC",0);
            jsonEmployeeFinish.put("FINISH_SMJRQLC",0);
            jsonEmployeeFinish.put("FINISH_XQLTYTS",0);
            jsonEmployeeFinish.put("FINISH_ZX",0);
            jsonEmployeeFinish.put("FINISH_YJALTS",0);
            jsonEmployeeFinish.put("FINISH_DKCSMU",0);
        } else {
            JSONObject tmp = JSONObject.parseObject(employeeFinish.getStatResult());
            jsonEmployeeFinish.put("FINISH_JW",tmp.getIntValue("JW"));
            jsonEmployeeFinish.put("FINISH_LTZDSTS",tmp.getIntValue("LTZDSTS"));
            jsonEmployeeFinish.put("FINISH_GZHGZ",tmp.getIntValue("GZHGZ"));
            jsonEmployeeFinish.put("FINISH_HXJC",tmp.getIntValue("HXJC"));
            jsonEmployeeFinish.put("FINISH_SMJRQLC",tmp.getIntValue("SMJRQLC"));
            jsonEmployeeFinish.put("FINISH_XQLTYTS",tmp.getIntValue("XQLTYTS"));
            jsonEmployeeFinish.put("FINISH_ZX",tmp.getIntValue("ZX"));
            jsonEmployeeFinish.put("FINISH_YJALTS",tmp.getIntValue("YJALTS"));
            jsonEmployeeFinish.put("FINISH_DKCSMU",tmp.getIntValue("DKCSMU"));
        }

        JSONObject dailySheet = new JSONObject();
        dailySheet.putAll(jsonEmployeePlan);
        dailySheet.putAll(jsonEmployeeFinish);
        dailySheet.put("EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(employeeId));

        return dailySheet;
    }
}
