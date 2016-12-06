package com.xuebang.o2o.business.service.realm;

import com.xuebang.o2o.business.entity.User;
import com.xuebang.o2o.business.service.UserSerivce;
import com.xuebang.o2o.core.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuwen on 2015/4/16.
 */
public class RealmUtils {

    /**
     * 获取当前用户ID
     * @return
     */
    public static String getCurrentUserId(){
        return getCurrentUserAttributes().get("id") == null ? null : getCurrentUserAttributes().get("id");
    }

    /**
     * 获取当前用户
     * @return
     */
    public static User getCurrentUser(){
        String id = getCurrentUserId();
        if(StringUtils.isNotBlank(id)){
            UserSerivce userSerivce = SpringContextHolder.getBean(UserSerivce.class);
            return userSerivce.findById(id);
        }
        return null;
    }

    /**
     * 获取当前用户数据
     * local目前包含{id,username,name}
     * cas回传目前包含{id,username,name,contact}
     * @return
     */
    public static Map<String,String> getCurrentUserAttributes(){
        if(SecurityUtils.getSubject().getPrincipals() != null && SecurityUtils.getSubject().getPrincipals().asList().size() > 1){ // cas
            return (Map<String, String>) SecurityUtils.getSubject().getPrincipals().asList().get(1);
        }else{ // local
            Map<String,String> map = new HashMap<String,String>(0);
            if(SecurityUtils.getSubject().getPrincipals()!=null){
                User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
                map.put("id",user.getId().toString());
                map.put("name",user.getName());
                map.put("username",user.getUsername());
            }

            return map;
        }
    }
}
