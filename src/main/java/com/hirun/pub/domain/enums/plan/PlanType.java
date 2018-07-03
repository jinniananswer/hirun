package com.hirun.pub.domain.enums.plan;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by pc on 2018-05-04.
 */
public enum PlanType {

    normalWork("正常上班","1"),
    active("活动","2"),
    holiday("休假","3");

    private String value;
    private String name;

    private PlanType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static String getNameByValue(String value){
        for(PlanType planType : PlanType.values()){
            if(StringUtils.equals(value, planType.getValue())){
                return planType.name;
            }
        }
        return "";
    }
}
