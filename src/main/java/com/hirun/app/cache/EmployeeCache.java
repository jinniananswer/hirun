package com.hirun.app.cache;

import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.most.core.app.cache.localcache.AbstractReadOnlyCache;
import com.most.core.app.cache.localcache.CacheFactory;
import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.util.*;

/**
 * Created by pc on 2018-04-28.
 */
public class EmployeeCache extends AbstractReadOnlyCache{

    public Map<String, Object> loadData() throws Exception {
        StrongObjectDAO dao = new StrongObjectDAO("ins");
        Map<String, Object> cacheMap = new HashMap<String, Object>();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put("STATUS", "0");
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT EMPLOYEE_ID,USER_ID,NAME FROM INS_EMPLOYEE ");
        sql.append(" WHERE STATUS = :STATUS ");
        List<EmployeeEntity> list = dao.queryBySql(EmployeeEntity.class, sql.toString(), parameter);
        for(EmployeeEntity employeeEntity : list) {
            cacheMap.put(employeeEntity.getEmployeeId(), employeeEntity);
        }

        return cacheMap;
    }

    public static EmployeeEntity getEmployeeEntityByEmployeeId(String employeeId) throws Exception
    {
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(EmployeeCache.class);

        return (EmployeeEntity) cache.get(employeeId);
    }
}
