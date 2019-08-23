package com.hirun.web.biz.datacenter.custservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.office.ExcelExport;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping("/exportCustServiceMonStat")
    public @ResponseBody void exportCustServiceMonStat(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CUSTSERVICEEMPID", request.getParameter("CUSTSERVICEEMPID"));
        parameter.put("ORG_ID", request.getParameter("ORG_ID"));
        parameter.put("MON_DATE", request.getParameter("MON_DATE"));

        ServiceResponse response = ServiceClient.call("DataCenter.custservice.CustServiceReportService.queryCustServMonStatInfo", parameter);

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
}
