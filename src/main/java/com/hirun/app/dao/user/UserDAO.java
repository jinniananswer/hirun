package com.hirun.app.dao.user;

import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 9:56
 * @Description:
 */
@DatabaseName("ins")
public class UserDAO extends StrongObjectDAO {

    public UserDAO(String databaseName){
        super(databaseName);
    }

    public UserEntity queryUserByUsername(String username) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USERNAME", username);
        List<UserEntity> users = this.query(UserEntity.class, "ins_user", parameter);
        if(ArrayTool.isEmpty(users))
            return null;
        return users.get(0);
    }

    public UserEntity queryUserByPk(String userId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USER_ID", userId);
        UserEntity user = this.queryByPk(UserEntity.class, "ins_user", parameter);
        return user;
    }

    public UserEntity queryUserByMobileNo(String mobileNo) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("MOBILE_NO", mobileNo);
        List<UserEntity> users = this.query(UserEntity.class, "ins_user", parameter);
        if(ArrayTool.isEmpty(users))
            return null;
        return users.get(0);
    }

    public UserEntity queryUserByEmployeeId(String employeeId) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.* FROM ins_user a, ins_employee b ");
        sb.append(" WHERE a.user_id = b.user_id ");
        sb.append(" AND b.employee_id = :EMPLOYEE_ID ");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("EMPLOYEE_ID", employeeId);
        List<UserEntity> users = this.queryBySql(UserEntity.class, sb.toString(), parameter);
        if(ArrayTool.isEmpty(users))
            return null;
        return users.get(0);
    }
}
