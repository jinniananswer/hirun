package com.hirun.pub.domain.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awx on 2018/9/2/002.
 */
public class UnEntryPlanEmployeeVO {

    private String companyName;
    private List<String> employeeNameList = new ArrayList<String>();

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<String> getEmployeeNameList() {
        return employeeNameList;
    }

    public void addEmployeeName(String employeeName) {
        this.employeeNameList.add(employeeName);
    }

    public JSONObject toJSONObeject() {
        JSONObject ret = new JSONObject();

        ret.put("NAME", this.companyName);
        JSONArray employeeList = new JSONArray();
        for(int i = 0, size = employeeNameList.size(); i <size; i++) {
            String employeeName = employeeNameList.get(i);
            JSONObject employeeNameObj = new JSONObject();
            employeeNameObj.put("EMPLOYEE_NAME", employeeName);
            employeeList.add(employeeNameObj);
        }
        ret.put("EMPLOYEE_LIST", employeeList);

        return ret;
    }
}
