package com.hirun.web.biz.family;


import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class FamilyController extends RootController {


    @RequestMapping(value = "createFamily")
    public @ResponseBody String createFamily(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.family.FamilyService.createFamily", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryParty4CreateFamily")
    public @ResponseBody String queryParty4CreateFamily(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.family.FamilyService.queryParty4CreateFamily", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "redirectPartyFamily")
    public String redirectPartyFamily(HttpServletRequest request) throws Exception {
        return "/biz/operations/family/family_member";
    }

    @RequestMapping(value = "initShowFamilyMember")
    public @ResponseBody String initShowFamilyMember(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.family.FamilyService.initShowFamilyMember", paramter);
        return response.toJsonString();
    }
}