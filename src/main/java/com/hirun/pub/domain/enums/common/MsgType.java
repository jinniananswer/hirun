package com.hirun.pub.domain.enums.common;

/**
 * Created by pc on 2018-06-09.
 */
public enum MsgType {

    sys("系统消息","1");

    private String value;
    private String name;

    private MsgType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
