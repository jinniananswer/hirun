package com.hirun.app.cache;

import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.most.core.app.cache.localcache.AbstractReadOnlyCache;
import com.most.core.app.cache.localcache.CacheFactory;
import com.most.core.app.cache.localcache.ReadWriteCache;
import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.cache.localcache.interfaces.IReadWriteCache;
import com.most.core.app.database.dao.StrongObjectDAO;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by pc on 2018-04-28.
 */
public class EmployeeCache {

    private static IReadWriteCache employeeCache = CacheFactory.getReadWriteCache("EMPLOYEE_NAME_CACHE");

    public static String getEmployeeNameEmployeeId(String employeeId) throws Exception
    {
        if(StringUtils.isBlank(employeeId)) {
            return null;
        }

        if(employeeCache.containsKey(employeeId)) {
            return (String)employeeCache.get(employeeId);
        } else {
            EmployeeEntity employeeEntity = EmployeeBean.getEmployeeByEmployeeId(employeeId);
            if(employeeEntity != null) {
                String employeeName = employeeEntity.getName();
                employeeCache.put(employeeId, employeeName);
                return employeeName;
            } else {
                return null;
            }
        }
    }
}
