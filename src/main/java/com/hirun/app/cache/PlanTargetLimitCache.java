package com.hirun.app.cache;

import com.hirun.pub.domain.entity.param.PlanTargetLimitEntity;
import com.most.core.app.cache.localcache.AbstractReadOnlyCache;
import com.most.core.app.cache.localcache.CacheFactory;
import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.util.*;

/**
 * Created by pc on 2018-04-28.
 */
public class PlanTargetLimitCache extends AbstractReadOnlyCache{

    public Map<String, Object> loadData() throws Exception {
        StrongObjectDAO dao = new StrongObjectDAO("sys");
        Map<String, Object> cacheMap = new HashMap<String, Object>();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put("STATUS", "1");
        List<PlanTargetLimitEntity> list = dao.query(PlanTargetLimitEntity.class, "SYS_PLAN_TARGET_LIMIT", parameter);
        for(PlanTargetLimitEntity planTargetLimitEntity : list) {
            cacheMap.put(planTargetLimitEntity.getTargetCode(), planTargetLimitEntity);
        }

        return cacheMap;
    }

    public static PlanTargetLimitEntity getPlanTargetLimit(String targetCode) throws Exception
    {
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(PlanTargetLimitCache.class);

        return (PlanTargetLimitEntity) cache.get(targetCode);
    }

    public static List<PlanTargetLimitEntity> getPlanTargetLimitList() throws Exception
    {
        List<PlanTargetLimitEntity> list = new ArrayList<PlanTargetLimitEntity>();
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(PlanTargetLimitCache.class);

        Iterator iter = cache.keySet().iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            list.add((PlanTargetLimitEntity) cache.get(key));
        }

        return list;
    }
}
