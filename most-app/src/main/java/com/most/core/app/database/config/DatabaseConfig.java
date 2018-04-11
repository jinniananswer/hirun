package com.most.core.app.database.config;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author jinnian
 * @Date 2018/2/14 1:11
 * @Description: 数据库配置类
 */
public class DatabaseConfig {

    private transient Logger log = LogManager.getLogger(this.getClass().getName());

    /** 数据库名称 */
    private String name;

    private String type;

    private String databaseType;

    private String driver;

    private String url;

    private String userName;

    private String password;

    private String initialSize;

    private String maxActive;

    private String maxIdle;

    private String maxWait;

    private String validQuery;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(String initialSize) {
        this.initialSize = initialSize;
    }

    public String getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(String maxActive) {
        this.maxActive = maxActive;
    }

    public String getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle;
    }

    public String getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(String maxWait) {
        this.maxWait = maxWait;
    }

    public String getValidQuery() {
        return validQuery;
    }

    public void setValidQuery(String validQuery) {
        this.validQuery = validQuery;
    }

    public String toString(){
        StringBuilder desc = new StringBuilder("Database name is " + name + ",");
        desc.append("type is " + type + ",");
        desc.append("database type is" + databaseType + ",");
        desc.append("driver is" + driver + ",");
        desc.append("url is" + url + ",");
        desc.append("user name is" + userName + ",");
        desc.append("password is" + password + ",");
        desc.append("initial size is" + initialSize + ",");
        desc.append("max active is" + maxActive + ",");
        desc.append("max idle is" + maxIdle + ",");
        desc.append("max wait is" + maxWait + ",");
        desc.append("valid query sql is " + validQuery + ",");
        return desc.toString();
    }

    public void export(){
        if(log.isDebugEnabled()){
            log.debug("database config detail : "+this.toString());
        }
    }
}
