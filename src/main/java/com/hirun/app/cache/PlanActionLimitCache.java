package com.hirun.app.cache;

import com.hirun.pub.domain.entity.param.PlanActionLimitEntity;
import com.most.core.app.cache.localcache.AbstractReadOnlyCache;
import com.most.core.app.cache.localcache.CacheFactory;
import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.util.*;

/**
 * Created by pc on 2018-04-28.
 */
public class PlanActionLimitCache extends AbstractReadOnlyCache{

    public Map<String, Object> loadData() throws Exception {
        StrongObjectDAO dao = new StrongObjectDAO("sys");
        Map<String, Object> cacheMap = new HashMap<String, Object>();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put("STATUS", "1");
        List<PlanActionLimitEntity> list = dao.query(PlanActionLimitEntity.class, "SYS_PLAN_ACTION_LIMIT", parameter);
        for(PlanActionLimitEntity planActionLimitEntity : list) {
            cacheMap.put(planActionLimitEntity.getActionCode(), planActionLimitEntity);
        }

        return cacheMap;
    }

    public static PlanActionLimitEntity getPlanActionLimit(String actionCode) throws Exception
    {
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(PlanActionLimitCache.class);

        return (PlanActionLimitEntity) cache.get(actionCode);
    }

    public static List<PlanActionLimitEntity> getPlanActionLimitList() throws Exception
    {
        List<PlanActionLimitEntity> list = new ArrayList<PlanActionLimitEntity>();
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(PlanActionLimitCache.class);

        Iterator iter = cache.keySet().iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            list.add((PlanActionLimitEntity) cache.get(key));
        }

        return list;
    }
}
