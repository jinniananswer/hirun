package com.hirun.pub.domain.entity.plan;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
public class PlanEntity extends GenericEntity {

    public PlanEntity () {
        super();
    }

    public PlanEntity(Map<String, String> data){
        super(data);
    }

    public String getPlanId(){
        return this.get("PLAN_ID");
    }

    public void setPlanId(String planId){
        this.set("PLAN_ID", planId);
    }

    public String getPlanName(){
        return this.get("PLAN_NAME");
    }

    public void setPlanName(String planName){
        this.set("PLAN_NAME", planName);
    }

    public String getPlanDate(){
        return this.get("PLAN_DATE");
    }

    public void setPlanDate(String planDate){
        this.set("PLAN_DATE", planDate);
    }

    public String getPlanType(){
        return this.get("PLAN_TYPE");
    }

    public void setPlanType(String planType){
        this.set("PLAN_TYPE", planType);
    }

    public String getPlanStatus(){
        return this.get("PLAN_STATUS");
    }

    public void setPlanStatus(String planStatus){
        this.set("PLAN_STATUS", planStatus);
    }

    public String getPlanMode(){
        return this.get("PLAN_MODE");
    }

    public void setPlanMode(String planMode){
        this.set("PLAN_MODE", planMode);
    }

    public String getPlanExecutorId(){
        return this.get("PLAN_EXECUTOR_ID");
    }

    public void setPlanExecutorId(String planExecutorId){
        this.set("PLAN_EXECUTOR_ID", planExecutorId);
    }
}
