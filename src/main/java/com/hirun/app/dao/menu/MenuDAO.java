package com.hirun.app.dao.menu;

import com.hirun.pub.domain.entity.menu.MenuEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/21 11:50
 * @Description:
 */
@DatabaseName("sys")
public class MenuDAO extends StrongObjectDAO{

    public MenuDAO(String databaseName){
        super(databaseName);
    }

    public List<MenuEntity> queryMenusByLevel(String menuLevel) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("MENU_LEVEL", menuLevel);

        List<MenuEntity> menus = this.query(MenuEntity.class, "sys_menu", parameter);
        return menus;
    }

    public List<MenuEntity> queryMenusByParent(String parentMenuId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PARENT_MENU_ID", parentMenuId);

        List<MenuEntity> menus = this.query(MenuEntity.class, "sys_menu", parameter);
        return menus;
    }

}
