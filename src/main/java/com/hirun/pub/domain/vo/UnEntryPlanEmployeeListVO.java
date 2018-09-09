package com.hirun.pub.domain.vo;

import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by awx on 2018/9/2/002.
 */
public class UnEntryPlanEmployeeListVO {

    private Map<String, UnEntryPlanEmployeeVO> unEntryPlanMap = new HashMap<String, UnEntryPlanEmployeeVO>();

    public void addEmployeeName(String companyName, String employeeName) {
        UnEntryPlanEmployeeVO unEntryPlanEmployeeVO;
        if(!unEntryPlanMap.containsKey(companyName)) {
            unEntryPlanEmployeeVO = new UnEntryPlanEmployeeVO();
            unEntryPlanEmployeeVO.setCompanyName(companyName);
            unEntryPlanMap.put(companyName, unEntryPlanEmployeeVO);
        }

        unEntryPlanEmployeeVO = unEntryPlanMap.get(companyName);
        unEntryPlanEmployeeVO.addEmployeeName(employeeName);
    }

    public JSONArray toJSONArray() {
        JSONArray ret = new JSONArray();

        Iterator<String> iter = unEntryPlanMap.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            UnEntryPlanEmployeeVO unEntryPlanEmployeeVO = unEntryPlanMap.get(key);
            ret.add(unEntryPlanEmployeeVO.toJSONObeject());
        }

        return ret;
    }
}
