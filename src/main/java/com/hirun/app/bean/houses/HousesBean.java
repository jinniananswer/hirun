package com.hirun.app.bean.houses;

import com.hirun.app.dao.houses.HouseDAO;
import com.hirun.pub.domain.entity.houses.HousesEntity;
import com.most.core.app.database.dao.factory.DAOFactory;

import java.util.List;

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
}
