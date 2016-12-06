package com.xuebang.o2o.core.repository.search;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * Created by xuwen on 2015/3/28.
 */
public class SearchFilter {

    public enum Operator {
        EQ,NEQ, LIKE, LLIKE, RLIKE, GT, LT, GTE, LTE
    }

    public String fieldName;
    public Object value;
    public Operator operator;

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    /**
     * searchParams中key的格式为OPERATOR_FIELDNAME
     */
    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();

        for (Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtils.isBlank((String) value)) {
                continue;
            }
            if(value.equals("false")){
                value=false;
            }else if(value.equals("true")){
                value=true;
            }

            // 拆分operator与filedAttribute
            String[] names = StringUtils.split(key, "_");
            if (names.length != 2) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }
            String filedName = names[1];
            Operator operator = Operator.valueOf(names[0]);

            // 创建searchFilter
            SearchFilter filter = new SearchFilter(filedName, operator, value);
            filters.put(key, filter);
        }

        return filters;
    }
}
