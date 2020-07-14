package com.hirun.app.biz.custservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.common.MsgBean;
import com.hirun.app.bean.custservice.BluePrintBean;
import com.hirun.app.bean.custservice.CustServiceStatBean;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.houses.HousesBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.out.SyncBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.EmployeeCache;
import com.hirun.app.dao.cust.CustDAO;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.pub.consts.CustomerServiceConst;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.custservice.PartyEntity;
import com.hirun.pub.domain.entity.custservice.ProjectEntity;
import com.hirun.pub.domain.entity.custservice.ProjectIntentionEntity;
import com.hirun.pub.domain.entity.houses.HousesEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.hirun.pub.domain.enums.common.MsgType;
import com.hirun.pub.tool.CustomerNoTool;
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

import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class CustServService extends GenericService {
    /**
     * 初始化客户的流程信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse initCsrTraceFlow(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = new CustomerServiceDAO("ins");
        String custServlinkEmpId = "";

        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        EmployeeJobRoleEntity employeeJobRoleEntity = EmployeeBean.queryEmployeeJobRoleByEmpId(employeeId);
        String jobRole = employeeJobRoleEntity.getJobRole();

        if (StringUtils.equals("103", jobRole) || StringUtils.equals("45", jobRole)) {
            Record flagRecord = new Record();
            flagRecord.put("FLAG", "TRUE");
            response.set("FLAG", ConvertTool.toJSONObject(flagRecord));

        } else {
            Record flagRecord = new Record();
            flagRecord.put("FLAG", "FALSE");
            response.set("FLAG", ConvertTool.toJSONObject(flagRecord));
        }

        String org_id = OrgBean.getOrgId(session.getSessionEntity());
        OrgEntity orgEntity = OrgBean.getAssignTypeOrg(org_id, "1");

        String partyId = request.getString("PARTY_ID");
        String projectId = request.getString("PROJECT_ID");
        /*这里查询一下对应的linkempid的原因是如果是主管或者组长查看流程，不能根据组长以及主管的id获取数据*/
        RecordSet linkmanSet = dao.queryLinkmanByProjectIdAndRoleType(projectId, CustomerServiceConst.CUSTOMERSERVICEROLETYPE, "valid");

        if (linkmanSet.size() > 0 || linkmanSet != null) {
            custServlinkEmpId = linkmanSet.get(0).get("LINK_EMPLOYEE_ID");
        }

        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("2");
        PartyEntity partyEntity = dao.queryPartyInfoByPartyId(partyId);

        RecordSet partyActionList = dao.queryPartyFlowByProjectId(projectId);
        RecordSet projectDesignerInfo = dao.queryLinkmanByProjectIdAndRoleType(projectId, CustomerServiceConst.PROJECTDESIGNERROLETYPE, "unvaild");
        RecordSet projectBluePrintInfo = dao.queryBluePrintByOpenIdAndActionCode(partyEntity.getOpenId(), "XQLTY");//查询需求蓝图一
        RecordSet cityCabinList = dao.getCityCabinByCityId(orgEntity.getCity(), "");//查询操作员对应地州的城市木屋
        RecordSet insScanCityInfo = dao.queryInsScanCityInfoByProIdAndPId(projectId, partyId);
        if (insScanCityInfo.size() > 0) {
            for (int i = 0; i < insScanCityInfo.size(); i++) {
                Record insCabins = insScanCityInfo.get(i);
                String cityCabins = insCabins.get("CITY_CABINS");
                if (StringUtils.isNotBlank(cityCabins)) {
                    String[] cityCabinId = cityCabins.split(",");
                    String cityCabinName = "";
                    for (int j = 0; j < cityCabinId.length; j++) {
                        Record cityCabinRecord = dao.getCityCabinById(cityCabinId[j]);
                        cityCabinName += cityCabinRecord.get("CITYCABIN_ADDRESS") + "|" + cityCabinRecord.get("CITYCABIN_BUILDING") + "栋|" + cityCabinRecord.get("CITYCABIN_ROOM") + "、";
                    }
                    insCabins.put("CITYCABINNAMES", cityCabinName.substring(0, cityCabinName.length() - 1));
                }
            }
        }

        if (partyActionList.size() <= 0 || partyActionList == null) {
            return response;
        }
        RecordSet sortRecordSet = new RecordSet();
        for (ActionEntity actionEntity : actionEntityList) {
            String actionCode = actionEntity.getActionCode();
            for (int i = 0; i < partyActionList.size(); i++) {
                Record partyAction = partyActionList.get(i);
                String partyActionCode = partyAction.get("ACTION_CODE");
                if (StringUtils.equals(actionCode, partyActionCode)) {
                    //查询需求蓝图一的内容
                    if (StringUtils.equals(actionCode, "XQLTY")) {
                        String openid = partyAction.get("OPEN_ID");
                        if (StringUtils.isNotBlank(openid)) {
                            RecordSet xqltyRecordSet = dao.queryBluePrintByOpenIdAndActionCode(openid, "XQLTY");
                            if (xqltyRecordSet.size() > 0) {
                                partyAction.put("STATUS", "1");
                                partyAction.put("XQLTY_FINISHTIME", xqltyRecordSet.get(0).get("MODE_TIME"));
                            }
                        }
                    }
                    if (StringUtils.equals(actionCode, "SMDLUPCD")) {
                        String openid = partyAction.get("OPEN_ID");
                        if (StringUtils.isNotBlank(openid)) {
                            RecordSet signInRecordSet = dao.querySignInInfoByOpenIdAndEmpId(openid);
                            if (signInRecordSet.size() > 0) {
                                partyAction.put("STATUS", "1");
                                partyAction.put("SMDLUPCD_FINISHTIME", signInRecordSet.get(0).get("SIGNIN_TIME"));
                            }
                        }
                    }

                    //查询需求蓝图二的内容
                    if (StringUtils.equals(actionCode, "XQLTE")) {
                        String openid = partyAction.get("OPEN_ID");
                        if (StringUtils.isNotBlank(openid)) {
                            RecordSet xqlteRecordSet = dao.queryXQLTEByOpenIdAndActionCode(openid, "XQLTE", custServlinkEmpId);
                            if (xqlteRecordSet.size() > 0) {
                                partyAction.put("STATUS", "1");
                                partyAction.put("XQLTE_FINISHTIME", xqlteRecordSet.get(0).get("XQLTE_UPDATE_TIME"));

                                Record xqlteRecord = xqlteRecordSet.get(0);
                                //功能蓝图内容
                                if (StringUtils.isNotEmpty(xqlteRecord.get("FUNC")) && !StringUtils.equals(xqlteRecord.get("FUNC"), "false")) {
                                    JSONObject jsonObject = BluePrintBean.getFuncTree(xqlteRecord.get("FUNC"), "A");
                                    if (jsonObject != null) {
                                        response.set("FUNC_TREE", jsonObject);
                                    }
                                }

                                if (StringUtils.isNotEmpty(xqlteRecord.get("FUNC_B")) && !StringUtils.equals(xqlteRecord.get("FUNC_B"), "false")) {
                                    JSONObject jsonObject = BluePrintBean.getFuncTree(xqlteRecord.get("FUNC_B"), "B");
                                    if (jsonObject != null) {
                                        response.set("FUNC_TREE_B", jsonObject);
                                    }
                                }

                                if (StringUtils.isNotEmpty(xqlteRecord.get("FUNC_C")) && !StringUtils.equals(xqlteRecord.get("FUNC_C"), "false")) {
                                    JSONObject jsonObject = BluePrintBean.getFuncTree(xqlteRecord.get("FUNC_C"), "C");
                                    if (jsonObject != null) {
                                        response.set("FUNC_TREE_C", jsonObject);
                                    }
                                }
                                //风格蓝图内容
                                JSONArray styleJsonObjectArray = BluePrintBean.getStyleContent(xqlteRecord);
                                if (styleJsonObjectArray != null && styleJsonObjectArray.size() > 0) {
                                    response.set("STYLECONTENT", styleJsonObjectArray);
                                }
                            }
                        }
                    }

                    String status = partyAction.get("STATUS");
                    partyAction.put("ACTION_NAME", ActionCache.getAction(partyAction.get("ACTION_CODE")).getActionName());
                    if (StringUtils.equals(status, "0")) {
                        partyAction.put("STATUS_NAME", "未完成");
                    } else {
                        partyAction.put("STATUS_NAME", "已完成");
                    }
                    sortRecordSet.add(partyAction);
                } else {
                    continue;
                }
            }
        }
        response.set("PARTYACTIONFLOW", ConvertTool.toJSONArray(sortRecordSet));
        response.set("PROJECTDESIGNERINFO", ConvertTool.toJSONArray(projectDesignerInfo));
        response.set("PROJECTXQLTYINFO", ConvertTool.toJSONArray(projectBluePrintInfo));
        response.set("CITYCABININFO", ConvertTool.toJSONArray(cityCabinList));
        response.set("INSSCANCITYINFO", ConvertTool.toJSONArray(insScanCityInfo));
        response.set("PARTYINFO", partyEntity.toJson());
        return response;
    }

    public ServiceResponse queryCityCabinByName(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String cityCabinName = request.getString("CityCabinName");
        String org_id = OrgBean.getOrgId(session.getSessionEntity());
        OrgEntity orgEntity = OrgBean.getAssignTypeOrg(org_id, "1");
        RecordSet cityCabinList = dao.getCityCabinByCityId(orgEntity.getCity(), cityCabinName);//查询操作员对应地州的城市木屋
        if (cityCabinList.size() <= 0) {
            return response;
        }
        response.set("CITYCABININFO", ConvertTool.toJSONArray(cityCabinList));

        return response;
    }

    /**
     * 查询用户关注公众号信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse queryGZGZHDetailByPartyId(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CustomerServiceDAO dao = new CustomerServiceDAO("ins");
        String party_id = request.getString("PARTY_ID");
        JSONObject partyInfo = new JSONObject();
        PartyEntity partyEntity = dao.queryPartyInfoByPartyId(party_id);
        if (partyEntity == null) {
            return response;
        }
        partyInfo.put("WX_NICK", partyEntity.getWxNick());
        response.set("PARTY_INFO", partyInfo);
        return response;
    }

    /**
     * 查询客户代表同门店的设计师，名字取成byEmployeeId实际是根据Employee归属部门查询
     *
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse queryDesignerByEmployeeId(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String name = request.getString("SEARCH_TEXT");
        String crossSwitch = request.getString("mySwitch");
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        CustomerServiceDAO dao = new CustomerServiceDAO("ins");
        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();
        String orgId = OrgBean.getOrgId(sessionEntity);

        if (StringUtils.equals("off", crossSwitch)) {
            OrgEntity orgEntity = OrgBean.getAssignTypeOrg(orgId, "2", allOrgs);

            if (orgEntity == null) {
                orgEntity = OrgBean.getAssignTypeOrg(orgId, "3", allOrgs);
            }
            orgId = OrgBean.getOrgLine(orgEntity.getOrgId(), allOrgs);
            RecordSet recordSet = dao.queryDesignerByOrgId(orgId, name);

            if (recordSet.size() <= 0 || recordSet == null) {
                return response;
            }
            for (int i = 0; i < recordSet.size(); i++) {
                Record record = recordSet.get(i);
                record.put("DESIGNER_LEVEL_NAME", StaticDataTool.getCodeName("JOB_ROLE", record.get("JOB_ROLE")));
            }
            response.set("DESIGNERINFO", ConvertTool.toJSONArray(recordSet));
        } else if (StringUtils.equals("on", crossSwitch)) {
            RecordSet recordSet = dao.queryDesignerByOrgId("", name);
            if (recordSet.size() <= 0 || recordSet == null) {
                return response;
            }
            for (int i = 0; i < recordSet.size(); i++) {
                Record record = recordSet.get(i);
                record.put("DESIGNER_LEVEL_NAME", StaticDataTool.getCodeName("JOB_ROLE", record.get("JOB_ROLE")));
            }
            response.set("DESIGNERINFO", ConvertTool.toJSONArray(recordSet));
        } else {
            return response;
        }
        return response;
    }

    /**
     * 根据传入的party_id与设计师EMPLOYEE_ID更新
     *
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse chooseDesigner(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        Map<String, String> parameter = new HashMap<String, String>();
        AppSession session = SessionManager.getSession();

        String projectId = request.getString("PROJECT_ID");
        String partyId = request.getString("PARTY_ID");

        String designer_employee_id = request.getString("SELECTED_EMPLOYEE_ID");
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        RecordSet linkMan = dao.queryProjectLinkManByProjectId(projectId, CustomerServiceConst.PROJECTDESIGNERROLETYPE);

        if (linkMan.size() <= 0 || linkMan == null) {
            parameter.put("PROJECT_ID", projectId);
            parameter.put("ROLE_TYPE", CustomerServiceConst.PROJECTDESIGNERROLETYPE);
            parameter.put("LINK_EMPLOYEE_ID", designer_employee_id);
            parameter.put("CREATE_TIME", session.getCreateTime());
            parameter.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
            parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            parameter.put("UPDATE_TIME", session.getCreateTime());
            dao.insertAutoIncrement("ins_project_linkman", parameter);
        } else {
            parameter.put("PROJECT_ID", projectId);
            parameter.put("ROLE_TYPE", CustomerServiceConst.PROJECTDESIGNERROLETYPE);
            parameter.put("LINK_EMPLOYEE_ID", designer_employee_id);
            parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            parameter.put("UPDATE_TIME", session.getCreateTime());
            dao.save("ins_project_linkman", new String[]{"PROJECT_ID", "ROLE_TYPE"}, parameter);
        }
        parameter.clear();
        parameter.put("PARTY_ID", partyId);
        parameter.put("PROJECT_ID", projectId);
        parameter.put("ACTION_CODE", "APSJS");
        parameter.put("STATUS", "1");
        parameter.put("FINISH_TIME", session.getCreateTime());
        parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        parameter.put("UPDATE_TIME", session.getCreateTime());
        dao.save("ins_project_original_action", new String[]{"PROJECT_ID", "ACTION_CODE"}, parameter);

        //同步选择的员工信息给家网
        PartyEntity partyEntity = dao.queryPartyInfoByPartyId(partyId);
        if (partyEntity != null) {
            if (StringUtils.isNotBlank(partyEntity.getOpenId())) {
                Map<String, String> logMap = new HashMap<>();
                logMap.put("OPEN_ID", partyEntity.getOpenId());
                logMap.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
                logMap.put("CREATE_TIME", TimeTool.now());
                SyncBean.syncRoleInfo(partyEntity.getOpenId(), designer_employee_id, logMap);
                dao.insert("SYNC_PARTY_LOG", logMap);
            }
        }

        return response;
    }

    /**
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse initCreateGoodSeeLiveInfo(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CustomerServiceDAO dao = new CustomerServiceDAO("ins");


        RecordSet hobbyList = StaticDataTool.getCodeTypeDatas("HOBBY");//兴趣爱好
        RecordSet chineseStyleList = HouseParamTool.getHouseTopicByType("CHINESESTYLE");
        RecordSet europeanClassicsList = HouseParamTool.getHouseTopicByType("EUROPEANCLASSICS");
        RecordSet modernSourceList = HouseParamTool.getHouseTopicByType("MODERNSOURCE");
        RecordSet funcSystemList = HouseParamTool.getFuncSystemByFuncType("1");//15大功能
        RecordSet advantagelist = StaticDataTool.getCodeTypeDatas("ADVANTAGEINTRODUCTION");//优势
        RecordSet criticalprocesslist = StaticDataTool.getCodeTypeDatas("CRITICALPROCESS");//关键流程
        RecordSet informationSourcelist = StaticDataTool.getCodeTypeDatas("INFORMATIONSOURCE");//信息来源
        if (hobbyList.size() <= 0 || hobbyList == null) {
            return response;
        }
        response.set("HOBBYLIST", ConvertTool.toJSONArray(hobbyList));
        response.set("CHINESESTYLELIST", ConvertTool.toJSONArray(chineseStyleList));
        response.set("EUROPEANCLASSICSLIST", ConvertTool.toJSONArray(europeanClassicsList));
        response.set("MODERNSOURCELIST", ConvertTool.toJSONArray(modernSourceList));
        response.set("FUNCSYSTEMLIST", ConvertTool.toJSONArray(funcSystemList));
        response.set("ADVANTAGELIST", ConvertTool.toJSONArray(advantagelist));
        response.set("CRITICALPROCESSLIST", ConvertTool.toJSONArray(criticalprocesslist));
        response.set("INFORMATIONSOURCELIST", ConvertTool.toJSONArray(informationSourcelist));

        /*20200517新增*/
        String customerNoSeq = CustomerNoTool.getCustomerNoSeq();
        response.set("CUSTNO", customerNoSeq);

        return response;
    }


    /**
     * @param request
     * @return
     * @throws Exception
     */

    public ServiceResponse createGoodSeeLiveInfo(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        AppSession session = SessionManager.getSession();
        String customerId = request.getString("cust_id");
        String project_id = request.getString("project_id");
        String customerType = request.getString("customer_type");
        String onlyWood = request.getString("onlyWood");
        String houseId = request.getString("house_id");
        String houseBuilding = request.getString("house_building");
        String houseRoomNo = request.getString("house_room_no");
        String consultTime = request.getString("consult_time");
        String house_mode = request.getString("HOUSEKIND");//户型
        String house_area = request.getString("AREA");//面积
        String prepareId = request.getString("prepare_id");

        Map<String, Map<String, String>> result = this.buildCommonInfo(request);
        Map<String, String> partyInfo = result.get("CUSTOMER_INFO");
        //保存客户信息
        Long partyId = null;
        if (StringUtils.isNotEmpty(customerId)) {
            //如果为报备客户，在客户信息录入界面无论选哪个类型，客户类型都为报备客户
            if (StringUtils.isNotEmpty(prepareId)) {
                partyInfo.put("CUST_TYPE", "4");
            }
            partyInfo.put("CUST_ID", customerId);
            dao.save("ins_party", new String[]{"CUST_ID"}, partyInfo);
        } else {
            partyInfo.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
            partyInfo.put("CREATE_TIME", session.getCreateTime());
            partyId = dao.insertAutoIncrement("ins_party", partyInfo);
        }


        //2、保存project信息
        Map<String, String> projectInfo = result.get("PROJECT_INFO");

        Long projectId = null;
        if (StringUtils.isNotEmpty(customerId)) {
            projectInfo.put("PROJECT_ID", project_id);
            dao.save("ins_project", new String[]{"PROJECT_ID"}, projectInfo);
        } else {
            projectInfo.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
            projectInfo.put("CREATE_TIME", session.getCreateTime());
            projectInfo.put("PARTY_ID", partyId + "");
            projectId = dao.insertAutoIncrement("ins_project", projectInfo);
        }
        //保存项目意向信息
        Map<String, String> projectIntentionInfo = result.get("PROJECT_INTENTION_INFO");

        if (StringUtils.isNotEmpty(project_id)) {
            projectIntentionInfo.put("PROJECT_ID", project_id);
            dao.save("ins_project_intention", new String[]{"PROJECT_ID"}, projectIntentionInfo);
        } else {
            projectIntentionInfo.put("PROJECT_ID", projectId + "");
            projectIntentionInfo.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
            projectIntentionInfo.put("CREATE_TIME", session.getCreateTime());
            dao.insertAutoIncrement("ins_project_intention", projectIntentionInfo);
        }

        //生成项目联系人信息
        Map<String, String> project_link_info = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(project_id)) {
            project_link_info.put("PROJECT_ID", project_id);
        } else {
            project_link_info.put("PROJECT_ID", projectId + "");
        }
        project_link_info.put("ROLE_TYPE", CustomerServiceConst.CUSTOMERSERVICEROLETYPE);
        project_link_info.put("LINK_EMPLOYEE_ID", session.getSessionEntity().get("EMPLOYEE_ID"));
        project_link_info.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        project_link_info.put("CREATE_TIME", session.getCreateTime());
        project_link_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        project_link_info.put("UPDATE_TIME", session.getCreateTime());
        dao.insertAutoIncrement("ins_project_linkman", project_link_info);
        //生成项目_action信息
        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("2");
        List<Map<String, String>> partyProjectActionList = new ArrayList<Map<String, String>>();

        for (int i = 0; i < actionEntityList.size(); i++) {
            ActionEntity actionEntity = actionEntityList.get(i);
            String actionCode = actionEntity.getActionCode();
            Map<String, String> partyProjectActionInfo = new HashMap<String, String>();
            if (StringUtils.isNotEmpty(project_id)) {
                partyProjectActionInfo.put("PROJECT_ID", project_id);
                partyProjectActionInfo.put("PARTY_ID", customerId + "");
            } else {
                partyProjectActionInfo.put("PROJECT_ID", projectId + "");
                partyProjectActionInfo.put("PARTY_ID", partyId + "");
            }

            partyProjectActionInfo.put("ACTION_CODE", actionCode);
            if (StringUtils.equals(actionCode, "HZHK")) {
                partyProjectActionInfo.put("STATUS", "1");
                partyProjectActionInfo.put("FINISH_TIME", session.getCreateTime());
            } else {
                partyProjectActionInfo.put("STATUS", "0");
            }
            partyProjectActionInfo.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
            partyProjectActionInfo.put("CREATE_TIME", session.getCreateTime());
            partyProjectActionInfo.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            partyProjectActionInfo.put("UPDATE_TIME", session.getCreateTime());
            partyProjectActionList.add(partyProjectActionInfo);
        }
        if (ArrayTool.isNotEmpty(partyProjectActionList)) {
            dao.insertBatch("ins_project_original_action", partyProjectActionList);
        }
        //如果为上门咨询客户，客户订单状态直接改成咨询，否则为初始化
        /*String orderStatus = "";
        String stage = "";
        if (StringUtils.equals(customerType, "1")) {
            stage = "10";
            orderStatus = "2";
        } else {
            stage = "0";
            orderStatus = "1";
        }
        //如果为单独木制品流程则订单类型为W
        String orderType = "H";
        if (StringUtils.isNotEmpty(onlyWood)) {
            orderType = "W";
        }

        String decorateAddress = HousesBean.getHousesEntityById(houseId).getName();
        if(StringUtils.isNotEmpty(houseBuilding)){
            decorateAddress=decorateAddress+houseBuilding+"::";
        }
        if(StringUtils.isNotEmpty(houseRoomNo)){
            decorateAddress=decorateAddress+houseRoomNo;
        }

        if (StringUtils.isNotEmpty(customerId)) {
            OrderBean.updateConsultOrder(customerId + "", houseId, decorateAddress, house_mode, house_area, ""
                    , "", session.getSessionEntity().get("EMPLOYEE_ID"), "",
                    "", orderStatus, orderType, consultTime, stage);
        } else {
            if(StringUtils.isEmpty(customerId)){
                customerId=partyId+"";
            }
            OrderBean.createConsultOrder(customerId + "", houseId, decorateAddress, house_mode, house_area, ""
                    , "", session.getSessionEntity().get("EMPLOYEE_ID"), "",
                    "", orderStatus, orderType, consultTime, stage);
        }
        //更新报备状态
        if (StringUtils.isNotEmpty(prepareId)) {
            Record record = CustPreparationBean.getCustomerPrepare(prepareId);
            String status = record.get("STATUS");
            String prepareExpireTime = record.get("PREPARATION_EXPIRE_TIME");
            String prepareHouseId = record.get("HOUSE_ID");
            System.out.println(TimeTool.compareTwoTime(prepareExpireTime, TimeTool.now()));
            if (TimeTool.compareTwoTime(prepareExpireTime, TimeTool.now()) != 0) {
                //有效期内
                if (StringUtils.equals(houseId, prepareHouseId)) {
                    //楼盘一致
                    CustPreparationBean.updateCustomerPrepare(prepareId, "2", session.getSessionEntity().getUserId());
                } else {
                    CustPreparationBean.updateCustomerPrepare(prepareId, "1", session.getSessionEntity().getUserId());
                }
            }else{
                CustPreparationBean.updateCustomerPrepare(prepareId, "1", session.getSessionEntity().getUserId());
            }
        }*/
        //更新报表数据，新增咨询数
        CustServiceStatBean.updateCustServiceStat(session.getSessionEntity().get("EMPLOYEE_ID"), "GOODSEEGOODLIVE");

        return response;
    }


    private Map<String, Map<String, String>> buildCommonInfo(ServiceRequest request) {
        Map<String, Map<String, String>> map = new HashMap<>();
        AppSession session = SessionManager.getSession();

        String customerNo = request.getString("customerNo");
        String houseId = request.getString("house_id");
        String houseBuilding = request.getString("house_building");
        String houseRoomNo = request.getString("house_room_no");
        String sampleHouse = request.getString("sample_house");
        String customerType = request.getString("customer_type");
        String marketingType = request.getString("marketing_type");
        String marketingName = request.getString("marketing_name");
        String marketingTime = request.getString("marketing_time");
        String consultTime = request.getString("consult_time");
        String phoneConsultTime = request.getString("phone_consult_time");
        String otherRemark = request.getString("other_remark");
        String designerWorks = request.getString("DESIGNER_OPUS");
        String woodWish = request.getString("WOOD_INTENTION");
        String bankId = request.getString("bank_id");
        String quota = request.getString("quota");
        String monthNum = request.getString("month_num");
        String onlyWood = request.getString("onlyWood");


        String party_name = request.getString("NAME");
        String moblie_no = request.getString("CONTACT");
        String qq_no = request.getString("QQCONTACT");
        String wx_no = request.getString("WXCONTACT");
        //户型
        String house_mode = request.getString("HOUSEKIND");
        //面积
        String house_area = request.getString("AREA");
        //装修地点
        String house_address = request.getString("FIX_PLACE");
        String age = request.getString("AGE");
        //学历
        String educate = request.getString("EDUCATE");
        String company = request.getString("COMPANY");
        //常驻人口
        String family_members_count = request.getString("PEOPLE_COUNT");
        String oldDetail = request.getString("OLDER_DETAIL");
        String childDetail = request.getString("CHILD_DETAIL");
        //个人爱好
        String hobby = request.getString("HOBBY");
        String other_hobby = request.getString("OTHER_HOBBY");//其他个人爱好
        String chineseStlye = request.getString("CHINESESTYLE");//中国骨风
        String europenanClassics = request.getString("EUROPEANCLASSICS");//欧洲经典
        String modernSource = request.getString("MODERNSOURCE");//现代之源
        String other_topic_req = request.getString("OTHER_TOPIC_REQ");//其他主题选择
        String func = request.getString("FUNC");//功能选择
        String hasBluePrint = request.getString("BULEPRINT");//生成蓝图
        String funcSpecReq = request.getString("FUNC_SPEC_REQ");//功能特殊要求

        String advantage = request.getString("ADVANTAGE");//优势介绍
        String totalPricePlan = request.getString("TOTALPRICEPLAN");//总计划投资
        //基础木材
        String basicandwoodpriceplan = request.getString("BASICANDWOODPRICEPLAN");
        //暖通投资
        String hvacpriceplan = request.getString("HVACPRICEPLAN");
        //主材投资
        String materialpriceplan = request.getString("MATERIALPRICEPLAN");
        //家具投资
        String furniturepriceplan = request.getString("FURNITUREPRICEPLAN");
        //电器投资
        String electricalpriceplan = request.getString("ELECTRICALPRICEPLAN");
        //关键流程介绍
        String criticalprocess = request.getString("CRITICALPROCESS");
        String otherinfo = request.getString("OTHER_INFO");//过的家装公司和家具卖场
        String planLiveTime = request.getString("PLAN_LIVE_TIME");//计划入住时间
        String mwExperienceTime = request.getString("MW_EXPERIENCE_TIME");//木屋体验时间
        String isscanvideo = request.getString("ISSCANVIDEO");//是否观看了宣传片
        String isScanShowRoom = request.getString("ISSCANSHOWROOM");//是否参管城市展厅
        String counselorName = request.getString("COUNSELOR_NAME");//家装顾问
        String informationSource = request.getString("INFORMATIONSOURCE");//信息来源
        String otherSource = request.getString("OTHER_SOURCE");//其他信息来源

        //量房时间
        String gaugeHouseTime = request.getString("GAUGE_HOUSE_TIME");
        //平面图时间
        String offerPlaneTime = request.getString("OFFER_PLANE_TIME");
        //再联系时间
        String cantactTime = request.getString("CONTACT_TIME");

        //1、保存party信息
        Map<String, String> partyInfo = new HashMap<String, String>();

        partyInfo.put("PARTY_NAME", party_name);
        partyInfo.put("AGE", age);
        partyInfo.put("MOBILE_NO", moblie_no);
        partyInfo.put("QQ_NO", qq_no);
        partyInfo.put("WX_NO", wx_no);
        //客户状态正常
        partyInfo.put("PARTY_STATUS", CustomerServiceConst.PARTY_STATUS_0);
        partyInfo.put("COMPANY", company);
        partyInfo.put("EDUCATIONAL", educate);
        partyInfo.put("FAMILY_MEMBERS_COUNT", family_members_count);
        partyInfo.put("OLDER_DETAIL", oldDetail);
        partyInfo.put("CHILD_DETAIL", childDetail);
        partyInfo.put("HOBBY", hobby);
        partyInfo.put("OTHER_HOBBY", other_hobby);

        partyInfo.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        partyInfo.put("UPDATE_TIME", session.getCreateTime());
        //新增
        partyInfo.put("CONSULT_TIME", consultTime);
        partyInfo.put("CUST_NO", customerNo);
        partyInfo.put("REMARK", otherRemark);
        partyInfo.put("CUST_TYPE", customerType);
        partyInfo.put("PLOY_TYPE", marketingType);
        partyInfo.put("PLOY_NAME", marketingName);
        partyInfo.put("PLOY_TIME", marketingTime);
        partyInfo.put("TEL_CONSULT_TIME", phoneConsultTime);


        map.put("CUSTOMER_INFO", partyInfo);

        Map<String, String> projectInfo = new HashMap<String, String>();
        projectInfo.put("HOUSE_MODE", house_mode);
        projectInfo.put("HOUSE_AREA", house_area);
        projectInfo.put("HOUSE_ADDRESS", house_address);
        projectInfo.put("GAUGE_HOUSE_TIME", gaugeHouseTime);
        projectInfo.put("OFFER_PLANE_TIME", offerPlaneTime);
        projectInfo.put("CONTACT_TIME", cantactTime);
        projectInfo.put("CRITICAL_PROCESS", criticalprocess);
        projectInfo.put("OTHER_INFO", otherinfo);
        projectInfo.put("ADVANTAGE", advantage);
        projectInfo.put("MW_EXPERIENCE_TIME", mwExperienceTime);
        projectInfo.put("IS_SCAN_VIDEO", isscanvideo);
        projectInfo.put("IS_SCAN_SHOWROOM", isScanShowRoom);
        projectInfo.put("COUNSELOR_NAME", counselorName);
        projectInfo.put("INFORMATION_SOURCE", informationSource);
        projectInfo.put("OTHER_INFORMATION_SOURCE", otherSource);

        projectInfo.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        projectInfo.put("UPDATE_TIME", session.getCreateTime());

        //新增
        projectInfo.put("HOUSE_ID", houseId);
        projectInfo.put("HOUSE_BUILDING", houseBuilding);
        projectInfo.put("HOUSE_ROOM_NO", houseRoomNo);

        map.put("PROJECT_INFO", projectInfo);

        Map<String, String> projectIntentionInfo = new HashMap<String, String>();
        projectIntentionInfo.put("CHINESESTYLE_TOPIC", chineseStlye);
        projectIntentionInfo.put("EUROPEANCLASSICS_TOPIC", europenanClassics);
        projectIntentionInfo.put("MODERNSOURCE_TOPIC", modernSource);
        projectIntentionInfo.put("OTHER_TOPIC_REQ", other_topic_req);
        projectIntentionInfo.put("FUNC", func);
        projectIntentionInfo.put("HASBLUEPRINT", hasBluePrint);
        projectIntentionInfo.put("FUNC_SPEC_REQ", funcSpecReq);
        projectIntentionInfo.put("TOTAL_PRICEPLAN", totalPricePlan);
        projectIntentionInfo.put("BASICANDWOOD_PRICEPLAN", basicandwoodpriceplan);
        projectIntentionInfo.put("HVAC_PRICEPLAN", hvacpriceplan);
        projectIntentionInfo.put("MATERIAL_PRICEPLAN", materialpriceplan);
        projectIntentionInfo.put("FURNITURE_PRICEPLAN", furniturepriceplan);
        projectIntentionInfo.put("ELECTRICAL_PRICEPLAN", electricalpriceplan);

        projectIntentionInfo.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        projectIntentionInfo.put("UPDATE_TIME", session.getCreateTime());
        projectIntentionInfo.put("PLAN_LIVE_TIME", planLiveTime);
        //2020/03/01主营业务系统新增
        projectIntentionInfo.put("DESIGNER_OPUS", designerWorks);
        projectIntentionInfo.put("WOOD_INTENTION", woodWish);
        projectIntentionInfo.put("COOPERATIVE_BANK", bankId);
        projectIntentionInfo.put("QUOTA", quota);
        projectIntentionInfo.put("MONTH_NUM", monthNum);
        projectIntentionInfo.put("SAMPLE_HOUSE", sampleHouse);
/*        if(StringUtils.equals(onlyWood,"on")){
            projectIntentionInfo.put("ONLY_WOOD","1");
        }*/


        map.put("PROJECT_INTENTION_INFO", projectIntentionInfo);
        return map;
    }

    public ServiceResponse initPartyManager(ServiceRequest request) throws Exception {
        String name = request.getString("NAME");
        String wxnick = request.getString("WXNICK");
        String moblie = request.getString("MOBLIE");
        String houseaddress = request.getString("HOUSEADDRESS");
        String queryTagId = request.getString("QUERY_TAG_ID");

        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);


        RecordSet childEmployeeRecordSet = EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId, "0");

        if (Permission.hasAllCity() || Permission.hasAllShop()) {
            Record flag = new Record();//用来判断是否展示客户代表可选项
            flag.put("FLAG", "TRUE");
            response.set("FLAG", ConvertTool.toJSONObject(flag));
        } else if (childEmployeeRecordSet.size() <= 0) {
            Record flag = new Record();//用来判断是否展示客户代表可选项
            flag.put("FLAG", "FALSE");
            response.set("FLAG", ConvertTool.toJSONObject(flag));
        } else {
            Record flag = new Record();//用来判断是否展示客户代表可选项
            flag.put("FLAG", "TRUE");
            response.set("FLAG", ConvertTool.toJSONObject(flag));
        }


        RecordSet tagSet = StaticDataTool.getCodeTypeDatas("PARTY_TAG");
        response.set("TAGINFO", ConvertTool.toJSONArray(tagSet));

        RecordSet partyInfoList = dao.queryPartyInfoByLinkEmployeeIdAndTag(CustomerServiceConst.CUSTOMERSERVICEROLETYPE, name, wxnick, moblie, houseaddress, employeeId, "");
        if (partyInfoList.size() <= 0) {
            return response;
        }

        for (int i = 0; i < partyInfoList.size(); i++) {
            Record partyRecord = partyInfoList.get(i);
            String linkemployeeid = partyRecord.get("LINK_EMPLOYEE_ID");
            if (StringUtils.equals(employeeId, linkemployeeid)) {
                partyRecord.put("SHOWMOBILE", "YES");
            } else {
                partyRecord.put("SHOWMOBILE", "NO");
            }
            String tagId = partyRecord.get("TAG_ID");
            partyRecord.put("CUSTSERVICENAME", EmployeeCache.getEmployeeNameEmployeeId(linkemployeeid));
            partyRecord.put("PARTYTAGNAME", StaticDataTool.getCodeName("PARTY_TAG", tagId));

        }


        response.set("PARTYINFOLIST", ConvertTool.toJSONArray(partyInfoList));

        return response;
    }

    /**
     * 根据客户代表ID查询对应的PARTY_INFO
     *
     * @param request
     * @return
     */
    public ServiceResponse queryPartyByCustServicerEmployeeId(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String custServicerEmployeeId = session.getSessionEntity().get("EMPLOYEE_ID");

        String name = request.getString("NAME");
        String wxnick = request.getString("WXNICK");
        String moblie = request.getString("MOBLIE");
        String houseaddress = request.getString("HOUSEADDRESS");
        String childCustEmpId = request.getString("CUSTSERVICEEMPLOYEEID");
        String queryTagId = request.getString("QUERY_TAG_ID");
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);

        String employeeIds = "";
        String orgId = "";
        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();

        if (Permission.hasAllCity()) {
            orgId = "7";
        } else if (Permission.hasAllShop()) {
            orgId = EmployeeBean.queryOrgByEmployee(custServicerEmployeeId, "3").getOrgId();
        } else {
            orgId = EmployeeBean.queryOrgByEmployee(custServicerEmployeeId, "2").getOrgId();
        }

        orgId = OrgBean.getOrgLine(orgId, allOrgs);


        if (StringUtils.isNotBlank(childCustEmpId)) {
            employeeIds = childCustEmpId;
        } else if (Permission.hasAllCity()) {
            RecordSet allCustServiceEmpEntity = EmployeeBean.queryEmployeeByEmpIdsAndOrgId("", orgId);
            for (int i = 0; i < allCustServiceEmpEntity.size(); i++) {
                Record childRecord = allCustServiceEmpEntity.get(i);
                employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
            }
            employeeIds = employeeIds + custServicerEmployeeId;
        } else if (Permission.hasAllShop()) {
            RecordSet allCustServiceEmpEntity = EmployeeBean.queryEmployeeByEmpIdsAndOrgId("", orgId);
            for (int i = 0; i < allCustServiceEmpEntity.size(); i++) {
                Record childRecord = allCustServiceEmpEntity.get(i);
                employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
            }
            employeeIds = employeeIds + custServicerEmployeeId;
        } else {
            RecordSet childEmployeeRecordSet = EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(custServicerEmployeeId, "0");
            if (childEmployeeRecordSet.size() <= 0 || childEmployeeRecordSet == null) {
                employeeIds = custServicerEmployeeId;
            } else {
                for (int i = 0; i < childEmployeeRecordSet.size(); i++) {
                    Record record = childEmployeeRecordSet.get(i);
                    employeeIds += record.get("EMPLOYEE_ID") + ",";
                }
                employeeIds += custServicerEmployeeId;
            }
        }


        RecordSet partyInfoList = dao.queryPartyInfoByLinkEmployeeIdAndTag(CustomerServiceConst.CUSTOMERSERVICEROLETYPE, name, wxnick, moblie, houseaddress, employeeIds, queryTagId);

        if (partyInfoList.size() <= 0 || partyInfoList == null) {
            return response;
        }
        for (int i = 0; i < partyInfoList.size(); i++) {
            Record record = partyInfoList.get(i);
            String linkemployeeid = record.get("LINK_EMPLOYEE_ID");

            if (StringUtils.equals(custServicerEmployeeId, linkemployeeid)) {
                record.put("SHOWMOBILE", "YES");
            } else {
                record.put("SHOWMOBILE", "NO");
            }

            String tagId = record.get("TAG_ID");
            record.put("CUSTSERVICENAME", EmployeeCache.getEmployeeNameEmployeeId(linkemployeeid));
            record.put("PARTYTAGNAME", StaticDataTool.getCodeName("PARTY_TAG", tagId));
        }

        response.set("PARTYINFOLIST", ConvertTool.toJSONArray(partyInfoList));
        return response;
    }

    public static JSONObject filterPartysByTag(RecordSet partySet) throws Exception {
        JSONObject rst = new JSONObject();
        if (ArrayTool.isEmpty(partySet)) {
            return rst;
        }

        int size = partySet.size();
        for (int i = 0; i < size; i++) {
            Record party = partySet.get(i);
            String tagId = party.get("TAG_ID");

            if (StringUtils.isBlank(tagId)) {
                tagId = "other";
            }

            JSONArray datas = null;
            if (rst.containsKey(tagId)) {
                datas = rst.getJSONArray(tagId);
            } else {
                datas = new JSONArray();
                rst.put(tagId, datas);
            }
            datas.add(ConvertTool.toJSONObject(party));
        }
        return rst;
    }

    /**
     * 初始化改变好看好住信息界面
     */
    public ServiceResponse initChangeGoodSeeLiveInfo(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        response = this.initCreateGoodSeeLiveInfo(request);

        String partyId = request.getString("PARTY_ID");
        String projectId = request.getString("PROJECT_ID");


        JSONObject partyInfo = new JSONObject();
        //拼装party信息
        //PartyEntity partyEntity = dao.queryPartyInfoByPartyId(partyId);
        Record customerRecord = dao.queryCustomerInfoByCustId(partyId);
        //20200517新增，初始化修改好看好住信息修改时，判断之前是否以及完成过该动作，如果有则需要控制前台有些字段不可编辑
        RecordSet goodLiveActionInfo = dao.queryGoodLiveInfoActionInfo(partyId, projectId, "HZHK", "1");
        if (goodLiveActionInfo.size() <= 0 ||StringUtils.isBlank(customerRecord.get("CONSULT_TIME"))) {
            response.set("HAS_EDIT_INFO_FLAG", "false");
        } else {
            response.set("HAS_EDIT_INFO_FLAG", "true");
        }

        partyInfo.put("COMPANY", customerRecord.get("COMPANY"));
        partyInfo.put("AGE", customerRecord.get("AGE"));
        partyInfo.put("EDUCATE", customerRecord.get("EDUCATIONAL"));
        partyInfo.put("PEOPLE_COUNT", customerRecord.get("FAMILY_MEMBERS_COUNT"));
        partyInfo.put("OLDER_DETAIL", customerRecord.get("OLDER_DETAIL"));

        partyInfo.put("CHILD_DETAIL", customerRecord.get("CHILD_DETAIL"));
        partyInfo.put("OTHER_HOBBY", customerRecord.get("OTHER_HOBBY"));
        partyInfo.put("HOBBY", customerRecord.get("HOBBY"));

        String hobby = customerRecord.get("HOBBY");
        if (StringUtils.isNotBlank(hobby)) {
            String[] hobbys = hobby.split(",");
            String hobbyName = "";
            for (int i = 0; i < hobbys.length; i++) {
                hobbyName += StaticDataTool.getCodeName("HOBBY", hobbys[i]) + "、";
            }
            partyInfo.put("HOBBY_TEXT", hobbyName.substring(0, hobbyName.length() - 1));
        }
        //2020/03/01新增
        partyInfo.put("CUST_NO", customerRecord.get("CUST_NO"));
        partyInfo.put("PARTY_NAME", customerRecord.get("PARTY_NAME"));
        partyInfo.put("PARTY_ID", customerRecord.get("PARTY_ID"));
        partyInfo.put("CUST_TYPE", customerRecord.get("CUST_TYPE"));
        partyInfo.put("MOBILE_NO", customerRecord.get("MOBILE_NO"));
        partyInfo.put("PLOY_TYPE", customerRecord.get("PLOY_TYPE"));
        partyInfo.put("PLOY_NAME", customerRecord.get("PLOY_NAME"));
        partyInfo.put("PLOY_TIME", customerRecord.get("PLOY_TIME"));
        partyInfo.put("OTHER_REMARK", customerRecord.get("REMARK"));
        partyInfo.put("CONSULT_TIME", customerRecord.get("CONSULT_TIME"));
        partyInfo.put("TEL_CONSULT_TIME", customerRecord.get("TEL_CONSULT_TIME"));
        partyInfo.put("PREPARE_ID", customerRecord.get("PREPARE_ID"));
        partyInfo.put("COMPANY", customerRecord.get("COMPANY"));
        partyInfo.put("OPEN_ID", customerRecord.get("OPEN_ID"));


        response.set("PARTYINFO", partyInfo);
        //拼装project信息
        JSONObject projectInfo = new JSONObject();
        Record projectRecord = dao.queryProjectInfoByCustId(partyId);
        projectInfo.put("PROJECT_ID", projectRecord.get("PROJECT_ID"));
        projectInfo.put("HOUSE_MODE", projectRecord.get("HOUSE_MODE"));
        projectInfo.put("AREA", projectRecord.get("HOUSE_AREA"));
        projectInfo.put("ADVANTAGE", projectRecord.get("ADVANTAGE"));
        String advantage = projectRecord.get("ADVANTAGE");

        if (StringUtils.isNotBlank(advantage)) {
            String[] advantages = advantage.split(",");
            String advantageText = "";
            for (int i = 0; i < advantages.length; i++) {
                advantageText += StaticDataTool.getCodeName("ADVANTAGEINTRODUCTION", advantages[i]) + "、";
            }
            projectInfo.put("ADVANTAG_TEXT", advantageText.substring(0, advantageText.length() - 1));
        }

        projectInfo.put("CRITICALPROCESS", projectRecord.get("CRITICAL_PROCESS"));
        String criticalProcess = projectRecord.get("CRITICAL_PROCESS");

        if (StringUtils.isNotBlank(criticalProcess)) {
            String[] criticalProcesss = criticalProcess.split(",");
            String criticalProcessText = "";
            for (int i = 0; i < criticalProcesss.length; i++) {
                criticalProcessText += StaticDataTool.getCodeName("CRITICALPROCESS", criticalProcesss[i]) + "、";
            }
            projectInfo.put("CRITICALPROCESS_TEXT", criticalProcessText.substring(0, criticalProcessText.length() - 1));
        }

        projectInfo.put("OTHER_INFO", projectRecord.get("OTHER_INFO"));
        projectInfo.put("MW_EXPERIENCE_TIME", projectRecord.get("MW_EXPERIENCE_TIME"));
        projectInfo.put("ISSCANVIDEO", projectRecord.get("IS_SCAN_VIDEO"));
        projectInfo.put("ISSCANSHOWROOM", projectRecord.get("IS_SCAN_SHOWROOM"));
        //兼容老数据
        projectInfo.put("HOUSE_ADDRESS", projectRecord.get("HOUSE_ADDRESS"));
        projectInfo.put("INFORMATIONSOURCE", projectRecord.get("INFORMATION_SOURCE"));//信息来源
        String infomationSource = projectRecord.get("INFORMATION_SOURCE");

        if (StringUtils.isNotBlank(infomationSource)) {
            String[] infomationSources = infomationSource.split(",");
            String informationText = "";
            for (int i = 0; i < infomationSources.length; i++) {
                informationText += StaticDataTool.getCodeName("INFORMATIONSOURCE", infomationSources[i]) + "、";
            }
            projectInfo.put("INFORMATIONSOURCE_TEXT", informationText.substring(0, informationText.length() - 1));
        }

        projectInfo.put("GAUGE_HOUSE_TIME", projectRecord.get("GAUGE_HOUSE_TIME"));
        projectInfo.put("OFFER_PLANE_TIME", projectRecord.get("OFFER_PLANE_TIME"));
        projectInfo.put("CONTACT_TIME", projectRecord.get("CONTACT_TIME"));
        projectInfo.put("OTHER_SOURCE", projectRecord.get("OTHER_INFORMATION_SOURCE"));
        //2020/03/02新增
        projectInfo.put("HOUSE_BUILDING", projectRecord.get("HOUSE_BUILDING"));
        projectInfo.put("HOUSE_ROOM_NO", projectRecord.get("HOUSE_ROOM_NO"));
        projectInfo.put("HOUSE_ID", projectRecord.get("HOUSE_ID"));

        HousesEntity housesEntity = HousesBean.getHousesEntityById(projectRecord.get("HOUSE_ID"));
        if (housesEntity == null) {
            projectInfo.put("HOUSE_NAME", "");
        } else {
            projectInfo.put("HOUSE_NAME", HousesBean.getHousesEntityById(projectRecord.get("HOUSE_ID")).getName());
        }

        response.set("PROJECTINFO", projectInfo);
        //拼装项目意向信息
        JSONObject projectIntentionInfo = new JSONObject();
        Record intentionRecord = dao.queryProjectIntentionInfoByProjectId(projectRecord.get("PROJECT_ID"));
        projectIntentionInfo.put("CHINESESTYLE", intentionRecord.get("CHINESESTYLE_TOPIC"));

        String chineseStyleTopic = intentionRecord.get("CHINESESTYLE_TOPIC");
        if (StringUtils.isNotBlank(chineseStyleTopic)) {
            String[] chineseStyleTopics = chineseStyleTopic.split(",");
            String chineseStyleTopicText = "";
            for (int i = 0; i < chineseStyleTopics.length; i++) {
                chineseStyleTopicText += HouseParamTool.getTopicNameByTypeAndCode("CHINESESTYLE", chineseStyleTopics[i]) + "、";
            }
            projectIntentionInfo.put("CHINESESTYLE_TEXT", chineseStyleTopicText.substring(0, chineseStyleTopicText.length() - 1));
        }


        projectIntentionInfo.put("EUROPEANCLASSICS", intentionRecord.get("EUROPEANCLASSICS_TOPIC"));

        String europeanClassicsTopic = intentionRecord.get("EUROPEANCLASSICS_TOPIC");
        if (StringUtils.isNotBlank(europeanClassicsTopic)) {
            String[] europeanClassicsTopics = europeanClassicsTopic.split(",");
            String europeanClassicsTopicText = "";
            for (int i = 0; i < europeanClassicsTopics.length; i++) {
                europeanClassicsTopicText += HouseParamTool.getTopicNameByTypeAndCode("EUROPEANCLASSICS", europeanClassicsTopics[i]) + "、";
            }
            projectIntentionInfo.put("EUROPEANCLASSICS_TEXT", europeanClassicsTopicText.substring(0, europeanClassicsTopicText.length() - 1));
        }


        projectIntentionInfo.put("MODERNSOURCE", intentionRecord.get("MODERNSOURCE_TOPIC"));
        String modernSource = intentionRecord.get("MODERNSOURCE_TOPIC");
        if (StringUtils.isNotBlank(modernSource)) {
            String[] modernSources = modernSource.split(",");
            String modernSourcesText = "";
            for (int i = 0; i < modernSources.length; i++) {
                modernSourcesText += HouseParamTool.getTopicNameByTypeAndCode("MODERNSOURCE", modernSources[i]) + "、";
            }
            projectIntentionInfo.put("MODERNSOURCE_TEXT", modernSourcesText.substring(0, modernSourcesText.length() - 1));
        }

        projectIntentionInfo.put("OTHER_TOPIC_REQ", intentionRecord.get("OTHER_TOPIC_REQ"));
        projectIntentionInfo.put("FUNC", intentionRecord.get("FUNC"));

        String func = intentionRecord.get("FUNC");
        if (StringUtils.isNotBlank(func)) {
            String[] funcs = func.split(",");
            String funcsText = "";
            for (int i = 0; i < funcs.length; i++) {
                funcsText += HouseParamTool.getFuncNameByTypeAndCode("1", funcs[i]) + "、";
            }
            projectIntentionInfo.put("FUNCS_TEXT", funcsText.substring(0, funcsText.length() - 1));
        }

        projectIntentionInfo.put("BULEPRINT", intentionRecord.get("HASBULEPRINT"));
        projectIntentionInfo.put("FUNC_SPEC_REQ", intentionRecord.get("FUNC_SPEC_REQ"));
        projectIntentionInfo.put("TOTALPRICEPLAN", intentionRecord.get("TOTAL_PRICEPLAN"));
        projectIntentionInfo.put("BASICANDWOODPRICEPLAN", intentionRecord.get("BASICANDWOOD_PRICEPLAN"));
        projectIntentionInfo.put("HVACPRICEPLAN", intentionRecord.get("HVAC_PRICEPLAN"));
        projectIntentionInfo.put("MATERIALPRICEPLAN", intentionRecord.get("MATERIAL_PRICEPLAN"));
        projectIntentionInfo.put("FURNITUREPRICEPLAN", intentionRecord.get("FURNITURE_PRICEPLAN"));
        projectIntentionInfo.put("ELECTRICALPRICEPLAN", intentionRecord.get("ELECTRICAL_PRICEPLAN"));
        projectIntentionInfo.put("PLAN_LIVE_TIME", intentionRecord.get("PLAN_LIVE_TIME"));
        //2020/03/02新增
        projectIntentionInfo.put("DESIGNER_OPUS", intentionRecord.get("DESIGNER_OPUS"));
        projectIntentionInfo.put("WOOD_INTENTION", intentionRecord.get("WOOD_INTENTION"));
        projectIntentionInfo.put("COOPERATIVE_BANK", intentionRecord.get("COOPERATIVE_BANK"));
        projectIntentionInfo.put("QUOTA", intentionRecord.get("QUOTA"));
        projectIntentionInfo.put("MONTH_NUM", intentionRecord.get("MONTH_NUM"));
        projectIntentionInfo.put("SAMPLE_HOUSE", intentionRecord.get("SAMPLE_HOUSE"));
        projectIntentionInfo.put("ONLY_WOOD", intentionRecord.get("ONLY_WOOD"));

        response.set("PROJECTINTENTIONINFO", projectIntentionInfo);

        return response;
    }

    /**
     * 变更好看好住信息，连带处理客户合并的情况，是将家网产生的客户往已录入好看好住信息客户上合并，
     * 以防止后续已录入客户已在主主营业务系统走了流程而去更改cust_no,cust_id等情况
     *
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse changeGoodSeeLiveInfo(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String projectId = request.getString("PROJECT_ID");
        String custId = request.getString("PARTY_ID");
        //String houseId = request.getString("house_id");

        String mergePartyId = request.getString("mergePartyId");
        String mergeProjectId = request.getString("mergeProjectId");

        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);

        Map<String, Map<String, String>> resultMap = this.buildCommonInfo(request);


        //1、保存party信息
        Map<String, String> customerInfo = resultMap.get("CUSTOMER_INFO");
        PartyEntity partyEntity = dao.queryPartyInfoByPartyId(custId);
        //处理客户合并的场景
        if (StringUtils.isNotBlank(mergePartyId)) {
            customerInfo.put("PARTY_ID", mergePartyId);
            customerInfo.put("OPEN_ID", partyEntity.getOpenId());
            customerInfo.put("WX_NICK", partyEntity.getWxNick());
            customerInfo.put("HEAD_URL", partyEntity.getHeadUrl());
        } else {
            customerInfo.put("PARTY_ID", custId);
        }

        customerInfo.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        customerInfo.put("UPDATE_TIME", session.getCreateTime());

        dao.save("ins_party", new String[]{"PARTY_ID"}, customerInfo);

        //保存项目信息
        Map<String, String> projectInfo = resultMap.get("PROJECT_INFO");
        //处理客户合并的情况
        if (StringUtils.isNotBlank(mergeProjectId)) {
            projectInfo.put("PROJECT_ID", mergeProjectId);
        } else {
            projectInfo.put("PROJECT_ID", projectId);
        }
        projectInfo.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        projectInfo.put("UPDATE_TIME", session.getCreateTime());

        dao.save("ins_project", new String[]{"PROJECT_ID"}, projectInfo);

        //保存项目意向信息表
        Map<String, String> projectIntentionInfo = resultMap.get("PROJECT_INTENTION_INFO");
        //处理客户合并的情况
        if (StringUtils.isNotBlank(mergeProjectId)) {
            projectIntentionInfo.put("PROJECT_ID", mergeProjectId);
        } else {
            projectIntentionInfo.put("PROJECT_ID", projectId);
        }

        dao.save("ins_project_intention", new String[]{"PROJECT_ID"}, projectIntentionInfo);

        //修改工程更新时间
        Map<String, String> projectActionInfo = new HashMap<String, String>();
        //处理客户合并的情况
        if (StringUtils.isNotBlank(mergeProjectId)) {
            projectActionInfo.put("PROJECT_ID", mergeProjectId);
        } else {
            projectActionInfo.put("PROJECT_ID", projectId);
        }
        projectActionInfo.put("ACTION_CODE", "HZHK");
        projectActionInfo.put("STATUS", "1");
        projectActionInfo.put("FINISH_TIME", session.getCreateTime());
        projectActionInfo.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        projectActionInfo.put("UPDATE_TIME", session.getCreateTime());
        dao.save("ins_project_original_action", new String[]{"PROJECT_ID", "ACTION_CODE"}, projectActionInfo);

        //新增特殊处理包括1、各环节客户信息同步
        //需要将完成的家网动作做转移
        if (StringUtils.isNotBlank(mergeProjectId)) {
            RecordSet actionList = dao.queryPartyFlowByProjectId(projectId);
            List<Map<String, String>> actionUpdateList = new ArrayList<>();
            for (int i = 0; i < actionList.size(); i++) {
                Map<String, String> updateActionMap = new HashMap<>();
                Record actionRecord = actionList.get(i);
                if (StringUtils.equals(actionRecord.get("ACTION_CODE"), "SMJRLC")
                        || StringUtils.equals(actionRecord.get("ACTION_CODE"), "XQLTE")
                        || StringUtils.equals(actionRecord.get("ACTION_CODE"), "GZGZH")) {
                    updateActionMap.put("PROJECT_ID", mergeProjectId);
                    updateActionMap.put("PARTY_ID", mergePartyId);
                    updateActionMap.put("ACTION_CODE", actionRecord.get("ACTION_CODE"));
                    updateActionMap.put("STATUS", actionRecord.get("STATUS"));
                    updateActionMap.put("FINISH_TIME", actionRecord.get("FINISH_TIME"));
                    updateActionMap.put("CREATE_USER_ID", actionRecord.get("CREATE_USER_ID"));
                    updateActionMap.put("CREATE_TIME", actionRecord.get("CREATE_TIME"));
                    updateActionMap.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
                    updateActionMap.put("UPDATE_TIME", TimeTool.now());

                    dao.save("ins_project_original_action", new String[]{"PROJECT_ID", "ACTION_CODE"}, updateActionMap);
                }
            }


        }
        // 3、发送通知给对应的家装顾问告知客户信息变更
        this.syncPartyInfo(partyEntity, request);

        //更改状态,将客户状态改成合并销户2
        if (StringUtils.isNotBlank(mergePartyId)) {
            Map<String, String> updatePartyStatusMap = new HashMap<>();
            updatePartyStatusMap.put("PARTY_STATUS", "2");
            updatePartyStatusMap.put("PARTY_ID", custId);
            updatePartyStatusMap.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            updatePartyStatusMap.put("UPDATE_TIME", TimeTool.now());
            dao.save("ins_party", new String[]{"PARTY_ID"}, updatePartyStatusMap);
        }


        //  OrderBean.updateOrder(custId,houseName+":"+houseBuilding+":"+houseRoomNo,house_mode,house_area);
        return response;
    }

    /**
     * 同步客户代表环节的信息至家装顾问环节以及家网
     *
     * @param partyEntity
     * @param request
     * @throws Exception
     */
    public void syncPartyInfo(PartyEntity partyEntity, ServiceRequest request) throws Exception {
        if (partyEntity == null) {
            return;
        }
        if (StringUtils.isBlank(partyEntity.getOpenId())) {
            return;
        }
        CustDAO dao = DAOFactory.createDAO(CustDAO.class);
        CustomerEntity customerEntity = dao.getCustomerEntityByIdentifyCode(partyEntity.getOpenId());
        if (customerEntity == null) {
            return;
        }
        if (!StringUtils.equals("1", customerEntity.getCustStatus())) {
            return;
        }


        String partyName = request.getString("NAME");
        String mobileNo = request.getString("CONTACT");
        String houseId = request.getString("house_id");
        String houseMode = request.getString("HOUSEKIND");
        String houseArea = request.getString("AREA");
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");


        //拼装需要发给家装顾问的消息
        StringBuilder msgContent = new StringBuilder();
        String houseCounselorId = customerEntity.getHouseCounselorId();
        msgContent.append(EmployeeBean.getEmployeeByEmployeeId(houseCounselorId).getName() + "，您好！" +
                "【" + EmployeeBean.getEmployeeByEmployeeId(employeeId).getName())
                .append("】已完善了您的客户【" + customerEntity.getCustName() + "||微信昵称:" + customerEntity.getWxNick() + "】相关信息。")
                .append("变更内容由原来的客户姓名:" + customerEntity.getCustName() + "变成" + "[" + partyName + "],")
                .append("联系号码:" + customerEntity.getMobileNo() + "变成" + "[" + mobileNo + "],")
                .append("楼盘:" + HousesBean.getHousesEntityById(customerEntity.getHouseId()).getName() +
                        "变成" + "[" + HousesBean.getHousesEntityById(houseId).getName() + "]。")
                .append("变更时间为:" + TimeTool.now());
        //同步信息给家装顾问环节
        customerEntity.setCustName(partyName);
        customerEntity.setMobileNo(mobileNo);
        customerEntity.setHouseId(houseId);
        customerEntity.setHouseMode(houseMode);
        customerEntity.setHouseArea(houseArea);
        dao.update("INS_CUSTOMER", customerEntity.getContent());

        //拼装日志数据
        Map<String, String> logMap = new HashMap<>();
        //同步家网
        SyncBean.syncCustomerInfo(customerEntity, logMap);
        //保存所有日志
        logMap.put("CONTENT", msgContent.toString());
        logMap.put("OPEN_ID", customerEntity.getIdentifyCode());
        logMap.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        logMap.put("CREATE_TIME", TimeTool.now());
        dao.insert("SYNC_PARTY_LOG", logMap);
        //发送消息
        if (!StringUtils.equals(customerEntity.getCustName(), partyName)
                || !StringUtils.equals(customerEntity.getMobileNo(), mobileNo)
                || !StringUtils.equals(customerEntity.getHouseId(), houseId)) {
            MsgBean.sendMsg(EmployeeBean.getEmployeeByEmployeeId(houseCounselorId).getUserId(), msgContent.toString(), "0", TimeTool.now(), MsgType.sys);

        }


    }

    public ServiceResponse initchangecustservice(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        RecordSet childEmployeeRecordSet = EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId, "0");
        EmployeeEntity employeeEntity = EmployeeBean.getEmployeeByEmployeeId(employeeId);
        Record record = new Record();
        record.put("NAME", employeeEntity.getName());
        record.put("EMPLOYEE_ID", employeeEntity.getEmployeeId());
        childEmployeeRecordSet.add(record);
        if (childEmployeeRecordSet.size() <= 0 || childEmployeeRecordSet == null) {
            return null;
        }

        response.set("CUSTSERVICEINFO", ConvertTool.toJSONArray(childEmployeeRecordSet));
        return response;
    }

    public ServiceResponse searchNewCustService(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String mySwitch = request.getString("mySwitch");
        String searchName = request.getString("SEARCH_TEXT");
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();
        String orgId = OrgBean.getOrgId(sessionEntity);

        if (StringUtils.equals("off", mySwitch)) {
            RecordSet childEmployeeRecordSet = EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId, "0");

            if (childEmployeeRecordSet.size() <= 0 || childEmployeeRecordSet == null) {
                return response;
            }

            String employeeIds = "";
            for (int i = 0; i < childEmployeeRecordSet.size(); i++) {
                Record childEmployeeRecord = childEmployeeRecordSet.get(i);
                String childEmpId = childEmployeeRecord.get("EMPLOYEE_ID");
                employeeIds += childEmpId + ",";
            }
            employeeIds = employeeIds + employeeId;

            RecordSet custServiceRecodSet = dao.queryChildEmpByEmpIdsAndName(employeeIds, searchName, "");
            if (custServiceRecodSet.size() <= 0) {
                return response;
            }
            response.set("CUSTSERVICEINFO", ConvertTool.toJSONArray(custServiceRecodSet));
        } else {
            OrgEntity orgEntity = OrgBean.getAssignTypeOrg(orgId, "3", allOrgs);
            orgId = OrgBean.getOrgLine(orgEntity.getOrgId(), allOrgs);
            RecordSet allCustServiceRecordSet = dao.queryAllCustServiceByName(searchName, orgId);
            if (allCustServiceRecordSet.size() <= 0) {
                return response;
            }
            response.set("CUSTSERVICEINFO", ConvertTool.toJSONArray(allCustServiceRecordSet));

        }
        return response;
    }

    public ServiceResponse queryPartyInfo4ChangeCustService(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String name = request.getString("NAME");
        String mobile = request.getString("MOBILE");
        String custserviceEmpId = request.getString("CUSTSERVICEEMPLOYEEID");
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        String employeeIds = "";
        String orgId = "";

        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();

        if (Permission.hasAllCity()) {
            orgId = "7";
        } else if (Permission.hasAllShop()) {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "3").getOrgId();
        } else {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "2").getOrgId();
        }

        orgId = OrgBean.getOrgLine(orgId, allOrgs);

        if (StringUtils.isNotBlank(custserviceEmpId)) {
            employeeIds = custserviceEmpId;
        } else if (Permission.hasAllCity()) {
            RecordSet allCustServiceEmpEntity = EmployeeBean.queryEmployeeByEmpIdsAndOrgId("", orgId);
            for (int i = 0; i < allCustServiceEmpEntity.size(); i++) {
                Record childRecord = allCustServiceEmpEntity.get(i);
                employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
            }
            employeeIds = employeeIds.substring(0, employeeIds.length() - 1);
        } else if (Permission.hasAllShop()) {
            RecordSet allCustServiceEmpEntity = EmployeeBean.queryEmployeeByEmpIdsAndOrgId("", orgId);
            for (int i = 0; i < allCustServiceEmpEntity.size(); i++) {
                Record childRecord = allCustServiceEmpEntity.get(i);
                employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
            }
            employeeIds = employeeIds.substring(0, employeeIds.length() - 1);
        } else {
            RecordSet childEmployeeRecordSet = EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId, "0");
            if (childEmployeeRecordSet.size() <= 0 || childEmployeeRecordSet == null) {
                employeeIds = employeeId;
            } else {
                for (int i = 0; i < childEmployeeRecordSet.size(); i++) {
                    Record childRecord = childEmployeeRecordSet.get(i);
                    employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
                }
                employeeIds = employeeIds + employeeId;
            }
        }

        RecordSet recordSet = dao.queryPartyInfoByLinkEmployeeIds(employeeIds, CustomerServiceConst.CUSTOMERSERVICEROLETYPE, name, mobile, "");
        if (recordSet.size() <= 0) {
            return response;
        }

        for (int i = 0; i < recordSet.size(); i++) {
            Record record = recordSet.get(i);
            String linkemployeeid = record.get("LINK_EMPLOYEE_ID");
            EmployeeEntity employeeEntity = EmployeeBean.getEmployeeByEmployeeId(linkemployeeid);
            record.put("CUSTSERVICENAME", employeeEntity.getName());
        }

        response.set("PARTYINFOLIST", ConvertTool.toJSONArray(recordSet));
        return response;
    }

    public ServiceResponse confirmChangeCustService(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String partyIds = request.getString("PARTY_IDS");
        String custServiceEmpId = request.getString("SELECTED_CUSTSERVICE_ID");
        String[] partyIdArr = partyIds.split(",");
        for (int i = 0; i < partyIdArr.length; i++) {
            ProjectEntity projectEntity = dao.queryProjectInfoByPartyId(partyIdArr[i]);
            RecordSet recordSet = dao.queryLinkManByPartyId(partyIdArr[i], CustomerServiceConst.CUSTOMERSERVICEROLETYPE);
            Map<String, String> project_linkman_info = new HashMap<String, String>();
            project_linkman_info.put("PROJECT_ID", projectEntity.getProjectId());
            project_linkman_info.put("ROLE_TYPE", CustomerServiceConst.CUSTOMERSERVICEROLETYPE);
            project_linkman_info.put("LINK_EMPLOYEE_ID", custServiceEmpId);
            project_linkman_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            project_linkman_info.put("UPDATE_TIME", session.getCreateTime());
            dao.save("ins_project_linkman", new String[]{"PROJECT_ID", "ROLE_TYPE"}, project_linkman_info);
            //记录变更日志
            Map<String, String> changecustlog = new HashMap<String, String>();
            changecustlog.put("PARTY_ID", partyIdArr[i]);
            changecustlog.put("B_CUSTSERVICE_EMPID", recordSet.get(0).get("LINK_EMPLOYEE_ID"));
            changecustlog.put("A_CUSTSERVICE_EMPID", custServiceEmpId);
            changecustlog.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            changecustlog.put("UPDATE_DATE", session.getCreateTime());
            dao.insertAutoIncrement("custservice_change_log", changecustlog);
            //将蓝图信息也要变更过来
            PartyEntity partyEntity = dao.queryPartyInfoByPartyId(partyIdArr[i]);
            if (StringUtils.isBlank(partyEntity.getOpenId())) {
                continue;
            }
            String openId = partyEntity.getOpenId();
            RecordSet blueSet=dao.queryBluePrintByOpenIdAndActionCode(openId,"XQLTE");
            if(blueSet.size()>0){
                Map<String, String> blueActionInfo = new HashMap<String, String>();
                blueActionInfo.put("OPEN_ID", openId);
                blueActionInfo.put("REL_EMPLOYEE_ID", custServiceEmpId);
                dao.save("ins_blueprint_action", new String[]{"OPEN_ID"}, blueActionInfo);
            }


        }
        return response;
    }


    public ServiceResponse confirmScanCityCabin(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String party_id = request.getString("PARTY_ID");
        String project_id = request.getString("PROJECT_ID");
        String experiencetime = request.getString("MW_EXPERIENCE_TIME");
        String experience = request.getString("MW_EXPERIENCE");
        String city_cabin_ids = request.getString("CITY_CABIN_IDS");
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");

        Map<String, String> scaninfo = new HashMap<String, String>();
        scaninfo.put("PARTY_ID", party_id);
        scaninfo.put("EMPLOYEE_ID", employeeId);
        scaninfo.put("PROJECT_ID", project_id);
        scaninfo.put("EXPERIENCE_TIME", experiencetime);
        scaninfo.put("CITY_CABINS", city_cabin_ids);
        scaninfo.put("EXPERIENCE", experience);
        scaninfo.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        scaninfo.put("CREATE_TIME", TimeTool.now());

        RecordSet insScanCityInfo = dao.queryInsScanCityInfoByProIdAndPId(project_id, party_id);
        PartyEntity partyEntity = dao.queryPartyInfoByPartyId(party_id);
        String month = getMonth(experiencetime);
        String createDate = partyEntity.getCreateTime();
        String nowMonth = TimeTool.now("yyyy-MM");
        //城市木屋带看时间与咨询时间同月报表才加1
        if (insScanCityInfo.size() <= 0 && StringUtils.equals(month, getMonth(createDate)) && StringUtils.equals(nowMonth, getMonth(createDate))) {
            dao.insertAutoIncrement("ins_scan_citycabin", scaninfo);
            CustServiceStatBean.updateCustServiceStat(employeeId, "DKCSMW");
        } else {
            dao.insertAutoIncrement("ins_scan_citycabin", scaninfo);
        }


        Map<String, String> project_action_info = new HashMap<String, String>();
        project_action_info.put("PROJECT_ID", project_id);
        project_action_info.put("ACTION_CODE", "DKCSMW");
        project_action_info.put("PARTY_ID", party_id);
        project_action_info.put("STATUS", "1");
        project_action_info.put("FINISH_TIME", experiencetime);
        project_action_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        project_action_info.put("UPDATE_TIME", session.getCreateTime());
        dao.save("ins_project_original_action", new String[]{"PROJECT_ID", "ACTION_CODE", "PARTY_ID"}, project_action_info);

        return response;
    }

    public ServiceResponse initQueryForCustClear(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String partyId = request.getString("PARTY_ID");
        String projectId = request.getString("PROJECT_ID");
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        RecordSet recordSet = dao.queryPartyInfoForCustClear(partyId, projectId);//查询基本信息
        RecordSet applyRecordSet = dao.queryCustClearInfo(partyId, "", "", "");//查询申请记录
        //转译申请记录部分数据
        if (applyRecordSet.size() > 0) {
            for (int i = 0; i < applyRecordSet.size(); i++) {
                Record applyRecord = applyRecordSet.get(i);
                String applyEmployeeId = applyRecord.get("APPLY_EMPLOYEE_ID");
                String auditEmployeeId = applyRecord.get("AUDIT_EMPLOYEE_ID");
                if (StringUtils.isNotBlank(applyEmployeeId)) {
                    applyRecord.put("APPLYNAME", EmployeeCache.getEmployeeNameEmployeeId(applyEmployeeId));
                }
                if (StringUtils.isNotBlank(auditEmployeeId)) {
                    applyRecord.put("AUDITNAME", EmployeeCache.getEmployeeNameEmployeeId(auditEmployeeId));

                }
            }
        }
        response.set("APPLYINFO", ConvertTool.toJSONArray(applyRecordSet));


        if (recordSet.size() <= 0) {
            return response;
        }
        //竖表变横表
        RecordSet rst = new RecordSet();
        Map<String, Record> partyRecord = new HashMap<String, Record>();

        for (int i = 0; i < recordSet.size(); i++) {
            Record record = recordSet.get(i);
            String roleType = record.get("ROLE_TYPE");
            String partyInfoId = record.get("PARTY_ID");
            String linkEmpId = record.get("LINK_EMPLOYEE_ID");

            //新增楼盘翻译
            if (record.get("HOUSE_ID") != null) {
                String houseAddress = HousesBean.getHousesEntityById(record.get("HOUSE_ID")).getName();

                if (StringUtils.isNotBlank(record.get("HOUSE_BUILDING"))) {
                    houseAddress = houseAddress + "::" + record.get("HOUSE_BUILDING");
                }
                record.put("HOUSE_ADDRESS", houseAddress);
            }

            if (!partyRecord.containsKey(partyInfoId)) {
                if (StringUtils.equals("CUSTOMERSERVICE", roleType)) {
                    record.put("CUSTSERVICENAME", EmployeeCache.getEmployeeNameEmployeeId(linkEmpId));
                }
                if (StringUtils.equals("PROJECTDESIGNER", roleType)) {
                    record.put("DESIGNERNAME", EmployeeCache.getEmployeeNameEmployeeId(linkEmpId));
                }
                partyRecord.put(partyInfoId, record);
            } else {
                Record tempRecord = partyRecord.get(partyInfoId);
                if (StringUtils.equals("CUSTOMERSERVICE", roleType) && (!tempRecord.containsKey("CUSTSERVICENAME"))) {
                    tempRecord.put("CUSTSERVICENAME", EmployeeCache.getEmployeeNameEmployeeId(linkEmpId));
                }
                if (StringUtils.equals("PROJECTDESIGNER", roleType) && (!tempRecord.containsKey("DESIGNERNAME"))) {
                    tempRecord.put("DESIGNERNAME", EmployeeCache.getEmployeeNameEmployeeId(linkEmpId));
                }
            }
        }
        Set<String> keys = partyRecord.keySet();
        for (String key : keys) {
            rst.add(partyRecord.get(key));
        }

        if (!StringUtils.equals(employeeId, rst.get(0).get("LINK_EMPLOYEE_ID"))) {
            String partyName = rst.get(0).get("PARTY_NAME");
            rst.get(0).put("MOBILE_NO", "***********");
            rst.get(0).put("PARTY_NAME", nameDesensitization(partyName));
        }

        response.set("CUSTSERVICEINFOLIST", ConvertTool.toJSONArray(rst));

        return response;
    }

    public ServiceResponse submitCustClearApply(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String partyId = request.getString("PARTY_ID");
        String reason = request.getString("REASON");
        AppSession session = SessionManager.getSession();
        //RecordSet recordSet =dao.queryCustClearInfo(partyId,CustomerServiceConst.APPLY_STATUS_0,"","");
        Map<String, String> partyClearInfo = new HashMap<String, String>();
        partyClearInfo.put("PARTY_ID", partyId);
        partyClearInfo.put("APPLY_EMPLOYEE_ID", session.getSessionEntity().get("EMPLOYEE_ID"));
        partyClearInfo.put("APPLY_REASON", reason);
        partyClearInfo.put("STATUS", CustomerServiceConst.APPLY_STATUS_0);
        partyClearInfo.put("APPLY_DATE", session.getCreateTime());
        dao.insertAutoIncrement("ins_party_clear", partyClearInfo);
        //todo 缺少给主管发数消息的内容
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        EmployeeJobRoleEntity emproleEntity = EmployeeBean.queryEmployeeJobRoleByEmpId(employeeId);//获取当前登录员工的ROLE_JOB
        EmployeeEntity parentEntity = EmployeeBean.getEmployeeByEmployeeId(emproleEntity.getParentEmployeeId());
        if (parentEntity != null) {
            StringBuilder msgContent = new StringBuilder();
            msgContent.append(EmployeeCache.getEmployeeNameEmployeeId(employeeId))
                    .append("发起了客户清理申请！");
            MsgBean.sendMsg(parentEntity.getUserId(), msgContent.toString(), "0", TimeTool.now(), MsgType.sys);
        }

        return response;
    }

    public ServiceResponse initCustServiceAudit(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String applyCustServiceEmpId = "";
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        RecordSet childEmployeeRecordSet = EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId, "0");

        response.set("CUSTSERVICEINFO", ConvertTool.toJSONArray(childEmployeeRecordSet));

        if (childEmployeeRecordSet.size() <= 0) {
            applyCustServiceEmpId = employeeId;
        } else {
            for (int i = 0; i < childEmployeeRecordSet.size(); i++) {
                Record employeeRecord = childEmployeeRecordSet.get(i);
                applyCustServiceEmpId += employeeRecord.get("EMPLOYEE_ID") + ",";
            }
            applyCustServiceEmpId = applyCustServiceEmpId + employeeId;
        }
        RecordSet applyInfoRecordSet = dao.queryCustClearInfo("", "0", applyCustServiceEmpId, "");
        if (applyInfoRecordSet.size() <= 0) {
            return response;
        }

        for (int i = 0; i < applyInfoRecordSet.size(); i++) {
            Record applyRecord = applyInfoRecordSet.get(i);
            String applyEmpId = applyRecord.get("APPLY_EMPLOYEE_ID");
            String auditEmpId = applyRecord.get("AUDIT_EMPLOYEE_ID");
            String partyId = applyRecord.get("PARTY_ID");

            applyRecord.put("APPLY_EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(applyEmpId));

            PartyEntity partyEntity = dao.queryPartyInfoByPartyId(partyId);
            if (partyEntity != null) {
                applyRecord.put("PARTY_NAME", partyEntity.getPartyName());
                applyRecord.put("WX_NICK", partyEntity.getWxNick());
                applyRecord.put("CREATE_TIME", partyEntity.getCreateTime());
            }
            if (StringUtils.isNotBlank(auditEmpId)) {
                applyRecord.put("AUDIT_EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(auditEmpId));

            }
        }
        response.set("APPLYINFOLIST", ConvertTool.toJSONArray(applyInfoRecordSet));

        return response;
    }

    public ServiceResponse queryApplyInfo4Audit(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String custServiceEmpId = request.getString("CUSTSERVICEEMPLOYEEID");
        String busiType = request.getString("BUSITYPE");//获取查询业务类型方便以后扩展
        String auditStatus = request.getString("AUDITSTATUS");
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        String orgId = "";
        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();
        String employeeIds = "";

        if (Permission.hasAllCity()) {
            orgId = "7";
        } else if (Permission.hasAllShop()) {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "3").getOrgId();
        } else {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "2").getOrgId();
        }
        orgId = OrgBean.getOrgLine(orgId, allOrgs);

        if (StringUtils.isNotBlank(custServiceEmpId)) {
            employeeIds = custServiceEmpId;
        } else if (Permission.hasAllCity()) {
            RecordSet allCustServiceEmpEntity = EmployeeBean.queryEmployeeByEmpIdsAndOrgId("", orgId);
            for (int i = 0; i < allCustServiceEmpEntity.size(); i++) {
                Record childRecord = allCustServiceEmpEntity.get(i);
                employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
            }
            employeeIds = employeeIds.substring(0, employeeIds.length() - 1);
        } else if (Permission.hasAllShop()) {
            RecordSet allCustServiceEmpEntity = EmployeeBean.queryEmployeeByEmpIdsAndOrgId("", orgId);
            for (int i = 0; i < allCustServiceEmpEntity.size(); i++) {
                Record childRecord = allCustServiceEmpEntity.get(i);
                employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
            }
            employeeIds = employeeIds.substring(0, employeeIds.length() - 1);
        } else {
            RecordSet childEmployeeRecordSet = EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId, "0");
            if (childEmployeeRecordSet.size() <= 0) {
                employeeIds = employeeId;
            } else {
                for (int i = 0; i < childEmployeeRecordSet.size(); i++) {
                    Record employeeRecord = childEmployeeRecordSet.get(i);
                    employeeIds += employeeRecord.get("EMPLOYEE_ID") + ",";
                }
                employeeIds = employeeIds + employeeId;
            }
        }


        RecordSet applyInfoRecordSet = dao.queryCustClearInfo("", auditStatus, employeeIds, "");
        if (applyInfoRecordSet.size() <= 0) {
            return response;
        }

        for (int i = 0; i < applyInfoRecordSet.size(); i++) {
            Record applyRecord = applyInfoRecordSet.get(i);
            String applyEmpId = applyRecord.get("APPLY_EMPLOYEE_ID");
            String auditEmpId = applyRecord.get("AUDIT_EMPLOYEE_ID");
            String partyId = applyRecord.get("PARTY_ID");

            applyRecord.put("APPLY_EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(applyEmpId));

            PartyEntity partyEntity = dao.queryPartyInfoByPartyId(partyId);
            if (partyEntity != null) {
                applyRecord.put("PARTY_NAME", partyEntity.getPartyName());
                applyRecord.put("WX_NICK", partyEntity.getWxNick());
                applyRecord.put("CREATE_TIME", partyEntity.getCreateTime());
            }
            if (StringUtils.isNotBlank(auditEmpId)) {
                applyRecord.put("AUDIT_EMPLOYEE_NAME", EmployeeCache.getEmployeeNameEmployeeId(auditEmpId));

            }
        }
        response.set("APPLYINFOLIST", ConvertTool.toJSONArray(applyInfoRecordSet));

        return response;
    }

    public ServiceResponse auditConfirm(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String partyId = request.getString("PARTY_ID");
        String id = request.getString("ID");
        String auditStatus = request.getString("AUDIT_STATUS");
        String applyEmployeeId = request.getString("APPLY_EMPLOYEE_ID");

        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String auditEmpId = session.getSessionEntity().get("EMPLOYEE_ID");
        //更新申请数据状态
        Map<String, String> auditInfo = new HashMap<String, String>();
        auditInfo.put("AUDIT_EMPLOYEE_ID", auditEmpId);
        auditInfo.put("ID", id);
        auditInfo.put("AUDIT_DATE", TimeTool.now());
        auditInfo.put("STATUS", auditStatus);
        auditInfo.put("PARTY_ID", partyId);
        dao.save("ins_party_clear", new String[]{"ID", "PARTY_ID"}, auditInfo);

        //更新客户数据状态
        if (StringUtils.equals("1", auditStatus)) {
            Map<String, String> partyInfo = new HashMap<String, String>();
            partyInfo.put("PARTY_ID", partyId);
            partyInfo.put("PARTY_STATUS", CustomerServiceConst.PARTY_STATUS_1);
            partyInfo.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            partyInfo.put("UPDATE_TIME", TimeTool.now());
            dao.save("ins_party", new String[]{"PARTY_ID"}, partyInfo);
        }

        //更新报表数据
/*        if(StringUtils.equals("1", auditStatus)) {
            PartyEntity partyEntity = dao.queryPartyInfoByPartyId(partyId);
            String createTime=partyEntity.getCreateTime();
            String statTime=createTime.substring(0,7);
            String[] split = statTime.split("-");
            String time=split[0]+split[1];
            RecordSet recordSet=dao.queryCustServMonStatInfo(applyEmployeeId,time);
            //更新报表
            CustServiceStatBean.clearCustomerUpdateStat(recordSet,partyEntity);
        }*/

        return response;
    }


    public ServiceResponse initQuery4PartyVisit(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String partyId = request.getString("PARTY_ID");
        String projectId = request.getString("PROJECT_ID");
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        RecordSet recordSet = dao.queryPartyInfoForCustClear(partyId, projectId);//查询基本信息
        RecordSet partyVisitRecordSet = dao.queryPartyVisitInfo(partyId, "");//查询回访记录
        //转译申请记录部分数据
        if (partyVisitRecordSet.size() > 0) {
            for (int i = 0; i < partyVisitRecordSet.size(); i++) {
                Record partyVisitRecord = partyVisitRecordSet.get(i);
                String visitEmployeeId = partyVisitRecord.get("VISIT_EMPLOYEE_ID");
                String visitType = partyVisitRecord.get("VISIT_TYPE");
                partyVisitRecord.put("VISIT_NAME", EmployeeCache.getEmployeeNameEmployeeId(visitEmployeeId));
                partyVisitRecord.put("VISIT_TYPE_NAME", StaticDataTool.getCodeName("VISIT_TYPE", visitType));
            }
        }

        response.set("PARTYVISITINFO", ConvertTool.toJSONArray(partyVisitRecordSet));

        if (recordSet.size() <= 0) {
            return response;
        }
        //竖表变横表
        RecordSet rst = new RecordSet();
        Map<String, Record> partyRecord = new HashMap<String, Record>();

        for (int i = 0; i < recordSet.size(); i++) {
            Record record = recordSet.get(i);
            String roleType = record.get("ROLE_TYPE");
            String partyInfoId = record.get("PARTY_ID");
            String linkEmpId = record.get("LINK_EMPLOYEE_ID");
            //新增楼盘翻译
            if(record.get("HOUSE_ID")!=null) {
                String houseAddress = HousesBean.getHousesEntityById(record.get("HOUSE_ID")).getName();
                if (StringUtils.isNotBlank(record.get("HOUSE_BUILDING"))) {
                    houseAddress = houseAddress + "::" + record.get("HOUSE_BUILDING");
                }
                record.put("HOUSE_ADDRESS", houseAddress);
            }
            if (!partyRecord.containsKey(partyInfoId)) {
                if (StringUtils.equals("CUSTOMERSERVICE", roleType)) {
                    record.put("CUSTSERVICENAME", EmployeeCache.getEmployeeNameEmployeeId(linkEmpId));
                }
                if (StringUtils.equals("PROJECTDESIGNER", roleType)) {
                    record.put("DESIGNERNAME", EmployeeCache.getEmployeeNameEmployeeId(linkEmpId));
                }
                partyRecord.put(partyInfoId, record);
            } else {
                Record tempRecord = partyRecord.get(partyInfoId);
                if (StringUtils.equals("CUSTOMERSERVICE", roleType) && (!tempRecord.containsKey("CUSTSERVICENAME"))) {
                    tempRecord.put("CUSTSERVICENAME", EmployeeCache.getEmployeeNameEmployeeId(linkEmpId));
                }
                if (StringUtils.equals("PROJECTDESIGNER", roleType) && (!tempRecord.containsKey("DESIGNERNAME"))) {
                    tempRecord.put("DESIGNERNAME", EmployeeCache.getEmployeeNameEmployeeId(linkEmpId));
                }
            }

        }
        Set<String> keys = partyRecord.keySet();
        for (String key : keys) {
            rst.add(partyRecord.get(key));
        }

        if (!StringUtils.equals(employeeId, rst.get(0).get("LINK_EMPLOYEE_ID"))) {
            String partyName = rst.get(0).get("PARTY_NAME");
            rst.get(0).put("MOBILE_NO", "***********");
            rst.get(0).put("PARTY_NAME", nameDesensitization(partyName));
        }


        response.set("CUSTSERVICEINFOLIST", ConvertTool.toJSONArray(rst));
        return response;
    }

    public ServiceResponse addPartyVisit(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String visitObject = request.getString("VISIT_OBJECT");//回访对象
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");//回访人
        String visitType = request.getString("VISIT_TYPE");//回访类型
        String visitWay = request.getString("VISIT_WAY");//回访方式
        String visitContent = request.getString("VISIT_CONTENT");//回访内容
        String visitTime = request.getString("VISIT_TIME");//回访时间
        if (StringUtils.isBlank(visitTime)) {
            visitTime = session.getCreateTime();
        }
        String partyId = request.getString("PARTY_ID");

        Map<String, String> visitInfo = new HashMap<String, String>();
        visitInfo.put("PARTY_ID", partyId);
        visitInfo.put("VISIT_OBJECT", visitObject);
        visitInfo.put("VISIT_EMPLOYEE_ID", employeeId);
        visitInfo.put("VISIT_TYPE", visitType);
        visitInfo.put("VISIT_WAY", visitWay);
        visitInfo.put("VISIT_CONTENT", visitContent);
        visitInfo.put("CREATE_TIME", visitTime);
        visitInfo.put("CREATE_USER_ID", session.getSessionEntity().getUserId());

        dao.insertAutoIncrement("ins_party_visit", visitInfo);
        return response;
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

    public ServiceResponse initPartyTagManager(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        RecordSet recordSet = StaticDataTool.getCodeTypeDatas("PARTY_TAG");
        if (recordSet.size() <= 0) {
            return response;
        }
        response.set("PARTYTAGINFO", ConvertTool.toJSONArray(recordSet));
        return response;
    }

    public ServiceResponse submitPartyTagInfo(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);
        String tagId = request.getString("TAG_ID");
        String partyId = request.getString("PARTY_ID");
        Map<String, String> partyTagInfoMap = new HashMap<String, String>();

        partyTagInfoMap.put("PARTY_ID", partyId);
        partyTagInfoMap.put("TAG_ID", tagId);
        partyTagInfoMap.put("STATUS", "0");

        partyTagInfoMap.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        partyTagInfoMap.put("UPDATE_TIME", session.getCreateTime());

        RecordSet partyTagInfoSet = dao.queryPartyTagInfoByPartyId(partyId);

        if (partyTagInfoSet.size() <= 0) {
            partyTagInfoMap.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
            partyTagInfoMap.put("CREATE_TIME", session.getCreateTime());
            dao.insertAutoIncrement("ins_party_tag", partyTagInfoMap);
        } else {
            dao.save("ins_party_tag", new String[]{"PARTY_ID", "STATUS"}, partyTagInfoMap);
        }

        return response;
    }

    public static String nameDesensitization(String name) {
        String newName = "";
        if (StringUtils.isBlank(name)) {
            return "";
        }
        char[] chars = name.toCharArray();
        if (chars.length == 1) {
            newName = name;
        } else if (chars.length == 2) {
            newName = name.replaceFirst(name.substring(1), "*");
        } else {
            newName = name.replaceAll(name.substring(1, chars.length - 1), "*");
        }
        return newName;
    }


    /**
     * 根据前台传入的手机号码匹配客户是否在系统中已存在过客户数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse checkCustomerByMobile(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CustomerServiceDAO dao = new CustomerServiceDAO("ins");
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        String mobileNo = request.getString("mobileNo");
        RecordSet recordSet = dao.queryCustomerInfo4Merge("CUSTOMERSERVICE", mobileNo, employeeId);
        if (recordSet.size() <= 0) {
            return response;
        }
        for (int i = 0; i < recordSet.size(); i++) {
            Record record = recordSet.get(i);
            record.put("HOUSE_NAME", "");
            //record.put("ORDER_STATUS_NAME", StaticDataTool.getCodeName("ORDER_STATUS", record.get("ORDER_STATUS")));
            //record.put("PREPARE_STATUS_NAME", StaticDataTool.getCodeName("PREPARATION_STATUS", record.get("PREPARE_STATUS")));
            record.put("CUST_TYPE_NAME", StaticDataTool.getCodeName("CUSTOMER_TYPE", record.get("CUST_TYPE")));
            //翻译客户代表
/*
            if (StringUtils.isEmpty(record.get("CUSTSERVICE_EMPLOYEE_ID"))) {
                record.put("CUST_SERVICE_NAME", "");
            } else {
                record.put("CUST_SERVICE_NAME", EmployeeBean.getEmployeeByEmployeeId(record.get("CUSTSERVICE_EMPLOYEE_ID")).getName());
            }
            //翻译申报人
            if (StringUtils.isEmpty(record.get("PREPARE_EMPLOYEE_ID"))) {
                record.put("PREPARE_NAME", "");
            } else {
                record.put("PREPARE_NAME", EmployeeBean.getEmployeeByEmployeeId(record.get("PREPARE_EMPLOYEE_ID")).getName());
            }
*/

            if (StringUtils.isEmpty(record.get("HOUSE_ID"))) {
                record.put("HOUSE_NAME", "");
            } else {
                record.put("HOUSE_NAME", HousesBean.getHousesEntityById(record.get("HOUSE_ID")).getName());
            }


/*            //如果订单状态不为初始化，则不允许选择
            if (!StringUtils.equals("0", record.get("ORDER_STATUS"))) {
                record.put("isSelect", false);
            } else {
                if (StringUtils.isEmpty(record.get("CUSTSERVICE_EMPLOYEE_ID"))) {
                    record.put("isSelect", true);
                } else {
                    record.put("isSelect", false);
                }
            }*/
        }

        response.set("CUSTOMERINFO", ConvertTool.toJSONArray(recordSet));
        return response;
    }

    public ServiceResponse checkInfoByOpenIdFromCounselor(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String openId = request.getString("openId");
        CustDAO custDao = new CustDAO("ins");
        CustomerEntity customerEntity = custDao.getCustomerEntityByIdentifyCode(openId);
        if (customerEntity == null || !StringUtils.equals("1", customerEntity.getCustStatus())) {
            response.set("EXIST_CUSTOMER_INFO_FLAG", "FLASE");
        } else {
            //在家装顾问环节存在客户信息，则打上标记
            response.set("CUSTOMER_ENTITY", customerEntity);
            response.set("HOUSE_NAME", HousesBean.getHousesEntityById(customerEntity.getHouseId()).getName());
            response.set("EXIST_CUSTOMER_INFO_FLAG", "TRUE");
        }
        return response;
    }

}
