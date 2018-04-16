package com.most.core.app.database.config.loader.impl;

import com.most.core.app.database.config.DatabaseConfig;
import com.most.core.app.database.config.loader.IDBConfigLoader;
import com.most.core.pub.tools.file.XmlTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/2/14 2:32
 * @Description: 解析xml方式的数据库配置类
 */
public class XmlDBConfigLoader implements IDBConfigLoader {

    private static String fileName = "database.xml";
    private static Logger log = LogManager.getLogger(XmlDBConfigLoader.class.getName());

    /**
     * 将配置文件解析成具体的DatabaseConfig数据类
     * @return
     */
    public List<DatabaseConfig> load() {
        Element root = null;
        try {
            root = XmlTool.getRoot(fileName);
        } catch (DocumentException e) {
            log.error("数据库配置文件初始化出错");
        } catch (IOException e) {
            log.error("数据库配置文件初始化出错");
        }
        if(root == null){
            log.error("读取配置文件出错，请检查database.xml的配置");
            return null;
        }

        List<Element> elements = XmlTool.getRootSubNodes(root);
        if(elements == null || elements.size() <= 0){
            log.error("没有读取到一个数据库配置节点，请检查database.xml文件配置");
            return null;
        }

        List<DatabaseConfig> configs = new ArrayList<DatabaseConfig>();
        for(Element element : elements){
            DatabaseConfig config = new DatabaseConfig();
            config.setName(element.attributeValue("name"));
            config.setType(element.attributeValue("type"));
            config.setDatabaseType(element.attributeValue("databaseType"));
            config.setDriver(element.attributeValue("driver"));
            config.setUrl(element.attributeValue("url"));
            config.setUserName(element.attributeValue("userName"));
            config.setPassword(element.attributeValue("password"));
            config.setInitialSize(element.attributeValue("initialSize"));
            config.setMaxActive(element.attributeValue("maxActive"));
            config.setMaxIdle(element.attributeValue("maxIdle"));
            config.setMaxWait(element.attributeValue("maxWait"));
            configs.add(config);
            if(log.isDebugEnabled()){
                log.debug("已加载数据库"+config.getName()+"的配置，配置清单为"+config);
            }
        }

        return configs;
    }
}
