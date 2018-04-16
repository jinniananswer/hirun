package com.most.core.pub.tools.file;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/4/16 21:49
 * @Description:
 */
public class XmlTool {

    public static Element getRoot(String fileName) throws FileNotFoundException,DocumentException,IOException {
        InputStream in = null;
        try {
            in = XmlTool.class.getClassLoader().getResourceAsStream(fileName);
            if (in == null) {
                throw new FileNotFoundException(fileName);
            }
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            Element root = doc.getRootElement();
            return root;
        }
        finally {
            if(in != null){
                in.close();
            }
        }
    }

    /**
     * 将根节点下所有的子节点返回
     * @param root
     * @return
     */
    public static List<Element> getRootSubNodes(Element root){
        if(root != null){
            return root.selectNodes("*");
        }
        return null;
    }
}
