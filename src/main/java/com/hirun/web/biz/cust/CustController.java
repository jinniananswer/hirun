package com.hirun.web.biz.cust;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-04-24.
 */
@Controller
@ResponseBody
public class CustController extends RootController{

    @RequestMapping(value = "/cust/addCust", method = RequestMethod.POST)
    public String addCust(@RequestParam Map custInfo) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.addCust", custInfo);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/editCust", method = RequestMethod.POST)
    public String editCust(@RequestParam Map custInfo) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.editCust", custInfo);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/queryCustList", method = RequestMethod.GET)
    public String queryCustList() throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.queryCustList", new HashMap());
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/addCustByNum", method = RequestMethod.POST)
    public String addCustByNum(@RequestParam Map pageData) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.addCustByNum", pageData);
        return response.toJsonString();
    }
}
