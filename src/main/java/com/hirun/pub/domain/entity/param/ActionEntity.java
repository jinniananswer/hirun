package com.hirun.pub.domain.entity.param;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
public class ActionEntity extends GenericEntity{

    public ActionEntity(Map<String, String> data){
        super(data);
    }

    public ActionEntity(){
        super();
    }

    public String getActionId(){
        return this.get("ACTION_ID");
    }

    public void setActionId(String actionId){
        this.set("ACTION_ID", actionId);
    }

    public String getActionCode(){
        return this.get("ACTION_CODE");
    }

    public void setActionCode(String actionCode){
        this.set("ACTION_CODE", actionCode);
    }

    public String getActionName(){
        return this.get("ACTION_NAME");
    }

    public void setActionName(String actionName){
        this.set("ACTION_NAME", actionName);
    }

    public String getActionType(){
        return this.get("ACTION_TYPE");
    }

    public void setActionType(String actionType){
        this.set("ACTION_TYPE", actionType);
    }
}
