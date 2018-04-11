package com.most.core.pub.tools.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author jinnian
 * @Date 2018/2/20 23:03
 * @Description: 解析Properties文件的工具类
 */
public class PropertiesTool {

    private static Logger log = LogManager.getLogger(PropertiesTool.class.getName());

    public static Properties getProperties(InputStream in){
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            log.error(e);
        }

        return properties;
    }

    public static Properties getProperties(String fileName){
        InputStream in = PropertiesTool.class.getClassLoader().getResourceAsStream(fileName);
        return getProperties(in);
    }

    public static Properties getProperties(File file){
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error(e);
        }

        if(in != null)
            return getProperties(in);
        else
            return null;
    }

    public static Map<String, String> getProperties(Properties properties){
        Map<String, String> result = new HashMap<String, String>();
        Enumeration<Object> keys = properties.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement().toString();
            String value = getPropertiesValue(properties, key);
            result.put(key, value);
        }

        return result;
    }

    public static String getPropertiesValue(Properties properties, String key){
        String value = properties.getProperty(key);
        try {
            return new String(value.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }

        return null;
    }

    public static String getPropertiesValue(Properties properties, String key, String defaultValue){
        String value = getPropertiesValue(properties, key);
        if(StringUtils.isBlank(value))
            return defaultValue;
        else
            return value;

    }
}
