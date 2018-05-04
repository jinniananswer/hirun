package com.hirun.app.cache;

import com.hirun.pub.domain.entity.param.ActionEntity;
import com.most.core.app.cache.localcache.AbstractReadOnlyCache;
import com.most.core.app.cache.localcache.CacheFactory;
import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.util.*;

/**
 * Created by pc on 2018-04-28.
 */
public class ActionCache extends AbstractReadOnlyCache{

    public Map<String, Object> loadData() throws Exception {
        StrongObjectDAO dao = new StrongObjectDAO("sys");
        Map<String, Object> cacheMap = new HashMap<String, Object>();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put("STATUS", "1");
        List<ActionEntity> list = dao.query(ActionEntity.class, "SYS_ACTION", parameter);
        for(ActionEntity actionEntity : list) {
            cacheMap.put(actionEntity.getActionCode(), actionEntity);
        }

        return cacheMap;
    }

    public static ActionEntity getAction(String actionCode) throws Exception
    {
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(ActionCache.class);

        return (ActionEntity) cache.get(actionCode);
    }

    public static List<ActionEntity> getActionList() throws Exception
    {
        List<ActionEntity> list = new ArrayList<ActionEntity>();
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(ActionCache.class);

        Iterator iter = cache.keySet().iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            list.add((ActionEntity) cache.get(key));
        }

        return list;
    }

    public static List<ActionEntity> getActionListByType(String actionType) throws Exception
    {
        List<ActionEntity> list = new ArrayList<ActionEntity>();
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(ActionCache.class);

        Iterator iter = cache.keySet().iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            ActionEntity entity = (ActionEntity) cache.get(key);
            if("actionType".equals(entity.getActionType())) {
                list.add(entity);
            }
        }

        return list;
    }
}
