package com.hirun.app.bean.out.hirunplusdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.custservice.PartyBean;
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


public class XQLTEdataImport {

    private static String host = "www.hi-run.net";
    private static String path = "/api/projectltv2";
    private static String pageSize = "100";

    public static void dataImport(String start, String end) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();


        long total = getTotal(httpclient, start, end);


        int totalPageSize = 0;
        if(total % 100 == 0) {
            totalPageSize = (int)total/100;
        } else {
            totalPageSize = (int)total/100 + 1;
        }

        JSONArray jsonDataList = getData(httpclient, totalPageSize, start, end);


        GenericDAO dao = new GenericDAO("out");
        List<Map<String,String>> arrayList=new ArrayList<Map<String,String>>();
        List<Map<String,String>> hisarrayList=new ArrayList<Map<String,String>>();

        for(int i = 0, size = jsonDataList.size(); i < size; i++) {
            Map<String, String> dbParam = new HashMap<String, String>();
            JSONObject jsonData = jsonDataList.getJSONObject(i);


            String openid=jsonData.getString("openid");
            String staffid=jsonData.getString("sjs_staff_id");

            if(OutBean.isExistData4XQLTE(openid, staffid, jsonData.getString("update_time"))) {
                //如果已经存在该数据了，则直接插历史表
                dbParam.put("PROJECT_ID", jsonData.getString("project_id"));
                dbParam.put("UID", jsonData.getString("uid"));
                dbParam.put("NICKNAME", jsonData.getString("nickname"));
                dbParam.put("HEADIMGURL", jsonData.getString("headimgurl"));
                dbParam.put("ADD_TIME", jsonData.getString("add_time"));
                dbParam.put("STAT", jsonData.getString("stat"));
                dbParam.put("MSG", jsonData.getString("msg"));
                dbParam.put("NAME", jsonData.getString("name"));
                dbParam.put("PHONE", jsonData.getString("phone"));
                dbParam.put("ADDRESS", jsonData.getString("address"));
                dbParam.put("ISHIDE", jsonData.getString("ishide"));
                dbParam.put("MODE_ID", jsonData.getString("mode_id"));
                dbParam.put("MODE_TIME", jsonData.getString("mode_time"));
                dbParam.put("LOUPAN", jsonData.getString("loupan"));
                dbParam.put("LNUMBER", jsonData.getString("lnumber"));
                dbParam.put("CUST_FROM", jsonData.getString("cus_from"));
                dbParam.put("STAFF_ID", jsonData.getString("staff_id"));
                dbParam.put("IS_COMMEND", jsonData.getString("iscommend"));
                dbParam.put("COMM_TIME", jsonData.getString("comm_time"));
                dbParam.put("IS_INPROCESS", jsonData.getString("is_inprocess"));
                dbParam.put("IN_TIME", jsonData.getString("in_time"));
                dbParam.put("METHOD", jsonData.getString("method"));
                dbParam.put("ISCCMW", jsonData.getString("isccmw"));
                dbParam.put("ISZJGD", jsonData.getString("iszjgd"));
                dbParam.put("TMPINT", jsonData.getString("tmpint"));
                dbParam.put("URL", jsonData.getString("url"));
                dbParam.put("LT2_TIME", jsonData.getString("update_time"));
                dbParam.put("LT3_TIME", jsonData.getString("lt3time"));
                dbParam.put("LT2_UPDATE_TIME", jsonData.getString("update_time"));
                dbParam.put("LT3_UPDATE_TIME", jsonData.getString("lt3update_time"));
                //新增返回功能蓝图保存时间
                dbParam.put("GNLT_CREATE_TIME", jsonData.getString("gnlt_update_time"));
                dbParam.put("GNLT_UPDATE_TIME", jsonData.getString("gnlt_update_time"));

                dbParam.put("FGLT_CREATE_TIME", jsonData.getString("fglt_update_time"));
                dbParam.put("FGLT_UPDATE_TIME", jsonData.getString("fglt_update_time"));


                dbParam.put("IS_CHANGE", jsonData.getString("ischange"));
                dbParam.put("OPEN_ID", jsonData.getString("openid"));
                dbParam.put("SJS_STAFF_ID", jsonData.getString("sjs_staff_id"));
                dbParam.put("SJS_ROLE_ID", jsonData.getString("sjs_role_id"));
                dbParam.put("FUNC", jsonData.getString("funs_A"));
                //2020-01-08家网需求蓝图变化新增A\B\C三类调整
                dbParam.put("FUNC_B", jsonData.getString("funs_B"));
                dbParam.put("FUNC_C", jsonData.getString("funs_C"));

                dbParam.put("STYLE", jsonData.getString("style"));

                dbParam.put("INDB_TIME", TimeTool.now());
                dbParam.put("DEAL_TAG", "1");
                dbParam.put("DEAL_TIME",TimeTool.now());

                hisarrayList.add(dbParam);
                continue;
            }

            boolean isSuccess=PartyBean.transXQLTEDataToIns(jsonData);

            if(isSuccess) {
                dbParam.put("PROJECT_ID", jsonData.getString("project_id"));
                dbParam.put("UID", jsonData.getString("uid"));
                dbParam.put("NICKNAME", jsonData.getString("nickname"));
                dbParam.put("HEADIMGURL", jsonData.getString("headimgurl"));
                dbParam.put("ADD_TIME", jsonData.getString("add_time"));
                dbParam.put("STAT", jsonData.getString("stat"));
                dbParam.put("MSG", jsonData.getString("msg"));
                dbParam.put("NAME", jsonData.getString("name"));
                dbParam.put("PHONE", jsonData.getString("phone"));
                dbParam.put("ADDRESS", jsonData.getString("address"));
                dbParam.put("ISHIDE", jsonData.getString("ishide"));
                dbParam.put("MODE_ID", jsonData.getString("mode_id"));
                dbParam.put("MODE_TIME", jsonData.getString("mode_time"));
                dbParam.put("LOUPAN", jsonData.getString("loupan"));
                dbParam.put("LNUMBER", jsonData.getString("lnumber"));
                dbParam.put("CUST_FROM", jsonData.getString("cus_from"));
                dbParam.put("STAFF_ID", jsonData.getString("staff_id"));
                dbParam.put("IS_COMMEND", jsonData.getString("iscommend"));
                dbParam.put("COMM_TIME", jsonData.getString("comm_time"));
                dbParam.put("IS_INPROCESS", jsonData.getString("is_inprocess"));
                dbParam.put("IN_TIME", jsonData.getString("in_time"));
                dbParam.put("METHOD", jsonData.getString("method"));
                dbParam.put("ISCCMW", jsonData.getString("isccmw"));
                dbParam.put("ISZJGD", jsonData.getString("iszjgd"));
                dbParam.put("TMPINT", jsonData.getString("tmpint"));
                dbParam.put("URL", jsonData.getString("url"));
                dbParam.put("LT2_TIME", jsonData.getString("update_time"));
                dbParam.put("LT3_TIME", jsonData.getString("lt3time"));
                dbParam.put("LT2_UPDATE_TIME", jsonData.getString("update_time"));
                dbParam.put("LT3_UPDATE_TIME", jsonData.getString("lt3update_time"));
                //新增返回功能蓝图保存时间
                dbParam.put("GNLT_CREATE_TIME", jsonData.getString("gnlt_update_time"));
                dbParam.put("GNLT_UPDATE_TIME", jsonData.getString("gnlt_update_time"));

                dbParam.put("FGLT_CREATE_TIME", jsonData.getString("fglt_update_time"));
                dbParam.put("FGLT_UPDATE_TIME", jsonData.getString("fglt_update_time"));

                dbParam.put("IS_CHANGE", jsonData.getString("ischange"));
                dbParam.put("OPEN_ID", jsonData.getString("openid"));
                dbParam.put("SJS_STAFF_ID", jsonData.getString("sjs_staff_id"));
                dbParam.put("SJS_ROLE_ID", jsonData.getString("sjs_role_id"));
                dbParam.put("FUNC", jsonData.getString("funs_A"));

                //2020-01-08家网需求蓝图变化新增A\B\C三类调整
                dbParam.put("FUNC_B", jsonData.getString("funs_B"));
                dbParam.put("FUNC_C", jsonData.getString("funs_C"));

                dbParam.put("STYLE", jsonData.getString("style"));

                dbParam.put("INDB_TIME", TimeTool.now());
                dbParam.put("DEAL_TAG", "1");
                dbParam.put("DEAL_TIME",TimeTool.now());

                hisarrayList.add(dbParam);

            }else{
                dbParam.put("PROJECT_ID", jsonData.getString("project_id"));
                dbParam.put("UID", jsonData.getString("uid"));
                dbParam.put("NICKNAME", jsonData.getString("nickname"));
                dbParam.put("HEADIMGURL", jsonData.getString("headimgurl"));
                dbParam.put("ADD_TIME", jsonData.getString("add_time"));
                dbParam.put("STAT", jsonData.getString("stat"));
                dbParam.put("MSG", jsonData.getString("msg"));
                dbParam.put("NAME", jsonData.getString("name"));
                dbParam.put("PHONE", jsonData.getString("phone"));
                dbParam.put("ADDRESS", jsonData.getString("address"));
                dbParam.put("ISHIDE", jsonData.getString("ishide"));
                dbParam.put("MODE_ID", jsonData.getString("mode_id"));
                dbParam.put("MODE_TIME", jsonData.getString("mode_time"));
                dbParam.put("LOUPAN", jsonData.getString("loupan"));
                dbParam.put("LNUMBER", jsonData.getString("lnumber"));
                dbParam.put("CUST_FROM", jsonData.getString("cus_from"));
                dbParam.put("STAFF_ID", jsonData.getString("staff_id"));
                dbParam.put("IS_COMMEND", jsonData.getString("iscommend"));
                dbParam.put("COMM_TIME", jsonData.getString("comm_time"));
                dbParam.put("IS_INPROCESS", jsonData.getString("is_inprocess"));
                dbParam.put("IN_TIME", jsonData.getString("in_time"));
                dbParam.put("METHOD", jsonData.getString("method"));
                dbParam.put("ISCCMW", jsonData.getString("isccmw"));
                dbParam.put("ISZJGD", jsonData.getString("iszjgd"));
                dbParam.put("TMPINT", jsonData.getString("tmpint"));
                dbParam.put("URL", jsonData.getString("url"));
                dbParam.put("LT2_TIME", jsonData.getString("update_time"));
                dbParam.put("LT3_TIME", jsonData.getString("lt3time"));
                dbParam.put("LT2_UPDATE_TIME", jsonData.getString("update_time"));
                dbParam.put("LT3_UPDATE_TIME", jsonData.getString("lt3update_time"));

                dbParam.put("GNLT_CREATE_TIME", jsonData.getString("gnlt_update_time"));
                dbParam.put("GNLT_UPDATE_TIME", jsonData.getString("gnlt_update_time"));
                dbParam.put("FGLT_CREATE_TIME", jsonData.getString("fglt_update_time"));
                dbParam.put("FGLT_UPDATE_TIME", jsonData.getString("fglt_update_time"));

                dbParam.put("IS_CHANGE", jsonData.getString("ischange"));
                dbParam.put("OPEN_ID", jsonData.getString("openid"));
                dbParam.put("SJS_STAFF_ID", jsonData.getString("sjs_staff_id"));
                dbParam.put("SJS_ROLE_ID", jsonData.getString("sjs_role_id"));
                dbParam.put("FUNC", jsonData.getString("funs_A"));

                //2020-01-08家网需求蓝图变化新增A\B\C三类调整
                dbParam.put("FUNC_B", jsonData.getString("funs_B"));
                dbParam.put("FUNC_C", jsonData.getString("funs_C"));

                dbParam.put("STYLE", jsonData.getString("style"));
                dbParam.put("INDB_TIME", TimeTool.now());
                dbParam.put("DEAL_TAG", "0");

                arrayList.add(dbParam);
            }
        }

        dao.insertBatch("out_hirunplus_xqlte",arrayList);
        dao.insertBatch("out_his_hirunplus_xqlte",hisarrayList);


        String api = "http://" + host + path;
        JSONObject reqestData = new JSONObject();
        reqestData.put("stage", "2");
        reqestData.put("start", String.valueOf(TimeTool.strToTime4DateTime(start, TimeTool.TIME_PATTERN)));
        reqestData.put("end", String.valueOf(TimeTool.strToTime4DateTime(end, TimeTool.TIME_PATTERN)));
        OutBean.insertDataGetInfo("http://" + host + path,reqestData.toJSONString(),end, jsonDataList.size());
    }


    public static long getTotal(CloseableHttpClient httpclient, String start, String end) throws Exception{
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPath(path)
                .setParameter("PageNo", "1")
                .setParameter("PageSize",pageSize)
                .setParameter("stage", "2")
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
                    .setParameter("stage","2")
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
