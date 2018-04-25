package com.hirun.web.biz.cust;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by pc on 2018-04-24.
 */
@Controller
@ResponseBody
public class CustController extends RootController{

    @RequestMapping(value = "/cust/addCust", method = RequestMethod.POST)
    public void addCust(@RequestParam Map custInfo) throws Exception {
        logger.debug("-------------新增客户开始-------------");

        ServiceResponse response = ServiceClient.call("CustCenter.cust.CustService.addCust", custInfo);

        logger.debug("-------------新增客户结束-------------");
    }
}
