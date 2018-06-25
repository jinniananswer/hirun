package com.hirun.pub.domain.enums.cust;

/**
 * Created by pc on 2018-05-18.
 */
public enum CustStatus {

    normal("正常","1"),
    pause("暂停","7"),
    del("删除","8"),
    toBeFilled("待补录","9");

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
