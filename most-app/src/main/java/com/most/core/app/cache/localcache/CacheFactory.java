package com.most.core.app.cache.localcache;

import com.most.core.app.cache.localcache.CacheXml.ReadOnlyCacheItem;
import com.most.core.app.cache.localcache.CacheXml.ReadWriteCacheItem;
import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.cache.localcache.interfaces.IReadWriteCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright: Copyright (c) 2013 Asiainfo-Linkage
 * 
 * @className: CacheFactory
 * @description: 本次缓存工厂类
 * 
 * @version: v1.0.0
 * @author: zhoulin2
 * @date: 2013-2-25
 */
public final class CacheFactory {

	private static final Logger log = LogManager.getLogger(CacheFactory.class);
	
	/**
	 * 本地只读缓存
	 */
	private static Map<Class, IReadOnlyCache> ROCACHES = new HashMap<Class, IReadOnlyCache>();
	
	/**
	 * 本地只读缓存名
	 */
	private static Set<String> ROCACHE_CLAZZNAME = new HashSet<String>();	
	
	/**
	 * 需要被立即初始化的本地缓存名
	 */
	private static Set<String> ROCACHE_NEEDINIT = new HashSet<String>();
	
	/**
	 * 本地读写缓存
	 */
	private static Map<String, IReadWriteCache> RWCACHES = new HashMap<String, IReadWriteCache>();

	
	private static List<CacheXml.ReadOnlyCacheItem> readonlyCacheItems;
	private static List<CacheXml.ReadWriteCacheItem> readwriteCacheItems;
	
	/**
	 * 判断本地缓存是否还未使用过，如果未使用过，就不会被记录在ROCACHES里。
	 * 
	 * @param clazz
	 * @return
	 */
	static boolean isNotUsed(Class clazz) {
		if (ROCACHES.containsKey(clazz)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取本地只读缓存
	 * 
	 * @param clazz: 本地只读缓存类名
	 * @return
	 * @throws Exception
	 */
	public static IReadOnlyCache getReadOnlyCache(Class clazz) throws Exception {
		
		if (!ROCACHE_CLAZZNAME.contains(clazz.getName())) {
			log.error("缓存类在配置文件中未定义!" + clazz.getName());
			return null;
		}
		
		IReadOnlyCache cache = ROCACHES.get(clazz);
		
		if (null == cache) {
			
			/** 加锁初始化本地缓存 */
			synchronized (clazz) {	
				/** 获得锁后，再次判断是否为空，防止重复初始化 */
				if ((cache = ROCACHES.get(clazz)) != null) {
					return cache;
				}
				
				long start = System.currentTimeMillis();
				cache = (IReadOnlyCache) clazz.newInstance();
				cache.setClassName(clazz.getName());
				cache.refresh();
				ROCACHES.put(clazz, cache);
				
				log.info("ReadOnlyCache:" + clazz.getName() + "刷新成功，加载数据量:" + cache.size() + "条，耗时:" + (System.currentTimeMillis() - start) + "毫秒");
			}
		}

		return cache;
	}
	
	/**
	 * 获取本地读写缓存
	 * 
	 * @param cacheName 读写缓存名
	 * @return
	 */
	public static final IReadWriteCache getReadWriteCache(final String cacheName) {
		IReadWriteCache cache = RWCACHES.get(cacheName);
		return cache;
	}
			
	/**
	 * 本地只读缓存配置初始化
	 * 
	 * @param items
	 */
	private static final void initReadOnlyCaches(List<CacheXml.ReadOnlyCacheItem> items) {

		for (ReadOnlyCacheItem item : items) {
			ROCACHE_CLAZZNAME.add(item.className);
			if (item.isInitial) {
				ROCACHE_NEEDINIT.add(item.className);
			}
			
			try {
				
				Class clazz = Class.forName(item.className);
				
				if (null != item.cronExpr) {

					
				}
			} catch (Exception e) {
				log.error("ReadOnlyCache配置加载出错! " + item.className, e);
			}
		}
	}
	
	/**
	 * 本地读写缓存初始化
	 * 
	 * @param items
	 */
	private static final void initReadWriteCaches(List<ReadWriteCacheItem> items) {
		
		for (ReadWriteCacheItem item : items) {
			
			IReadWriteCache cache = new ReadWriteCache(item.maxSize);
			cache.setName(item.name);
			String name = item.name;
			RWCACHES.put(name, cache);
			
			if (null != item.cronExpr) {
				

			}
		}
	}

	/**
	 * 获取只读缓存列表
	 * 
	 * @return
	 */
	public static final List<Map<String, String>> listReadOnlyCache() {
		List<Map<String, String>> rtn = new ArrayList<Map<String, String>>();
		for (ReadOnlyCacheItem item : readonlyCacheItems) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("className", item.className);
			map.put("init", String.valueOf(item.isInitial));
			map.put("cronExpr", item.cronExpr);
			rtn.add(map);
		}
		return rtn;
	}
	
	/**
	 * 获取读写缓存列表
	 * 
	 * @return
	 */
	public static final List<Map<String, String>> listReadWriteCache() {
		List<Map<String, String>> rtn = new ArrayList<Map<String, String>>();
		for (ReadWriteCacheItem item : readwriteCacheItems) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", item.name);
			map.put("maxSize", String.valueOf(item.maxSize));
			map.put("cronExpr", item.cronExpr);
			rtn.add(map);
		}
		return rtn;
	}
	
	/**
	 * 初始化本地缓存
	 * 
	 * @throws Exception
	 */
	public static final void init() {
		
		/** 加锁初始化本地缓存 */
		synchronized (CacheFactory.class) {
			try {
				// 初始化本地只读缓存，进行预加载工作 
				for (String clazzName : ROCACHE_NEEDINIT) {
					long start = System.currentTimeMillis();
					
					Class clazz = Class.forName(clazzName);
					IReadOnlyCache cache = (IReadOnlyCache) clazz.newInstance();
					cache.setClassName(clazzName);
					cache.refresh();
					ROCACHES.put(clazz, cache);
					
					log.info("ReadOnlyCache:" + clazz.getName() + "刷新成功，加载数据量:" + cache.size() + "条，耗时:" + (System.currentTimeMillis() - start) + "毫秒");
				}
			} catch (Exception e) {
				log.error("本地只读缓存初始化时发生错误!", e);
			}
		}
	}
	
	static {
		
		try {
			CacheXml cacheXml = CacheXml.getInstance();
			readonlyCacheItems = cacheXml.getReadOnlyCacheItems();
			readwriteCacheItems = cacheXml.getReadWriteCacheItems();
			initReadOnlyCaches(readonlyCacheItems); // 初始化本地只读缓存
			initReadWriteCaches(readwriteCacheItems); // 初始化本地读写缓存
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
