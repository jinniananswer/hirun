package com.hirun.app.bean.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;

/**
 * @Author jinnian
 * @Date 2018/5/8 22:20
 * @Description:
 */
public class OrgBean {

    public static String getOrgId(SessionEntity sessionEntity){
        JSONArray jobRoles = sessionEntity.getJSONArray("JOB_ROLES");

        if(ArrayTool.isNotEmpty(jobRoles)){
            JSONObject jobRole = jobRoles.getJSONObject(0);
            String orgId = jobRole.getString("ORG_ID");
            return orgId;
        }

        return null;
    }
}
