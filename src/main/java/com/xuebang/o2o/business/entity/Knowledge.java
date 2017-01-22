package com.xuebang.o2o.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xuebang.o2o.core.repository.entity.TreeIdEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */
@Entity
public class Knowledge extends TreeIdEntity<Knowledge>{

    private String name;

    private String sectionId;//学段

    private String number;

    private String subjectId;

    private Knowledge parent;


    private Integer sort;

    private Integer isLeaf; //是否叶子节点 0 不是  1是

    private List<Knowledge> children;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @ManyToOne
    @JoinColumn
    public Knowledge getParent() {
        return parent;
    }

    @Override
    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    public List<Knowledge> getChildren() {
        return children;
    }

    public void setChildren(List<Knowledge> children) {
        this.children = children;
    }

    public void setParent(Knowledge parent) {
        this.parent = parent;
    }


    @Override
    public String toString() {
        return "Knowledge{" +
                "number='" + number + '\'' +
                ", parent='" + parent + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public int getSort() {
        return sort == null ? 0 : sort.intValue();
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
