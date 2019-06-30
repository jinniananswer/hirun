package com.hirun.app.cache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.most.core.app.cache.localcache.AbstractReadOnlyCache;
import com.most.core.app.cache.localcache.CacheFactory;
import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by pc on 2018-04-28.
 */
public class HirunPlusStaffDataCache extends AbstractReadOnlyCache{

    public Map<String, Object> loadData() throws Exception {
        GenericDAO dao = new GenericDAO("out");
        Map<String, Object> cacheMap = new HashMap<String, Object>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM out_hirunplus_staff ");
        JSONArray jsonStaffList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));
        for(int i = 0, size = jsonStaffList.size(); i < size; i++) {
            JSONObject jsonStaff = jsonStaffList.getJSONObject(i);
            String staffId = jsonStaff.getString("STAFF_ID");
            String mobile = jsonStaff.getString("MOBILE");
            cacheMap.put(staffId, mobile);
        }

        return cacheMap;
    }

    public static String getMobileByStaffId(String staffId) throws Exception
    {
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(HirunPlusStaffDataCache.class);

        return (String) cache.get(staffId);
    }

    public static String getStaffIdByMobile(String mobile) throws Exception
    {
        IReadOnlyCache cache = CacheFactory.getReadOnlyCache(HirunPlusStaffDataCache.class);
        Set<String> set=cache.keySet();
        String staffId="";
        for(String key : set){
            String mobile1=getMobileByStaffId(key);
            if(StringUtils.equals(mobile1,mobile)){
                staffId=key;
            }
        }
        return staffId;
    }
}
