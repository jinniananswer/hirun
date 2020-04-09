package com.hirun.web.biz.datacenter.custservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.org.OrgBean;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.office.ExcelExport;
import com.most.core.pub.tools.time.TimeTool;
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
import java.util.*;

@Controller
public class CustServiceReportController extends RootController {


    @RequestMapping(value = "queryCustServFinishActionInfo")
    public @ResponseBody String queryCustServFinishActionInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryCustServFinishActionInfo", paramter);
        return response.toJsonString();
    }



    @RequestMapping(value = "initQueryActionFinishInfo")
    public @ResponseBody String initQueryActionFinishInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.initQueryActionFinishInfo", paramter);
        return response.toJsonString();
    }


    @RequestMapping(value = "queryCustServiceByName")
    public @ResponseBody String queryCustServiceByName(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryCustServiceByName", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryCustServMonStatInfo")
    public @ResponseBody String queryCustServMonStatInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryCustServMonStatInfo", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryNewCustServMonStatInfo")
    public @ResponseBody String queryNewCustServMonStatInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryNewCustServMonStatInfo", paramter);
        return response.toJsonString();
    }

    @RequestMapping("/exportCustServiceMonStat")
    public @ResponseBody void exportCustServiceMonStat(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUSTSERVICEEMPID", request.getParameter("CUSTSERVICEEMPID"));
        parameter.put("ORG_ID", request.getParameter("ORG_ID"));
        parameter.put("MON_DATE", request.getParameter("MON_DATE"));



        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryNewCustServMonStatInfo", parameter);

        JSONArray statInfoArray = response.getJSONArray("CUSTSERVICESTATINFO");

        if(statInfoArray == null || statInfoArray.size() <= 0){
            return;
        }

        List<String> titles = new ArrayList<String>();
        titles.add("客户代表");
        titles.add("新客户咨询数");
        titles.add("生成风格蓝图风格数");
        titles.add("生成风格蓝图比例");
        titles.add("生成功能蓝图风格数");
        titles.add("生成功能蓝图比例");
        titles.add("蓝图二生成比例");
        titles.add("扫客户代表码进入全流程数");
        titles.add("扫客户代表码进入全流程比例");
        titles.add("带看城市木屋数");
        titles.add("带看城市木屋比例");

        List<List<String>> values = new ArrayList<List<String>>();

            int size = statInfoArray.size();
            for(int i=0;i<size;i++) {
                JSONObject data = statInfoArray.getJSONObject(i);
                List<String> value = new ArrayList<String>();
                value.add(data.getString("EMPLOYEE_NAME"));
                value.add(data.getString("CONSULT_COUNT"));
                value.add(data.getString("STYLE_COUNT"));
                value.add(data.getString("STYLE_SCALE"));
                value.add(data.getString("FUNC_COUNT"));
                value.add(data.getString("FUNC_SCALE"));
                value.add(data.getString("XQLTE_SCALE"));
                value.add(data.getString("SCAN_COUNT"));
                value.add(data.getString("SCAN_SCALE"));
                value.add(data.getString("SCANCITYHOUSE_COUNT"));
                value.add(data.getString("SCANCITYHOUSE_SCALE"));
                values.add(value);

        }

        HSSFWorkbook excel = ExcelExport.getDataExcel(request.getParameter("MON_DATE"), titles, values);

        String fileName = TimeTool.now()+ ".xls";
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


    /**
     * 2020/03/14 新增
     * @param pageData
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "initQueryCustomerAction")
    public @ResponseBody String initQueryCustomerAction(@RequestParam Map pageData) throws Exception {
        String employeeIds = (String)pageData.get("EMPLOYEE_IDS");
        if(StringUtils.isBlank(employeeIds)) {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
            employeeIds = sessionEntity.get("EMPLOYEE_ID");
            pageData.put("EMPLOYEE_IDS", employeeIds);
        }

        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.getAllSubordinatesCounselors", pageData);

        return response.toJsonString();
    }


    @RequestMapping("/exportCustActionInfo")
    public @ResponseBody void exportCustActionInfo(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception{

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUSTSERVICEEMPID", request.getParameter("CUSTSERVICEEMPID"));
        parameter.put("START_DATE", request.getParameter("START_DATE"));
        parameter.put("END_DATE", request.getParameter("END_DATE"));
        parameter.put("NAME", request.getParameter("NAME"));
        parameter.put("TAG_ID", request.getParameter("TAG_ID"));
        parameter.put("WX_NICK", request.getParameter("WX_NICK"));
        parameter.put("ORG_ID", request.getParameter("ORG_ID"));

        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryCustServFinishActionInfo", parameter);

        JSONArray statInfoArray = response.getJSONArray("CUSTSERVICEFINISHACTIONINFO");

        if(statInfoArray == null || statInfoArray.size() <= 0){
            return;
        }

        List<String> titles = new ArrayList<String>();

        titles.add("客户姓名");
        titles.add("微信昵称");
        titles.add("咨询时间");
        titles.add("楼盘地址");
        titles.add("客户代表");
        titles.add("进入全流程时间");
        titles.add("生成风格蓝图时间");
        titles.add("生成功能蓝图时间");
        titles.add("录入客户信息时间");
        titles.add("安排设计师时间");
        titles.add("看城市木屋时间");
        titles.add("带看城市木屋楼盘地址");
        titles.add("带看后反馈");
        titles.add("回访次数");
        titles.add("客户标签");


        List<List<String>> values = new ArrayList<List<String>>();

        int size = statInfoArray.size();
        for(int i=0;i<size;i++) {
            JSONObject data = statInfoArray.getJSONObject(i);
            List<String> value = new ArrayList<String>();
            value.add(data.getString("PARTY_NAME"));
            value.add(data.getString("WX_NICK"));
            value.add(data.getString("CREATE_TIME"));
            if(StringUtils.isBlank(data.getString("HOUSE_ADDRESS"))||StringUtils.equals(data.getString("HOUSE_ADDRESS"),"null")){
                value.add("");
            }else{
                value.add(data.getString("HOUSE_ADDRESS"));
            }
            value.add(data.getString("CUSTSERVICENAME"));
            value.add(data.getString("SMJRLC_FINISHTIME"));
            value.add(data.getString("STYLEPRINT_CREATE_TIME"));
            value.add(data.getString("FUNCPRINT_CREATE_TIME"));
            value.add(data.getString("HZHK_FINISHTIME"));
            value.add(data.getString("APSJS_FINISHTIME"));
            value.add(data.getString("EXPERIENCE_TIME"));
            value.add(data.getString("CITYCABINNAMES"));
            value.add(data.getString("EXPERIENCE"));
            value.add(data.getString("VISITCOUNT"));
            value.add(data.getString("TAG_NAME"));
            values.add(value);

        }

        HSSFWorkbook excel = ExcelExport.getDataExcel("数据", titles, values);

        String fileName = "动作检查"+ ".xls";
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
