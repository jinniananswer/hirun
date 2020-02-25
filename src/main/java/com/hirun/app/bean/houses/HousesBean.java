package com.hirun.app.bean.houses;

import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.dao.houses.HouseDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.pub.domain.entity.houses.HousesEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.exception.GenericException;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-06-03.
 */
public class HousesBean {

    public static HousesEntity getHousesEntityById(String housesId) throws Exception {
        HouseDAO houseDAO = DAOFactory.createDAO(HouseDAO.class);

        return houseDAO.getHousesEntityById(housesId);
    }

    public static List<HousesEntity> queryHousesEntityListByName(String housesName) throws Exception{
        HouseDAO dao = DAOFactory.createDAO(HouseDAO.class);
        return dao.queryHousesEntityListByName(housesName);
    }

    public static List<HousesEntity> queryScatterHouses(String name) throws Exception {
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        OrgEntity org = null;
        if (StringUtils.isNotBlank(orgId)) {
            OrgDAO dao = new OrgDAO("ins");
            org = dao.queryOrgById(orgId);
        }

        String city = null;
        boolean hasAllCity = Permission.hasAllCity();
        if (org != null && !hasAllCity) {
            city = org.getCity();
        }

        HouseDAO houseDAO = DAOFactory.createDAO(HouseDAO.class);
        return houseDAO.queryScatterHouses(name, city);
    }

    public static List<HousesEntity> queryMyScatterHouses(String name) throws Exception {
        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String employeeId = sessionEntity.get("EMPLOYEE_ID");
        String orgId = OrgBean.getOrgId(sessionEntity);
        OrgEntity org = null;
        if (StringUtils.isNotBlank(orgId)) {
            OrgDAO dao = new OrgDAO("ins");
            org = dao.queryOrgById(orgId);
        }

        String city = null;
        boolean hasAllCity = Permission.hasAllCity();
        if (org != null && !hasAllCity) {
            city = org.getCity();
        }

        HouseDAO houseDAO = DAOFactory.createDAO(HouseDAO.class);
        return houseDAO.queryMyScatterHouses(name, city, employeeId);
    }

    public static long createScatterHouse(String name, String city) throws Exception {
        Map<String, String> house = new HashMap<String, String>();
        AppSession session = SessionManager.getSession();
        String now = TimeTool.now();
        String orgId = OrgBean.getOrgId(session.getSessionEntity());
        OrgEntity org = null;
        if (StringUtils.isBlank(city) && StringUtils.isNotBlank(orgId)) {
            OrgDAO dao = new OrgDAO("ins");
            org = dao.queryOrgById(orgId);
            city = org.getCity();
        }

        house.put("NAME", name);
        house.put("CITY", city);
        house.put("NATURE", "3");//责任楼盘
        house.put("PLAN_IN_DATE", now);
        house.put("DESTROY_DATE", "3000-12-31 23:59:59");
        house.put("STATUS", "1");
        String userId = session.getSessionEntity().getUserId();
        house.put("CREATE_USER_ID", userId);
        house.put("UPDATE_USER_ID", userId);
        house.put("UPDATE_TIME", session.getCreateTime());
        house.put("CREATE_TIME", session.getCreateTime());

        HouseDAO dao = new HouseDAO("ins");
        List<HousesEntity> houses = dao.queryHousesByName(name, false);
        if(ArrayTool.isNotEmpty(houses)){
            throw new GenericException("HIRUN_CREATE_HOUSE_000001","该楼盘名称已经存在，请重新检查楼盘名称！");
        }
        long houseId = dao.insertAutoIncrement("ins_houses", house);
        return houseId;
    }

}
