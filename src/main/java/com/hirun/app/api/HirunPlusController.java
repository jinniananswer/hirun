package com.hirun.app.api;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by pc on 2018-04-24.
 */
@Controller
@ResponseBody
public class HirunPlusController {

    @RequestMapping(value = "/api/hirunplus/syncSubscription")
    public String addCust(@RequestParam Map requestData) throws Exception {
        ServiceResponse response = ServiceClient.call("OutCenter.hirunplus.HirunPlusService.syncSubscription", requestData);
        return response.toJsonString();
    }
}
