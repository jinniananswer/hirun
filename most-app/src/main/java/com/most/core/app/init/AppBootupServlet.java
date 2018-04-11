package com.most.core.app.init;

import com.most.core.app.database.conn.ConnectionFactory;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.wrapper.ConnectionWrapper;
import com.most.core.pub.data.RecordSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/1/23 22:21
 * @Description:
 */
public class AppBootupServlet extends GenericServlet{

    private Logger log = LogManager.getLogger(AppBootupServlet.class.getName());
    /**
     * app层初始化方法
     * @param config
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException{
        //1.初始化数据库连接
        if(log.isDebugEnabled()){
            log.debug("***************************数据库连接初始化开始***************************");
        }
        ConnectionFactory.init();
        if(log.isDebugEnabled()){
            log.debug("***************************数据库连接初始化结束***************************");
        }


        try {
            testQuery("5");
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    private void testQuery(String type) throws SQLException{

        ConnectionWrapper conn = ConnectionFactory.getConnection("hirun_sys");
        GenericDAO dao = new GenericDAO(conn);
        Map<String, String> param = new HashMap<String, String>();
        param.put("TYPE",type);
        RecordSet result = dao.query("sys_static_data", param);
        log.debug("result==============="+result.get(0, "CREATE_DATE"));
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

    }
}
