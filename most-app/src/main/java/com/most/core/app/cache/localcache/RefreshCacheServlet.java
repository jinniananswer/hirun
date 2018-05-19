package com.most.core.app.cache.localcache;

import com.most.core.app.cache.localcache.interfaces.IReadOnlyCache;
import com.most.core.app.database.conn.ConnectionFactory;
import com.most.core.app.service.register.ServiceRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author jinnian
 * @Date 2018/1/23 22:21
 * @Description:
 */
public class RefreshCacheServlet extends GenericServlet{

    private Logger log = LogManager.getLogger(RefreshCacheServlet.class.getName());

    public void init(ServletConfig config) throws ServletException{

    }


    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        String className = servletRequest.getParameter("className");
        Class clazz = null;
        try{
            clazz = Class.forName(className);
        } catch (Exception e){
            return;
        }

        IReadOnlyCache cache = null;
        try{
            cache = CacheFactory.getReadOnlyCache(clazz);
            cache.refresh();
        } catch(Exception e) {
            e.printStackTrace();
        }

        PrintWriter printWriter = servletResponse.getWriter();
        printWriter.println("cache[" + className + "]refresh ok");
        printWriter.println("total size : " + cache.size());
        printWriter.close();
    }
}
