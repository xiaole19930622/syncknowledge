package com.xuebang.o2o.business.entity;

import com.xuebang.o2o.core.repository.entity.LongIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by xuwen on 2015/7/18.
 */
@Entity
public class OperateLog extends LongIdEntity {

    private String requestPath; // 请求接口

    private String requestParams; // 请求参数

    private Date requestTime; // 请求时间

    private Date responseTime; // 响应时间

    private Long responseLength; // 响应数据长度

    private Long processTime; // 处理时间

    private User user; // 用户

    private String userAgent;

    private String ipAddress; // 用户IP地址

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    @Column(length = 2000)
    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
        if(this.requestParams.length() > 2000){
            this.requestParams = this.requestParams.substring(0,2000);
        }
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public Long getResponseLength() {
        return responseLength;
    }

    public void setResponseLength(Long responseLength) {
        this.responseLength = responseLength;
    }

    public Long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Long processTime) {
        this.processTime = processTime;
    }

    @ManyToOne
    @JoinColumn
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "OperatorLog{" +
                "requestPath='" + requestPath + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", requestTime=" + requestTime +
                ", responseTime=" + responseTime +
                ", responseLength=" + responseLength +
                ", processTime=" + processTime +
                ", user=" + user +
                ", userAgent='" + userAgent + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
