package com.hirun.app.cache;

import com.hirun.pub.domain.entity.common.PerformDueTaskDefEntity;
import com.most.core.app.cache.localcache.AbstractReadOnlyCache;
import com.most.core.app.cache.localcache.CacheFactory;
import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.util.*;

public class PerformDueTaskDefCache extends AbstractReadOnlyCache{

	public Map<String, Object> loadData() throws Exception {
		StrongObjectDAO dao = new StrongObjectDAO("sys");
		Map<String, Object> cacheMap = new HashMap<String, Object>();
		Map<String, String> parameter = new HashMap<String, String>();

		parameter.put("STATUS", "1");
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM SYS_PERFORM_DUE_TASK_DEF ");
		sql.append(" WHERE STATUS = :STATUS ");
		List<PerformDueTaskDefEntity> list = dao.queryBySql(PerformDueTaskDefEntity.class, sql.toString(), parameter);
		for(PerformDueTaskDefEntity entity : list) {
			cacheMap.put(entity.getTaskType(), entity);
		}

		return cacheMap;
	}

	public static String getServiceNameByTaskType(String taskType) throws Exception
	{
		String serviceName = null;
		IReadOnlyCache cache = CacheFactory.getReadOnlyCache(PerformDueTaskDefCache.class);

		PerformDueTaskDefEntity entity = (PerformDueTaskDefEntity) cache.get(taskType);
		if(entity != null) {
			serviceName = entity.getServiceName();
		}

		return serviceName;
	}
}