package com.hirun.web.biz.operations.houses;

import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/26 10:17
 * @Description:
 */
@Controller
public class HousesController extends RootController{

    @RequestMapping("/houses/queryHousesByName")
    public @ResponseBody String queryHousesByName(@RequestParam Map condition) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryHousesByName", condition);

        return response.toJsonString();
    }

    @RequestMapping("/houses/queryHousesByNameNotSan")
    public @ResponseBody String queryHousesByNameNotSan(@RequestParam Map condition) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryHousesByNameNotSan", condition);

        return response.toJsonString();
    }

    @RequestMapping("/houses/queryScatterHouses")
    public @ResponseBody String queryScatterHouses(@RequestParam Map condition) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryScatterHouses", condition);

        return response.toJsonString();
    }
}
