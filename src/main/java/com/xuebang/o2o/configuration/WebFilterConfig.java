package com.xuebang.o2o.configuration;

import com.xuebang.o2o.business.service.realm.ShiroDbRealm;
import com.xuebang.o2o.utils.PropertiesUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Apache Shiro 安全配置
 * Created by xuwen on 2015/3/29.
 */
@Configuration
public class WebFilterConfig {

    private static Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

    private String ssoRemoteUrl = PropertiesUtils.getStringValue("xuebangsoft.sso.remote.url"); // 单点登录远程服务地址
    private String ssoLocalUrl = PropertiesUtils.getStringValue("xuebangsoft.sso.local.url"); // 单点登录本地认证地址

    /**
     * Shiro过滤器
     *
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());
//        shiroFilterFactoryBean.setLoginUrl(ssoRemoteUrl + "?service=" + ssoLocalUrl);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403.jsp");
        CasFilter casFilter = new CasFilter();
        casFilter.setFailureUrl("/400.jsp");
        filterMap.put("casFilter", casFilter);
        filterMap.put("authc", new SubFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
//        filterChainDefinitionMap.put("/shiro-cas", "casFilter");
//        filterChainDefinitionMap.put("/thirdpart/**", "anon");
//        filterChainDefinitionMap.put("/static/**", "anon");
//        filterChainDefinitionMap.put("/user/login", "anon");
//        filterChainDefinitionMap.put("/**/favicon.ico", "anon");
//        filterChainDefinitionMap.put("/webjars/**", "anon");
//        filterChainDefinitionMap.put("/spiderInterface/**", "anon");
//        filterChainDefinitionMap.put("/mobileInterface/**", "anon");
//        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean(name = "operatorLogFilter")
    public OperateLogFilter getOperatorLogFilter(){
        return new OperateLogFilter();
    }

    /**
     * Ehcache缓存
     *
     * @return
     */
    @Bean(name = "shiroEhcacheManager")
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }


    /**
     * Shiro生命周期管理器 Required
     *
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 默认代理
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    /**
     * 安全管理器
     *
     * @return
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(getShiroRealm());
        dwsm.setCacheManager(getEhCacheManager());
        return dwsm;
    }

    /**
     * 认证处理器
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }

    /**
     * Shiro Realm
     *
     * @return
     */
    @Bean(name = "shiroRealm", autowire = Autowire.BY_TYPE)
    public Realm getShiroRealm() {
        return new ShiroDbRealm();
//        ShiroCasRealm realm = new ShiroCasRealm();
//        realm.setCasServerUrlPrefix(ssoRemoteUrl);
//        realm.setCasService(ssoLocalUrl);
//        return realm;
    }

}