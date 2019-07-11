package com.hirun.app.bean.out.hirunplusdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.custservice.PartyBean;
import com.hirun.app.bean.out.OutBean;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustServiceScandataImport {

    private static String host = "www.hi-run.net";
    private static String path = "/api/scanlog";
    private static String pageSize = "100";

    public static void dataImport(String start, String end,String staffId) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();


        long total = getTotal(httpclient, start, end,staffId);

        int totalPageSize = 0;
        if(total % 100 == 0) {
            totalPageSize = (int)total/100;
        } else {
            totalPageSize = (int)total/100 + 1;
        }

        JSONArray jsonDataList = getData(httpclient, totalPageSize, start, end,staffId);
        List<Map<String,String>> array= new  ArrayList<Map<String,String>>();
        List<Map<String,String>> hisarray= new  ArrayList<Map<String,String>>();


        GenericDAO dao = new GenericDAO("out");
        for(int i = 0, size = jsonDataList.size(); i < size; i++) {
            Map<String, String> dbParam = new HashMap<String, String>();
            JSONObject jsonData = jsonDataList.getJSONObject(i);
            String openid=jsonData.getString("openid");
            String staffid=jsonData.getString("staff_id");


            if(OutBean.isExistData4CustServiceScan(openid, staffid, jsonData.getString("add_time"))) {
                //如果已经存在该数据了，则保存到历史表
                dbParam.put("PROJECT_ID", jsonData.getString("project_id"));
                dbParam.put("UID", jsonData.getString("uid"));
                dbParam.put("HEAD_URL", jsonData.getString("headimgurl"));
                dbParam.put("NICKNAME", jsonData.getString("nickname"));
                dbParam.put("SCAN_ID", jsonData.getString("scan_id"));
                dbParam.put("ROLE_ID", jsonData.getString("role_id"));
                dbParam.put("CID", jsonData.getString("cid"));
                dbParam.put("ADD_TIME", jsonData.getString("add_time"));
                dbParam.put("NAME", jsonData.getString("name"));
                dbParam.put("PHONE", jsonData.getString("phone"));
                dbParam.put("ADDRESS", jsonData.getString("address"));
                dbParam.put("MODE_ID", jsonData.getString("mode_id"));
                dbParam.put("MODE_TIME", jsonData.getString("mode_time"));
                dbParam.put("LOUPAN", jsonData.getString("loupan"));
                dbParam.put("LNUMBER", jsonData.getString("lnumber"));
                dbParam.put("STAFF_ID", jsonData.getString("staff_id"));
                dbParam.put("OPENID", jsonData.getString("openid"));
                dbParam.put("INDB_TIME", TimeTool.now());
                dbParam.put("DEAL_TAG", "1");
                dbParam.put("DEAL_TIME", TimeTool.now());
                hisarray.add(dbParam);
                continue;
            }

            boolean isSuccess=PartyBean.transScanDataToIns(jsonData);

            if(isSuccess){
                dbParam.put("PROJECT_ID", jsonData.getString("project_id"));
                dbParam.put("UID", jsonData.getString("uid"));
                dbParam.put("HEAD_URL", jsonData.getString("headimgurl"));
                dbParam.put("NICKNAME", jsonData.getString("nickname"));
                dbParam.put("SCAN_ID", jsonData.getString("scan_id"));
                dbParam.put("ROLE_ID", jsonData.getString("role_id"));
                dbParam.put("CID", jsonData.getString("cid"));
                dbParam.put("ADD_TIME", jsonData.getString("add_time"));
                dbParam.put("NAME", jsonData.getString("name"));
                dbParam.put("PHONE", jsonData.getString("phone"));
                dbParam.put("ADDRESS", jsonData.getString("address"));
                dbParam.put("MODE_ID", jsonData.getString("mode_id"));
                dbParam.put("MODE_TIME", jsonData.getString("mode_time"));
                dbParam.put("LOUPAN", jsonData.getString("loupan"));
                dbParam.put("LNUMBER", jsonData.getString("lnumber"));
                dbParam.put("STAFF_ID", jsonData.getString("staff_id"));
                dbParam.put("OPENID", jsonData.getString("openid"));
                dbParam.put("INDB_TIME", TimeTool.now());
                dbParam.put("DEAL_TAG", "1");
                dbParam.put("DEAL_TIME", TimeTool.now());
                hisarray.add(dbParam);
            }else {
                dbParam.put("PROJECT_ID", jsonData.getString("project_id"));
                dbParam.put("UID", jsonData.getString("uid"));
                dbParam.put("HEAD_URL", jsonData.getString("headimgurl"));
                dbParam.put("NICKNAME", jsonData.getString("nickname"));
                dbParam.put("SCAN_ID", jsonData.getString("scan_id"));
                dbParam.put("ROLE_ID", jsonData.getString("role_id"));
                dbParam.put("CID", jsonData.getString("cid"));
                dbParam.put("ADD_TIME", jsonData.getString("add_time"));
                dbParam.put("NAME", jsonData.getString("name"));
                dbParam.put("PHONE", jsonData.getString("phone"));
                dbParam.put("ADDRESS", jsonData.getString("address"));
                dbParam.put("MODE_ID", jsonData.getString("mode_id"));
                dbParam.put("MODE_TIME", jsonData.getString("mode_time"));
                dbParam.put("LOUPAN", jsonData.getString("loupan"));
                dbParam.put("LNUMBER", jsonData.getString("lnumber"));
                dbParam.put("STAFF_ID", jsonData.getString("staff_id"));
                dbParam.put("OPENID", jsonData.getString("openid"));
                dbParam.put("INDB_TIME", TimeTool.now());
                dbParam.put("DEAL_TAG", "0");
                array.add(dbParam);
            }
        }
        dao.insertBatch("out_hirunplus_custservice_scan",array);
        dao.insertBatch("out_his_hirunplus_custservice_scan",hisarray);


        String api = "http://" + host + path;
        JSONObject reqestData = new JSONObject();
        reqestData.put("role_id", "11");
        if(StringUtils.isNotBlank(staffId)){
            reqestData.put("staff_id",staffId);
        }

        reqestData.put("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)));
        reqestData.put("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)));
        OutBean.insertDataGetInfo("http://" + host + path,reqestData.toJSONString(),end, jsonDataList.size());
    }


    public static long getTotal(CloseableHttpClient httpclient, String start, String end,String staffId) throws Exception{
        URI uri = null;
        if(StringUtils.isBlank(staffId)) {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPath(path)
                    .setParameter("PageNo", "1")
                    .setParameter("PageSize", pageSize)
                    .setParameter("role_id", "11")
                    .setParameter("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)))
                    .setParameter("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)))
                    .build();
        } else {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPath(path)
                    .setParameter("PageNo", "1")
                    .setParameter("PageSize", pageSize)
                    .setParameter("staff_id", staffId)
                    .setParameter("role_id", "11")
                    .setParameter("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)))
                    .setParameter("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)))
                    .build();
        }
        HttpGet httpGet = new HttpGet(uri);
        long total = 0;
        CloseableHttpResponse response = null;
        try{
            //调接口获取数据
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            JSONObject jsonResponse = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"));
            String page=jsonResponse.getString("page");
            if(StringUtils.equals("false",page)){
                total=0;
            }else{
                total = jsonResponse.getJSONObject("page").getLong("total");//总数
            }
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



    public static JSONArray getData(CloseableHttpClient httpclient, int totalPageSize, String start, String end,String staffId) throws Exception{
        JSONArray jsonDataList = new JSONArray();
        URI uri = null;
        for(int pageNo = 1; pageNo <= totalPageSize; pageNo++) {
                if(StringUtils.isBlank(staffId)) {
                    uri = new URIBuilder()
                            .setScheme("http")
                            .setHost(host)
                            .setPath(path)
                            .setParameter("PageNo", String.valueOf(pageNo))
                            .setParameter("PageSize", pageSize)
                            .setParameter("role_id", "11")
                            .setParameter("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)))
                            .setParameter("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)))
                            .build();
                }else{
                    uri = new URIBuilder()
                            .setScheme("http")
                            .setHost(host)
                            .setPath(path)
                            .setParameter("PageNo", String.valueOf(pageNo))
                            .setParameter("PageSize", pageSize)
                            .setParameter("staff_id", staffId)
                            .setParameter("role_id", "11")
                            .setParameter("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)))
                            .setParameter("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)))
                            .build();
                }

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
