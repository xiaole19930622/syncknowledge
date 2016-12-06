package com.xuebang.o2o.business.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专题知识点的基本信息
 * Created by xiaole on 2016/11/9.
 */
public class KnowBaseInfo {
    //学段
    private Integer section ;
    //科目
    private Integer subject ;

    //专题知识点序号
    private Integer knowledgeNumber ;


    // 章节名称  2  列的位置
    private Map<String , Integer> name2position = new HashMap<>();

    //列的位置   2  专题知识点对象
    private Map<Integer , Knowledge> position2Objec = new HashMap<>();

    // 存储 知识点名字的列的位置
    private List<Integer> nameColPostion = new ArrayList<>();

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public Map<String, Integer> getName2position() {
        return name2position;
    }

    public void setName2position(Map< String , Integer> name2position) {
        this.name2position = name2position;
    }

    public Map<Integer, Knowledge> getPosition2Objec() {
        return position2Objec;
    }

    public void setPosition2Objec(Map<Integer, Knowledge> position2Objec) {
        this.position2Objec = position2Objec;
    }

    public Integer getKnowledgeNumber() {
        return knowledgeNumber;
    }

    public void setKnowledgeNumber(Integer knowledgeNumber) {
        this.knowledgeNumber = knowledgeNumber;
    }

    public List<Integer> getNameColPostion() {
        return nameColPostion;
    }

    public void setNameColPostion(List<Integer> nameColPostion) {
        this.nameColPostion = nameColPostion;
    }

    @Override
    public String toString() {
        return "KnowBaseInfo{" +
                "section=" + section +
                ", subject=" + subject +
                ", knowledgeNumber=" + knowledgeNumber +
                ", name2position=" + name2position +
                ", position2Objec=" + position2Objec +
                '}';
    }
}
