package com.xuebang.o2o.business.service.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import java.io.Serializable;
import java.util.*;

/**
 * Created by xuwen on 2015/4/13.
 */
public class ShiroCasRealm extends org.apache.shiro.cas.CasRealm {

    /**
     * 登录函数
     * 当用户需要登录而缓存中无登录信息时调用此函数
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AuthenticationInfo info = super.doGetAuthenticationInfo(token);
        List list = info.getPrincipals().asList();
        Map<String,String> attrs = (Map<String,String>)list.get(1);
        System.out.println(attrs);
        return info;
    }

    /**
     * 鉴权函数
     * 当用户需要访问权限而缓存中不存在该权限时调用此函数获取角色权限
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return super.doGetAuthorizationInfo(principals);
    }

}
