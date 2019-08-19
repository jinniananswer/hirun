package com.hirun.app.bean.custservice;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.pub.consts.CustomerServiceConst;
import com.hirun.pub.domain.entity.custservice.PartyEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustServiceStatBean {


    public static void updateCustServiceStat(String employeeId,String actionCode) throws Exception {
        String monthDate=TimeTool.now("YYYYMM");
        CustomerServiceDAO dao=DAOFactory.createDAO(CustomerServiceDAO.class);
        RecordSet recordSet=dao.queryCustServMonStatInfo(employeeId,monthDate);
        if(recordSet.size()<=0){
            if(StringUtils.equals("GOODSEEGOODLIVE",actionCode)) {
                Map<String, String> custservicestat = new HashMap<String, String>();
                custservicestat.put("STAT_MONTH", monthDate);
                custservicestat.put("OBJECT_ID", employeeId);
                custservicestat.put("CONSULT_COUNT", "1");
                custservicestat.put("STYLE_COUNT", "0");
                custservicestat.put("FUNC_COUNT", "0");
                custservicestat.put("SCAN_COUNT", "0");
                custservicestat.put("XQLTE_COUNT", "0");
                custservicestat.put("SCANCITYHOUSE_COUNT", "0");
                custservicestat.put("STYLE_SCALE", "0.00%");
                custservicestat.put("FUNC_SCALE", "0.00%");
                custservicestat.put("XQLTE_SCALE", "0.00%");
                custservicestat.put("SCAN_SCALE", "0.00%");
                custservicestat.put("SCANCITYHOUSE_SCALE", "0.00%");
                dao.insert("stat_custservice_month", custservicestat);
            }
            if(StringUtils.equals("DKCSMW",actionCode)){
                    Map<String, String> custservicestat = new HashMap<String, String>();
                    custservicestat.put("STAT_MONTH", monthDate);
                    custservicestat.put("OBJECT_ID", employeeId);
                    custservicestat.put("CONSULT_COUNT", "0");
                    custservicestat.put("STYLE_COUNT", "0");
                    custservicestat.put("FUNC_COUNT", "0");
                    custservicestat.put("SCAN_COUNT", "0");
                    custservicestat.put("XQLTE_COUNT", "0");
                    custservicestat.put("SCANCITYHOUSE_COUNT", "1");
                    custservicestat.put("STYLE_SCALE", "0.00%");
                    custservicestat.put("FUNC_SCALE", "0.00%");
                    custservicestat.put("XQLTE_SCALE", "0.00%");
                    custservicestat.put("SCAN_SCALE", "0.00%");
                    custservicestat.put("SCANCITYHOUSE_SCALE", "100.00%");
                    dao.insert("stat_custservice_month", custservicestat);
            }
            if(StringUtils.equals("SCANDATA",actionCode)){
                Map<String, String> custservicestat = new HashMap<String, String>();
                custservicestat.put("STAT_MONTH", monthDate);
                custservicestat.put("OBJECT_ID", employeeId);
                custservicestat.put("CONSULT_COUNT", "1");
                custservicestat.put("STYLE_COUNT", "0");
                custservicestat.put("FUNC_COUNT", "0");
                custservicestat.put("XQLTE_COUNT", "0");
                custservicestat.put("SCAN_COUNT", "1");
                custservicestat.put("SCANCITYHOUSE_COUNT", "0");
                custservicestat.put("STYLE_SCALE", "0.00%");
                custservicestat.put("FUNC_SCALE", "0.00%");
                custservicestat.put("XQLTE_SCALE", "0.00%");
                custservicestat.put("SCAN_SCALE", "100.00%");
                custservicestat.put("SCANCITYHOUSE_SCALE", "0.00%");
                dao.insert("stat_custservice_month", custservicestat);
            }
            if (StringUtils.equals("XQLTEFUNC",actionCode)){
                Map<String, String> custservicestat = new HashMap<String, String>();
                custservicestat.put("STAT_MONTH", monthDate);
                custservicestat.put("OBJECT_ID", employeeId);
                custservicestat.put("CONSULT_COUNT", "0");
                custservicestat.put("STYLE_COUNT", "0");
                custservicestat.put("FUNC_COUNT", "1");
                custservicestat.put("SCAN_COUNT", "0");
                custservicestat.put("SCANCITYHOUSE_COUNT", "0");
                custservicestat.put("STYLE_SCALE", "0.00%");
                custservicestat.put("FUNC_SCALE", "100.00%");
                custservicestat.put("XQLTE_COUNT", "0");
                custservicestat.put("XQLTE_SCALE", "0.00%");
                custservicestat.put("SCAN_SCALE", "0.00%");
                custservicestat.put("SCANCITYHOUSE_SCALE", "0.00%");
                dao.insert("stat_custservice_month", custservicestat);
            }
            if (StringUtils.equals("XQLTESTYLE",actionCode)){
                Map<String, String> custservicestat = new HashMap<String, String>();
                custservicestat.put("STAT_MONTH", monthDate);
                custservicestat.put("OBJECT_ID", employeeId);
                custservicestat.put("CONSULT_COUNT", "0");
                custservicestat.put("STYLE_COUNT", "1");
                custservicestat.put("FUNC_COUNT", "0");
                custservicestat.put("SCAN_COUNT", "0");
                custservicestat.put("SCANCITYHOUSE_COUNT", "0");
                custservicestat.put("STYLE_SCALE", "100.00%");
                custservicestat.put("FUNC_SCALE", "0.00%");
                custservicestat.put("XQLTE_COUNT", "0");
                custservicestat.put("XQLTE_SCALE", "0.00%");
                custservicestat.put("SCAN_SCALE", "0.00%");
                custservicestat.put("SCANCITYHOUSE_SCALE", "0.00%");
                dao.insert("stat_custservice_month", custservicestat);
            }
            if (StringUtils.equals("XQLTE",actionCode)){
                Map<String, String> custservicestat = new HashMap<String, String>();
                custservicestat.put("STAT_MONTH", monthDate);
                custservicestat.put("OBJECT_ID", employeeId);
                custservicestat.put("CONSULT_COUNT", "0");
                custservicestat.put("STYLE_COUNT", "0");
                custservicestat.put("FUNC_COUNT", "0");
                custservicestat.put("SCAN_COUNT", "0");
                custservicestat.put("SCANCITYHOUSE_COUNT", "0");
                custservicestat.put("STYLE_SCALE", "0.00%");
                custservicestat.put("FUNC_SCALE", "0.00%");
                custservicestat.put("XQLTE_COUNT", "1");
                custservicestat.put("XQLTE_SCALE", "100.00%");
                custservicestat.put("SCAN_SCALE", "0.00%");
                custservicestat.put("SCANCITYHOUSE_SCALE", "0.00%");
                dao.insert("stat_custservice_month", custservicestat);
            }
        }else{
            if(StringUtils.equals("GOODSEEGOODLIVE",actionCode)){
                Record record=recordSet.get(0);
                Map<String, String> custservicestat = new HashMap<String, String>();
                DecimalFormat df = new DecimalFormat("0.00%");

                int counsultCount=Integer.parseInt(record.get("CONSULT_COUNT"))+1;
                int styleCount=Integer.parseInt(record.get("STYLE_COUNT"));
                int funcCount=Integer.parseInt(record.get("FUNC_COUNT"));
                int xqtleCount=Integer.parseInt(record.get("XQLTE_COUNT"));
                int scanCount=Integer.parseInt(record.get("SCAN_COUNT"));
                int scancityhouseCount=Integer.parseInt(record.get("SCANCITYHOUSE_COUNT"));


                String styleScale=df.format(styleCount/(counsultCount*1.0));
                String funcScale=df.format(funcCount/(counsultCount*1.0));
                String xqlteScale=df.format(xqtleCount/(counsultCount*1.0));
                String scanScale=df.format(scanCount/(counsultCount*1.0));
                String scanCityHouseScale=df.format(scancityhouseCount/(counsultCount*1.0));


                custservicestat.put("CONSULT_COUNT",counsultCount+"");
                custservicestat.put("STYLE_SCALE", styleScale);
                custservicestat.put("FUNC_SCALE", funcScale);
                custservicestat.put("XQLTE_SCALE", xqlteScale);
                custservicestat.put("SCAN_SCALE",scanScale);
                custservicestat.put("SCANCITYHOUSE_SCALE",scanCityHouseScale);
                custservicestat.put("OBJECT_ID",employeeId);
                custservicestat.put("STAT_MONTH",monthDate);

                dao.save("stat_custservice_month",new String[]{"OBJECT_ID","STAT_MONTH"},custservicestat);
            }
            if(StringUtils.equals("DKCSMW",actionCode)) {
                Record record=recordSet.get(0);
                Map<String, String> custservicestat = new HashMap<String, String>();
                DecimalFormat df = new DecimalFormat("0.00%");
                int scancityhouseCount=Integer.parseInt(record.get("SCANCITYHOUSE_COUNT"))+1;
                int counsultCount=Integer.parseInt(record.get("CONSULT_COUNT"));
                String scanCityHouseScale = "100.00%";

                if(counsultCount!=0) {
                    scanCityHouseScale=df.format(scancityhouseCount/(counsultCount*1.0));
                }
                custservicestat.put("OBJECT_ID",employeeId);
                custservicestat.put("STAT_MONTH",monthDate);
                custservicestat.put("SCANCITYHOUSE_COUNT",scancityhouseCount+"");
                custservicestat.put("SCANCITYHOUSE_SCALE",scanCityHouseScale);

                dao.save("stat_custservice_month",new String[]{"OBJECT_ID","STAT_MONTH"},custservicestat);
            }
            if(StringUtils.equals("SCANDATA",actionCode)){
                Record record=recordSet.get(0);
                Map<String, String> custservicestat = new HashMap<String, String>();
                DecimalFormat df = new DecimalFormat("0.00%");

                int counsultCount=Integer.parseInt(record.get("CONSULT_COUNT"))+1;
                int styleCount=Integer.parseInt(record.get("STYLE_COUNT"));
                int funcCount=Integer.parseInt(record.get("FUNC_COUNT"));
                int xqlteCount=Integer.parseInt(record.get("XQLTE_COUNT"));
                int scanCount=Integer.parseInt(record.get("SCAN_COUNT"))+1;
                int scancityhouseCount=Integer.parseInt(record.get("SCANCITYHOUSE_COUNT"));


                String styleScale=df.format(styleCount/(counsultCount*1.0));
                String funcScale=df.format(funcCount/(counsultCount*1.0));
                String xqlteScale=df.format(xqlteCount/(counsultCount*1.0));
                String scanScale=df.format(scanCount/(counsultCount*1.0));
                String scanCityHouseScale=df.format(scancityhouseCount/(counsultCount*1.0));

                custservicestat.put("CONSULT_COUNT",counsultCount+"");
                custservicestat.put("STYLE_SCALE", styleScale);
                custservicestat.put("FUNC_SCALE", funcScale);
                custservicestat.put("XQLTE_SCALE", xqlteScale);
                custservicestat.put("SCAN_SCALE",scanScale);
                custservicestat.put("SCAN_COUNT",scanCount+"");

                custservicestat.put("SCANCITYHOUSE_SCALE",scanCityHouseScale);
                custservicestat.put("OBJECT_ID",employeeId);
                custservicestat.put("STAT_MONTH",monthDate);
                dao.save("stat_custservice_month",new String[]{"OBJECT_ID","STAT_MONTH"},custservicestat);

            }
            if(StringUtils.equals("XQLTEFUNC",actionCode)){
                Record record=recordSet.get(0);
                Map<String, String> custservicestat = new HashMap<String, String>();
                DecimalFormat df = new DecimalFormat("0.00%");
                int counsultCount=Integer.parseInt(record.get("CONSULT_COUNT"));
                int funcCount=Integer.parseInt(record.get("FUNC_COUNT"))+1;
                String funcScale=df.format(funcCount/(counsultCount*1.0));
                custservicestat.put("FUNC_SCALE", funcScale);
                custservicestat.put("FUNC_COUNT",funcCount+"");
                custservicestat.put("OBJECT_ID",employeeId);
                custservicestat.put("STAT_MONTH",monthDate);
                dao.save("stat_custservice_month",new String[]{"OBJECT_ID","STAT_MONTH"},custservicestat);

            }
            if(StringUtils.equals("XQLTESTYLE",actionCode)){
                Record record=recordSet.get(0);
                Map<String, String> custservicestat = new HashMap<String, String>();
                DecimalFormat df = new DecimalFormat("0.00%");
                int counsultCount=Integer.parseInt(record.get("CONSULT_COUNT"));
                int styleCount=Integer.parseInt(record.get("STYLE_COUNT"))+1;
                String styleScale=df.format(styleCount/(counsultCount*1.0));
                custservicestat.put("STYLE_SCALE", styleScale);
                custservicestat.put("STYLE_COUNT",styleCount+"");
                custservicestat.put("OBJECT_ID",employeeId);
                custservicestat.put("STAT_MONTH",monthDate);
                dao.save("stat_custservice_month",new String[]{"OBJECT_ID","STAT_MONTH"},custservicestat);
            }
            if(StringUtils.equals("XQLTE",actionCode)){
                Record record=recordSet.get(0);
                Map<String, String> custservicestat = new HashMap<String, String>();
                DecimalFormat df = new DecimalFormat("0.00%");
                int counsultCount=Integer.parseInt(record.get("CONSULT_COUNT"));
                int xqlteCount=Integer.parseInt(record.get("XQLTE_COUNT"))+1;
                String xqlteScale=df.format(xqlteCount/(counsultCount*1.0));
                custservicestat.put("XQLTE_SCALE", xqlteScale);
                custservicestat.put("XQLTE_COUNT",xqlteCount+"");
                custservicestat.put("OBJECT_ID",employeeId);
                custservicestat.put("STAT_MONTH",monthDate);
                dao.save("stat_custservice_month",new String[]{"OBJECT_ID","STAT_MONTH"},custservicestat);
            }

            }
    }


}
