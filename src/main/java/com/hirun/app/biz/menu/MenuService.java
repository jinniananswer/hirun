package com.hirun.app.biz.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.dao.menu.MenuDAO;
import com.hirun.pub.domain.entity.menu.MenuEntity;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/4/21 23:01
 * @Description:
 */
public class MenuService extends GenericService{

    public ServiceResponse loadMenus(ServiceRequest request) throws Exception{

        ServiceResponse response = new ServiceResponse();
        //先查询一级目录的菜单
        String menuLevel = "1";
        MenuDAO dao = new MenuDAO("sys");

        List<MenuEntity> allMenus = dao.queryAllMenus();
        if (ArrayTool.isEmpty(allMenus)) {
            return response;
        }
        allMenus = Permission.filterMenus(allMenus);

        List<MenuEntity> dir = new ArrayList<MenuEntity>();

        for (MenuEntity menu : allMenus) {
            if (StringUtils.equals(menuLevel, menu.getMenuLevel())) {
                dir.add(menu);
            }
        }

        if(ArrayTool.isEmpty(dir)){
            return response;
        }

        JSONArray menus = new JSONArray();
        for(MenuEntity menu : dir){
            String menuId = menu.getMenuId();
            List<MenuEntity> subMenus = queryMenusByParent(menuId, allMenus);

            JSONObject menuJson = menu.toJson();
            menus.add(menuJson);
            if(ArrayTool.isEmpty(subMenus)){
                continue;
            }
            JSONArray subMenuJsons = new JSONArray();
            for(MenuEntity subMenu : subMenus){
                JSONObject subMenuJson = subMenu.toJson();
                subMenuJsons.add(subMenuJson);
            }

            menuJson.put("SUB_MENU", subMenuJsons);
        }
        response.set("MENUS", menus);
        return response;
    }

    private List<MenuEntity> queryMenusByParent(String menuId, List<MenuEntity> menus) {
        List<MenuEntity> result = new ArrayList<MenuEntity>();

        for (MenuEntity menu : menus) {
            if (StringUtils.equals(menuId, menu.getParentMenuId())) {
                result.add(menu);
            }
        }

        return result;
    }
}
