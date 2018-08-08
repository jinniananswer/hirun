package com.hirun.app.biz.organization.personnel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.authority.AuthorityJudgement;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.func.FuncDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.*;
import com.most.core.pub.exception.GenericException;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.security.Encryptor;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/5/18 19:30
 * @Description:
 */
public class PersonnelService extends GenericService {

    public ServiceResponse changePassword(ServiceRequest request) throws Exception{
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        UserDAO dao = new UserDAO("ins");
        UserEntity user = dao.queryUserByPk(userId);
        if(user == null)
            throw new GenericException("ORG.CHANGE.PASSWORD.001", "用户不存在");

        String userPassword = user.getPassword();
        String oldPassword = request.getString("OLD_PASSWORD");
        String encryptorOldPassword = Encryptor.encryptMd5(oldPassword);

        if(!StringUtils.equals(userPassword, encryptorOldPassword))
            throw new GenericException("ORG.CHANGE.PASSWORD.002", "原密码输入错误");
        String newPassword = request.getString("PASSWORD");
        String encryptNewPassword = Encryptor.encryptMd5(newPassword);
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PASSWORD", encryptNewPassword);
        parameter.put("USER_ID", userId);

        dao.save("ins_user", parameter);
        return new ServiceResponse();
    }
}
