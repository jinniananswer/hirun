package com.hirun.web.biz.cust;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.office.ExcelExport;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-04-24.
 */
@Controller
@ResponseBody
public class CustController extends RootController{

    @RequestMapping(value = "/cust/addCust", method = RequestMethod.POST)
    public String addCust(@RequestParam Map pageData) throws Exception {
//        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
//        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        //TODO 后续优化
//        pageData.put("HOUSE_COUNSELOR_ID", sessionEntity.get("EMPLOYEE_ID"));

        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.addCust", pageData);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/editCust", method = RequestMethod.POST)
    public String editCust(@RequestParam Map pageData) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.editCust", pageData);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/queryCustList", method = RequestMethod.GET)
    public String queryCustList(@RequestParam Map pageData) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.queryCustList", pageData);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/addCustByNum", method = RequestMethod.POST)
    public String addCustByNum(@RequestParam Map pageData) throws Exception {
//        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
//        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        //TODO 后续优化
//        pageData.put("HOUSE_COUNSELOR_ID", sessionEntity.get("EMPLOYEE_ID"));

        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.addCustByNum", pageData);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/getCustById", method = RequestMethod.GET)
    public String getCustById(@RequestParam Map map) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.getCustById", map);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/deleteCustById", method = RequestMethod.POST)
    public String deleteCustById(@RequestParam Map map) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.deleteCustById", map);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/addCustContact", method = RequestMethod.POST)
    public String addCustContact(@RequestParam Map map) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.addCustContact", map);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/changeCounselor", method = RequestMethod.POST)
    public String changeCounselor(@RequestParam Map map) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.changeCounselor", map);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/queryCustContact", method = RequestMethod.GET)
    public String queryCustContact(@RequestParam Map map) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.queryCustContact", map);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/restoreCustById", method = RequestMethod.POST)
    public String restoreCustById(@RequestParam Map map) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.restoreCustById", map);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/queryCustList4TopEmployeeId", method = RequestMethod.GET)
    public String queryCustList4TopEmployeeId(@RequestParam Map pageData) throws Exception {
        String employeeId = (String)pageData.get("TOP_EMPLOYEE_ID");
        if(StringUtils.isBlank(employeeId)) {
            logger.info("/cust/queryCustList4TopEmployeeId没有取到TOP_EMPLOYEE_ID");
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
            employeeId = sessionEntity.get("EMPLOYEE_ID");
            logger.info("/cust/queryCustList4TopEmployeeId从session里【"+session.getId()+"】取EMPLOYEE_ID，值为" + employeeId);
            pageData.put("TOP_EMPLOYEE_ID", employeeId);
        }
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.queryCustList", pageData);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/queryCustList4HouseCounselorId", method = RequestMethod.GET)
    public String queryCustList4HouseCounselorId(@RequestParam Map pageData) throws Exception {
        String employeeId = (String)pageData.get("HOUSE_COUNSELOR_ID");
        if(StringUtils.isBlank(employeeId)) {
            logger.info("/cust/queryCustList4HouseCounselorId没有取到HOUSE_COUNSELOR_ID");
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
            employeeId = sessionEntity.get("EMPLOYEE_ID");
            logger.info("/cust/queryCustList4HouseCounselorId从session里【"+session.getId()+"】取EMPLOYEE_ID，值为" + employeeId);
            pageData.put("HOUSE_COUNSELOR_ID", employeeId);
        }
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.queryCustList", pageData);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/queryCustAction4HouseCounselor", method = RequestMethod.GET)
    public String queryCustAction4HouseCounselor(@RequestParam Map pageData) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        pageData.put("TOP_EMPLOYEE_ID", sessionEntity.get("EMPLOYEE_ID"));
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.queryCustAction4HouseCounselor", pageData);
        return response.toJsonString();
    }


    @RequestMapping(value = "showCustomerBluePrintDetail")
    public @ResponseBody String showCustomerBluePrintDetail(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.showCustomerBluePrintDetail", paramter);
        return response.toJsonString();
    }

    @RequestMapping("/cust/exportCustomerInfo4Counselor")
    public @ResponseBody void exportCustomerInfo4Counselor(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("START_DATE", request.getParameter("START_DATE"));
        parameter.put("END_DATE", request.getParameter("END_DATE"));
        parameter.put("FINISH_ACTION", request.getParameter("FINISH_ACTION"));
        parameter.put("HOUSE_COUNSELOR_IDS", request.getParameter("HOUSE_COUNSELOR_IDS"));
        parameter.put("CUST_NAME", URLDecoder.decode(request.getParameter("CUST_NAME"),"UTF-8"));
        parameter.put("WX_NICK", URLDecoder.decode(request.getParameter("WX_NICK"),"UTF-8"));
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        parameter.put("TOP_EMPLOYEE_ID", sessionEntity.get("EMPLOYEE_ID"));


        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.queryCustAction4HouseCounselor", parameter);

        JSONArray statInfoArray = response.getJSONArray("RESULT");

        if(statInfoArray == null || statInfoArray.size() <= 0){
            return;
        }

        List<String> titles = new ArrayList<String>();

        titles.add("客户姓名");
        titles.add("家装顾问");
        titles.add("加微完成次数");
        titles.add("加微最后完成时间");
        titles.add("客户产品需求库使用指导书完成次数");
        titles.add("客户产品需求库使用指导书最后完成时间");
        titles.add("关注公众号完成时间");
        titles.add("核心接触完成时间");
        titles.add("扫码完成次数");
        titles.add("扫码完成时间");
        titles.add("需求蓝图一完成次数");
        titles.add("需求蓝图一最后完成时间");
        titles.add("咨询完成时间");
        titles.add("一键案例完成次数");
        titles.add("一键案例最后完成时间");
        titles.add("城市木屋完成时间");
        titles.add("微信昵称");


        List<List<String>> values = new ArrayList<List<String>>();

        int size = statInfoArray.size();
        for(int i=0;i<size;i++) {
            JSONObject data = statInfoArray.getJSONObject(i);
            List<String> value = new ArrayList<String>();
            value.add(data.getString("CUST_NAME"));
            value.add(data.getString("NAME"));
            value.add(data.getString("JW_NUM"));
            value.add(data.getString("JW_LAST_TIME"));
            value.add(data.getString("LTZDSTS_NUM"));
            value.add(data.getString("LTZDSTS_LAST_TIME"));
            value.add(data.getString("GZHGZ_LAST_TIME"));
            value.add(data.getString("HXJC_LAST_TIME"));
            value.add(data.getString("SMJRQLC_NUM"));
            value.add(data.getString("SMJRQLC_LAST_TIME"));
            value.add(data.getString("XQLTYTS_NUM"));
            value.add(data.getString("XQLTYTS_LAST_TIME"));
            value.add(data.getString("ZX_LAST_TIME"));
            value.add(data.getString("YJALTS_NUM"));
            value.add(data.getString("YJALTS_LAST_TIME"));
            value.add(data.getString("DKCSMU_LAST_TIME"));
            value.add(data.getString("WX_NICK"));
            values.add(value);

        }

        HSSFWorkbook excel = ExcelExport.getDataExcel("数据", titles, values);

        String fileName = "客户查询"+ ".xls";
        fileName = new String(fileName.getBytes(),"ISO8859-1");

        httpResponse.setContentType("application/octet-stream;charset=ISO8859-1");
        httpResponse.setHeader("Content-Disposition", "attachment;filename="+ fileName);
        httpResponse.addHeader("Pargam", "no-cache");
        httpResponse.addHeader("Cache-Control", "no-cache");

        OutputStream os = httpResponse.getOutputStream();
        try {
            excel.write(os);
        }
        catch (Exception e) {

        }
        finally {
            os.flush();
            os.close();
        }

    }
}
