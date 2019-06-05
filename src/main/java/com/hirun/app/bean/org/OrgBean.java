package com.hirun.app.bean.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/5/8 22:20
 * @Description:
 */
public class OrgBean {

    public static String getOrgId(SessionEntity sessionEntity){
        JSONArray jobRoles = sessionEntity.getJSONArray("JOB_ROLES");

        if(ArrayTool.isNotEmpty(jobRoles)){
            JSONObject jobRole = jobRoles.getJSONObject(0);
            String orgId = jobRole.getString("ORG_ID");
            return orgId;
        }

        return null;
    }

    public static JSONObject getOrgTree() throws Exception{
        JSONObject root = new JSONObject();
        OrgDAO dao = DAOFactory.createDAO(OrgDAO.class);
        List<OrgEntity> orgs = dao.queryAllOrgs();
        if(ArrayTool.isEmpty(orgs))
            return root;

        boolean hasAllOrg = Permission.hasAllOrg();

        OrgEntity rootOrg = null;
        if(hasAllOrg) {
            //找出根结点
            for (OrgEntity org : orgs) {
                if (StringUtils.isBlank(org.getParentOrgId())) {
                    rootOrg = org;
                    break;
                }
            }
        }
        else{
            AppSession session = SessionManager.getSession();
            SessionEntity sessionEntity = session.getSessionEntity();
            String orgId = OrgBean.getOrgId(sessionEntity);
            OrgEntity employeeOrg = null;
            if (StringUtils.isNotBlank(orgId)) {
                OrgDAO orgDAO = new OrgDAO("ins");
                employeeOrg = orgDAO.queryOrgById(orgId);
            }
//            String city = employeeOrg.getCity();
            rootOrg = findEmployeeRoot(employeeOrg.getOrgId(), orgs);
//            for (OrgEntity org : orgs) {
//                if (StringUtils.equals(org.getCity(), city) && StringUtils.equals("2", org.getType())) {
//                    rootOrg = org;
//                    break;
//                }
//            }
        }

        /** 未找到根节点，直接返回 **/
        if(rootOrg == null)
            return root;

        root.put("text", rootOrg.getName());
        root.put("id", rootOrg.getOrgId());
        root.put("dataid", rootOrg.getOrgId());
        root.put("order", "0");
        root.put("value", rootOrg.getOrgId());
        root.put("expand", "true");
        root.put("disabled", "false");
        root.put("complete", "false");

        JSONObject children = buildTreeNode(orgs, rootOrg.getOrgId(), rootOrg.getOrgId(), 0);
        if(children == null){
            root.put("haschild", "false");
        }
        else{
            root.put("haschild", "true");
            root.put("childNodes", children);
        }

        JSONObject rst = new JSONObject();
        rst.put(rootOrg.getOrgId(), root);
        return rst;
    }

    public static JSONObject buildTreeNode(List<OrgEntity> orgs, String parentOrgId, String prefix, int level){
        if(ArrayTool.isEmpty(orgs))
            return null;
        level++;
        JSONObject node = new JSONObject();
        int order = 0;
        for(OrgEntity org : orgs){
            if(StringUtils.equals(parentOrgId, org.getParentOrgId())){
                JSONObject childNode = new JSONObject();
                childNode.put("text", org.getName());
                childNode.put("id", org.getOrgId());
                childNode.put("order", order+"");
                childNode.put("value", org.getOrgId());
                childNode.put("dataid", prefix+"●"+org.getOrgId());
                childNode.put("expand", "false");
                childNode.put("complete", "false");
                childNode.put("disabled", "false");
                JSONObject children = buildTreeNode(orgs, org.getOrgId(), prefix+"●"+org.getOrgId(), level);
                if(children == null)
                    childNode.put("haschild", "false");
                else{
                    childNode.put("haschild", "true");
                    childNode.put("childNodes", children);
                }
                node.put(org.getOrgId(), childNode);
            }
            order++;
        }

        if(node.isEmpty())
            return null;
        else
            return node;
    }

    public static String getOrgLine(String rootOrgId) throws Exception{
        OrgDAO dao = DAOFactory.createDAO(OrgDAO.class);
        List<OrgEntity> orgs = dao.queryAllOrgs();
        if(ArrayTool.isEmpty(orgs))
            return null;

        String orgLine = buildSubOrg(rootOrgId, orgs, rootOrgId);
        return orgLine;

    }

    public static String getOrgLine(String rootOrgId, List<OrgEntity> orgs) throws Exception{
        if(ArrayTool.isEmpty(orgs))
            return null;

        String orgLine = buildSubOrg(rootOrgId, orgs, rootOrgId);
        return orgLine;
    }

    public static String buildSubOrg(String rootOrgId, List<OrgEntity> orgs, String orgLine) throws Exception{
        if(ArrayTool.isEmpty(orgs))
            return orgLine;

        for(OrgEntity org : orgs){
            if(StringUtils.equals(rootOrgId, org.getParentOrgId())){
                String subOrgs =  buildSubOrg(org.getOrgId(), orgs, org.getOrgId());
                if(StringUtils.isNotBlank(subOrgs))
                    orgLine += ","+subOrgs;
            }
        }

        return orgLine;
    }

    public static OrgEntity getAssignTypeOrg(String orgId, String type) throws Exception{
        OrgDAO dao = DAOFactory.createDAO(OrgDAO.class);
        List<OrgEntity> orgs = dao.queryAllOrgs();
        return getParentOrg(orgId, type, orgs);
    }

    public static OrgEntity getAssignTypeOrg(String orgId, String type, List<OrgEntity> orgs) throws Exception{
        return getParentOrg(orgId, type, orgs);
    }

    public static OrgEntity getParentOrg(String orgId, String type, List<OrgEntity> orgs) throws Exception{
        if(ArrayTool.isEmpty(orgs))
            return null;

        for(OrgEntity org : orgs){

            if(StringUtils.equals(orgId, org.getOrgId())){
                if(StringUtils.equals("1", type)){
                    //查自身
                    return org;
                }
                else if(StringUtils.equals("2", type)){
                    //查店面
                    if(StringUtils.equals("4", org.getType())){
                        return org;
                    }
                    else{
                        return getParentOrg(org.getParentOrgId(), type, orgs);
                    }
                }
                else if(StringUtils.equals("3", type)){
                    //查分公司
                    if(StringUtils.equals("2", org.getType())){
                        return org;
                    }
                    else if(StringUtils.equals(org.getOrgId(), "122")) {
                        //已经是集团公司了
                        return org;
                    }
                    else{
                        return getParentOrg(org.getParentOrgId(), type, orgs);
                    }
                }
                else if(StringUtils.equals("4", type)){
                    //查事业部
                    if(StringUtils.equals("1", org.getType())){
                        return org;
                    }
                    else{
                        return getParentOrg(org.getParentOrgId(), type, orgs);
                    }
                }
                else if(StringUtils.equals("5", type)){
                    //查集团总部
                    if(StringUtils.equals("0", org.getType())){
                        return org;
                    }
                    else{
                        return getParentOrg(org.getParentOrgId(), type, orgs);
                    }
                }
            }

        }
        return null;
    }

    public static List<OrgEntity> getAllOrgs() throws Exception{
        OrgDAO dao = DAOFactory.createDAO(OrgDAO.class);
        List<OrgEntity> orgs = dao.queryAllOrgs();
        return orgs;
    }

    public static OrgEntity findEmployeeRoot(String employeeOrgId, List<OrgEntity> orgs) throws Exception {
        if(ArrayTool.isEmpty(orgs)) {
            return null;
        }

        for(OrgEntity org : orgs) {
            if(StringUtils.equals(employeeOrgId, org.getOrgId())) {
                String parentOrgId = org.getParentOrgId();
                if(StringUtils.isBlank(org.getParentOrgId())) {
                    //已经是根结点了，直接返回
                    return org;
                }

                if(StringUtils.equals("122", parentOrgId)) {
                    //上级已经是根结点了，直接返回
                    return org;
                }

                String type = org.getType();
                if(StringUtils.equals("2",type)) {
                    //上级已经是分公司了，直接返回
                    return org;
                }

                return findEmployeeRoot(org.getParentOrgId(), orgs);
            }
        }
        return null;
    }

    public static List<OrgEntity> findBloodOrg(String employeeOrgId, List<OrgEntity> allOrgs) throws Exception {
        if (ArrayTool.isEmpty(allOrgs)) {
            return null;
        }

        List<OrgEntity> bloodLine = new ArrayList<OrgEntity>();

        for (OrgEntity org : allOrgs) {
            if (StringUtils.equals(employeeOrgId, org.getOrgId())) {

                bloodLine.add(org);//先加入本身

                if (StringUtils.isBlank(org.getParentOrgId())) {
                    //已经到头了
                    break;
                }
                else if(StringUtils.equals("2", org.getType())) {
                    break;
                }
                else {
                    List<OrgEntity> orgs = findBloodOrg(org.getParentOrgId(), allOrgs);
                    bloodLine.addAll(orgs);
                    break;
                }
            }
        }

        return bloodLine;
    }

    public static List<OrgEntity> bloodOrgDesc(String employeeOrgId, List<OrgEntity> allOrgs) throws Exception{
        List<OrgEntity> bloodLine = findBloodOrg(employeeOrgId, allOrgs);
        //因为顺序是按从小到大的，所以重新排下序
        List<OrgEntity> rst = new ArrayList<OrgEntity>();
        if (ArrayTool.isNotEmpty(bloodLine)) {
            int size = bloodLine.size();
            for (int i = size - 1; i >= 0; i--) {
                rst.add(bloodLine.get(i));
            }
        }
        return rst;
    }

    public static List<OrgEntity> findSubordinateOrg(String orgId, List<OrgEntity> allOrgs, String type) throws Exception {
        if (ArrayTool.isEmpty(allOrgs)) {
            return null;
        }

        List<OrgEntity> bloodLine = new ArrayList<OrgEntity>();

        for (OrgEntity org : allOrgs) {
            if (StringUtils.equals(orgId, org.getParentOrgId())) {

                if(StringUtils.equals(type, org.getType())) {
                    bloodLine.add(org);
                }
                else {
                    List<OrgEntity> orgs = findSubordinateOrg(org.getOrgId(), allOrgs, type);
                    if(orgs != null) {
                        bloodLine.addAll(orgs);
                    }
                }
            }
        }

        return bloodLine;
    }

}
