package com.xuebang.o2o.core.repository.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Long型ID实体
 * Created by xuwen on 2015/3/29.
 */
@MappedSuperclass
public class LongIdEntity implements Serializable{

    protected Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
