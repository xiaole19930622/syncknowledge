package com.xuebang.o2o.configuration;

import com.xuebang.o2o.business.entity.OperateLog;
import com.xuebang.o2o.business.service.OperateLogService;
import com.xuebang.o2o.business.service.realm.RealmUtils;
import com.xuebang.o2o.core.SpringContextHolder;
import com.xuebang.o2o.core.web.WapperedResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by xuwen on 2015/7/18.
 */
public class OperateLogFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(OperateLogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        if (StringUtils.isBlank(servletPath)
                || servletPath.startsWith("/WEB-INF")
                || servletPath.startsWith("/login")
                || servletPath.startsWith("/logout")
                || servletPath.startsWith("/app/user/login")
                || servletPath.startsWith("/index")
                || servletPath.startsWith("/static")
                || servletPath.startsWith("/thirdpart")) {
            filterChain.doFilter(request, response);
            return;
        }
        WapperedResponse wapper = new WapperedResponse(response);
        OperateLog log = new OperateLog();
        log.setRequestPath(servletPath);
        StringBuffer inputParameterSb = new StringBuffer();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement();
            String value = request.getParameter(key.toString());
            inputParameterSb.append(key + "=" + value);
            inputParameterSb.append(";");
        }
        log.setRequestParams(inputParameterSb.toString());
        log.setRequestTime(new Date());
        filterChain.doFilter(request, wapper);//往下服务层请求，完成请求后返回这里
        log.setResponseTime(new Date());
        log.setProcessTime(log.getResponseTime().getTime() - log.getRequestTime().getTime());
        byte[] responseData = wapper.getResponseData();
        log.setResponseLength(new Long(responseData.length));
        log.setUser(RealmUtils.getCurrentUser());
        log.setUserAgent(request.getHeader("USER-AGENT"));
        log.setIpAddress(request.getRemoteAddr());
        // 输出处理后的数据(参考资料 http://www.java3z.com/cwbwebhome/article/article8/899.html?id=2380)
        ServletOutputStream output = response.getOutputStream();
        output.write(responseData);
        output.flush();
        output.close();
//        logger.info(log.toString());
        SpringContextHolder.getBean(OperateLogService.class).save(log);
    }

    @Override
    public void destroy() {

    }
}
