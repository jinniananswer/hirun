package com.hirun.app.bean.out;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.houses.HousesBean;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.hirun.pub.domain.entity.cust.CustomerEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.Map;


/**
 * 同步信息给家网
 * Created by pc on 2020-05-21.
 */
public class SyncBean {

    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    private static String host = "www.hi-run.net";
    private static String addStaffPath = "/api/addstaff";
    private static String syncCustomerInfoPath="/api/userinfo";

    /**
     * 同步客户信息给家网
     * @return
     * @throws Exception
     */
    public static void syncCustomerInfo(CustomerEntity customerEntity, Map<String,String> logMap) throws Exception {
        CloseableHttpResponse response = null;
        try{
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPath(syncCustomerInfoPath)
                    .setParameter("openid",customerEntity.getIdentifyCode())
                    .setParameter("name",customerEntity.getCustName())
                    .setParameter("phone", customerEntity.getMobileNo())
                    .setParameter("address",customerEntity.getHouseDetail())
                    .setParameter("loupan", HousesBean.getHousesEntityById(customerEntity.getHouseId()).getName())
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            response = httpclient.execute(httpGet);
            logMap.put("OUT_URL",host+syncCustomerInfoPath+"?open_id"+customerEntity.getIdentifyCode()
                    +"&name"+customerEntity.getCustName()
                    +"&phone="+customerEntity.getMobileNo()
                    +"&address="+customerEntity.getHouseDetail()
                    +"&loupan="+HousesBean.getHousesEntityById(customerEntity.getHouseId()).getName());
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(response != null) {
                try{
                    HttpEntity entity = response.getEntity();
                    JSONObject jsonResponse = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"));
                    logMap.put("RESULT",jsonResponse.toJSONString());
                    response.close();
                } catch(Exception e) {

                }
            }
        }
    }

    /**
     * 同步角色信息给家网家网
     * @return
     * @throws Exception
     */
    public static void syncRoleInfo(String openId,String employeeId, Map<String,String> logMap) throws Exception {
        CloseableHttpResponse response = null;
        String staffId=getStaffIdByEmployeeId(employeeId);

        if(StringUtils.isBlank(staffId)){
            logMap.put("RESULT",employeeId+"||未找到对应的staffId");
            return;
        }

        try{
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPath(addStaffPath)
                    .setParameter("openid",openId)
                    .setParameter("staff_id",staffId)
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            response = httpclient.execute(httpGet);
            logMap.put("OUT_URL",host+addStaffPath+"?openid="+openId+"&staff_id="+staffId);
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(response != null) {
                try{
                    HttpEntity entity = response.getEntity();
                    JSONObject jsonResponse = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"));
                    logMap.put("RESULT",jsonResponse.toJSONString());
                    response.close();
                } catch(Exception e) {

                }
            }
        }
    }

    private static String getStaffIdByEmployeeId(String employeeId) throws Exception{
        if(StringUtils.isBlank(employeeId)){
            return "";
        }
        EmployeeEntity employeeEntity= EmployeeBean.getEmployeeByEmployeeId(employeeId);
        if(employeeEntity==null){
            return "";
        }
        return HirunPlusStaffDataCache.getStaffIdByMobile(employeeEntity.getMobileNo());
    }

}
