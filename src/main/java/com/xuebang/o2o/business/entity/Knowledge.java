package com.xuebang.o2o.business.entity;

import com.xuebang.o2o.core.repository.entity.LongIdEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Administrator on 2016/7/26.
 */
@Entity
public class Knowledge extends LongIdEntity{

    private String name;

    private String section;//学段

    private String number;

    private String subject;

    private Knowledge parent;

    private Long parentId;

    private Integer sort;

    private Integer isLeaf; //是否叶子节点 0 不是  1是



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @ManyToOne
    @JoinColumn
    public Knowledge getParent() {
        return parent;
    }

    public void setParent(Knowledge parent) {
        this.parent = parent;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }


    @Override
    public String toString() {
        return "Knowledge{" +
                "number='" + number + '\'' +
                ", parent='" + parent + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }
}
