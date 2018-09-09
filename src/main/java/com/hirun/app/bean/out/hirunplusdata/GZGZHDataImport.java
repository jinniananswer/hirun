package com.hirun.app.bean.out.hirunplusdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.out.OutBean;
import com.hirun.app.dao.out.DataGetInfoDAO;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.tools.time.TimeTool;
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
public class GZGZHDataImport {

    private static String host = "www.hi-run.net";
    private static String path = "/api/subscribe";
    private static String pageSize = "100";

    public static void dataImport(String start, String end) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        List<Map<String, String>> dbList = new ArrayList<Map<String, String>>();

        long total = getTotal(httpclient, start, end);

        int totalPageSize = 0;
        if(total % 100 == 0) {
            totalPageSize = (int)total/100;
        } else {
            totalPageSize = (int)total/100 + 1;
        }
        JSONArray jsonDataList = getData(httpclient, totalPageSize, start, end);
        GenericDAO dao = new GenericDAO("out");
        for(int i = 0, size = jsonDataList.size(); i < size; i++) {
            Map<String, String> dbParam = new HashMap<String, String>();
            JSONObject jsonData = jsonDataList.getJSONObject(i);
            if(OutBean.isExistData4Reg(jsonData.getString("subscribe_time"), jsonData.getString("openid"))) {
                //如果已经存在该数据了，则过滤掉
                continue;
            }

            dbParam.put("PROJECT_ID", jsonData.getString("project_id"));
            dbParam.put("UID", jsonData.getString("uid"));
            dbParam.put("NICKNAME", jsonData.getString("nickname"));
            dbParam.put("HEADIMGURL", jsonData.getString("headimgurl"));
            dbParam.put("SUBSCRIBE_TIME", jsonData.getString("subscribe_time"));
            dbParam.put("SUBSCRIBE_CHECK", jsonData.getString("subscribe_check"));
            dbParam.put("SUBSCRIBE", jsonData.getString("subscribe"));
            dbParam.put("IS_REAL", jsonData.getString("is_real"));
            dbParam.put("LAST_AID", jsonData.getString("last_aid"));
            dbParam.put("LAST_EDIT", jsonData.getString("last_edit"));
            dbParam.put("EDIT_UID", jsonData.getString("edit_uid"));
            dbParam.put("EDIT_TIME", jsonData.getString("edit_time"));
            dbParam.put("LAST_UPDATE", jsonData.getString("last_update"));
            dbParam.put("OPENID", jsonData.getString("openid"));
            dbParam.put("INDB_TIME", end);
            dbParam.put("DEAL_TAG", "0");

            dbList.add(dbParam);
        }

        dao.insertBatch("out_hirunplus_reg",dbList);

        String api = "http://" + host + path;
        JSONObject reqestData = new JSONObject();
        reqestData.put("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)));
        reqestData.put("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)));
        OutBean.insertDataGetInfo("http://" + host + path,reqestData.toJSONString(),end, total);
    }

    public static long getTotal(CloseableHttpClient httpclient, String start, String end) throws Exception{
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPath(path)
                .setParameter("PageNo", "1")
                .setParameter("PageSize",pageSize)
                .setParameter("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)))
                .setParameter("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)))
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

    public static JSONArray getData(CloseableHttpClient httpclient, int totalPageSize, String start, String end) throws Exception{
        JSONArray jsonDataList = new JSONArray();
        for(int pageNo = 1; pageNo <= totalPageSize; pageNo++) {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPath(path)
                    .setParameter("PageNo", String.valueOf(pageNo))
                    .setParameter("PageSize",pageSize)
                    .setParameter("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)))
                    .setParameter("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)))
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
