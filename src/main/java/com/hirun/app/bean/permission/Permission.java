package com.hirun.app.bean.permission;

import com.hirun.pub.domain.entity.menu.MenuEntity;
import com.hirun.pub.domain.entity.session.BizSessionEntity;
import com.most.core.app.session.RightsCollection;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Permission {

    public static List<MenuEntity> filterMenus(List<MenuEntity> menus) throws Exception{
        if(isSuperUser())
            return menus;

        if(ArrayTool.isEmpty(menus))
            return menus;

        RightsCollection rights = RightsCollection.getInstance();
        List<MenuEntity> rst = new ArrayList<MenuEntity>();
        for(MenuEntity menu : menus){
            String funcId = menu.getFuncId();
            if(rights.hasFuncId(funcId))
                rst.add(menu);
        }
        return rst;
    }

    public static boolean hasAllCity() {
        if (isSuperUser())
            return true;

        return false;
    }

    public static boolean hasAllShop() {
        if (isSuperUser())
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
