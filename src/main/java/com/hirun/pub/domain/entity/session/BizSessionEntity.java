package com.hirun.pub.domain.entity.session;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;

/**
 * @Author jinnian
 * @Date 2018/4/30 15:47
 * @Description:
 */
public class BizSessionEntity {

    private SessionEntity session;

    public BizSessionEntity(SessionEntity session){
        this.session = session;
    }

    public String getEmployeeId() {
        return this.session.get("EMPLOYEE_ID");
    }


    public String getJobRole(){
        JSONArray jobRoles = this.session.getJSONArray("JOB_ROLES");
        if(ArrayTool.isEmpty(jobRoles))
            return null;

        JSONObject jobRole = jobRoles.getJSONObject(0);
        return jobRole.getString("JOB_ROLE");
    }

    public String getEmployeeName(){
        return this.session.get("EMPLOYEE_NAME");
    }

    public String getUserId(){
        return this.session.getUserId();
    }

    public String getUsername(){
        return this.session.getUsername();
    }

    public String getEmployeeSex(){
        JSONObject employee = this.session.getJSONObject("EMPLOYEE");
        return employee.getString("SEX");
    }

}
