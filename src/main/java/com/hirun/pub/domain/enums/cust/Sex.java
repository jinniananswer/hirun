package com.hirun.pub.domain.enums.cust;

/**
 * Created by pc on 2018-05-18.
 */
public enum Sex {

    man("男","1"),
    woman("女","2");

    private String value;
    private String name;

    private Sex(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static String getNameByValue(String value) {
        for(Sex sex : Sex.values()) {
            if(sex.getValue().equals(value)) {
                return sex.name;
            }
        }
        return "未知";
    }
}
