package com.most.core.pub.data.interfaces;

/**
 * @Author jinnian
 * @Date 2018/4/16 15:03
 * @Description: 注入到会话中的用户相关信息，可以由业务侧实现
 */
public interface IVisitor {
    public String get(String key);
    public void put(String key, String value);
    public String getUsername();
    public String getUserId();
}
