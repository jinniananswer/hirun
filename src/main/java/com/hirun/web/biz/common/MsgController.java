package com.hirun.web.biz.common;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by pc on 2018-06-09.
 */
@Controller
public class MsgController {

    @RequestMapping("/common/msg/queryMsgListByRecvId")
    public @ResponseBody String queryMsgListByRecvId(@RequestParam Map pageData) throws Exception{
        ServiceResponse response = ServiceClient.call("Common.msg.MsgService.queryMsgListByRecvId", pageData);
        return response.toJsonString();
    }

    @RequestMapping("/common/msg/getUnReadMsgCount")
    public @ResponseBody String getUnReadMsgCount(@RequestParam Map pageData) throws Exception{
        ServiceResponse response = ServiceClient.call("Common.msg.MsgService.getUnReadMsgCount", pageData);
        return response.toJsonString();
    }

    @RequestMapping("/common/msg/setMsgListToRead")
    public @ResponseBody String setMsgListToRead(@RequestParam Map pageData) throws Exception{
        ServiceResponse response = ServiceClient.call("Common.msg.MsgService.setMsgListToRead", pageData);
        return response.toJsonString();
    }
}
