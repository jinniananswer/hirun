package com.most.core.app.init;

import com.most.core.app.database.conn.ConnectionFactory;
import com.most.core.app.service.register.ServiceRegister;
import com.most.core.pub.backgroundtask.BackgroundTaskFactory;
import com.most.core.pub.config.VersionConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.Version;

import javax.servlet.*;
import java.io.IOException;

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

        //2.完成所有服务注册
        ServiceRegister.register();

        //3.获取版本号
        VersionConfig.init();
        if(log.isDebugEnabled()){
            log.debug("***************************版本号:"+VersionConfig.getVersion() +"***************************");
        }

        //4.启后台任务
        BackgroundTaskFactory.start();
    }


    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

    }
}
