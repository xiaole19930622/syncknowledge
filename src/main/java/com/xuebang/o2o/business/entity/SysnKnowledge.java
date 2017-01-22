package com.xuebang.o2o.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xuebang.o2o.core.repository.entity.LongIdEntity;
import com.xuebang.o2o.core.repository.entity.TreeIdEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */
@Entity
@Table(name = "sync_knowledge")
public class SysnKnowledge extends TreeIdEntity<SysnKnowledge>{


    private String number;//本知识点的序号

    private String name;

    private String sectionId;//学段

    private String subjectId;

    private SysnKnowledge parent;

    private List<SysnKnowledge> children; // 子节点


    private String publishVersion;//教材版本

    private String bookVersion;//书本


    private String  knowName ; //专题知识点名字
    private String  knowNumber;//专题知识点序号


    private Knowledge knowledge;//专题知识点id

    private Integer sort;

    private Integer isLeaf;//是否叶子节点 0 不是  1是

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

    @ManyToOne
    @JoinColumn
    public SysnKnowledge getParent() {
        return parent;
    }


    public void setChildren(List<SysnKnowledge> children) {
        this.children = children;
    }


    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    public List<SysnKnowledge> getChildren() {
        return children;
    }

    public void setParent(SysnKnowledge parent) {
        this.parent = parent;
    }


    public String getPublishVersion() {
        return publishVersion;
    }

    public void setPublishVersion(String publishVersion) {
        this.publishVersion = publishVersion;
    }

    public String getBookVersion() {
        return bookVersion;
    }

    public void setBookVersion(String bookVersion) {
        this.bookVersion = bookVersion;
    }

    public String getKnowName() {
        return knowName;
    }

    public void setKnowName(String knowName) {
        this.knowName = knowName;
    }

    public String getKnowNumber() {
        return knowNumber;
    }

    public void setKnowNumber(String knowNumber) {
        this.knowNumber = knowNumber;
    }



    public int getSort() {
        return sort == null ? 0 : sort.intValue() ;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @ManyToOne
    @JoinColumn
    public Knowledge getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
    }
}
