package com.most.core.pub.exception;

/**
 * @Author jinnian
 * @Date 2018/5/18 19:40
 * @Description:
 */
public class GenericException extends RuntimeException {

    protected String code;

    protected String desc;

    public GenericException(String code, String desc){
        super(code + ":" + desc);
        this.code = code;
        this.desc = desc;
    }

    public GenericException(String code, String desc, Throwable e){
        super(code + ":" + desc, e);
        this.code = code;
        this.desc = desc;
    }

}
