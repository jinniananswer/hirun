package com.hirun.app.biz.organization.personnel;

import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.exception.GenericException;
import com.most.core.pub.tools.security.Encryptor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
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
