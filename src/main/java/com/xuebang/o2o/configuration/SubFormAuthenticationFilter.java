package com.xuebang.o2o.configuration;

import com.xuebang.o2o.business.service.LoginLogService;
import com.xuebang.o2o.business.service.realm.RealmUtils;
import com.xuebang.o2o.core.SpringContextHolder;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * Created by xuwen on 2015-6-24.
 */
public class SubFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected void saveRequest(ServletRequest request) {
//        HttpServletRequest req = (HttpServletRequest) request;
//        if(!"XMLHttpRequest".equals(req.getHeader("X-Requested-With"))){
//            super.saveRequest(request);
//        }

        //任何条件下，不要记录登陆以前的url,要强制跳转到首页
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        LoginLogService loginLogService = SpringContextHolder.getBean(LoginLogService.class);
        loginLogService.log(RealmUtils.getCurrentUser(), request.getRemoteAddr());

        return super.onLoginSuccess(token, subject, request, response);
    }

}
