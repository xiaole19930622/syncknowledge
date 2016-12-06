package com.xuebang.o2o.business.entity;

import com.xuebang.o2o.core.repository.entity.LongIdEntity;

import javax.persistence.Entity;
import java.util.Date;

/**
 * 用户操作记录
 * Created by xuwen on 2015/4/26.
 */
@Entity
public class OperateRecord extends LongIdEntity {

    private String name; // 记录的类型

    private String val; // 用户选取的值

    private String createUser; // 操作人

    private Date createTime; // 操作时间

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
