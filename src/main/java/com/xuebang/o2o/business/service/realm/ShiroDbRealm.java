package com.xuebang.o2o.business.service.realm;

import com.xuebang.o2o.business.entity.Permission;
import com.xuebang.o2o.business.entity.Role;
import com.xuebang.o2o.business.entity.User;
import com.xuebang.o2o.business.service.UserSerivce;
import com.xuebang.o2o.core.SpringContextHolder;
import com.xuebang.o2o.core.repository.search.DynamicSpecifications;
import com.xuebang.o2o.core.repository.search.SearchFilter;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Shiro DB realm
 * Created by xuwen on 2015/4/10.
 */
public class ShiroDbRealm extends AuthorizingRealm {

    private UserSerivce userSerivce;

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        user = SpringContextHolder.getBean(UserSerivce.class).findById(user.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
            info.addRole(role.getName());
            for (Permission permission : role.getPermissions()) {
                info.addStringPermission(permission.getName());
            }
        }
        return info;
    }

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("username", SearchFilter.Operator.EQ, token.getUsername()));
        Specification<User> spec = DynamicSpecifications.bySearchFilter(filters);
        User user = SpringContextHolder.getBean(UserSerivce.class).findBySpecification(spec);
        if (user != null) {
            if(user.getStatus() == 1){
                throw new AuthenticationException("账号已冻结");
            }
            return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getPasswordSalt()), getName());
        } else {
            return null;
        }
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserSerivce.HASH_ALGORITHM);
        matcher.setHashIterations(UserSerivce.HASH_INTERATIONS);
//        setCredentialsMatcher(matcher);
    }

}
