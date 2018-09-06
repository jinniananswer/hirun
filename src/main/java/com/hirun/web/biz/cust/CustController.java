package com.hirun.web.biz.cust;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
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
}
