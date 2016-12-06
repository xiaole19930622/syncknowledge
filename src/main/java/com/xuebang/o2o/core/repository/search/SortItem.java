package com.xuebang.o2o.core.repository.search;

/**
 * 排序
 * Created by xuwen on 2015/4/1.
 */
public class SortItem {

    private String field; // 排序属性

    private String type = "desc"; // 排序规则，默认倒序

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
