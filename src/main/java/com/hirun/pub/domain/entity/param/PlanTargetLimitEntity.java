package com.hirun.pub.domain.entity.param;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
public class PlanTargetLimitEntity extends GenericEntity{

    public PlanTargetLimitEntity(Map<String, String> data){
        super(data);
    }

    public PlanTargetLimitEntity(){
        super();
    }

    public String getTargetCode(){
        return this.get("TARGET_CODE");
    }

    public void setTargetCode(String targetCode){
        this.set("TARGET_CODE", targetCode);
    }

    public String getTimeInterval(){
        return this.get("TIME_INTERVAL");
    }

    public void setTimeInterval(String timeInterval){
        this.set("TIME_INTERVAL", timeInterval);
    }

    public String getUnit(){
        return this.get("UNIT");
    }

    public void setUnit(String unit){
        this.set("UNIT", unit);
    }

    public String getLimitNum(){
        return this.get("LIMIT_NUM");
    }

    public void setLimitNum(String limitNum){
        this.set("LIMIT_NUM", limitNum);
    }
}
