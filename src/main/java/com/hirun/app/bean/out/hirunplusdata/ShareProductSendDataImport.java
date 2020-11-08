package com.hirun.app.bean.out.hirunplusdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.out.OutBean;
import com.most.core.app.database.dao.GenericDAO;
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
 * @author 推送数据采集
 */
public class ShareProductSendDataImport {

    private static String host = "www.hi-run.net";
    private static String path = "/api/midprosendlogs";
    private static String pageSize = "100";

    public static void dataImport(String start, String end) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();


        long total = getTotal(httpclient, start, end);


        int totalPageSize = 0;
        if (total % 100 == 0) {
            totalPageSize = (int) total / 100;
        } else {
            totalPageSize = (int) total / 100 + 1;
        }

        JSONArray jsonDataList = getData(httpclient, totalPageSize, start, end);


        GenericDAO dao = new GenericDAO("out");
        List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> hisarrayList = new ArrayList<Map<String, String>>();

        for (int i = 0, size = jsonDataList.size(); i < size; i++) {
            Map<String, String> dbParam = new HashMap<String, String>();
            JSONObject jsonData = jsonDataList.getJSONObject(i);

            String staffId = jsonData.getString("staff_id");

            if (OutBean.isExistData4ProdSend(staffId, jsonData.getString("create_time"))) {
                //如果已经存在该数据了，则直接插历史表
                dbParam.put("UID", jsonData.getString("uid"));
                dbParam.put("MODE_ID", jsonData.getString("mode_id"));
                dbParam.put("STAFF_ID", jsonData.getString("staff_id"));
                dbParam.put("SHARE_UID", jsonData.getString("share_uid"));
                dbParam.put("STAGE", "");
                dbParam.put("TITLE", jsonData.getString("title"));
                dbParam.put("AUTHER", jsonData.getString("auther"));
                dbParam.put("COMPANY", jsonData.getString("company"));
                dbParam.put("DEPARTMENT", jsonData.getString("department"));
                dbParam.put("CONTENT", jsonData.getString("content"));
                dbParam.put("ROLE_ID", jsonData.getString("role_id"));
                dbParam.put("STAFF_NAME", jsonData.getString("staff_name"));
                dbParam.put("SHARE_DATE", jsonData.getString("create_time"));
                dbParam.put("INDB_TIME", TimeTool.now());
                dbParam.put("DEAL_TAG", "1");
                dbParam.put("SEND_ID", jsonData.getString("curr_flog"));

                hisarrayList.add(dbParam);
                continue;
            }
            dbParam.put("UID", jsonData.getString("uid"));
            dbParam.put("MODE_ID", jsonData.getString("mode_id"));
            dbParam.put("STAFF_ID", jsonData.getString("staff_id"));
            dbParam.put("SHARE_UID", jsonData.getString("share_uid"));
            dbParam.put("STAGE", "");
            dbParam.put("TITLE", jsonData.getString("title"));
            dbParam.put("AUTHER", jsonData.getString("auther"));
            dbParam.put("COMPANY", jsonData.getString("company"));
            dbParam.put("DEPARTMENT", jsonData.getString("department"));
            dbParam.put("CONTENT", jsonData.getString("content"));
            dbParam.put("ROLE_ID", jsonData.getString("role_id"));
            dbParam.put("STAFF_NAME", jsonData.getString("staff_name"));
            dbParam.put("SHARE_DATE", jsonData.getString("create_time"));
            dbParam.put("SEND_ID", jsonData.getString("curr_flog"));
            dbParam.put("INDB_TIME", TimeTool.now());
            dbParam.put("DEAL_TAG", "0");

            arrayList.add(dbParam);

        }

        if(arrayList.size()>0){
            dao.insertBatch("out_hirunplus_product_send", arrayList);
        }

        if(hisarrayList.size()>0){
            dao.insertBatch("out_his_hirunplus_product_send", hisarrayList);
        }


        String api = "http://" + host + path;
        JSONObject reqestData = new JSONObject();
        reqestData.put("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)));
        reqestData.put("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)));
        OutBean.insertDataGetInfo("http://" + host + path, reqestData.toJSONString(), end, jsonDataList.size());
    }


    public static long getTotal(CloseableHttpClient httpclient, String start, String end) throws Exception {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPath(path)
                .setParameter("PageNo", "1")
                .setParameter("PageSize", pageSize)
                .setParameter("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)))
                .setParameter("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)))
                .build();
        HttpGet httpGet = new HttpGet(uri);
        long total = 0;
        CloseableHttpResponse response = null;
        try {
            //调接口获取数据
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            JSONObject jsonResponse = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"));
            total = jsonResponse.getJSONObject("page").getLong("total");//总数
            EntityUtils.consume(entity);
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {

                }
            }
        }


        return total;
    }


    public static JSONArray getData(CloseableHttpClient httpclient, int totalPageSize, String start, String end) throws Exception {
        JSONArray jsonDataList = new JSONArray();
        for (int pageNo = 1; pageNo <= totalPageSize; pageNo++) {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPath(path)
                    .setParameter("PageNo", String.valueOf(pageNo))
                    .setParameter("PageSize", pageSize)
                    .setParameter("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)))
                    .setParameter("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)))
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response = null;
            //调接口获取数据
            try {
                response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                JSONObject jsonResponse = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"));
                jsonDataList.addAll(jsonResponse.getJSONArray("data"));
                EntityUtils.consume(entity);
            } catch (Exception e) {
                throw e;
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (Exception e) {

                    }
                }
            }
        }

        return jsonDataList;
    }

}
