package com.xuebang.o2o.business.entity;

import com.xuebang.o2o.core.repository.entity.LongIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by xuwen on 2015/3/29.
 */
@Entity
public class Permission extends LongIdEntity {

    private String name;

    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
