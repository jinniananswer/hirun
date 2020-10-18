package com.hirun.app.bean.out;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.out.DataGetInfoDAO;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.RecordSet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-21.
 */
public class OutBean {

    public static void insertDataGetInfo(String api, String requestData, String indbTime, long totalNum) throws Exception{
        DataGetInfoDAO dataGetInfoDAO = DAOFactory.createDAO(DataGetInfoDAO.class);
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("API", api);
        JSONObject reqestData = new JSONObject();
        parameter.put("REQUEST_DATA", requestData);
        parameter.put("INDB_TIME", indbTime);
        parameter.put("TOTAL_NUM", String.valueOf(totalNum));
        dataGetInfoDAO.insert("OUT_DATA_GET_INFO", parameter);
    }

    public static boolean isExistData4Reg(String subscribeTime,String openid) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("SUBSCRIBE_TIME", subscribeTime);
        parameter.put("OPENID", openid);

        //查在线表
        sql.append(" select * from out_hirunplus_reg ");
        sql.append(" where 1=1 ");
        sql.append(" and subscribe_time = :SUBSCRIBE_TIME");
        sql.append(" and openid = :OPENID");
        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_reg ");
            sql.append(" where 1=1 ");
            sql.append(" and subscribe_time = :SUBSCRIBE_TIME");
            sql.append(" and openid = :OPENID");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }

    public static boolean isExistData4Reg(String openid) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("OPENID", openid);

        //查在线表
        sql.append(" select * from out_hirunplus_reg ");
        sql.append(" where 1=1 ");
        sql.append(" and openid = :OPENID");
        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_reg ");
            sql.append(" where 1=1 ");
            sql.append(" and openid = :OPENID");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }

    public static boolean isExistData4Scan(String openId,String staffId, String addTime) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("OPENID", openId);
        parameter.put("STAFF_ID", staffId);
        parameter.put("ADD_TIME", addTime);

        //查在线表
        sql.append(" select * from out_his_hirunplus_scan ");
        sql.append(" where 1=1 ");
        sql.append(" and OPENID = :OPENID");
        sql.append(" and STAFF_ID = :STAFF_ID");
        sql.append(" and ADD_TIME = :ADD_TIME");
        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_scan ");
            sql.append(" where 1=1 ");
            sql.append(" and OPENID = :OPENID");
            sql.append(" and STAFF_ID = :STAFF_ID");
            sql.append(" and ADD_TIME = :ADD_TIME");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }

    public static boolean isExistData4CustServiceScan(String openId,String staffId, String addTime) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("OPENID", openId);
        parameter.put("STAFF_ID", staffId);
        parameter.put("ADD_TIME", addTime);

        //查在线表
        sql.append(" select * from out_hirunplus_custservice_scan ");
        sql.append(" where 1=1 ");
        sql.append(" and OPENID = :OPENID");
        sql.append(" and STAFF_ID = :STAFF_ID");
        sql.append(" and ADD_TIME = :ADD_TIME");
        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_custservice_scan ");
            sql.append(" where 1=1 ");
            sql.append(" and OPENID = :OPENID");
            sql.append(" and STAFF_ID = :STAFF_ID");
            sql.append(" and ADD_TIME = :ADD_TIME");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }


    public static boolean isExistData4Projects(String openId,String staffId, String commTime) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("OPENID", openId);
        parameter.put("STAFF_ID", staffId);
        parameter.put("COMM_TIME", commTime);

        //查在线表
        sql.append(" select * from out_hirunplus_projects ");
        sql.append(" where 1=1 ");
        sql.append(" and OPENID = :OPENID");
        sql.append(" and STAFF_ID = :STAFF_ID");
        sql.append(" and COMM_TIME = :COMM_TIME");
        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_projects ");
            sql.append(" where 1=1 ");
            sql.append(" and OPENID = :OPENID");
            sql.append(" and STAFF_ID = :STAFF_ID");
            sql.append(" and COMM_TIME = :COMM_TIME");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }

    public static boolean isExistData4Commends(String openId,String staffId, String modeTime) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("OPENID", openId);
        parameter.put("STAFF_ID", staffId);
        parameter.put("MODE_TIME", modeTime);

        //查在线表
        sql.append(" select * from out_hirunplus_commends ");
        sql.append(" where 1=1 ");
        sql.append(" and OPENID = :OPENID");
        sql.append(" and STAFF_ID = :STAFF_ID");
        sql.append(" and MODE_TIME = :MODE_TIME");
        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_commends ");
            sql.append(" where 1=1 ");
            sql.append(" and OPENID = :OPENID");
            sql.append(" and STAFF_ID = :STAFF_ID");
            sql.append(" and MODE_TIME = :MODE_TIME");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }

    public static boolean isExistData4Yjal(String openId,String staffId, String createTime) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("OPENID", openId);
        parameter.put("STAFF_ID", staffId);
        parameter.put("CREATE_TIME", createTime);

        //查在线表
        sql.append(" select * from out_hirunplus_yjal ");
        sql.append(" where 1=1 ");
        sql.append(" and OPENID = :OPENID");
        sql.append(" and STAFF_ID = :STAFF_ID");
        sql.append(" and CREATE_TIME = :CREATE_TIME");
        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_yjal ");
            sql.append(" where 1=1 ");
            sql.append(" and OPENID = :OPENID");
            sql.append(" and STAFF_ID = :STAFF_ID");
            sql.append(" and CREATE_TIME = :CREATE_TIME");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }

    public static boolean isExistData4SignIn(String openId, String sign_time) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("OPEN_ID", openId);
       // parameter.put("STAFF_ID", staffId);
        parameter.put("SIGN_TIME", sign_time);

        //查在线表
        sql.append(" select * from out_hirunplus_signpc ");
        sql.append(" where 1=1 ");
        sql.append(" and OPEN_ID = :OPEN_ID");
       // sql.append(" and STAFF_ID = :STAFF_ID");
        sql.append(" and SIGN_TIME = :SIGN_TIME");
        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_signpc ");
            sql.append(" where 1=1 ");
            sql.append(" and OPEN_ID = :OPEN_ID");
            sql.append(" and SIGN_TIME = :SIGN_TIME");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }

    public static boolean isExistData4XQLTE(String openId,String staffId, String updateTime) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("OPEN_ID", openId);
        parameter.put("STAFF_ID", staffId);
        parameter.put("LT2_UPDATE_TIME", updateTime);


        //查在线表
        sql.append(" select * from out_hirunplus_xqlte ");
        sql.append(" where 1=1 ");
        sql.append(" and OPEN_ID = :OPEN_ID");
        sql.append(" and SJS_STAFF_ID = :STAFF_ID");
        sql.append(" and LT2_UPDATE_TIME = :LT2_UPDATE_TIME");

        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_xqlte ");
            sql.append(" where 1=1 ");
            sql.append(" and OPEN_ID = :OPEN_ID");
            sql.append(" and SJS_STAFF_ID = :STAFF_ID");
            sql.append(" and LT2_UPDATE_TIME = :LT2_UPDATE_TIME");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }

    public static boolean isExistData4ProdShare(String openId,String staffId, String openDate) throws Exception{
        boolean isExist = false;
        GenericDAO dao = new GenericDAO("out");

        StringBuilder sql = new StringBuilder();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("OPEN_ID", openId);
        parameter.put("STAFF_ID", staffId);
        parameter.put("OPEN_DATE", openDate);


        //查在线表
        sql.append(" select * from out_hirunplus_product_share ");
        sql.append(" where 1=1 ");
        sql.append(" and OPEN_ID = :OPEN_ID");
        sql.append(" and STAFF_ID = :STAFF_ID");
        sql.append(" and OPEN_DATE = :OPEN_DATE");

        RecordSet set = dao.queryBySql(sql.toString(), parameter);
        if(set == null || set.size() == 0) {
            //查历史表
            sql = new StringBuilder();
            sql.append(" select * from out_his_hirunplus_product_share ");
            sql.append(" where 1=1 ");
            sql.append(" and OPEN_ID = :OPEN_ID");
            sql.append(" and STAFF_ID = :STAFF_ID");
            sql.append(" and OPEN_DATE = :OPEN_DATE");
            set = dao.queryBySql(sql.toString(), parameter);
            if(set != null && set.size() > 0) {
                isExist = true;
            }
        } else {
            isExist = true;
        }

        return isExist;
    }
}
