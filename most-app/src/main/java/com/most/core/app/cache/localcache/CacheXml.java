package com.most.core.app.cache.localcache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Copyright: Copyright (c) 2013 Asiainfo-Linkage
 * 
 * @className: CacheXml
 * @description: Cache配置文件解析类。
 * 
 * @version: v1.0.0
 * @author: zhoulin2
 * @date: 2013-2-25
 */
final class CacheXml {

	protected Logger log = LogManager.getLogger(this.getClass().getName());
	private static final String CACHE_FILENAME = "localcache.xml";

	private static CacheXml instance = new CacheXml();
	private static Element root;
	private static Document document;

	private CacheXml() {
		SAXReader reader = new SAXReader();
		InputStream ins = null;

		try {
			ins = CacheFactory.class.getClassLoader().getResourceAsStream(CACHE_FILENAME);
			if (null == ins) {
				throw new FileNotFoundException(CACHE_FILENAME);
			}

			document = reader.read(ins);
			root = document.getRootElement();
		} catch (Exception e) {
			log.error("本地缓存配置解析错误!", e);
		} finally {
			if (null != ins) {
				try {
					ins.close();
				} catch (IOException e) {
					log.error("关闭本地缓存配置文件句柄错误!", e);
				}
			}
		}
	}

	public static final CacheXml getInstance() {
		return instance;
	}

	private List getList(Element from, String propPath) {
		Element element = from;
		String[] nodes = propPath.split("/");
		for (int i = 0; i < nodes.length - 1; i++) {
			element = element.element(nodes[i]);
		}
		if (null != element) {
			return element.elements();
		}
		return new ArrayList();
	}

	public List<ReadOnlyCacheItem> getReadOnlyCacheItems() {
		List<ReadOnlyCacheItem> rtn = new ArrayList<ReadOnlyCacheItem>();

		Iterator iter = getList(root, "readonly/cache").iterator();
		while (iter.hasNext()) {
			Element elem = (Element) iter.next();
			String className = elem.attributeValue("className");
			String init = elem.attributeValue("init");
			String cronExpr = elem.attributeValue("cronExpr");

			if(init == null || "".equals(init))  //默认不初始化 , 适配jdom老版本 ,不使用 getAttributeValue(name, def)方法    //xiedx 2015/9/15
				init = "true";

			if(cronExpr == null)   //默认不定时清空缓存
				cronExpr = "";

			ReadOnlyCacheItem item = new ReadOnlyCacheItem(className, init, cronExpr);
			rtn.add(item);
		}

		return rtn;
	}

	public List<ReadWriteCacheItem> getReadWriteCacheItems() {
		List<ReadWriteCacheItem> rtn = new ArrayList<ReadWriteCacheItem>();
		Iterator iter = getList(root, "readwrite/cache").iterator();
		while (iter.hasNext()) {
			Element elem = (Element) iter.next();
			String name = elem.attributeValue("name");
			String maxSize = elem.attributeValue("maxSize");
			String cronExpr = elem.attributeValue("cronExpr");

			if(maxSize == null || "".equals(maxSize)) //最大缓存记录数
				maxSize = "2000";

			if(cronExpr == null)  //默认不定时清空缓存
				cronExpr = "";
			
			ReadWriteCacheItem item = new ReadWriteCacheItem(name, Integer.parseInt(maxSize), cronExpr);
			rtn.add(item);
		}

		return rtn;
	}
	
	public class ReadWriteCacheItem {
		public String name;
		public int maxSize;
		public String cronExpr;
		
		public ReadWriteCacheItem(String name, int maxSize, String cronExpr) {
			if (null == name) {
				throw new IllegalArgumentException("读写缓存配置错误：name不可为空！");
			}
			
			if (maxSize < 0) {
				throw new IllegalArgumentException("读写缓存配置错误: maxSize < 0");
			}
			
			if (null == cronExpr) {
				throw new IllegalArgumentException("读写缓存配置错误：cronExpr不可为空！");
			}
			
			this.cronExpr = null;
			if (!"".equals(cronExpr)) {
				String[] items = cronExpr.split(" ");
				if (5 != items.length) {
					throw new IllegalArgumentException("读写缓存配置错误：cronExpr 只可配置：分    小时    日    月    周");
				}
				this.cronExpr = "0 " + cronExpr;
			}
	
			this.name = name;
			this.maxSize = maxSize;
		
		}
	}
	
	public class ReadOnlyCacheItem {
		// 缓存类
		public String className;
		
		// 是否初始化系统时进行缓存加载 
		public boolean isInitial;
		
		// 缓存自动刷新cron表达式
		public String cronExpr;
		
		public ReadOnlyCacheItem(String className, String init, String cronExpr) {
			if (null == className) {
				throw new IllegalArgumentException("只读缓存配置错误：className不可为空!");
			}

			if ((!"true".equals(init)) && (!"false".equals(init))) {
				throw new IllegalArgumentException("只读缓存配置错误：init参数只能为true或false: " + className);
			}
			
			this.cronExpr = null;
			if (!"".equals(cronExpr)) {
				String[] items = cronExpr.split(" ");
				if (5 != items.length) {
					throw new IllegalArgumentException("读写缓存配置错误：cronExpr 只可配置：分    小时    日    月    周");
				}
				this.cronExpr = "0 " + cronExpr;
			}
			
			this.className = className;
			this.isInitial = Boolean.parseBoolean(init);
		}
	}

	public static void main(String[] args) {
		CacheXml xml = new CacheXml();
		Document document = CacheXml.document;
		List<ReadOnlyCacheItem> list1 = CacheXml.getInstance().getReadOnlyCacheItems();
		List<ReadWriteCacheItem> list2 = CacheXml.getInstance().getReadWriteCacheItems();
		System.out.println("------------------");
	}
}