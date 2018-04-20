package com.most.core.pub.data;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author jinnian
 * @Date 2018/4/17 13:16
 * @Description:
 */
public class Header extends JSONData{

    public Header(){
        this.data = new JSONObject();
    }

    public Header(JSONObject data){
        this.data = new JSONObject();
        this.data.putAll(data);
    }

}
