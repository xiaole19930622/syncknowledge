package com.xuebang.o2o.core.repository.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by xuwen on 2015/4/23.
 */
@MappedSuperclass
public class UUIDEntity implements Serializable {

    protected String id;

    @Id
    @GenericGenerator(name = "generator", strategy = "com.xuebang.o2o.core.repository.strategy.UUIDGenerator")
    @GeneratedValue(generator = "generator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
