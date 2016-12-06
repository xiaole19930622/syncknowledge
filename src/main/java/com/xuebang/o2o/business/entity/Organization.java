package com.xuebang.o2o.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xuebang.o2o.business.enums.OrganizationType;
import com.xuebang.o2o.core.repository.strategy.TreeEntityWithoutIdStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * 组织架构
 * Created by xuwen on 2015/4/23.
 */
@Entity
public class Organization extends TreeEntityWithoutIdStrategy<Organization> {

    private String name; // 节点名称

    private Organization parent; // 父节点

    private List<Organization> children; // 子节点

    private String parentId; // 父节点ID
    private String regionId;//归属地区
    private String remark;
    private String orgLevel;
    private Integer orgOrder;
    private OrganizationType orgType;
    private String address;//地址
    private String contact;//电话
    private String customerPoolName;//CUSTOMER_POOL_NAME
    private String isPublicPool;//IS_PUBLIC_POOL
    private String accessRoles;//ACCESS_ROLES



    private int sort; // 序号

    @Override
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Column(name = "parentID", length = 32)

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "remark", length = 100)

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the orgLevel
     */
    @Column(name = "orgLevel", length = 50)
    public String getOrgLevel() {
        return orgLevel;
    }

    /**
     * @param orgLevel the orgLevel to set
     */
    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    /**
     * @return the orgOrder
     */
    @Column(name = "orgOrder")
    public Integer getOrgOrder() {
        return orgOrder;
    }

    /**
     * @param orgOrder the orgOrder to set
     */
    public void setOrgOrder(Integer orgOrder) {
        this.orgOrder = orgOrder;
    }

    /**
     * @return the orgType
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "orgType")
    public OrganizationType getOrgType() {
        return orgType;
    }

    /**
     * @param orgType the orgType to set
     */
    public void setOrgType(OrganizationType orgType) {
        this.orgType = orgType;
    }

    @Column(name = "REGION_ID")
    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "CONTACT")
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Column(name = "CUSTOMER_POOL_NAME")
    public String getCustomerPoolName() {
        return customerPoolName;
    }

    public void setCustomerPoolName(String customerPoolName) {
        this.customerPoolName = customerPoolName;
    }

    @Column(name = "IS_PUBLIC_POOL")
    public String getIsPublicPool() {
        return isPublicPool;
    }

    public void setIsPublicPool(String isPublicPool) {
        this.isPublicPool = isPublicPool;
    }

    @Column(name = "ACCESS_ROLES")
    public String getAccessRoles() {
        return accessRoles;
    }

    public void setAccessRoles(String accessRoles) {
        this.accessRoles = accessRoles;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn
    public Organization getParent() {
        return parent;
    }

    public void setParent(Organization parent) {
        this.parent = parent;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    public List<Organization> getChildren() {
        return children;
    }

    public void setChildren(List<Organization> children) {
        this.children = children;
    }


}
