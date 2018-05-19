package com.hirun.pub.domain.enums.plan;

/**
 * Created by pc on 2018-05-04.
 */
public enum ActionStatus {

    innerPlan("计划中","0"),
    outerPlan("计划外","1");

    private String value;
    private String name;

    private ActionStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
