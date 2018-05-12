package com.hirun.web.biz.cust;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by pc on 2018-04-24.
 */
@Controller
@ResponseBody
public class CustController extends RootController{

    @RequestMapping(value = "/cust/addCust", method = RequestMethod.POST)
    public String addCust(@RequestParam Map pageData) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        //TODO 后续优化
        pageData.put("HOUSE_COUNSELOR_ID", sessionEntity.get("EMPLOYEE_ID"));

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
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        pageData.put("HOUSE_COUNSELOR_ID", sessionEntity.get("EMPLOYEE_ID"));

        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.queryCustList", pageData);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/addCustByNum", method = RequestMethod.POST)
    public String addCustByNum(@RequestParam Map pageData) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
        //TODO 后续优化
        pageData.put("HOUSE_COUNSELOR_ID", sessionEntity.get("EMPLOYEE_ID"));

        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.addCustByNum", pageData);
        return response.toJsonString();
    }

    @RequestMapping(value = "/cust/getCustById", method = RequestMethod.GET)
    public String getCustById(@RequestParam Map map) throws Exception {
        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.getCustById", map);
        return response.toJsonString();
    }
}
