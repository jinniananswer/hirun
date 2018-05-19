package com.hirun.pub.domain.enums.plan;

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
}
