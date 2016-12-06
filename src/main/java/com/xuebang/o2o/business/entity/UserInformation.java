package com.xuebang.o2o.business.entity;

import com.xuebang.o2o.core.repository.entity.LongIdEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Administrator on 2015/5/5.
 */
@Entity
public class UserInformation extends LongIdEntity{
    private String subject;
    private String teachingVersion;
    private String grade;
    private User user;


    @ManyToOne
    @JoinColumn
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeachingVersion() {
        return teachingVersion;
    }

    public void setTeachingVersion(String teachingVersion) {
        this.teachingVersion = teachingVersion;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
