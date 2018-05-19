package com.hirun.pub.domain.enums.cust;

/**
 * Created by pc on 2018-05-18.
 */
public enum CustStatus {

    normal("正常","1"),
    toBeFilled("待补录","1");

    private String value;
    private String name;

    private CustStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
