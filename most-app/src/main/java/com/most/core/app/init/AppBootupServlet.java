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
            this.testDelete();
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

    private void testInsert() throws SQLException{
        ConnectionWrapper conn = ConnectionFactory.getConnection("hirun_sys");
        GenericDAO dao = new GenericDAO(conn);
        Map<String, String> param = new HashMap<String, String>();
        param.put("DATA_ID","5");
        param.put("TYPE","6");
        param.put("VALUE","7");
        param.put("PARENT_DATA_ID","2");
        param.put("CREATE_DATE","2018-04-11 16:41:00");
        int num = dao.insert("sys_static_data", param);
        log.debug("*******插入数据******"+num+"条");
    }

    private void testUpdate() throws SQLException{
        ConnectionWrapper conn = ConnectionFactory.getConnection("hirun_sys");
        GenericDAO dao = new GenericDAO(conn);
        Map<String, String> param = new HashMap<String, String>();
        param.put("DATA_ID","5");
        param.put("TYPE","7");
        param.put("VALUE","8");
        param.put("PARENT_DATA_ID","9");
        param.put("CREATE_DATE","2018-04-10 16:41:00");
        int num = dao.update("sys_static_data", param);
        log.debug("*******更新数据******"+num+"条");
    }

    private void testSave() throws SQLException{
        ConnectionWrapper conn = ConnectionFactory.getConnection("hirun_sys");
        GenericDAO dao = new GenericDAO(conn);
        Map<String, String> param = new HashMap<String, String>();
        param.put("DATA_ID","5");
        param.put("CREATE_DATE","2018-04-12 16:02:00");
        int num = dao.save("sys_static_data", param);
        log.debug("*******更新数据******"+num+"条");
    }

    private void testDelete() throws SQLException{
        ConnectionWrapper conn = ConnectionFactory.getConnection("hirun_sys");
        GenericDAO dao = new GenericDAO(conn);
        Map<String, String> param = new HashMap<String, String>();
        param.put("DATA_ID","5");
        int num = dao.delete("sys_static_data", param);
        log.debug("*******删除数据******"+num+"条");
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

    }
}
