package com.most.core.pub.exception;

/**
 * @Author jinnian
 * @Date 2018/5/18 19:40
 * @Description:
 */
public class GenericException extends RuntimeException {

    protected String code;

    protected String desc;

    protected String userId;

    public GenericException(String code, String desc){
        super(code + ":" + desc);
        this.code = code;
        this.desc = desc;
    }

    public GenericException(String userId, String code, String desc){
        super("用户{" + userId + "}执行错误\n" + code + ":" + desc);
        this.code = code;
        this.desc = desc;
        this.userId = userId;
    }

    public GenericException(String code, String desc, Throwable e){
        super(code + ":" + desc, e);
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
