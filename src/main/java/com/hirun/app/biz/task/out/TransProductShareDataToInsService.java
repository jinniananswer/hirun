package com.hirun.app.biz.task.out;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class TransProductShareDataToInsService extends GenericService {

    public ServiceResponse transProductShareData2Ins(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        GenericDAO dao = new GenericDAO("out");
        StringBuilder sql = new StringBuilder();
        String now = TimeTool.now();

        sql.append(" SELECT * ");
        sql.append(" FROM out_hirunplus_product_send ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY SHARE_DATE ");
        JSONArray jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for (int i = 0, size = jsonProjectList.size(); i < size; i++) {
            Map<String, String> dbParam = new HashMap<String, String>();
            Map<String, String> updateParam = new HashMap<String, String>();

            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            updateParam.put("ID", id);
            boolean isSuccess = this.transSendDataToIns(jsonProject);
            if (isSuccess) {
                dbParam.put("UID", jsonProject.getString("UID"));
                dbParam.put("MODE_ID", jsonProject.getString("MODE_ID"));
                dbParam.put("STAFF_ID", jsonProject.getString("STAFF_ID"));
                dbParam.put("SHARE_UID", jsonProject.getString("SHARE_UID"));
                dbParam.put("STAGE", "");
                dbParam.put("TITLE", jsonProject.getString("TITLE"));
                dbParam.put("AUTHER", jsonProject.getString("AUTHER"));
                dbParam.put("COMPANY", jsonProject.getString("COMPANY"));
                dbParam.put("DEPARTMENT", jsonProject.getString("DEPARTMENT"));
                dbParam.put("CONTENT", jsonProject.getString("CONTENT"));
                dbParam.put("ROLE_ID", jsonProject.getString("ROLE_ID"));
                dbParam.put("STAFF_NAME", jsonProject.getString("STAFF_NAME"));
                dbParam.put("SHARE_DATE", jsonProject.getString("SHARE_DATE"));
                dbParam.put("INDB_TIME", jsonProject.getString("INDB_TIME"));
                dbParam.put("SEND_ID", jsonProject.getString("id"));
                dbParam.put("DEAL_TAG", "1");
                dbParam.put("DEAL_TIME", TimeTool.now());

                dao.insertAutoIncrement("out_his_hirunplus_product_send", dbParam);
                dao.executeUpdate("delete from out_hirunplus_product_send where ID = :ID ", updateParam);
            }
        }

        sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM out_hirunplus_product_share ");
        sql.append(" WHERE DEAL_TAG = '0' ");
        sql.append(" ORDER BY OPEN_DATE ");
        jsonProjectList = ConvertTool.toJSONArray(dao.queryBySql(sql.toString(), new HashMap<String, String>()));

        for (int i = 0, size = jsonProjectList.size(); i < size; i++) {
            Map<String, String> dbParam = new HashMap<String, String>();
            Map<String, String> updateParam = new HashMap<String, String>();

            JSONObject jsonProject = jsonProjectList.getJSONObject(i);
            String id = jsonProject.getString("ID");
            updateParam.put("ID", id);

            boolean isSuccess = this.transOpenDataToIns(jsonProject);
            if (isSuccess) {
                dbParam.put("UID", jsonProject.getString("UID"));
                dbParam.put("MODE_ID", jsonProject.getString("MODE_ID"));
                dbParam.put("STAFF_ID", jsonProject.getString("STAFF_ID"));
                dbParam.put("SHARE_UID", jsonProject.getString("SHARE_UID"));
                dbParam.put("STAGE", "");
                dbParam.put("TITLE", jsonProject.getString("TITLE"));
                dbParam.put("AUTHER", jsonProject.getString("AUTHER"));
                dbParam.put("COMPANY", jsonProject.getString("COMPANY"));
                dbParam.put("DEPARTMENT", jsonProject.getString("DEPARTMENT"));
                dbParam.put("CONTENT", jsonProject.getString("CONTENT"));
                dbParam.put("ROLE_ID", jsonProject.getString("ROLE_ID"));
                dbParam.put("STAFF_NAME", jsonProject.getString("STAFF_NAME"));
                dbParam.put("SHARE_DATE", jsonProject.getString("SHARE_DATE"));
                dbParam.put("INDB_TIME", jsonProject.getString("INDB_TIME"));
                dbParam.put("SEND_ID", jsonProject.getString("ID"));
                dbParam.put("OPEN_ID", jsonProject.getString("OPEN_ID"));
                dbParam.put("OPEN_DATE", jsonProject.getString("OPEN_DATE"));
                dbParam.put("NICKNAME", jsonProject.getString("NICKNAME"));
                dbParam.put("HEAD_URL", jsonProject.getString("HEAD_URL"));
                dbParam.put("DEAL_TAG", "1");
                dbParam.put("DEAL_TIME", TimeTool.now());

                dao.insertAutoIncrement("out_his_hirunplus_product_share", dbParam);
                dao.executeUpdate("delete from out_hirunplus_product_share where ID = :ID", updateParam);
            }
        }
        return response;
    }



    /**
     * 转换推送数据
     * @param jsonObject
     * @return
     * @throws Exception
     */
    private boolean transSendDataToIns(JSONObject jsonObject) throws Exception {
        Map<String, String> sendData = new HashMap<String, String>();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);

        String employeeId = getEmployeeIdByHirunPlusStaffId(jsonObject.getString("STAFF_ID"));
        if (StringUtils.isBlank(employeeId)) {
            return false;
        }
        EmployeeJobRoleEntity employeeJobRoleEntity=EmployeeBean.queryEmployeeJobRoleByEmpId(employeeId);
        //查询员工归属门店
        OrgEntity shopEntity= OrgBean.getAssignTypeOrg(employeeJobRoleEntity.getOrgId(),"2");
        String shopId="999";
        if(shopEntity!=null){
            shopId=shopEntity.getOrgId();
        }
        //查询员工归属分公司
        OrgEntity companyEntity=OrgBean.getAssignTypeOrg(employeeJobRoleEntity.getOrgId(),"3");
        String companyId="999";
        if(companyEntity!=null){
            companyId=companyEntity.getOrgId();
        }
        sendData.put("ORG_ID",employeeJobRoleEntity.getOrgId());
        sendData.put("SEND_ID",jsonObject.getString("SEND_ID"));
        sendData.put("MODE_ID",jsonObject.getString("MODE_ID"));
        sendData.put("TITLE",jsonObject.getString("TITLE"));
        sendData.put("AUTHER",jsonObject.getString("AUTHER"));
        sendData.put("COMPANY",jsonObject.getString("COMPANY"));
        sendData.put("DEPARTMENT",jsonObject.getString("DEPARTMENT"));
        sendData.put("CONTENT",jsonObject.getString("CONTENT"));
        sendData.put("STAFF_ID",jsonObject.getString("STAFF_ID"));
        sendData.put("STAFF_NAME",jsonObject.getString("STAFF_NAME"));
        sendData.put("EMPLOYEE_ID",employeeId);
        sendData.put("SEND_TIME",transUnixTimeToNormal(jsonObject.getString("SHARE_DATE")));
        sendData.put("SHOP_ID",shopId);
        sendData.put("COMPANY_ID",companyId);
        sendData.put("CREATE_TIME",TimeTool.now());
        dao.insertAutoIncrement("ins_midprod_send",sendData);
        return true;
    }

    /**
     * 转换打开数据
     * @param jsonObject
     * @return
     * @throws Exception
     */
    private boolean transOpenDataToIns(JSONObject jsonObject) throws Exception {
        Map<String, String> openData = new HashMap<String, String>();
        CustomerServiceDAO dao = DAOFactory.createDAO(CustomerServiceDAO.class);

        String employeeId = getEmployeeIdByHirunPlusStaffId(jsonObject.getString("STAFF_ID"));
        if (StringUtils.isBlank(employeeId)) {
            return false;
        }
        EmployeeJobRoleEntity employeeJobRoleEntity=EmployeeBean.queryEmployeeJobRoleByEmpId(employeeId);
        //查询员工归属门店
        OrgEntity shopEntity= OrgBean.getAssignTypeOrg(employeeJobRoleEntity.getOrgId(),"2");
        String shopId="999";
        if(shopEntity!=null){
            shopId=shopEntity.getOrgId();
        }
        //查询员工归属分公司
        OrgEntity companyEntity=OrgBean.getAssignTypeOrg(employeeJobRoleEntity.getOrgId(),"3");
        String companyId="999";
        if(companyEntity!=null){
            companyId=companyEntity.getOrgId();
        }
        openData.put("ORG_ID",employeeJobRoleEntity.getOrgId());
        openData.put("SEND_ID",jsonObject.getString("SEND_ID"));
        openData.put("MODE_ID",jsonObject.getString("MODE_ID"));
        openData.put("TITLE",jsonObject.getString("TITLE"));
        openData.put("AUTHER",jsonObject.getString("AUTHER"));
        openData.put("COMPANY",jsonObject.getString("COMPANY"));
        openData.put("DEPARTMENT",jsonObject.getString("DEPARTMENT"));
        openData.put("CONTENT",jsonObject.getString("CONTENT"));
        openData.put("STAFF_ID",jsonObject.getString("STAFF_ID"));
        openData.put("STAFF_NAME",jsonObject.getString("STAFF_NAME"));
        openData.put("EMPLOYEE_ID",employeeId);
        openData.put("SEND_TIME",transUnixTimeToNormal(jsonObject.getString("SHARE_DATE")));
        openData.put("OPEN_TIME",transUnixTimeToNormal(jsonObject.getString("OPEN_DATE")));
        openData.put("OPEN_ID",jsonObject.getString("OPEN_ID"));
        openData.put("WX_NICK",jsonObject.getString("NICKNAME"));
        openData.put("HEAD_URL",jsonObject.getString("HEAD_URL"));

        openData.put("SHOP_ID",shopId);
        openData.put("COMPANY_ID",companyId);
        openData.put("CREATE_TIME",TimeTool.now());
        dao.insertAutoIncrement("ins_midprod_open",openData);
        return true;
    }

    /**
     * 通过家网给的staffId查询对应的employeeId
     * @param staffId
     * @return
     * @throws Exception
     */
    public static String getEmployeeIdByHirunPlusStaffId(String staffId) throws Exception {
        if (StringUtils.isBlank(staffId) || "0".equals(staffId) || "null".equals(staffId)) {
            return null;
        }
        String mobileNo = HirunPlusStaffDataCache.getMobileByStaffId(staffId);
        if (StringUtils.isBlank(mobileNo)) {
            return null;
        }
        EmployeeEntity employeeEntity = EmployeeBean.getEmployeeByMobileNo(mobileNo);
        if (employeeEntity == null) {
            return null;
        }

        return employeeEntity.getEmployeeId();
    }


    /**
     * 将unix时间转换成正常时间
     * @param timestampString
     * @return
     */
    private static String transUnixTimeToNormal(String timestampString) {
        if (StringUtils.isBlank(timestampString) || StringUtils.equals(timestampString, "0")) {
            return "";
        }
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        return date;
    }
}