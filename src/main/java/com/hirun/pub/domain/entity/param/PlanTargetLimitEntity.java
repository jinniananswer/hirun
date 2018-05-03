package com.hirun.pub.domain.entity.param;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * Created by pc on 2018-04-28.
 */
public class PlanTargetLimitEntity extends GenericEntity{

    public PlanTargetLimitEntity(){
        super();
    }

    public PlanTargetLimitEntity(Map<String, String> data){
        super(data);
    }

    public String getTargetLimitId(){
        return this.get("TARGET_LIMIT_ID");
    }

    public void setTargetLimitId(String targetLimitId){
        this.set("TARGET_LIMIT_ID", targetLimitId);
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
