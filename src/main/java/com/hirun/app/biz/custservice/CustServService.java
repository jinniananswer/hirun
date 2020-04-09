package com.hirun.app.biz.custservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.common.MsgBean;
import com.hirun.app.bean.custservice.BluePrintBean;
import com.hirun.app.bean.custservice.CustServiceStatBean;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.EmployeeCache;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.pub.consts.CustomerServiceConst;
import com.hirun.pub.domain.entity.custservice.PartyEntity;
import com.hirun.pub.domain.entity.custservice.ProjectEntity;
import com.hirun.pub.domain.entity.custservice.ProjectIntentionEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.hirun.pub.domain.enums.common.MsgType;
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
import java.text.SimpleDateFormat;
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
        RecordSet linkmanSet = dao.queryLinkmanByProjectIdAndRoleType(projectId, CustomerServiceConst.CUSTOMERSERVICEROLETYPE);

        if (linkmanSet.size() > 0 || linkmanSet != null) {
            custServlinkEmpId = linkmanSet.get(0).get("LINK_EMPLOYEE_ID");
        }

        List<ActionEntity> actionEntityList = ActionCache.getActionListByType("2");
        PartyEntity partyEntity = dao.queryPartyInfoByPartyId(partyId);

        RecordSet partyActionList = dao.queryPartyFlowByProjectId(projectId);
        RecordSet projectDesignerInfo = dao.queryLinkmanByProjectIdAndRoleType(projectId, CustomerServiceConst.PROJECTDESIGNERROLETYPE);
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
                                if(StringUtils.isNotEmpty(xqlteRecord.get("FUNC"))&&!StringUtils.equals(xqlteRecord.get("FUNC"),"false")){
                                    JSONObject jsonObject = BluePrintBean.getFuncTree(xqlteRecord.get("FUNC"), "A");
                                    if (jsonObject != null) {
                                        response.set("FUNC_TREE", jsonObject);
                                    }
                                }

                                if(StringUtils.isNotEmpty(xqlteRecord.get("FUNC_B"))&&!StringUtils.equals(xqlteRecord.get("FUNC_B"),"false")){
                                    JSONObject jsonObject = BluePrintBean.getFuncTree(xqlteRecord.get("FUNC_B"),"B");
                                    if (jsonObject != null) {
                                        response.set("FUNC_TREE_B", jsonObject);
                                    }
                                }

                                if(StringUtils.isNotEmpty(xqlteRecord.get("FUNC_C"))&&!StringUtils.equals(xqlteRecord.get("FUNC_C"),"false")){
                                    JSONObject jsonObject = BluePrintBean.getFuncTree(xqlteRecord.get("FUNC_C"),"C");
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
        Map<String, String> party_info = new HashMap<String, String>();
        AppSession session = SessionManager.getSession();


        String party_name = request.getString("NAME");
        String moblie_no = request.getString("CONTACT");
        String qq_no = request.getString("QQCONTACT");
        String wx_no = request.getString("WXCONTACT");
        String house_mode = request.getString("HOUSEKIND");//户型
        String house_area = request.getString("AREA");//面积
        String house_address = request.getString("FIX_PLACE");//装修地点
        String age = request.getString("AGE");
        String educate = request.getString("EDUCATE");//学历
        String company = request.getString("COMPANY");
        String family_members_count = request.getString("PEOPLE_COUNT");//常驻人口
        //String oldman_count=request.getString("ELDER_MAN");//老男人个数
        //String oldwoman_count=request.getString("ELDER_WOMAN");//老女人个数
        //String boy_count=request.getString("CHILD_BOY");//小男孩个数
        //String girl_count=request.getString("CHILD_GIRL");//小女孩个数
        String oldDetail = request.getString("OLDER_DETAIL");
        String childDetail = request.getString("CHILD_DETAIL");
        String hobby = request.getString("HOBBY");//个人爱好
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
        String basicandwoodpriceplan = request.getString("BASICANDWOODPRICEPLAN");//基础木材
        String hvacpriceplan = request.getString("HVACPRICEPLAN");//暖通投资
        String materialpriceplan = request.getString("MATERIALPRICEPLAN");//主材投资
        String furniturepriceplan = request.getString("FURNITUREPRICEPLAN");//家具投资
        String electricalpriceplan = request.getString("ELECTRICALPRICEPLAN");//电器投资

        String criticalprocess = request.getString("CRITICALPROCESS");//关键流程介绍
        String otherinfo = request.getString("OTHER_INFO");//过的家装公司和家具卖场
        String planLiveTime = request.getString("PLAN_LIVE_TIME");//计划入住时间
        String mwExperienceTime = request.getString("MW_EXPERIENCE_TIME");//木屋体验时间
        String isscanvideo = request.getString("ISSCANVIDEO");//是否观看了宣传片
        String isScanShowRoom = request.getString("ISSCANSHOWROOM");//是否参管城市展厅
        String counselorName = request.getString("COUNSELOR_NAME");//家装顾问
        String informationSource = request.getString("INFORMATIONSOURCE");//信息来源
        String otherSource = request.getString("OTHER_SOURCE");//其他信息来源


        String gaugeHouseTime = request.getString("GAUGE_HOUSE_TIME");//量房时间
        String offerPlaneTime = request.getString("OFFER_PLANE_TIME");//平面图时间
        String cantactTime = request.getString("CONTACT_TIME");//再联系时间


        //1、保存party信息
        party_info.put("PARTY_NAME", party_name);
        party_info.put("AGE", age);
        party_info.put("MOBILE_NO", moblie_no);
        party_info.put("QQ_NO", qq_no);
        party_info.put("WX_NO", wx_no);
        party_info.put("PARTY_STATUS", CustomerServiceConst.PARTY_STATUS_0);//客户状态正常
        party_info.put("COMPANY", company);
        party_info.put("EDUCATIONAL", educate);
        party_info.put("FAMILY_MEMBERS_COUNT", family_members_count);
        //party_info.put("OLDMAN_COUNT",oldman_count);
        //party_info.put("OLDWOMAN_COUNT",oldwoman_count);
        //party_info.put("BOY_COUNT",boy_count);
        //party_info.put("GIRL_COUNT",girl_count);
        party_info.put("OLDER_DETAIL", oldDetail);
        party_info.put("CHILD_DETAIL", childDetail);
        party_info.put("HOBBY", hobby);
        party_info.put("OTHER_HOBBY", other_hobby);
        party_info.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        party_info.put("CREATE_TIME", session.getCreateTime());
        party_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        party_info.put("UPDATE_TIME", session.getCreateTime());

        long party_id = dao.insertAutoIncrement("INS_PARTY", party_info);


        //2、保存project信息
        Map<String, String> project_info = new HashMap<String, String>();
        project_info.put("PARTY_ID", party_id + "");
        project_info.put("HOUSE_MODE", house_mode);
        project_info.put("HOUSE_AREA", house_area);
        project_info.put("HOUSE_ADDRESS", house_address);
        project_info.put("GAUGE_HOUSE_TIME", gaugeHouseTime);
        project_info.put("OFFER_PLANE_TIME", offerPlaneTime);
        project_info.put("CONTACT_TIME", cantactTime);
        project_info.put("CRITICAL_PROCESS", criticalprocess);
        project_info.put("OTHER_INFO", otherinfo);
        project_info.put("ADVANTAGE", advantage);
        project_info.put("MW_EXPERIENCE_TIME", mwExperienceTime);
        project_info.put("IS_SCAN_VIDEO", isscanvideo);
        project_info.put("IS_SCAN_SHOWROOM", isScanShowRoom);
        project_info.put("COUNSELOR_NAME", counselorName);
        project_info.put("INFORMATION_SOURCE", informationSource);
        project_info.put("OTHER_INFORMATION_SOURCE", otherSource);
        project_info.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        project_info.put("CREATE_TIME", session.getCreateTime());
        project_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        project_info.put("UPDATE_TIME", session.getCreateTime());
        long project_id = dao.insertAutoIncrement("ins_project", project_info);
        //保存项目意向信息
        Map<String, String> project_intention_info = new HashMap<String, String>();
        project_intention_info.put("PROJECT_ID", project_id + "");
        project_intention_info.put("CHINESESTYLE_TOPIC", chineseStlye);
        project_intention_info.put("EUROPEANCLASSICS_TOPIC", europenanClassics);
        project_intention_info.put("MODERNSOURCE_TOPIC", modernSource);
        project_intention_info.put("OTHER_TOPIC_REQ", other_topic_req);
        project_intention_info.put("FUNC", func);
        project_intention_info.put("HASBLUEPRINT", hasBluePrint);
        project_intention_info.put("FUNC_SPEC_REQ", funcSpecReq);
        project_intention_info.put("TOTAL_PRICEPLAN", totalPricePlan);
        project_intention_info.put("BASICANDWOOD_PRICEPLAN", basicandwoodpriceplan);
        project_intention_info.put("HVAC_PRICEPLAN", hvacpriceplan);
        project_intention_info.put("MATERIAL_PRICEPLAN", materialpriceplan);
        project_intention_info.put("FURNITURE_PRICEPLAN", furniturepriceplan);
        project_intention_info.put("ELECTRICAL_PRICEPLAN", electricalpriceplan);
        project_intention_info.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        project_intention_info.put("CREATE_TIME", session.getCreateTime());
        project_intention_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        project_intention_info.put("UPDATE_TIME", session.getCreateTime());
        project_intention_info.put("PLAN_LIVE_TIME", planLiveTime);
        dao.insertAutoIncrement("ins_project_intention", project_intention_info);
        //生成项目联系人信息
        Map<String, String> project_link_info = new HashMap<String, String>();
        project_link_info.put("PROJECT_ID", project_id + "");
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
            partyProjectActionInfo.put("PROJECT_ID", project_id + "");
            partyProjectActionInfo.put("PARTY_ID", party_id + "");
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

        //更新报表数据，新增咨询数
        CustServiceStatBean.updateCustServiceStat(session.getSessionEntity().get("EMPLOYEE_ID"), "GOODSEEGOODLIVE");

        return response;
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
            employeeIds = employeeIds+custServicerEmployeeId;
        } else if (Permission.hasAllShop()) {
            RecordSet allCustServiceEmpEntity = EmployeeBean.queryEmployeeByEmpIdsAndOrgId("", orgId);
            for (int i = 0; i < allCustServiceEmpEntity.size(); i++) {
                Record childRecord = allCustServiceEmpEntity.get(i);
                employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
            }
            employeeIds = employeeIds+custServicerEmployeeId;
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
        String elderText = "";
        String childText = "";

        RecordSet partyLinkInfo = dao.queryLinkmanByProjectIdAndRoleType(projectId, CustomerServiceConst.CUSTOMERSERVICEROLETYPE);

        //拼装party信息
        PartyEntity partyEntity = dao.queryPartyInfoByPartyId(partyId);
        if (partyLinkInfo.size() <= 0) {

        } else {
            String linkEmpId = partyLinkInfo.get(0).get("LINK_EMPLOYEE_ID");
            if (StringUtils.equals(employeeId, linkEmpId)) {
                partyInfo.put("NAME", partyEntity.getPartyName());
                partyInfo.put("CONTACT", partyEntity.getMobileNo());
            } else {
                String name = nameDesensitization(partyEntity.getPartyName());
                partyInfo.put("NAME", name);
                partyInfo.put("CONTACT", "***********");
            }
        }
        partyInfo.put("QQCONTACT", partyEntity.getQQNO());
        partyInfo.put("WXCONTACT", partyEntity.getWXNO());
        partyInfo.put("COMPANY", partyEntity.getCompany());
        partyInfo.put("AGE", partyEntity.getAge());
        partyInfo.put("EDUCATE", partyEntity.getEducational());
        partyInfo.put("PEOPLE_COUNT", partyEntity.getfamilyMembersCount());
        //partyInfo.put("ELDER_MAN",partyEntity.getOldmanCount());
        //partyInfo.put("ELDER_WOMAN",partyEntity.getOldwomanCount());
        /*
        if(StringUtils.isNotBlank(partyEntity.getOldmanCount())){
            elderText="男："+partyEntity.getOldmanCount()+"人 ";
        }
        if(StringUtils.isNotBlank(partyEntity.getOldwomanCount())){
            elderText +="女："+partyEntity.getOldwomanCount()+"人";
        }
        */
        partyInfo.put("OLDER_DETAIL", partyEntity.getOlderDetail());
        //partyInfo.put("CHILD_BOY",partyEntity.getBoyCount());
        //partyInfo.put("CHILD_GIRL",partyEntity.getGirlCount());
        /*
        if(StringUtils.isNotBlank(partyEntity.getBoyCount())){
            childText="男："+partyEntity.getBoyCount()+"人 ";
        }
        if(StringUtils.isNotBlank(partyEntity.getGirlCount())){
            childText +="女："+partyEntity.getGirlCount()+"人";
        }
        */
        partyInfo.put("CHILD_DETAIL", partyEntity.getChildDetail());
        partyInfo.put("OTHER_HOBBY", partyEntity.getOtherHobby());
        partyInfo.put("HOBBY", partyEntity.getHobby());

        String hobby = partyEntity.getHobby();
        if (StringUtils.isNotBlank(hobby)) {
            String[] hobbys = hobby.split(",");
            String hobbyName = "";
            for (int i = 0; i < hobbys.length; i++) {
                hobbyName += StaticDataTool.getCodeName("HOBBY", hobbys[i]) + "、";
            }
            partyInfo.put("HOBBY_TEXT", hobbyName.substring(0, hobbyName.length() - 1));
        }
        response.set("PARTYINFO", partyInfo);
        //拼装project信息
        JSONObject projectInfo = new JSONObject();
        ProjectEntity projectEntity = dao.queryProjectInfoByProjectId(projectId);
        projectInfo.put("PROJECT_ID", projectId);
        projectInfo.put("HOUSEKIND", projectEntity.getHouseMode());
        projectInfo.put("AREA", projectEntity.getHouseArea());
        projectInfo.put("FIX_PLACE", projectEntity.getHouseAddress());
        projectInfo.put("ADVANTAGE", projectEntity.getAdvantage());
        String advantage = projectEntity.getAdvantage();

        if (StringUtils.isNotBlank(advantage)) {
            String[] advantages = advantage.split(",");
            String advantageText = "";
            for (int i = 0; i < advantages.length; i++) {
                advantageText += StaticDataTool.getCodeName("ADVANTAGEINTRODUCTION", advantages[i]) + "、";
            }
            projectInfo.put("ADVANTAG_TEXT", advantageText.substring(0, advantageText.length() - 1));
        }

        projectInfo.put("CRITICALPROCESS", projectEntity.getCriticalProcess());
        String criticalProcess = projectEntity.getCriticalProcess();

        if (StringUtils.isNotBlank(criticalProcess)) {
            String[] criticalProcesss = criticalProcess.split(",");
            String criticalProcessText = "";
            for (int i = 0; i < criticalProcesss.length; i++) {
                criticalProcessText += StaticDataTool.getCodeName("CRITICALPROCESS", criticalProcesss[i]) + "、";
            }
            projectInfo.put("CRITICALPROCESS_TEXT", criticalProcessText.substring(0, criticalProcessText.length() - 1));
        }

        projectInfo.put("OTHER_INFO", projectEntity.getOtherInfo());
        projectInfo.put("MW_EXPERIENCE_TIME", projectEntity.getMWExperienceTime());
        projectInfo.put("ISSCANVIDEO", projectEntity.getIsScanVideo());
        projectInfo.put("ISSCANSHOWROOM", projectEntity.getIsScanShowRoom());
        projectInfo.put("COUNSELOR_NAME", projectEntity.getCounselorName());//家装顾问名字
        projectInfo.put("INFORMATIONSOURCE", projectEntity.getInformationSource());//信息来源
        String infomationSource = projectEntity.getInformationSource();

        if (StringUtils.isNotBlank(infomationSource)) {
            String[] infomationSources = infomationSource.split(",");
            String informationText = "";
            for (int i = 0; i < infomationSources.length; i++) {
                informationText += StaticDataTool.getCodeName("INFORMATIONSOURCE", infomationSources[i]) + "、";
            }
            projectInfo.put("INFORMATIONSOURCE_TEXT", informationText.substring(0, informationText.length() - 1));
        }

        projectInfo.put("GAUGE_HOUSE_TIME", projectEntity.getGaugeHouseTime());
        projectInfo.put("OFFER_PLANE_TIME", projectEntity.getOfferPlaneTime());
        projectInfo.put("CONTACT_TIME", projectEntity.getContactTime());
        projectInfo.put("OTHER_SOURCE", projectEntity.getOtherInformationSource());

        response.set("PROJECTINFO", projectInfo);
        //拼装项目意向信息
        JSONObject projectIntentionInfo = new JSONObject();
        ProjectIntentionEntity projectIntentionEntity = dao.queryProjectIntentionInfoByProjectId(projectId);
        projectIntentionInfo.put("INTENTION_ID", projectIntentionEntity.getIntetionId());
        projectIntentionInfo.put("CHINESESTYLE", projectIntentionEntity.getChineseStyleTopic());

        String chineseStyleTopic = projectIntentionEntity.getChineseStyleTopic();
        if (StringUtils.isNotBlank(chineseStyleTopic)) {
            String[] chineseStyleTopics = chineseStyleTopic.split(",");
            String chineseStyleTopicText = "";
            for (int i = 0; i < chineseStyleTopics.length; i++) {
                chineseStyleTopicText += HouseParamTool.getTopicNameByTypeAndCode("CHINESESTYLE", chineseStyleTopics[i]) + "、";
            }
            projectIntentionInfo.put("CHINESESTYLE_TEXT", chineseStyleTopicText.substring(0, chineseStyleTopicText.length() - 1));
        }


        projectIntentionInfo.put("EUROPEANCLASSICS", projectIntentionEntity.getEuropeanClassicsTopic());

        String europeanClassicsTopic = projectIntentionEntity.getEuropeanClassicsTopic();
        if (StringUtils.isNotBlank(europeanClassicsTopic)) {
            String[] europeanClassicsTopics = europeanClassicsTopic.split(",");
            String europeanClassicsTopicText = "";
            for (int i = 0; i < europeanClassicsTopics.length; i++) {
                europeanClassicsTopicText += HouseParamTool.getTopicNameByTypeAndCode("EUROPEANCLASSICS", europeanClassicsTopics[i]) + "、";
            }
            projectIntentionInfo.put("EUROPEANCLASSICS_TEXT", europeanClassicsTopicText.substring(0, europeanClassicsTopicText.length() - 1));
        }


        projectIntentionInfo.put("MODERNSOURCE", projectIntentionEntity.getModernSource());
        String modernSource = projectIntentionEntity.getModernSource();
        if (StringUtils.isNotBlank(modernSource)) {
            String[] modernSources = modernSource.split(",");
            String modernSourcesText = "";
            for (int i = 0; i < modernSources.length; i++) {
                modernSourcesText += HouseParamTool.getTopicNameByTypeAndCode("MODERNSOURCE", modernSources[i]) + "、";
            }
            projectIntentionInfo.put("MODERNSOURCE_TEXT", modernSourcesText.substring(0, modernSourcesText.length() - 1));
        }

        projectIntentionInfo.put("OTHER_TOPIC_REQ", projectIntentionEntity.getOtherTopicReq());
        projectIntentionInfo.put("FUNC", projectIntentionEntity.getFunc());

        String func = projectIntentionEntity.getFunc();
        if (StringUtils.isNotBlank(func)) {
            String[] funcs = func.split(",");
            String funcsText = "";
            for (int i = 0; i < funcs.length; i++) {
                funcsText += HouseParamTool.getFuncNameByTypeAndCode("1", funcs[i]) + "、";
            }
            projectIntentionInfo.put("FUNCS_TEXT", funcsText.substring(0, funcsText.length() - 1));
        }

        projectIntentionInfo.put("BULEPRINT", projectIntentionEntity.getHasBluePrint());
        projectIntentionInfo.put("FUNC_SPEC_REQ", projectIntentionEntity.getFuncSpecReq());
        projectIntentionInfo.put("TOTALPRICEPLAN", projectIntentionEntity.getTotalPricePlan());
        projectIntentionInfo.put("BASICANDWOODPRICEPLAN", projectIntentionEntity.getBasicAndWoodPricePlan());
        projectIntentionInfo.put("HVACPRICEPLAN", projectIntentionEntity.getHvacPricePlan());
        projectIntentionInfo.put("MATERIALPRICEPLAN", projectIntentionEntity.getMaterialPricePlan());
        projectIntentionInfo.put("FURNITUREPRICEPLAN", projectIntentionEntity.getFurniturePricePlan());
        projectIntentionInfo.put("ELECTRICALPRICEPLAN", projectIntentionEntity.getElectricalPricePlan());
        projectIntentionInfo.put("PLAN_LIVE_TIME", projectIntentionEntity.getPlanLiveTime());

        response.set("PROJECTINTENTIONINFO", projectIntentionInfo);


        return response;
    }

    public ServiceResponse changeGoodSeeLiveInfo(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String projectId = request.getString("PROJECT_ID");
        String partyId = request.getString("PARTY_ID");
        AppSession session = SessionManager.getSession();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);

        String party_name = request.getString("NAME");
        String moblie_no = request.getString("CONTACT");
        String qq_no = request.getString("QQCONTACT");
        String wx_no = request.getString("WXCONTACT");
        String house_mode = request.getString("HOUSEKIND");//户型
        String house_area = request.getString("AREA");//面积
        String house_address = request.getString("FIX_PLACE");//装修地点
        String age = request.getString("AGE");
        String educate = request.getString("EDUCATE");//学历
        String company = request.getString("COMPANY");
        String family_members_count = request.getString("PEOPLE_COUNT");//常驻人口
        //String oldman_count=request.getString("ELDER_MAN");//老男人个数
        //String oldwoman_count=request.getString("ELDER_WOMAN");//老女人个数
        //String boy_count=request.getString("CHILD_BOY");//小男孩个数
        //String girl_count=request.getString("CHILD_GIRL");//小女孩个数
        String oldDetail = request.getString("OLDER_DETAIL");
        String childDetail = request.getString("CHILD_DETAIL");

        String hobby = request.getString("HOBBY");//个人爱好
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
        String basicandwoodpriceplan = request.getString("BASICANDWOODPRICEPLAN");//基础木材
        String hvacpriceplan = request.getString("HVACPRICEPLAN");//暖通投资
        String materialpriceplan = request.getString("MATERIALPRICEPLAN");//主材投资
        String furniturepriceplan = request.getString("FURNITUREPRICEPLAN");//家具投资
        String electricalpriceplan = request.getString("ELECTRICALPRICEPLAN");//电器投资

        String criticalprocess = request.getString("CRITICALPROCESS");//关键流程介绍
        String otherinfo = request.getString("OTHER_INFO");//过的家装公司和家具卖场
        String planLiveTime = request.getString("PLAN_LIVE_TIME");//计划入住时间
        String mwExperienceTime = request.getString("MW_EXPERIENCE_TIME");//木屋体验时间
        String isScanShowRoom = request.getString("ISSCANSHOWROOM");//是否参管城市展厅
        String isScanVideo = request.getString("ISSCANVIDEO");//是否观看了宣传片
        String counselorName = request.getString("COUNSELOR_NAME");//家装顾问
        String informationSource = request.getString("INFORMATIONSOURCE");//信息来源
        String otherSource = request.getString("OTHER_SOURCE");//其他信息来源


        String gaugeHouseTime = request.getString("GAUGE_HOUSE_TIME");//量房时间
        String offerPlaneTime = request.getString("OFFER_PLANE_TIME");//平面图时间
        String cantactTime = request.getString("CONTACT_TIME");//再联系时间

        //1、保存party信息
        Map<String, String> party_info = new HashMap<String, String>();
        party_info.put("PARTY_ID", partyId);
        if (party_name.toLowerCase().indexOf("*") == -1) {
            party_info.put("PARTY_NAME", party_name);
        }
        party_info.put("AGE", age);

        if (moblie_no.toLowerCase().indexOf("*") == -1) {
            party_info.put("MOBILE_NO", moblie_no);
        }
        party_info.put("QQ_NO", qq_no);
        party_info.put("WX_NO", wx_no);
        party_info.put("COMPANY", company);
        party_info.put("EDUCATIONAL", educate);
        party_info.put("FAMILY_MEMBERS_COUNT", family_members_count);
        //party_info.put("OLDMAN_COUNT",oldman_count);
        //party_info.put("OLDWOMAN_COUNT",oldwoman_count);
        //party_info.put("BOY_COUNT",boy_count);
        //party_info.put("GIRL_COUNT",girl_count);

        party_info.put("OLDER_DETAIL", oldDetail);
        party_info.put("CHILD_DETAIL", childDetail);

        party_info.put("HOBBY", hobby);
        party_info.put("OTHER_HOBBY", other_hobby);
        party_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        party_info.put("UPDATE_TIME", session.getCreateTime());


        dao.save("ins_party", new String[]{"PARTY_ID"}, party_info);

        //保存项目信息
        Map<String, String> project_info = new HashMap<String, String>();
        project_info.put("PROJECT_ID", projectId);
        project_info.put("HOUSE_MODE", house_mode);
        project_info.put("HOUSE_AREA", house_area);
        project_info.put("HOUSE_ADDRESS", house_address);
        project_info.put("GAUGE_HOUSE_TIME", gaugeHouseTime);
        project_info.put("OFFER_PLANE_TIME", offerPlaneTime);
        project_info.put("CONTACT_TIME", cantactTime);
        project_info.put("CRITICAL_PROCESS", criticalprocess);
        project_info.put("OTHER_INFO", otherinfo);
        project_info.put("ADVANTAGE", advantage);
        project_info.put("MW_EXPERIENCE_TIME", mwExperienceTime);
        project_info.put("IS_SCAN_VIDEO", isScanVideo);
        project_info.put("IS_SCAN_SHOWROOM", isScanShowRoom);
        project_info.put("COUNSELOR_NAME", counselorName);
        project_info.put("INFORMATION_SOURCE", informationSource);
        project_info.put("OTHER_INFORMATION_SOURCE", otherSource);
        project_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        project_info.put("UPDATE_TIME", session.getCreateTime());

        dao.save("ins_project", new String[]{"PROJECT_ID"}, project_info);

        //保存项目意向信息表
        Map<String, String> project_intention_info = new HashMap<String, String>();
        project_intention_info.put("PROJECT_ID", projectId);
        project_intention_info.put("CHINESESTYLE_TOPIC", chineseStlye);
        project_intention_info.put("EUROPEANCLASSICS_TOPIC", europenanClassics);
        project_intention_info.put("MODERNSOURCE_TOPIC", modernSource);
        project_intention_info.put("OTHER_TOPIC_REQ", other_topic_req);
        project_intention_info.put("FUNC", func);
        project_intention_info.put("HASBLUEPRINT", hasBluePrint);
        project_intention_info.put("FUNC_SPEC_REQ", funcSpecReq);
        project_intention_info.put("TOTAL_PRICEPLAN", totalPricePlan);
        project_intention_info.put("BASICANDWOOD_PRICEPLAN", basicandwoodpriceplan);
        project_intention_info.put("HVAC_PRICEPLAN", hvacpriceplan);
        project_intention_info.put("MATERIAL_PRICEPLAN", materialpriceplan);
        project_intention_info.put("FURNITURE_PRICEPLAN", furniturepriceplan);
        project_intention_info.put("ELECTRICAL_PRICEPLAN", electricalpriceplan);
        project_intention_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        project_intention_info.put("UPDATE_TIME", session.getCreateTime());
        project_intention_info.put("PLAN_LIVE_TIME", planLiveTime);

        dao.save("ins_project_intention", new String[]{"PROJECT_ID"}, project_intention_info);

        //修改工程更新时间
        Map<String, String> project_action_info = new HashMap<String, String>();
        project_action_info.put("PROJECT_ID", projectId);
        project_action_info.put("ACTION_CODE", "HZHK");
        project_action_info.put("STATUS", "1");
        project_action_info.put("FINISH_TIME", session.getCreateTime());
        project_action_info.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        project_action_info.put("UPDATE_TIME", session.getCreateTime());
        dao.save("ins_project_original_action", new String[]{"PROJECT_ID", "ACTION_CODE"}, project_action_info);

        return response;
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
            PartyEntity partyEntity=dao.queryPartyInfoByPartyId(partyIdArr[i]);
            if(StringUtils.isBlank(partyEntity.getOpenId())){
                return response;
            }
            String openId=partyEntity.getOpenId();
            Map<String, String> blueActionInfo = new HashMap<String, String>();
            blueActionInfo.put("OPEN_ID",openId);
            blueActionInfo.put("REL_EMPLOYEE_ID",custServiceEmpId);
            dao.save("ins_blueprint_action", new String[]{"OPEN_ID"}, blueActionInfo);

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
                applyRecord.put("CREATE_TIME",partyEntity.getCreateTime());
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
                applyRecord.put("CREATE_TIME",partyEntity.getCreateTime());
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
        if(StringUtils.isBlank(visitTime)){
            visitTime=session.getCreateTime();
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


}
