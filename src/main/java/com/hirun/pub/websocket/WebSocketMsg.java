package com.hirun.pub.websocket;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by pc on 2018-06-09.
 */
public class WebSocketMsg {

    private String userId;
    private String type;
    private String content;

    public WebSocketMsg() {

    }

    public WebSocketMsg(String str) {
        JSONObject jsonObject = JSONObject.parseObject(str);
        this.userId = jsonObject.getString("USER_ID");
        this.type = jsonObject.getString("TYPE");
        this.content = jsonObject.getString("CONTENT");
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("USER_ID", this.userId);
        jsonObject.put("TYPE", this.type);
        jsonObject.put("CONTENT", this.content);
        return jsonObject.toJSONString();
    }
}
