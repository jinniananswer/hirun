package com.most.core.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @Author jinnian
 * @Date 2018/4/20 16:38
 * @Description:
 */
public class SessionVerifyFilter implements Filter{

    private Pattern excepUrlPattern;

    private String forwardUrl;

    private String sessionKey;

    public void init(FilterConfig filterConfig) throws ServletException {
        String excepUrlRegex = filterConfig.getInitParameter("excepUrlRegex");
        this.forwardUrl = filterConfig.getInitParameter("forwardUrl");
        this.sessionKey = filterConfig.getInitParameter("sessionKey");
        if (!StringUtils.isBlank(excepUrlRegex)) {
            excepUrlPattern = Pattern.compile(excepUrlRegex);
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Logger log = LogManager.getLogger(SessionVerifyFilter.class.getName());
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String servletPath = request.getServletPath();
        String uri = request.getRequestURI();

        log.debug("===========================servletpath============================="+servletPath);
        if (excepUrlPattern.matcher(servletPath).matches() || uri.contains(".css") || uri.contains(".js") || uri.contains(".png") || uri.contains(".jpg")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpSession session = request.getSession();
        Object object = session.getAttribute(this.sessionKey);
        if(object == null){
            if(StringUtils.isBlank(this.forwardUrl))
                this.forwardUrl = "/login";
            response.sendRedirect(request.getContextPath()+this.forwardUrl);
        }
        else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void destroy() {

    }
}
