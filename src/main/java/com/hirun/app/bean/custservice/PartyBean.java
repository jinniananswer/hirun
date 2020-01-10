package com.hirun.app.bean.custservice;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.employee.EmployeeJobRoleDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.consts.CustomerServiceConst;
import com.hirun.pub.domain.entity.custservice.PartyEntity;
import com.hirun.pub.domain.entity.custservice.PartyOriginalActionEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;


public class PartyBean {


    public static PartyEntity getPartyInfoByOpenId(String openid) throws Exception {

        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        PartyEntity partyEntityEntity = dao.queryPartyInfoByOpenId(openid);
        if (partyEntityEntity == null) {
            return null;
        }
        return partyEntityEntity;
    }

    public static RecordSet getPartyInfoByOpenIdAndEmployeeId(String openid, String employeeid) throws Exception {

        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        RecordSet recordSet = dao.queryPartyInfoByOpenIdAndEmployeeId(openid, employeeid);
        return recordSet;
    }

    public static String getEmployeeIdByHirunPlusStaffId(String staffId) throws Exception {
        if (StringUtils.isBlank(staffId) || "0".equals(staffId) || "null".equals(staffId)) {
            return null;
        }
        String mobileNo = HirunPlusStaffDataCache.getMobileByStaffId(staffId);
        if (StringUtils.isBlank(mobileNo)) {
            return null;
        }
        EmployeeEntity employeeEntity = EmployeeBean.getEmployeeByMobileNo(mobileNo);
        if (employeeEntity == null) {
            return null;
        }

        return employeeEntity.getEmployeeId();
    }

    public static boolean transScanDataToIns(JSONObject jsonObject) throws Exception {
        boolean isSuccess = false;
        Map<String, String> party_info = new HashMap<String, String>();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String openid = jsonObject.getString("openid");
        //将家网的操作员转换成鸿助手的操作员
        String employeeId = PartyBean.getEmployeeIdByHirunPlusStaffId(jsonObject.getString("staff_id"));

        if (StringUtils.isBlank(employeeId)) {
            return isSuccess;
        }
        //这个地方加上employee的判断是因为可以允许同一个客户属于不同的客户代表
        RecordSet recordSet = PartyBean.getPartyInfoByOpenIdAndEmployeeId(openid, employeeId);
        if (recordSet.size() > 0) {
            Record record = recordSet.get(0);
            String projectId = record.get("PROJECT_ID");
            String partyId = record.get("PARTY_ID");
            String createDate = record.get("CREATE_TIME");
            List<PartyOriginalActionEntity> partyOriginalActionEntityList = dao.queryPartyOriginalAction(partyId, projectId, "SMJRLC");
            PartyOriginalActionEntity partyOriginalActionEntity = partyOriginalActionEntityList.get(0);
            String finishTime = partyOriginalActionEntity.getFinishTime();

            Map<String, String> updateActionInfo = new HashMap<String, String>();
            updateActionInfo.put("PROJECT_ID", projectId);
            updateActionInfo.put("PARTY_ID", partyId);
            updateActionInfo.put("ACTION_CODE", "SMJRLC");
            updateActionInfo.put("STATUS", "1");
            updateActionInfo.put("FINISH_TIME", transUnixTimeToNormal(jsonObject.getString("add_time")));
            updateActionInfo.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            updateActionInfo.put("UPDATE_TIME", TimeTool.now());
            dao.save("ins_project_original_action", new String[]{"PROJECT_ID", "PARTY_ID", "ACTION_CODE"}, updateActionInfo);
            // 如果通过功能蓝图和风格蓝图生成的客户数据，后面再扫码，咨询时间与扫码进入全流程时间是同一个月，扫码数才+1
            String addTime = transUnixTimeToNormal(jsonObject.getString("add_time"));
            createDate = getMonth(createDate);

            if (StringUtils.isBlank(finishTime) && StringUtils.equals(createDate, getMonth(addTime))) {
                CustServiceStatBean.updateCustServiceStat(employeeId, "SCANDATASPEC");
            }

            return true;
        }
        //生成party信息
        party_info.put("OPEN_ID", openid);
        party_info.put("WX_NICK", jsonObject.getString("nickname"));
        party_info.put("HEAD_URL", jsonObject.getString("headimgurl"));
        party_info.put("PARTY_NAME", jsonObject.getString("name"));
        party_info.put("MOBILE_NO", jsonObject.getString("phone"));
        party_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        party_info.put("CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("add_time")));
        party_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        party_info.put("UPDATE_TIME", TimeTool.now());
        party_info.put("PARTY_STATUS", CustomerServiceConst.PARTY_STATUS_0);//正常客户状态


        long party_id = dao.insertAutoIncrement("INS_PARTY", party_info);

        //保存project信息
        Map<String, String> project_info = new HashMap<String, String>();
        project_info.put("PARTY_ID", party_id + "");
        project_info.put("HOUSE_ADDRESS", jsonObject.getString("address") + jsonObject.getString("loupan") + jsonObject.getString("lnumber"));
        project_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_info.put("CREATE_TIME", TimeTool.now());
        project_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_info.put("UPDATE_TIME", TimeTool.now());

        long project_id = dao.insertAutoIncrement("ins_project", project_info);
        //保存项目意向信息
        Map<String, String> project_intention_info = new HashMap<String, String>();
        project_intention_info.put("PROJECT_ID", project_id + "");
        project_intention_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_intention_info.put("CREATE_TIME", TimeTool.now());
        project_intention_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_intention_info.put("UPDATE_TIME", TimeTool.now());

        dao.insertAutoIncrement("ins_project_intention", project_intention_info);
        //生成项目联系人信息
        Map<String, String> project_link_info = new HashMap<String, String>();
        project_link_info.put("PROJECT_ID", project_id + "");
        project_link_info.put("ROLE_TYPE", CustomerServiceConst.CUSTOMERSERVICEROLETYPE);
        project_link_info.put("LINK_EMPLOYEE_ID", employeeId);
        project_link_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_link_info.put("CREATE_TIME", TimeTool.now());
        project_link_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_link_info.put("UPDATE_TIME", TimeTool.now());

        dao.insertAutoIncrement("ins_project_linkman", project_link_info);

        //生成项目_action信息
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("2");
        List<Map<String, String>> partyProjectActionList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < actionEntityList.size(); i++) {
            ActionEntity actionEntity = actionEntityList.get(i);
            String actionCode = actionEntity.getActionCode();
            Map<String, String> partyProjectActionInfo = new HashMap<String, String>();
            partyProjectActionInfo.put("PROJECT_ID", project_id + "");
            partyProjectActionInfo.put("PARTY_ID", party_id + "");
            partyProjectActionInfo.put("ACTION_CODE", actionCode);
            if (StringUtils.equals(actionCode, "SMJRLC")) {
                partyProjectActionInfo.put("STATUS", "1");
                partyProjectActionInfo.put("FINISH_TIME", transUnixTimeToNormal(jsonObject.getString("add_time")));
            } else if (StringUtils.equals(actionCode, "GZGZH")) {
                partyProjectActionInfo.put("STATUS", "1");
            } else {
                partyProjectActionInfo.put("STATUS", "0");
            }
            partyProjectActionInfo.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            partyProjectActionInfo.put("CREATE_TIME", TimeTool.now());
            partyProjectActionInfo.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            partyProjectActionInfo.put("UPDATE_TIME", TimeTool.now());
            partyProjectActionList.add(partyProjectActionInfo);
        }
        if (ArrayTool.isNotEmpty(partyProjectActionList)) {
            dao.insertBatch("ins_project_original_action", partyProjectActionList);
        }

        CustServiceStatBean.updateCustServiceStat(employeeId, "SCANDATA");

        return true;
    }

    public static boolean transScanDataFromTableToIns(JSONObject jsonObject) throws Exception {
        boolean isSuccess = false;
        Map<String, String> party_info = new HashMap<String, String>();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String openid = jsonObject.getString("OPENID");
        //将家网的操作员转换成鸿助手的操作员
        String employeeId = PartyBean.getEmployeeIdByHirunPlusStaffId(jsonObject.getString("STAFF_ID"));

        if (StringUtils.isBlank(employeeId)) {
            return isSuccess;
        }
        //这个地方加上employee的判断是因为可以允许同一个客户属于不同的客户代表
        RecordSet recordSet = PartyBean.getPartyInfoByOpenIdAndEmployeeId(openid, employeeId);
        if (recordSet.size() > 0) {
            Record record = recordSet.get(0);
            Map<String, String> updateActionInfo = new HashMap<String, String>();
            updateActionInfo.put("PROJECT_ID", record.get("PROJECT_ID"));
            updateActionInfo.put("PARTY_ID", record.get("PARTY_ID"));
            updateActionInfo.put("ACTION_CODE", "SMJRLC");
            updateActionInfo.put("STATUS", "1");
            updateActionInfo.put("FINISH_TIME", transUnixTimeToNormal(jsonObject.getString("ADD_TIME")));
            updateActionInfo.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            updateActionInfo.put("UPDATE_TIME", TimeTool.now());
            dao.save("ins_project_original_action", new String[]{"PROJECT_ID", "PARTY_ID", "ACTION_CODE"}, updateActionInfo);

            return true;
        }
        //生成party信息
        party_info.put("OPEN_ID", openid);
        party_info.put("WX_NICK", jsonObject.getString("NICKNAME"));
        party_info.put("HEAD_URL", jsonObject.getString("HEAD_URL"));
        party_info.put("PARTY_NAME", jsonObject.getString("NAME"));
        party_info.put("MOBILE_NO", jsonObject.getString("PHONE"));
        party_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        party_info.put("CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("ADD_TIME")));
        party_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        party_info.put("UPDATE_TIME", TimeTool.now());
        party_info.put("PARTY_STATUS", CustomerServiceConst.PARTY_STATUS_0);//正常客户状态


        long party_id = dao.insertAutoIncrement("INS_PARTY", party_info);

        //保存project信息
        Map<String, String> project_info = new HashMap<String, String>();
        project_info.put("PARTY_ID", party_id + "");
        project_info.put("HOUSE_ADDRESS", jsonObject.getString("ADDRESS") + jsonObject.getString("LOUPAN") + jsonObject.getString("LNUMBER"));
        project_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_info.put("CREATE_TIME", TimeTool.now());
        project_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_info.put("UPDATE_TIME", TimeTool.now());

        long project_id = dao.insertAutoIncrement("ins_project", project_info);
        //保存项目意向信息
        Map<String, String> project_intention_info = new HashMap<String, String>();
        project_intention_info.put("PROJECT_ID", project_id + "");
        project_intention_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_intention_info.put("CREATE_TIME", TimeTool.now());
        project_intention_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_intention_info.put("UPDATE_TIME", TimeTool.now());

        dao.insertAutoIncrement("ins_project_intention", project_intention_info);
        //生成项目联系人信息
        Map<String, String> project_link_info = new HashMap<String, String>();
        project_link_info.put("PROJECT_ID", project_id + "");
        project_link_info.put("ROLE_TYPE", CustomerServiceConst.CUSTOMERSERVICEROLETYPE);
        project_link_info.put("LINK_EMPLOYEE_ID", employeeId);
        project_link_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_link_info.put("CREATE_TIME", TimeTool.now());
        project_link_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_link_info.put("UPDATE_TIME", TimeTool.now());

        dao.insertAutoIncrement("ins_project_linkman", project_link_info);

        //生成项目_action信息
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("2");
        List<Map<String, String>> partyProjectActionList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < actionEntityList.size(); i++) {
            ActionEntity actionEntity = actionEntityList.get(i);
            String actionCode = actionEntity.getActionCode();
            Map<String, String> partyProjectActionInfo = new HashMap<String, String>();
            partyProjectActionInfo.put("PROJECT_ID", project_id + "");
            partyProjectActionInfo.put("PARTY_ID", party_id + "");
            partyProjectActionInfo.put("ACTION_CODE", actionCode);
            if (StringUtils.equals(actionCode, "SMJRLC")) {
                partyProjectActionInfo.put("STATUS", "1");
                partyProjectActionInfo.put("FINISH_TIME", transUnixTimeToNormal(jsonObject.getString("ADD_TIME")));
            } else if (StringUtils.equals(actionCode, "GZGZH")) {
                partyProjectActionInfo.put("STATUS", "1");
            } else {
                partyProjectActionInfo.put("STATUS", "0");
            }
            partyProjectActionInfo.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            partyProjectActionInfo.put("CREATE_TIME", TimeTool.now());
            partyProjectActionInfo.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            partyProjectActionInfo.put("UPDATE_TIME", TimeTool.now());
            partyProjectActionList.add(partyProjectActionInfo);
        }
        if (ArrayTool.isNotEmpty(partyProjectActionList)) {
            dao.insertBatch("ins_project_original_action", partyProjectActionList);
        }

        CustServiceStatBean.updateCustServiceStat(employeeId, "SCANDATA");

        return true;
    }

    public static boolean transSignInDataToIns(JSONObject jsonObject) throws Exception {
        boolean isSuccess = false;
        Map<String, String> signin_info = new HashMap<String, String>();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String openid = jsonObject.getString("openid");
        //将家网的操作员转换成鸿助手的操作员
        String employeeId = PartyBean.getEmployeeIdByHirunPlusStaffId(jsonObject.getString("staff_id"));

        if (StringUtils.isBlank(employeeId)) {
            employeeId = "0";
        }

        signin_info.put("UID", jsonObject.getString("uid"));
        signin_info.put("OUT_PROJECT_ID", jsonObject.getString("project_id"));
        signin_info.put("SIGNIN_TIME", transUnixTimeToNormal(jsonObject.getString("signin_time")));
        signin_info.put("OPEN_ID", jsonObject.getString("openid"));
        signin_info.put("EMPLOYEE_ID", employeeId);
        signin_info.put("CREATE_TIME", TimeTool.now());
        dao.insertAutoIncrement("ins_signin_action", signin_info);

        return true;
    }

    public static boolean transXQLTEDataToIns(JSONObject jsonObject) throws Exception {
        boolean isSuccess = false;
        Map<String, String> party_info = new HashMap<String, String>();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String openid = jsonObject.getString("openid");
        String roleId = jsonObject.getString("sjs_role_id");
        String funcA = jsonObject.getString("funs_A");//功能蓝图内容
        String funcB = jsonObject.getString("funs_B");//功能蓝图内容
        String funcC = jsonObject.getString("funs_C");//功能蓝图内容

        String style = jsonObject.getString("style");//风格蓝图内容

        if (!StringUtils.equals(roleId, "11")) {
            return isSuccess;
        }

        //将家网的操作员转换成鸿助手的操作员
        String employeeId = PartyBean.getEmployeeIdByHirunPlusStaffId(jsonObject.getString("sjs_staff_id"));

        if (StringUtils.isBlank(employeeId)) {
            return isSuccess;
        }
        //这个地方加上employee的判断是因为可以允许同一个客户属于不同的客户代表
        RecordSet recordSet = PartyBean.getPartyInfoByOpenIdAndEmployeeId(openid, employeeId);
        if (recordSet.size() > 0) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("OPEN_ID", openid);
            param.put("ACTION_CODE", "XQLTE");
            param.put("MODE_ID", jsonObject.getString("mode_id"));
            param.put("MODE_TIME", jsonObject.getString("mode_time"));
            param.put("NAME", jsonObject.getString("name"));
            param.put("STAFF_ID", jsonObject.getString("sjs_staff_id"));
            param.put("FUNC", jsonObject.getString("funs"));
            param.put("REL_EMPLOYEE_ID", employeeId);
            param.put("CREATE_TIME", TimeTool.now());
            param.put("XQLTE_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("update_time")));
            param.put("XQLTE_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("update_time")));

            if ((StringUtils.isNotBlank(funcA) && !StringUtils.equals("false", funcA))
                    || (StringUtils.isNotBlank(funcB) && !StringUtils.equals("false", funcB))
                    || (StringUtils.isNotBlank(funcC) && !StringUtils.equals("false", funcC))) {
                param.put("FUNCPRINT_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("gnlt_update_time")));
                param.put("FUNCPRINT_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("gnlt_update_time")));
            }
            //发现有风格蓝图内容为空，但是风格蓝图时间不为空的数据。增加判断如果风格蓝图为空，则不认为客户代表完成该动作
            if (StringUtils.isNotBlank(style) && !StringUtils.equals("false", style)) {
                param.put("STYLEPRINT_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("fglt_update_time")));
                param.put("STYLEPRINT_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("fglt_update_time")));
            }
            param.put("STYLE", jsonObject.getString("style"));


            Record partyRecord = recordSet.get(0);
            String createDate = getMonth(partyRecord.get("CREATE_TIME"));
            String gnltTime = transUnixTimeToNormal(jsonObject.getString("update_time"));


            //判断需求蓝图二的推送时间是否与咨询时间是同一个月，如果不是同一个月则不更新报表数据，否则就更新报表数据
            RecordSet bulePrintSet = dao.queryXQLTEByOpenIdAndActionCode(openid, "XQLTE", employeeId);
            if (bulePrintSet.size() <= 0 && StringUtils.equals(createDate, getMonth(gnltTime))) {
                if ((StringUtils.isNotBlank(funcA) && !StringUtils.equals("false", funcA))
                        || (StringUtils.isNotBlank(funcB) && !StringUtils.equals("false", funcB))
                        || (StringUtils.isNotBlank(funcC) && !StringUtils.equals("false", funcC))) {
                    CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTEFUNC");
                }
                if (StringUtils.isNotBlank(style) && !StringUtils.equals("false", style)) {
                    CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTESTYLE");
                }
                //如果风格蓝图和功能蓝图都不为空，则更新需求蓝图二
                if (((StringUtils.isNotBlank(funcA) && !StringUtils.equals("false", funcA))
                        || (StringUtils.isNotBlank(funcB) && !StringUtils.equals("false", funcB))
                        || (StringUtils.isNotBlank(funcC) && !StringUtils.equals("false", funcC)))
                        && (StringUtils.isNotBlank(style) && !StringUtils.equals("false", style))) {
                    CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTE");
                }
            }
            if (bulePrintSet.size() > 0 && StringUtils.equals(createDate, getMonth(gnltTime))) {
                Record bulePrintRecord = bulePrintSet.get(0);
                String insFunc = bulePrintRecord.get("FUNC");
                String insFuncB = bulePrintRecord.get("FUNC_B");
                String insFuncC = bulePrintRecord.get("FUNC_C");

                String insStyle = bulePrintRecord.get("STYLE");
                if (((StringUtils.isBlank(insFunc) || StringUtils.equals("false", insFunc))
                        && (StringUtils.isBlank(insFunc) || StringUtils.equals("false", insFunc))
                        && (StringUtils.isBlank(insFuncB) || StringUtils.equals("false", insFuncB)))
                        && (StringUtils.isNotBlank(insFuncC) && !StringUtils.equals("false", insFuncC))) {
                    CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTEFUNC");
                    if (StringUtils.isNotBlank(insStyle) && !StringUtils.equals("false", insStyle)) {
                        CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTE");
                    }
                }
                if ((StringUtils.isBlank(insStyle) || StringUtils.equals("false", insStyle)) && (StringUtils.isNotBlank(style) && !StringUtils.equals("false", style))) {
                    CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTESTYLE");
                    if (StringUtils.isNotBlank(insFunc) && !StringUtils.equals("false", insFunc)) {
                        CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTE");
                    }
                }
            }
            dao.insertAutoIncrement("ins_blueprint_action", param);//将需求蓝图二的内容转换成ins数据
            return true;
        }
        //生成party信息
        party_info.put("OPEN_ID", openid);
        party_info.put("WX_NICK", jsonObject.getString("nickname"));
        party_info.put("HEAD_URL", jsonObject.getString("headimgurl"));
        party_info.put("PARTY_NAME", jsonObject.getString("name"));
        party_info.put("MOBILE_NO", jsonObject.getString("phone"));
        party_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        party_info.put("CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("update_time")));
        party_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        party_info.put("UPDATE_TIME", TimeTool.now());
        party_info.put("PARTY_STATUS", CustomerServiceConst.PARTY_STATUS_0);//代表虚拟party信息


        long party_id = dao.insertAutoIncrement("INS_PARTY", party_info);

        //保存project信息
        Map<String, String> project_info = new HashMap<String, String>();
        project_info.put("PARTY_ID", party_id + "");
        project_info.put("HOUSE_ADDRESS", jsonObject.getString("address") + jsonObject.getString("loupan") + jsonObject.getString("lnumber"));
        project_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_info.put("CREATE_TIME", TimeTool.now());
        project_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_info.put("UPDATE_TIME", TimeTool.now());

        long project_id = dao.insertAutoIncrement("ins_project", project_info);
        //保存项目意向信息
        Map<String, String> project_intention_info = new HashMap<String, String>();
        project_intention_info.put("PROJECT_ID", project_id + "");
        project_intention_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_intention_info.put("CREATE_TIME", TimeTool.now());
        project_intention_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_intention_info.put("UPDATE_TIME", TimeTool.now());

        dao.insertAutoIncrement("ins_project_intention", project_intention_info);
        //生成项目联系人信息
        Map<String, String> project_link_info = new HashMap<String, String>();
        project_link_info.put("PROJECT_ID", project_id + "");
        project_link_info.put("ROLE_TYPE", CustomerServiceConst.CUSTOMERSERVICEROLETYPE);
        project_link_info.put("LINK_EMPLOYEE_ID", employeeId);
        project_link_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_link_info.put("CREATE_TIME", TimeTool.now());
        project_link_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_link_info.put("UPDATE_TIME", TimeTool.now());

        dao.insertAutoIncrement("ins_project_linkman", project_link_info);

        //生成项目_action信息
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("2");
        List<Map<String, String>> partyProjectActionList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < actionEntityList.size(); i++) {
            ActionEntity actionEntity = actionEntityList.get(i);
            String actionCode = actionEntity.getActionCode();
            Map<String, String> partyProjectActionInfo = new HashMap<String, String>();
            partyProjectActionInfo.put("PROJECT_ID", project_id + "");
            partyProjectActionInfo.put("PARTY_ID", party_id + "");
            partyProjectActionInfo.put("ACTION_CODE", actionCode);
            if (StringUtils.equals(actionCode, "XQLTE")) {
                partyProjectActionInfo.put("STATUS", "1");
                partyProjectActionInfo.put("FINISH_TIME", transUnixTimeToNormal(jsonObject.getString("update_time")));
            } else if (StringUtils.equals(actionCode, "GZGZH")) {
                partyProjectActionInfo.put("STATUS", "1");
            } else {
                partyProjectActionInfo.put("STATUS", "0");
            }


            partyProjectActionInfo.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            partyProjectActionInfo.put("CREATE_TIME", TimeTool.now());
            partyProjectActionInfo.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            partyProjectActionInfo.put("UPDATE_TIME", TimeTool.now());
            partyProjectActionInfo.put("CREATE_TIME", TimeTool.now());

            partyProjectActionList.add(partyProjectActionInfo);
        }
        if (ArrayTool.isNotEmpty(partyProjectActionList)) {
            dao.insertBatch("ins_project_original_action", partyProjectActionList);
        }

        Map<String, String> param = new HashMap<String, String>();
        param.put("OPEN_ID", openid);
        param.put("ACTION_CODE", "XQLTE");
        param.put("MODE_ID", jsonObject.getString("mode_id"));
        param.put("MODE_TIME", jsonObject.getString("mode_time"));
        param.put("NAME", jsonObject.getString("name"));
        param.put("STAFF_ID", jsonObject.getString("sjs_staff_id"));
        param.put("FUNC", jsonObject.getString("funs_A"));
        //2020/01/08
        param.put("FUNC_B", jsonObject.getString("funs_B"));
        param.put("FUNC_C", jsonObject.getString("funs_C"));

        param.put("REL_EMPLOYEE_ID", employeeId);
        param.put("CREATE_TIME", TimeTool.now());
        param.put("XQLTE_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("update_time")));
        param.put("XQLTE_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("update_time")));
        param.put("FUNCPRINT_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("gnlt_update_time")));
        param.put("FUNCPRINT_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("gnlt_update_time")));
        param.put("STYLEPRINT_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("fglt_update_time")));
        param.put("STYLEPRINT_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("fglt_update_time")));
        param.put("STYLE", jsonObject.getString("style"));


        dao.insertAutoIncrement("ins_blueprint_action", param);//将需求蓝图二的内容转换成ins数据
        //更新报表
        CustServiceStatBean.updateCustServiceStat(employeeId, "GOODSEEGOODLIVE");
        if ((StringUtils.isNotBlank(funcA) && !StringUtils.equals("false", funcA))
                || (StringUtils.isNotBlank(funcB) && !StringUtils.equals("false", funcB))
                || (StringUtils.isNotBlank(funcC) && !StringUtils.equals("false", funcC))) {
            CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTEFUNC");
        }
        if (StringUtils.isNotBlank(style) && !StringUtils.equals("false", style)) {
            CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTESTYLE");
        }
        //如果风格蓝图和功能蓝图都不为空，则更新需求蓝图二
        if (((StringUtils.isNotBlank(funcA) && !StringUtils.equals("false", funcA))
                || (StringUtils.isNotBlank(funcB) && !StringUtils.equals("false", funcB))
                || (StringUtils.isNotBlank(funcC) && !StringUtils.equals("false", funcC)))
                && (StringUtils.isNotBlank(style) && !StringUtils.equals("false", style))) {
            CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTE");
        }

        return true;
    }

    public static boolean transXQLTEDataFromTableToIns(JSONObject jsonObject) throws Exception {
        boolean isSuccess = false;
        Map<String, String> party_info = new HashMap<String, String>();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String openid = jsonObject.getString("OPEN_ID");
        String roleId = jsonObject.getString("SJS_ROLE_ID");


        if (!StringUtils.equals(roleId, "11")) {
            return isSuccess;
        }

        //将家网的操作员转换成鸿助手的操作员
        String employeeId = PartyBean.getEmployeeIdByHirunPlusStaffId(jsonObject.getString("SJS_STAFF_ID"));

        if (StringUtils.isBlank(employeeId)) {
            return isSuccess;
        }
        //这个地方加上employee的判断是因为可以允许同一个客户属于不同的客户代表
        RecordSet recordSet = PartyBean.getPartyInfoByOpenIdAndEmployeeId(openid, employeeId);
        if (recordSet.size() > 0) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("OPEN_ID", openid);
            param.put("ACTION_CODE", "XQLTE");
            param.put("MODE_ID", jsonObject.getString("MODE_ID"));
            param.put("MODE_TIME", jsonObject.getString("MODE_TIME"));
            param.put("NAME", jsonObject.getString("NAME"));
            param.put("STAFF_ID", jsonObject.getString("SJS_STAFF_ID"));
            param.put("FUNC", jsonObject.getString("FUNC"));
            param.put("REL_EMPLOYEE_ID", employeeId);
            param.put("CREATE_TIME", TimeTool.now());
            param.put("XQLTE_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));
            param.put("XQLTE_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));
            param.put("FUNCPRINT_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));
            param.put("FUNCPRINT_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));

            Record partyRecord = recordSet.get(0);
            String createDate = getMonth(partyRecord.get("CREATE_TIME"));
            String gnltTime = transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME"));


            //判断需求蓝图二的推送时间是否与咨询时间是同一个月，如果不是同一个月则不更新报表数据，否则就更新报表数据
            RecordSet bulePrintSet = dao.queryXQLTEByOpenIdAndActionCode(openid, "XQLTE", employeeId);
            if (bulePrintSet.size() <= 0 && StringUtils.equals(createDate, getMonth(gnltTime))) {
                CustServiceStatBean.updateCustServiceStat(employeeId, "XQLTEFUNC");
            }

            dao.insertAutoIncrement("ins_blueprint_action", param);//将需求蓝图二的内容转换成ins数据
            return true;
        }
        //生成party信息
        party_info.put("OPEN_ID", openid);
        party_info.put("WX_NICK", jsonObject.getString("NICKNAME"));
        party_info.put("HEAD_URL", jsonObject.getString("HEADIMGURL"));
        party_info.put("PARTY_NAME", jsonObject.getString("NAME"));
        party_info.put("MOBILE_NO", jsonObject.getString("PHONE"));
        party_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        party_info.put("CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));
        party_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        party_info.put("UPDATE_TIME", TimeTool.now());
        party_info.put("PARTY_STATUS", CustomerServiceConst.PARTY_STATUS_0);//代表虚拟party信息


        long party_id = dao.insertAutoIncrement("INS_PARTY", party_info);

        //保存project信息
        Map<String, String> project_info = new HashMap<String, String>();
        project_info.put("PARTY_ID", party_id + "");
        project_info.put("HOUSE_ADDRESS", jsonObject.getString("ADDRESS") + jsonObject.getString("LOUPAN") + jsonObject.getString("LNUMBER"));
        project_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_info.put("CREATE_TIME", TimeTool.now());
        project_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_info.put("UPDATE_TIME", TimeTool.now());

        long project_id = dao.insertAutoIncrement("ins_project", project_info);
        //保存项目意向信息
        Map<String, String> project_intention_info = new HashMap<String, String>();
        project_intention_info.put("PROJECT_ID", project_id + "");
        project_intention_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_intention_info.put("CREATE_TIME", TimeTool.now());
        project_intention_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_intention_info.put("UPDATE_TIME", TimeTool.now());

        dao.insertAutoIncrement("ins_project_intention", project_intention_info);
        //生成项目联系人信息
        Map<String, String> project_link_info = new HashMap<String, String>();
        project_link_info.put("PROJECT_ID", project_id + "");
        project_link_info.put("ROLE_TYPE", CustomerServiceConst.CUSTOMERSERVICEROLETYPE);
        project_link_info.put("LINK_EMPLOYEE_ID", employeeId);
        project_link_info.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_link_info.put("CREATE_TIME", TimeTool.now());
        project_link_info.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
        project_link_info.put("UPDATE_TIME", TimeTool.now());

        dao.insertAutoIncrement("ins_project_linkman", project_link_info);

        //生成项目_action信息
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("2");
        List<Map<String, String>> partyProjectActionList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < actionEntityList.size(); i++) {
            ActionEntity actionEntity = actionEntityList.get(i);
            String actionCode = actionEntity.getActionCode();
            Map<String, String> partyProjectActionInfo = new HashMap<String, String>();
            partyProjectActionInfo.put("PROJECT_ID", project_id + "");
            partyProjectActionInfo.put("PARTY_ID", party_id + "");
            partyProjectActionInfo.put("ACTION_CODE", actionCode);
            if (StringUtils.equals(actionCode, "XQLTE")) {
                partyProjectActionInfo.put("STATUS", "1");
                partyProjectActionInfo.put("FINISH_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));
            } else if (StringUtils.equals(actionCode, "GZGZH")) {
                partyProjectActionInfo.put("STATUS", "1");
            } else {
                partyProjectActionInfo.put("STATUS", "0");
            }
            partyProjectActionInfo.put("CREATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            partyProjectActionInfo.put("CREATE_TIME", TimeTool.now());
            partyProjectActionInfo.put("UPDATE_USER_ID", EmployeeBean.getEmployeeByEmployeeId(employeeId).getUserId());
            partyProjectActionInfo.put("UPDATE_TIME", TimeTool.now());
            partyProjectActionList.add(partyProjectActionInfo);
        }
        if (ArrayTool.isNotEmpty(partyProjectActionList)) {
            dao.insertBatch("ins_project_original_action", partyProjectActionList);
        }

        Map<String, String> param = new HashMap<String, String>();
        param.put("OPEN_ID", openid);
        param.put("ACTION_CODE", "XQLTE");
        param.put("MODE_ID", jsonObject.getString("MODE_ID"));
        param.put("MODE_TIME", jsonObject.getString("MODE_TIME"));
        param.put("NAME", jsonObject.getString("NAME"));
        param.put("STAFF_ID", jsonObject.getString("SJS_STAFF_ID"));
        param.put("FUNC", jsonObject.getString("FUNC"));
        param.put("REL_EMPLOYEE_ID", employeeId);
        param.put("CREATE_TIME", TimeTool.now());
        param.put("XQLTE_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));
        param.put("XQLTE_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));
        param.put("FUNCPRINT_CREATE_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));
        param.put("FUNCPRINT_UPDATE_TIME", transUnixTimeToNormal(jsonObject.getString("GNLT_UPDATE_TIME")));
        dao.insertAutoIncrement("ins_blueprint_action", param);//将需求蓝图二的内容转换成ins数据

        /*
        String funs= jsonObject.getString("funs");
        if(StringUtils.equals(funs,"false") || StringUtils.isBlank(funs) || "null".equals(funs)){
            CustServiceStatBean.updateCustServiceStat(employeeId,"XQLTEFUNC");
        }else{
            CustServiceStatBean.updateCustServiceStat(employeeId,"XQLTEFUNC");
        }*/
        //CustServiceStatBean.updateCustServiceStat(employeeId,"XQLTEFUNC");
        return true;
    }


    //将unix时间转换成正常时间
    private static String transUnixTimeToNormal(String timestampString) {
        if (StringUtils.isBlank(timestampString) || StringUtils.equals(timestampString, "0")) {
            return "";
        }
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        return date;
    }


    public static String getMonth(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        if (StringUtils.isBlank(stringDate)) {
            return "";
        }
        try {
            Date date = format.parse(stringDate);
            String newDate = format.format(date);
            return newDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}

