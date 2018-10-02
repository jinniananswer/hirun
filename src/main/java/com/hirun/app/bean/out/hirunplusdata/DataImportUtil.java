package com.hirun.app.bean.out.hirunplusdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

/**
 * Created by awx on 2018/10/2/002.
 */
public class DataImportUtil {

    private static String pageSize = "100";
    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static JSONArray getDataByApi(String host, String path, String start, String end) throws Exception{

        long total = getTotal(httpclient, host, path, pageSize, start, end);
        int totalPageSize = 0;
        if(total % 100 == 0) {
            totalPageSize = (int)total/100;
        } else {
            totalPageSize = (int)total/100 + 1;
        }

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

    private static long getTotal(CloseableHttpClient httpclient, String host, String path, String pageSize, String start, String end) throws Exception{
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
}
