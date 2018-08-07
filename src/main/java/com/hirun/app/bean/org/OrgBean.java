package com.hirun.app.bean.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.authority.AuthorityJudgement;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

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
            String city = employeeOrg.getCity();
            for (OrgEntity org : orgs) {
                if (StringUtils.equals(org.getCity(), city) && StringUtils.equals("2", org.getType())) {
                    rootOrg = org;
                    break;
                }
            }
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
}
