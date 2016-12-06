package com.xuebang.o2o.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xuebang.o2o.core.repository.entity.UUIDEntity;
import com.xuebang.o2o.utils.AliyunOSSUtils;
import com.xuebang.o2o.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by xuwen on 2015/3/29.
 */
@Entity
public class User implements Serializable{

    private String id; // 用户ID

    private String username; // 用户名

    private String password; // 密码

    private String passwordSalt; // 加密盐

    private String name; // 姓名

    private Long score; // 积分

    private Set<Role> roles; // 用户权限

    private Organization organization; // 用户所属组织架构

    private String pictureUrl;  //头像

    private int status; // 账户状态 0-正常 1-冻结


    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPictureUrl() {
        return pictureUrl;
    }

    @Transient
    public String getPictureUrlDisplay(){
        if(StringUtils.isNotBlank(pictureUrl)){
            return AliyunOSSUtils.getSignatureURL(pictureUrl.replace(PropertiesUtils.getStringValue("oss.access.url.prefix"), ""));
        }
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Column(unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable
    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (passwordSalt != null ? !passwordSalt.equals(user.passwordSalt) : user.passwordSalt != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (passwordSalt != null ? passwordSalt.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
