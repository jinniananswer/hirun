package com.hirun.pub.domain.entity.param;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
public class PlanActionLimitEntity extends GenericEntity{

    public PlanActionLimitEntity(){
        super();
    }

    public PlanActionLimitEntity(Map<String, String> data){
        super(data);
    }

    public String getLimitId(){
        return this.get("LIMIT_ID");
    }

    public void setLimitId(String limitId){
        this.set("LIMIT_ID", limitId);
    }

    public String getActionCode(){
        return this.get("ACTION_CODE");
    }

    public void setActionCode(String actionCode){
        this.set("ACTION_CODE", actionCode);
    }

    public String getLimitType(){
        return this.get("LIMIT_TYPE");
    }

    public void setLimitType(String limitType){
        this.set("LIMIT_TYPE", limitType);
    }

    public String getRelActionCode(){
        return this.get("REL_ACTION_CODE");
    }

    public void setRelActionCode(String relActionCode){
        this.set("REL_ACTION_CODE", relActionCode);
    }

    public String getLimitParam(){
        return this.get("LIMIT_PARAM");
    }

    public void setLimitParam(String limitParam){
        this.set("LIMIT_PARAM", limitParam);
    }

    public String getStatus(){
        return this.get("STATUS");
    }

    public void setStatus(String status){
        this.set("STATUS", status);
    }

    public String getStartTime(){
        return this.get("START_TIME");
    }

    public void setStartTime(String startTime){
        this.set("START_TIME", startTime);
    }

    public String getEndTime(){
        return this.get("END_TIME");
    }

    public void setEndTime(String endTime){
        this.set("END_TIME", endTime);
    }
}
