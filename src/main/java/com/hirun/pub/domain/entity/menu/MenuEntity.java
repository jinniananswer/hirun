package com.hirun.pub.domain.entity.menu;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/21 11:51
 * @Description:
 */
public class MenuEntity extends GenericEntity {

    public MenuEntity(){
        super();
    }

    public MenuEntity(Map<String, String> menu){
        super(menu);
    }

    public String getMenuId(){
        return this.get("MENU_ID");
    }

    public void setMenuId(String menuId){
        this.set("MENU_ID", menuId);
    }

    public String getTitle(){
        return this.get("TITLE");
    }

    public void setTitle(String title){
        this.set("TITLE", title);
    }

    public String getMenuDesc(){
        return this.get("MENU_DESC");
    }

    public void setMenuDesc(String menuDesc){
        this.set("MENU_DESC", menuDesc);
    }

    public String getMenuUrl(){
        return this.get("MENU_URL");
    }

    public void setMenuUrl(String menuUrl){
        this.set("MENU_URL", menuUrl);
    }

    public String getDomainId(){
        return this.get("DOMAIN_ID");
    }

    public void setDomainId(String domainId){
        this.set("DOMAIN_ID", domainId);
    }

    public String getMenuLevel(){
        return this.get("MENU_LEVEL");
    }

    public void setMenuLevel(String menuLevel){
        this.set("MENU_LEVEL", menuLevel);
    }

    public String getParentMenuId(){
        return this.get("PARENT_MENU_ID");
    }

    public void setParentMenuId(String parentMenuId){
        this.set("PARENT_MENU_ID", parentMenuId);
    }

    public String getFuncId(){
        return this.get("FUNC_ID");
    }

    public void setFuncId(String funcId){
        this.set("FUNC_ID", funcId);
    }

    public String getIcoUrl(){
        return this.get("ICO_URL");
    }

    public void setIcoUrl(String icoUrl){
        this.set("ICO_URL", icoUrl);
    }

    public String getIsCommonUse(){
        return this.get("IS_COMMON_USE");
    }

    public void setIsCommonUse(String isCommonUse){
        this.set("IS_COMMON_USE", isCommonUse);
    }
}
