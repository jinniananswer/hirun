package com.hirun.web.biz.organization.prework;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.office.ExcelExport;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2019-01-27 00:35
 **/
@Controller
public class PreWorkController extends RootController {

    @RequestMapping("/initQueryPreWorkEvaluation")
    public @ResponseBody String initQueryPreWorkEvaluation(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initQueryPreWorkEvaluation", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToViewPreWorkNotice")
    public String redirectToViewPreWorkNotice(HttpServletRequest request) throws Exception {
        return "/biz/organization/prework/show_prework_evaluation_notice";
    }

    @RequestMapping("/initViewPreWorkNotice")
    public @ResponseBody String initViewPreWorkNotice(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initViewPreWorkNotice", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToSignPreWork")
    public String redirectToSignPreWork(HttpServletRequest request) throws Exception {
        return "/biz/organization/prework/choose_employee_sign_prework";
    }

    @RequestMapping("/initChooseEmployeeSignPreWork")
    public @ResponseBody String initChooseEmployeeSignPreWork(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initChooseEmployeeSignPreWork", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/addNewPreworkSign")
    public @ResponseBody String addNewPreworkSign(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.addNewPreworkSign", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToPreworkSignList")
    public String redirectToPreworkSignList(HttpServletRequest request) throws Exception {
        return "/biz/organization/prework/prework_sign_list";
    }

    @RequestMapping("/initQueryPreworkSignList")
    public @ResponseBody String initQueryPreworkSignList(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initQueryPreworkSignList", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initViewPreworkCourseware")
    public @ResponseBody String initViewPreworkCourseware(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initViewPreworkCourseware", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/showOnlineScore")
    public @ResponseBody String showOnlineScore(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.showOnlineScore", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToPreworkScoreManager")
    public String redirectToPreworkScoreManager(HttpServletRequest request) throws Exception {
        return "/biz/organization/score/score_input";
    }

    @RequestMapping("/redirectToPreworkScoreQuery")
    public String redirectToPreworkScoreQuery(HttpServletRequest request) throws Exception {
        return "/biz/organization/score/score_query";
    }

    @RequestMapping("/exportPreworkSignList")
    public @ResponseBody void exportPreworkSignList(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", request.getParameter("TRAIN_ID"));
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initQueryPreworkSignList", parameter);

        JSONObject train = response.getJSONObject("TRAIN");
        JSONObject signs = response.getJSONObject("SIGN_LIST");
        String sheetName = train.getString("NAME");

        if(signs == null || signs.size() <= 0){
            return;
        }

        List<String> titles = new ArrayList<String>();
        titles.add("姓名");
        titles.add("归属公司");
        titles.add("归属部门");
        titles.add("性别");
        titles.add("工作岗位");
        titles.add("联系电话");
        titles.add("毕业院校");
        titles.add("学历");
        titles.add("专业");
        titles.add("毕业证书编号");
        titles.add("入职日期");
        titles.add("考试类型");
        titles.add("考试科目");
        titles.add("是否食堂就餐");

        Set<String> companys = signs.keySet();
        List<List<String>> values = new ArrayList<List<String>>();
        for(String companyName : companys) {
            JSONArray datas = signs.getJSONArray(companyName);

            if(ArrayTool.isEmpty(datas)) {
                continue;
            }
            int size = datas.size();
            for(int i=0;i<size;i++) {
                JSONObject data = datas.getJSONObject(i);
                List<String> value = new ArrayList<String>();
                value.add(data.getString("NAME"));
                value.add(data.getString("ENTERPRISE_NAME"));
                value.add(data.getString("ALL_ORG_NAME"));
                String sex = data.getString("SEX");
                String sexName = "";
                if(StringUtils.equals("1", sex)) {
                    sexName = "男";
                }
                else {
                    sexName = "女";
                }
                value.add(sexName);
                value.add(data.getString("JOB_ROLE_NAME"));
                value.add(data.getString("MOBILE_NO"));
                value.add(data.getString("SCHOOL"));
                value.add(data.getString("EDUCATION"));
                value.add(data.getString("MAJOR"));
                value.add(data.getString("CERTIFICATE_NO"));
                value.add(data.getString("IN_DATE"));

                String type = data.getString("TYPE");
                if (StringUtils.equals("0", type)) {
                    value.add("初次考评");
                }
                else if (StringUtils.equals("1", type)) {
                    value.add("补考");
                }
                else if (StringUtils.equals("2", type)) {
                    value.add("转岗专业考评");
                }
                else if (StringUtils.equals("3", type)) {
                    value.add("复职考评");
                }
                else {
                    value.add("初次考评");
                }

                if(StringUtils.equals("1", type) || StringUtils.equals("3", type)) {
                    String examItem = "";
                    String examItemComm = data.getString("EXAM_ITEM_COMM");
                    String examItemPro = data.getString("EXAM_ITEM_PRO");
                    if(StringUtils.equals("true", examItemComm)) {
                        examItem += "通用";
                    }

                    if(StringUtils.equals("true", examItemPro)) {
                        if(examItem.length() > 0) {
                            examItem += "|专业";
                        }
                        else {
                            examItem += "专业";
                        }
                    }
                    value.add(examItem);
                }
                else {
                    value.add("通用|专业");
                }

                String inCanteen = data.getString("IN_CANTEEN");
                if(StringUtils.equals("1", inCanteen)) {
                    value.add("是");
                }
                else {
                    value.add("否");
                }
                values.add(value);
            }
        }

        HSSFWorkbook excel = ExcelExport.getDataExcel(sheetName, titles, values);

        String fileName = "岗前考评报名表-"+sheetName+".xls";
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
