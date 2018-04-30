package com.hirun.app.cache;

import com.hirun.pub.domain.entity.param.PlanUnfinishCauseEntity;
import com.most.core.app.cache.localcache.AbstractReadOnlyCache;
import com.most.core.app.cache.localcache.CacheFactory;
import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.util.*;

/**
 * Created by pc on 2018-04-30.
 */
public class PlanUnFinishCauseCache extends AbstractReadOnlyCache{

    @Override
    public Map<String, Object> loadData() throws Exception {
        StrongObjectDAO dao = new StrongObjectDAO("sys");
        Map<String, Object> cacheMap = new HashMap<String, Object>();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put("STATUS", "1");
        List<PlanUnfinishCauseEntity> list = dao.query(PlanUnfinishCauseEntity.class, "SYS_PLAN_UNFINISH_CAUSE", parameter);
        for(PlanUnfinishCauseEntity planUnfinishCauseEntity : list) {
            cacheMap.put(planUnfinishCauseEntity.getCauseId(), planUnfinishCauseEntity);
        }

        return cacheMap;
    }

    public static List<PlanUnfinishCauseEntity> getCauseListByActionCode(String actionCode) throws Exception{
        List<PlanUnfinishCauseEntity> list = new ArrayList<PlanUnfinishCauseEntity>();

        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(PlanUnFinishCauseCache.class);

        Iterator iter = cache.keySet().iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            PlanUnfinishCauseEntity entity = (PlanUnfinishCauseEntity)cache.get(key);
            if(actionCode.equals(entity.getActionCode())) {
                list.add(entity);
            }
        }

        return list;
    }
}
