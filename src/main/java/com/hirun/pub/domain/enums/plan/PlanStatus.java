package com.hirun.pub.domain.enums.plan;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by pc on 2018-05-04.
 */
public enum PlanStatus {

    createStatus("新创建","0"),
    audited("已审核","1"),
    summarized("已总结","2");

    private String value;
    private String name;

    private PlanStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static String getNameByValue(String value){
        for(PlanStatus planStatus : PlanStatus.values()){
            if(StringUtils.equals(value, planStatus.getValue())){
                return planStatus.name;
            }
        }
        return "";
    }
}
