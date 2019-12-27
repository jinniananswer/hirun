package com.hirun.app.bean.permission;

import com.hirun.pub.domain.entity.menu.MenuEntity;
import com.hirun.pub.domain.entity.session.BizSessionEntity;
import com.most.core.app.session.RightsCollection;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Permission {

    public static List<MenuEntity> filterMenus(List<MenuEntity> menus) throws Exception{
        if(isSuperUser())
            return menus;

        if(ArrayTool.isEmpty(menus))
            return menus;

        RightsCollection rights = RightsCollection.getInstance();
        List<MenuEntity> rst = new ArrayList<MenuEntity>();
        Set<MenuEntity> tempMenus = new LinkedHashSet<>();
        for(MenuEntity menu : menus){
            String menuId = menu.getMenuId();
            if(rights.hasMenu(menuId))
                addMenu(menus, menu, tempMenus);
        }
        rst.addAll(tempMenus);
        return rst;
    }

    public static void addMenu(List<MenuEntity> menus, MenuEntity current, Set<MenuEntity> result) {
        result.add(current);
        if (current.getParentMenuId() == null) {
            //表示到了根节点
            return;
        }

        for (MenuEntity menu : menus) {
            if (StringUtils.equals(current.getParentMenuId(), menu.getMenuId())) {
                addMenu(menus, menu, result);
            }
        }
    }

    public static boolean hasAllCity() throws Exception{
        if(isSuperUser())
            return true;
        RightsCollection rights = RightsCollection.getInstance();
        if(rights.hasFuncCode("ALL_CITY"))
            return true;

        return false;
    }

    public static boolean hasAllShop() throws Exception{
        if(isSuperUser())
            return true;
        RightsCollection rights = RightsCollection.getInstance();
        if(rights.hasFuncCode("ALL_SHOP"))
            return true;

        return false;
    }

    public static boolean hasChangeHouse() throws Exception{
        if(isSuperUser())
            return true;
        RightsCollection rights = RightsCollection.getInstance();
        if(rights.hasFuncCode("CHANGE_HOUSE"))
            return true;

        return false;
    }

    public static boolean hasAuditHouse() throws Exception{
        if(isSuperUser())
            return true;
        RightsCollection rights = RightsCollection.getInstance();
        if(rights.hasFuncCode("AUDIT_HOUSE"))
            return true;

        return false;
    }

    public static boolean hasAllOrg() throws Exception{
        if(isSuperUser())
            return true;
        RightsCollection rights = RightsCollection.getInstance();
        if(rights.hasFuncCode("ALL_ORG"))
            return true;

        return false;
    }

    public static boolean hasEndSignOper() throws Exception{
        if(isSuperUser())
            return true;
        RightsCollection rights = RightsCollection.getInstance();
        if(rights.hasFuncCode("END_SIGN_OPER"))
            return true;

        return false;
    }

    public static boolean isSuperUser() {
        SessionEntity session = SessionManager.getSession().getSessionEntity();
        BizSessionEntity bizSession = new BizSessionEntity(session);
        String jobRole = bizSession.getJobRole();
        if (StringUtils.equals("0", jobRole))
            return true;
        else
            return false;
    }


}
