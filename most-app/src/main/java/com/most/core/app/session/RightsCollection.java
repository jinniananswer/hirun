package com.most.core.app.session;

import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public class RightsCollection {

    private Map<String, String> rights;

    private RightsCollection() throws Exception{
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USER_ID", userId);

        StringBuilder sb = new StringBuilder();
        sb.append("select b.func_id, b.func_code ");
        sb.append("from ins.ins_user_func a, sys.sys_func b ");
        sb.append("where b.func_id = a.func_id ");
        sb.append("and b.status = '0' ");
        sb.append("and a.status = '0' ");
        sb.append("and a.user_id = :USER_ID");

        GenericDAO dao = new GenericDAO("all");
        this.rights = new HashMap<String, String>();
        RecordSet recordSet = dao.queryBySql(sb.toString(), parameter);
        if(recordSet == null || recordSet.size() <= 0)
            return;

        int size = recordSet.size();
        for(int i=0;i<size;i++){
            Record record = recordSet.get(i);
            rights.put(record.get("FUNC_ID"), record.get("FUNC_CODE"));
        }
    }

    public static RightsCollection getInstance() throws Exception{
        AppSession session = SessionManager.getSession();
        if(session.getRights() != null)
            return session.getRights();
        else{
            RightsCollection rights = new RightsCollection();
            session.setRights(rights);
            return rights;
        }
    }

    public boolean hasFuncId(String funcId){
        return rights.containsKey(funcId);
    }

    public boolean hasFuncCode(String funcCode){
        return rights.containsValue(funcCode);
    }
}
