package com.hirun.app.dao.custservice.citycabin;

import com.hirun.pub.domain.entity.custservice.PartyEntity;
import com.hirun.pub.domain.entity.custservice.ProjectEntity;
import com.hirun.pub.domain.entity.custservice.ProjectIntentionEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@DatabaseName("ins")
public class CityCabinDAO extends StrongObjectDAO {

    public CityCabinDAO(String databaseName) {
        super(databaseName);
    }

    public RecordSet getCityCabinByCityId(String city_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_citycabin  ");
        sb.append(" where BIZ_CITY= :BIZ_CITY ");
        sb.append(" and SCAN_END_TIME > now()  ");
        parameter.put("BIZ_CITY",city_id);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public RecordSet queryCityCabinInfo(String city_id,String houseAddress,String shop,String style,String custName,String isVaild) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_citycabin  ");
        sb.append(" where BIZ_CITY= :BIZ_CITY ");

        if(StringUtils.isNotBlank(houseAddress)){
            sb.append("and CITYCABIN_ADDRESS like concat('%',:CITYCABIN_ADDRESS,'%') ");
            parameter.put("CITYCABIN_ADDRESS",houseAddress);
        }

        if (StringUtils.isNotBlank(shop)) {
            sb.append("and SHOP like concat('%',:SHOP,'%') ");
            parameter.put("SHOP", shop);
        }

        if (StringUtils.isNotBlank(style)) {
            sb.append("and CITYCABIN_TITLE like concat('%',:CITYCABIN_TITLE,'%') ");
            parameter.put("CITYCABIN_TITLE", style);
        }

        if (StringUtils.isNotBlank(custName)) {
            sb.append("and CUST_NAME like concat('%',:CUST_NAME,'%') ");
            parameter.put("CUST_NAME", custName);
        }

        if(StringUtils.equals("1",isVaild)){
            sb.append(" and SCAN_END_TIME > now()  ");
            sb.append(" and SCAN_END_TIME > SCAN_START_TIME ");

        }

        if(StringUtils.equals("2",isVaild)){
            sb.append(" and SCAN_END_TIME < now()  ");
        }

        parameter.put("BIZ_CITY",city_id);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

}