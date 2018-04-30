package com.hirun.pub.domain.entity.session;

import com.alibaba.fastjson.JSONArray;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;

/**
 * @Author jinnian
 * @Date 2018/4/30 15:47
 * @Description:
 */
public class BizSessionEntity extends SessionEntity {

    public String getEmployeeId() {
        return this.data.getString("EMPLOYEE_ID");
    }

    public void setEmployeeId(String employeeId) {
        this.data.put("EMPLOYEE_ID", employeeId);
    }

    public JSONArray getJobRoles() {
        return this.data.getJSONArray("EMPLOYEE_JOB_ROLES");
    }

    public void setJobRoles(JSONArray jobRoles){
        if(ArrayTool.isEmpty(jobRoles))
            return;

        this.data.put("EMPLOYEE_JOB_ROLES", jobRoles);
    }
}
