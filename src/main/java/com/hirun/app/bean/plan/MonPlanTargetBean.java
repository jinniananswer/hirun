package com.hirun.app.bean.plan;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.plan.MonPlanTargetDAO;
import com.hirun.pub.domain.entity.plan.MonPlanTargetEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.tools.time.TimeTool;

/**
 * Created by awx on 2018/7/21/021.
 */
public class MonPlanTargetBean {

    public static void setMonPlanTarget(JSONObject target, String objType, String[] objs, String targetTimeType, String targetTimeValue) throws Exception {
        MonPlanTargetDAO monPlanTargetDAO = DAOFactory.createDAO(MonPlanTargetDAO.class);
        String userId = SessionManager.getSession().getSessionEntity().getUserId();
        String now = TimeTool.now();

        for(String obj : objs) {
            MonPlanTargetEntity monPlanTargetEntity = monPlanTargetDAO.getMonPlanTargetEntityByObjTypeAndObj(objType, obj, targetTimeType, targetTimeValue);
            if(monPlanTargetEntity == null) {
                monPlanTargetEntity = new MonPlanTargetEntity();
                monPlanTargetEntity.setEffectObjType(objType);
                monPlanTargetEntity.setEffectObj(obj);
                monPlanTargetEntity.setTargetValue(target.toJSONString());
                monPlanTargetEntity.setTargetTimeType(targetTimeType);
                monPlanTargetEntity.setTargetTimeValue(targetTimeValue);
                monPlanTargetEntity.setCreateUserId(userId);
                monPlanTargetEntity.setCreateDate(now);
                monPlanTargetEntity.setUpdateUserId(userId);
                monPlanTargetEntity.setUpdateTime(now);

                monPlanTargetDAO.insertAutoIncrement("INS_MON_PLAN_TARGET", monPlanTargetEntity.getContent());
            } else {
                monPlanTargetEntity.setTargetValue(target.toJSONString());
                monPlanTargetEntity.setUpdateUserId(userId);
                monPlanTargetEntity.setUpdateTime(now);

                monPlanTargetDAO.update("INS_MON_PLAN_TARGET", monPlanTargetEntity.getContent());
            }
        }
    }
}
