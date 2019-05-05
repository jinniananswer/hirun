package com.hirun.web.biz.organization.train;

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
 * @create: 2018-12-12 23:54
 **/
@Controller
public class TrainController extends RootController {

    @RequestMapping("/initCreateTrain")
    public @ResponseBody String initCreateTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initCreateTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/createTrain")
    public @ResponseBody String createTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.createTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initManagerTrains")
    public @ResponseBody String initManagerTrains(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initManagerTrains", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initQueryTrains")
    public @ResponseBody String initQueryTrains(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initQueryTrains", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToTrainDetail")
    public String redirectToTrainDetail(HttpServletRequest request) throws Exception {
        return "/biz/organization/train/train_detail";
    }

    @RequestMapping("/initTrainDetail")
    public @ResponseBody String initTrainDetail(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initTrainDetail", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/deleteTrain")
    public @ResponseBody String deleteTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.deleteTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToChangeTrain")
    public String redirectToChangeTrain(HttpServletRequest request) throws Exception {
        return "/biz/organization/train/change_train";
    }

    @RequestMapping("/initChangeTrain")
    public @ResponseBody String initChangeTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initChangeTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/changeTrain")
    public @ResponseBody String changeTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.changeTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/signTrain")
    public @ResponseBody String signTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.signTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initQuerySignList")
    public @ResponseBody String initQuerySignList(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initQuerySignList", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToSignList")
    public String redirectToSignList(HttpServletRequest request) throws Exception {
        return "/biz/organization/train/sign_list";
    }

    @RequestMapping("/deleteSignedEmployee")
    public @ResponseBody String deleteSignedEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.deleteSignedEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/endSign")
    public @ResponseBody String endSign(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.endSign", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToSign")
    public String redirectToSign(HttpServletRequest request) throws Exception {
        return "/biz/organization/train/choose_employee_sign_train";
    }

    @RequestMapping("/initChooseEmployeeSignTrain")
    public @ResponseBody String initChooseEmployeeSignTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initChooseEmployeeSignTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryWantSignEmployee")
    public @ResponseBody String queryWantSignEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.queryWantSignEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/signNewEmployee")
    public @ResponseBody String signNewEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.signNewEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/createPreworkEvaluation")
    public @ResponseBody String createPreworkEvaluation(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.createPreworkEvaluation", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToViewTrainNotice")
    public String redirectToViewTrainNotice(HttpServletRequest request) throws Exception {
        return "/biz/organization/train/view_train_notice";
    }

    @RequestMapping("/initViewTrainNotice")
    public @ResponseBody String initViewTrainNotice(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initViewTrainNotice", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initQueryMyTrain")
    public @ResponseBody String initQueryMyTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initQueryMyTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToTrainScoreManager")
    public String redirectToTrainScoreManager(HttpServletRequest request) throws Exception {
        return "/biz/organization/score/score_input";
    }

    @RequestMapping("/redirectToTrainScoreQuery")
    public String redirectToTrainScoreQuery(HttpServletRequest request) throws Exception {
        return "/biz/organization/score/score_query";
    }

    @RequestMapping("/exportSignList")
    public @ResponseBody void exportSignList(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", request.getParameter("TRAIN_ID"));
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initQuerySignList", parameter);

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
        titles.add("鸿扬工作年限");
        titles.add("社会工作年限");
        titles.add("毕业院校");
        titles.add("学历");
        titles.add("专业");
        titles.add("毕业证书编号");
        titles.add("入职日期");
        if(StringUtils.equals(train.getString("TYPE"), "3")) {
            titles.add("业绩");
        }

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
                value.add(data.getString("IN_DATE_DIFF"));
                value.add(data.getString("JOB_DATE_DIFF"));
                value.add(data.getString("SCHOOL"));
                value.add(data.getString("EDUCATION"));
                value.add(data.getString("MAJOR"));
                value.add(data.getString("CERTIFICATE_NO"));
                value.add(data.getString("IN_DATE"));
                if(StringUtils.equals(train.getString("TYPE"), "3")) {
                    value.add(data.getString("BUSI_GRADE"));
                }
                values.add(value);
            }
        }

        HSSFWorkbook excel = ExcelExport.getDataExcel(sheetName, titles, values);

        String fileName = "培训报名表-"+sheetName+".xls";
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
