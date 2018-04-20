package com.most.core.app.service.config.loader.impl;

import com.most.core.app.service.config.ServiceConfig;
import com.most.core.app.service.config.loader.IServiceConfigLoader;
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
 * @Date 2018/4/16 21:44
 * @Description:
 */
public class XmlServiceConfigLoader implements IServiceConfigLoader{

    private static String fileName = "services.xml";
    private static Logger log = LogManager.getLogger(XmlServiceConfigLoader.class.getName());

    @Override
    public List<ServiceConfig> load() {
        Element root = null;
        try {
            root = XmlTool.getRoot(fileName);
        } catch (DocumentException e) {
            log.error("服务配置文件初始化出错");
        } catch (IOException e) {
            log.error("服务配置文件初始化出错");
        }
        if(root == null){
            log.error("读取配置文件出错，请检查services.xml的配置");
            return null;
        }

        List<Element> elements = XmlTool.getRootSubNodes(root);
        if(elements == null || elements.size() <= 0){
            log.error("没有读取到一个服务配置节点，请检查services.xml文件配置");
            return null;
        }

        List<ServiceConfig> configs = new ArrayList<ServiceConfig>();
        for(Element element : elements){
            ServiceConfig config = new ServiceConfig();
            config.setServiceName(element.attributeValue("name"));
            config.setClassPath(element.attributeValue("class"));
            config.setMethodName(element.attributeValue("method"));
            config.setDesc(element.attributeValue("desc"));
            configs.add(config);
            if(log.isDebugEnabled()){
                log.debug("已加载服务"+config.getServiceName()+"的配置，服务名为"+config.getServiceName());
            }
        }

        return configs;
    }

}
