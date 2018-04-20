package com.hirun.app.dao.user;

import com.hirun.pub.domain.entity.user.UserEntity;
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
}
