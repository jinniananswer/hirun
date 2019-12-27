package com.most.core.app.session;

import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.SessionEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RightsCollection {

    private Set<String> rights;

    private Set<String> menus;

    private RightsCollection() throws Exception{
        SessionEntity sessionEntity = SessionManager.getSession().getSessionEntity();
        String userId = sessionEntity.getUserId();

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USER_ID", userId);

        GenericDAO insDAO = new GenericDAO("ins");
        StringBuilder sb = new StringBuilder();
        sb.append("select a.user_id, a.role_id ");
        sb.append("from ins_user_role a ");
        sb.append("where user_id = :USER_ID ");
        sb.append("and now() between start_date and end_date ");

        RecordSet userRoles = insDAO.queryBySql(sb.toString(), parameter);
        Record defaultRole = new Record();
        defaultRole.put("ROLE_ID", "2");
        defaultRole.put("USER_ID", userId);
        if (userRoles == null) {
            userRoles = new RecordSet();
        }
        userRoles.add(defaultRole);

        String roles = "";
        for (int i=0;i<userRoles.size();i++) {
            roles += userRoles.get(i).get("ROLE_ID") + ",";
        }
        roles = roles.substring(0, roles.length() - 1);

        StringBuilder funcRoleSql = new StringBuilder();
        funcRoleSql.append("select b.func_id, b.func_code ");
        funcRoleSql.append("from ins.ins_func_role a, sys.sys_func b ");
        funcRoleSql.append("where b.func_id = a.func_id ");
        funcRoleSql.append("and b.status = '0' ");
        funcRoleSql.append("and a.status = '0' ");
        funcRoleSql.append("and a.role_id in ("+roles+")");

        GenericDAO dao = new GenericDAO("all");
        this.rights = new HashSet<String>();
        RecordSet funcRoles = dao.queryBySql(sb.toString(), new HashMap<>());
        if (funcRoles != null) {
            for (int i=0;i<funcRoles.size();i++) {
                Record funcRole = funcRoles.get(i);
                this.rights.add(funcRole.get("FUNC_CODE"));
            }
        }

        StringBuilder menuRoleSql = new StringBuilder();
        menuRoleSql.append("select a.role_id, a.menu_id ");
        menuRoleSql.append("from ins_menu_role a ");
        menuRoleSql.append("where a.status = '0' ");
        menuRoleSql.append("and role_id in ("+roles+") ");

        RecordSet menuRoles = insDAO.queryBySql(menuRoleSql.toString(), new HashMap<>());
        this.menus = new HashSet<String>();
        if (menuRoles != null) {
            for (int i=0;i<menuRoles.size();i++) {
                Record menuRole = menuRoles.get(i);
                this.menus.add(menuRole.get("MENU_ID"));
            }
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

    public boolean hasFuncCode(String funcCode){
        return rights.contains(funcCode);
    }

    public boolean hasMenu(String menuId) {
        return this.menus.contains(menuId);
    }
}
