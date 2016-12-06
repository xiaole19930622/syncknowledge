package com.xuebang.o2o.business.service.realm;

import com.xuebang.o2o.business.entity.Permission;
import com.xuebang.o2o.business.entity.Role;
import com.xuebang.o2o.business.entity.User;
import com.xuebang.o2o.business.service.UserSerivce;
import com.xuebang.o2o.core.SpringContextHolder;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by Administrator on 2016/5/24.
 */
public class MyCasRealm extends CasRealm {

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        User user = SpringContextHolder.getBean(UserSerivce.class).findByUsername(username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        List<Role> roles = SpringContextHolder.getBean(RoleService.class).findByUseId(user.getId());
        for (Role role : user.getRoles()) {
            info.addRole(role.getName());
            for (Permission permission : role.getPermissions()) {
                info.addStringPermission(permission.getName());
            }
        }
        return info;
    }

}
