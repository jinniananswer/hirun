package com.hirun.web.biz.custservice;

import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.domain.entity.session.BizSessionEntity;
import com.hirun.pub.tool.PlanTool;
import com.most.core.app.service.invoker.ServiceInvoker;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CustServiceController extends RootController {

    @RequestMapping(value = "initCsrTraceFlow")
    public @ResponseBody
    String initCsrTraceFlow(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.initCsrTraceFlow", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryGZGZHDetailByPartyId")
    public @ResponseBody
    String queryGZGZHDetailByPartyId(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.queryGZGZHDetailByPartyId", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryDesignerByEmployeeId")
    public @ResponseBody
    String queryDesignerByEmployeeId(@RequestParam Map paramter) throws Exception {

        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.queryDesignerByEmployeeId", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "chooseDesigner")
    public @ResponseBody
    String chooseDesigner(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.chooseDesigner", paramter);

        return response.toJsonString();
    }

    @RequestMapping(value = "initCreateGoodSeeLiveInfo")
    public @ResponseBody
    String initCreateGoodSeeLiveInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.initCreateGoodSeeLiveInfo", paramter);

        return response.toJsonString();
    }

    @RequestMapping(value = "createGoodSeeLiveInfo")
    public @ResponseBody
    String createGoodSeeLiveInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.createGoodSeeLiveInfo", paramter);

        return response.toJsonString();
    }

    @RequestMapping(value = "initPartyManager")
    public @ResponseBody
    String initPartyManager(@RequestParam Map paramter) throws Exception {

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());

        paramter.put("IN_MODE_CODE","0");
        paramter.put("EMPLOYEE_ID",sessionEntity.get("EMPLOYEE_ID"));
        JSONObject jsonObject = new JSONObject(paramter);
        ServiceRequest request = new ServiceRequest(jsonObject);
        try {
            //初始化的时候调家网接口进行扫码数据查询，保证数据实时性
            ServiceResponse hirunresponse = ServiceInvoker.invoke("OperationCenter.custservice.ScanDataImportTaskService.hirunplusDataImport", request);
        }catch (Exception e){
            e.printStackTrace();
        }
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.initPartyManager", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryPartyByCustServicerEmployeeId")
    public @ResponseBody
    String queryPartyByCustServicerEmployeeId(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.queryPartyByCustServicerEmployeeId", paramter);

        return response.toJsonString();
    }

    @RequestMapping(value = "redirectToProjectFlow")
    public String redirectToProjectFlow(HttpServletRequest request) throws Exception {
        return "/biz/operations/customerservice/csr_traceflow";
    }

    @RequestMapping(value = "initChangeGoodSeeLiveInfo")
    public @ResponseBody String initChangeGoodSeeLiveInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.initChangeGoodSeeLiveInfo", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "changeGoodSeeLiveInfo")
    public @ResponseBody String changeGoodSeeLiveInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.changeGoodSeeLiveInfo", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "redirectToChangeGoodSeeLiveInfo")
    public String redirectToChangeGoodSeeLiveInfo(HttpServletRequest request) throws Exception {
        return "/biz/operations/customerservice/change_goodseeliveinfo";

    }

    @RequestMapping(value = "queryPartyInfo4ChangeCustService")
    public @ResponseBody String queryPartyInfo4ChangeCustService(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.queryPartyInfo4ChangeCustService", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "initchangecustservice")
    public @ResponseBody String initchangecustservice(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.initchangecustservice", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "searchNewCustService")
    public @ResponseBody String searchNewCustService(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.searchNewCustService", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryCityCabinByName")
    public @ResponseBody String queryCityCabinByName(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.queryCityCabinByName", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "confirmChangeCustService")
    public @ResponseBody String confirmChangeCustService(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.confirmChangeCustService", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "confirmScanCityCabin")
    public @ResponseBody String confirmScanCityCabin(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.confirmScanCityCabin", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "redirectToCustClear")
    public String redirectToCustClear(HttpServletRequest request) throws Exception {
        return "/biz/operations/customerservice/cust_clear";
    }

    @RequestMapping(value = "initQueryForCustClear")
    public @ResponseBody String initQueryForCustClear(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.initQueryForCustClear", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "submitCustClearApply")
    public @ResponseBody String submitCustClearApply(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.submitCustClearApply", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "initCustServiceAudit")
    public @ResponseBody String initCustServiceAudit(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.initCustServiceAudit", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryApplyInfo4Audit")
    public @ResponseBody String queryApplyInfo4Audit(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.queryApplyInfo4Audit", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "auditConfirm")
    public @ResponseBody String auditConfirm(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.auditConfirm", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "redirectPartyVisit")
    public String redirectPartyVisit(HttpServletRequest request) throws Exception {
        return "/biz/operations/customerservice/party_visit";
    }

    @RequestMapping(value = "initQuery4PartyVisit")
    public @ResponseBody String initQuery4PartyVisit(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.initQuery4PartyVisit", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "addPartyVisit")
    public @ResponseBody String addPartyVisit(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.addPartyVisit", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "initPartyTagManager")
    public @ResponseBody String initPartyTagManager(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.initPartyTagManager", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "submitPartyTagInfo")
    public @ResponseBody String submitPartyTagInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.CustServService.submitPartyTagInfo", paramter);
        return response.toJsonString();
    }
}