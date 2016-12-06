package com.xuebang.o2o.business.entity;

import com.xuebang.o2o.core.repository.entity.LongIdEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by xuwen on 2015/3/29.
 */
@Entity
public class Role extends LongIdEntity {

    private String name;

    private Set<Permission> permissions;

    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable
    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
