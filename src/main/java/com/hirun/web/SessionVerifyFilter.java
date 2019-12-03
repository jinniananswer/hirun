package com.hirun.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.hirun.web.biz.sso.RestResult;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author jinnian
 * @Date 2018/4/20 16:38
 * @Description:
 */
public class SessionVerifyFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SessionVerifyFilter.class);

    private Pattern excepUrlPattern;

    private String forwardUrl;

    private String sessionKey;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String excepUrlRegex = filterConfig.getInitParameter("excepUrlRegex");
        this.forwardUrl = filterConfig.getInitParameter("forwardUrl");
        this.sessionKey = filterConfig.getInitParameter("sessionKey");
        if (!StringUtils.isBlank(excepUrlRegex)) {
            excepUrlPattern = Pattern.compile(excepUrlRegex);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getServletPath();
        String uri = request.getRequestURI();

        if (!uri.contains(".jsp")) {
            if (excepUrlPattern.matcher(servletPath).matches() || uri.contains(".css") || uri.contains(".js") || uri.contains(".png") || uri.contains(".jpg") || uri.contains("gif") || uri.contains("ttf") || uri.contains(".apk") || uri.contains("download.html") || uri.contains(".ppt") || uri.contains(".doc") || uri.contains(".pdf")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        HttpSession session = request.getSession(false);
        // Object object = session.getAttribute(this.sessionKey);

        if (null == session) {

            String hirunToken = request.getParameter("hirun-token");

            if (StringUtils.isNotBlank(hirunToken)) {
                String username = authentication(hirunToken);
                if (StringUtils.isNotBlank(username)) {
                    if (autoLogin(request, username)) {
                        log.info("新系统打开老系统页面成功！ username: {}, hirun-sid: {}", username, hirunToken);
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    } else {
                        log.error("自动登录失败！username: {}", username);
                    }
                } else {
                    log.error("认证失败！hirunToken: {}", hirunToken);

                }
                return;
            }

            //检查是否有自动登陆
            if (isAutomaticAuth(request)) {
                filterChain.doFilter(servletRequest, servletResponse);
                //response.sendRedirect(request.getContextPath()+"/index");
                return;
            }


            if (StringUtils.isBlank(this.forwardUrl)) {
                this.forwardUrl = "/login";
            }
            response.sendRedirect(request.getContextPath() + this.forwardUrl);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 调用主营业务体统的 Rest 接口，对令牌进行认证
     *
     * @param hirunToken
     * @return 成功返回用户名，失败返回空
     */
    private String authentication(String hirunToken) {

        StringBuilder sb = new StringBuilder();
        HttpURLConnection conn = null;
        BufferedReader br = null;
        try {
            URL restURL = new URL("http://47.105.64.145:80/hirun/api/system/session/authentication/" + hirunToken);
            conn = (HttpURLConnection) restURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            IOUtils.closeQuietly(br);
        }

        RestResult restResult = JSONObject.parseObject(sb.toString(), RestResult.class);
        if (0 == restResult.getCode()) {
            String username = restResult.getRows().getUsername();
            return username;
        }

        return null;
    }

    /**
     * 自动登录（LayUI集成）
     *
     * @param request
     * @param username
     * @return
     */
    private boolean autoLogin(HttpServletRequest request, String username) {

        log.info("autoLogin: " + username);

        HttpSession session = request.getSession();
        Map<String, String> parameter = new HashMap(16);
        parameter.put("username", username);
        ServiceResponse response = null;
        try {
            response = ServiceClient.call("OrgCenter.login.LoginService.autoLogin", parameter, session);
        } catch (Exception e) {
            log.error("SessionVerifyFilter 自动登录失败！-----------", e);
            return false;
        }

        if (response.isSuccess()) {

            JSONObject userInfo = response.getJSONObject("USER");
            JSONObject employeeInfo = response.getJSONObject("EMPLOYEE");
            JSONArray jobRoles = response.getJSONArray("JOB_ROLE");
            UserEntity user = new UserEntity(JSON.parseObject(userInfo.toJSONString(), Map.class));
            EmployeeEntity employee = new EmployeeEntity(JSON.parseObject(employeeInfo.toJSONString(), Map.class));
            SessionEntity sessionEntity = new SessionEntity();

            session.setAttribute("USER", user);
            session.setAttribute("EMPLOYEE", employee);
            session.setAttribute("JOB_ROLE", jobRoles);
            if (user != null) {
                sessionEntity.setUserId(user.getUserId());
                sessionEntity.setUsername(user.getUserName());
                sessionEntity.put("USER", userInfo);
            }

            if (employee != null) {
                sessionEntity.put("EMPLOYEE_ID", employee.getEmployeeId());
                sessionEntity.put("EMPLOYEE_NAME", employee.getName());
                sessionEntity.put("EMPLOYEE", employeeInfo);
            }

            if (ArrayTool.isNotEmpty(jobRoles)) {
                sessionEntity.put("JOB_ROLES", jobRoles);
            }

            HttpSessionManager.putSessionEntity(session.getId(), sessionEntity);

            session.setAttribute("USER", user);
            return true;
        }

        return false;
    }

    private boolean isAutomaticAuth(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie auth = null;

        if (cookies == null) {
            return false;
        }

        for (Cookie cookie : cookies) {
            if (StringUtils.equals("auth", cookie.getName())) {
                auth = cookie;
                break;
            }
        }

        if (auth == null) {
            return false;
        }

        String[] values = auth.getValue().split("@");
        if (values == null || values.length != 2) {
            return false;
        }

        String username = values[0];
        String password = values[1];

        HttpSession session = request.getSession();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put("username", username);
        parameter.put("password", password);
        parameter.put("IS_ENCRPT", "1");

        ServiceResponse response = null;
        try {
            response = ServiceClient.call("OrgCenter.login.LoginService.login", parameter, session);
        } catch (Exception e) {
            log.error("SessionVerifyFilter自动登录失败-----------", e);
            return false;
        }

        if (response.isSuccess()) {

            JSONObject userInfo = response.getJSONObject("USER");
            JSONObject employeeInfo = response.getJSONObject("EMPLOYEE");
            JSONArray jobRoles = response.getJSONArray("JOB_ROLE");
            UserEntity user = new UserEntity(JSON.parseObject(userInfo.toJSONString(), Map.class));
            EmployeeEntity employee = new EmployeeEntity(JSON.parseObject(employeeInfo.toJSONString(), Map.class));
            SessionEntity sessionEntity = new SessionEntity();

            session.setAttribute("USER", user);
            session.setAttribute("EMPLOYEE", employee);
            session.setAttribute("JOB_ROLE", jobRoles);
            if (user != null) {
                sessionEntity.setUserId(user.getUserId());
                sessionEntity.setUsername(user.getUserName());
                sessionEntity.put("USER", userInfo);
            }

            if (employee != null) {
                sessionEntity.put("EMPLOYEE_ID", employee.getEmployeeId());
                sessionEntity.put("EMPLOYEE_NAME", employee.getName());
                sessionEntity.put("EMPLOYEE", employeeInfo);
            }

            if (ArrayTool.isNotEmpty(jobRoles)) {
                sessionEntity.put("JOB_ROLES", jobRoles);
            }

            HttpSessionManager.putSessionEntity(session.getId(), sessionEntity);

            session.setAttribute("USER", user);
            return true;
        }

        return false;
    }

    @Override
    public void destroy() {

    }

    public static void main(String[] args) {
        String excepUrlRegex = "/(login|register|loginPost|login.jsp|register.jsp|common.jsp|phone_include.jsp|refreshCache|websocketServer.*)";
        String excepUrlRegex2 = "^/websocketServer.*";
        Pattern excepUrlPattern = Pattern.compile(excepUrlRegex);
        System.out.println(excepUrlPattern.matcher("/websocketServer/123").matches());
    }
}
