package com.hirun.app.bean.authority;

import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author jinnian
 * @Date 2018/5/10 14:40
 * @Description:
 */
public class AuthorityJudgement {

    public static boolean hasAllCity() throws Exception {
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        boolean allCity = false;
        OrgEntity org = null;
        if (StringUtils.isNotBlank(orgId)) {
            //这段逻辑要替换成根据权限来判断
            OrgDAO dao = new OrgDAO("ins");
            org = dao.queryOrgById(orgId);
            String parentOrgId = org.getParentOrgId();
            if (StringUtils.isBlank(parentOrgId)) {
                //表示是集团公司的员工
                allCity = true;
            }
        } else {
            allCity = true;
        }

        return allCity;
    }

    public static boolean hasAllShop() throws Exception {
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        OrgDAO dao = new OrgDAO("ins");
        boolean needAllShop = true;
        OrgEntity org = null;
        OrgEntity parentOrg = null;
        if (StringUtils.isNotBlank(orgId)) {
            //以后要换成权限判断
            org = dao.queryOrgById(orgId);
            String type = org.getType();
            if (StringUtils.equals("4", type)) {
                needAllShop = false;
            } else if (StringUtils.equals("3", type)) {
                String parentOrgId = org.getParentOrgId();
                if (StringUtils.isNotBlank(parentOrgId)) {
                    parentOrg = dao.queryOrgById(parentOrgId);
                    String parentType = parentOrg.getType();
                    if (StringUtils.equals("4", parentType)) {
                        needAllShop = false;
                    }
                }
            }
        }
        return needAllShop;
    }
}
