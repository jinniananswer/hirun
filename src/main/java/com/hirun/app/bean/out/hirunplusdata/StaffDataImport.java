package com.hirun.app.bean.out.hirunplusdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.most.core.app.cache.localcache.CacheFactory;
import com.most.core.app.database.dao.GenericDAO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-05-21.
 */
public class StaffDataImport {

    private static String host = "www.hi-run.net";
    private static String path = "/api/staffs";
    private static String pageSize = "100";

    public static void dataImport() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        List<Map<String, String>> dbList = new ArrayList<Map<String, String>>();

        long total = getTotal(httpclient);

        int totalPageSize = 0;
        if(total % 100 == 0) {
            totalPageSize = (int)total/100;
        } else {
            totalPageSize = (int)total/100 + 1;
        }
        JSONArray jsonDataList = getData(httpclient, totalPageSize);
        GenericDAO dao = new GenericDAO("out");
        //先删
        StringBuilder delSql = new StringBuilder();
        delSql.append("DELETE FROM out_hirunplus_staff");
        dao.executeUpdate(delSql.toString(), new HashMap());

        for(int i = 0, size = jsonDataList.size(); i < size; i++) {
            Map<String, String> dbParam = new HashMap<String, String>();
            JSONObject jsonData = jsonDataList.getJSONObject(i);

            dbParam.put("STAFF_ID", jsonData.getString("staff_id"));
            dbParam.put("UID", jsonData.getString("uid"));
            dbParam.put("REALNAME", jsonData.getString("realname"));
            dbParam.put("MOBILE", jsonData.getString("mobile"));
            dbParam.put("ROLE_ID", jsonData.getString("role_id"));
            dbParam.put("CID", jsonData.getString("cid"));
            dbParam.put("AVATAR_DOCID", jsonData.getString("avatar_docid"));
            dbParam.put("U_LEVEL", jsonData.getString("u_level"));
            dbParam.put("U_POST", jsonData.getString("u_post"));
            dbParam.put("U_TITLE", jsonData.getString("u_title"));
            dbParam.put("STAT", jsonData.getString("stat"));
            dbParam.put("CREATE_AID", jsonData.getString("create_aid"));
            dbParam.put("CREATE_TIME", jsonData.getString("create_time"));
            dbParam.put("LAST_AID", jsonData.getString("last_aid"));
            dbParam.put("LAST_EDIT", jsonData.getString("last_edit"));

            dbList.add(dbParam);
        }

        dao.insertBatch("out_hirunplus_staff",dbList);

        CacheFactory.getReadOnlyCache(HirunPlusStaffDataCache.class).refresh();
    }

    public static long getTotal(CloseableHttpClient httpclient) throws Exception{
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPath(path)
                .setParameter("PageNo", "1")
                .setParameter("PageSize",pageSize)
                .build();
        HttpGet httpGet = new HttpGet(uri);
        long total = 0;
        CloseableHttpResponse response = null;
        try{
            //调接口获取数据
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            JSONObject jsonResponse = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"));
            total = jsonResponse.getJSONObject("page").getLong("total");//总数
            EntityUtils.consume(entity);
        } catch(Exception e) {
            throw e;
        } finally {
            if(response != null) {
                try{
                    response.close();
                } catch(Exception e) {

                }
            }
        }


        return total;
    }

    public static JSONArray getData(CloseableHttpClient httpclient, int totalPageSize) throws Exception{
        JSONArray jsonDataList = new JSONArray();
        for(int pageNo = 1; pageNo <= totalPageSize; pageNo++) {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPath(path)
                    .setParameter("PageNo", String.valueOf(pageNo))
                    .setParameter("PageSize",pageSize)
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response = null;
            //调接口获取数据
            try{
                response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                JSONObject jsonResponse = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"));
                jsonDataList.addAll(jsonResponse.getJSONArray("data"));
                EntityUtils.consume(entity);
            } catch(Exception e) {
                throw e;
            } finally {
                if(response != null) {
                    try{
                        response.close();
                    } catch(Exception e) {

                    }
                }
            }
        }

        return jsonDataList;
    }
}
