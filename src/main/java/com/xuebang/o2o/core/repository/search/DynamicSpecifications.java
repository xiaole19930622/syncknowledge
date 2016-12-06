package com.xuebang.o2o.core.repository.search;

import com.xuebang.o2o.business.enums.OrganizationType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 动态查询构造器
 * Created by xuwen on 2015/3/28.
 */
public class DynamicSpecifications {

    private static Logger logger = LoggerFactory.getLogger(DynamicSpecifications.class);

    /**
     * 通过SearchFilter构建动态查询
     *
     * @param filters searchFilter表达式对象
     * @param <T>
     * @return
     */
    public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (filters != null && !filters.isEmpty()) {
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    for (SearchFilter filter : filters) {
                        // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
                        String[] names = StringUtils.split(filter.fieldName, ".");
                        Path expression = root.get(names[0]);
                        for (int i = 1; i < names.length; i++) {
                            if (Collection.class.isAssignableFrom(expression.getJavaType())) {
                                Join parentJoin = null;
                                for (int j = 0; j < i; j++) {
                                    if(j == 0){
                                        parentJoin = root.join(names[j]);
                                    }else{
                                        parentJoin = parentJoin.join(names[j]);
                                    }
                                }
                                expression = parentJoin.get(names[i]);
                            }else{
                                expression = expression.get(names[i]);
                            }
                        }
                        if(expression.getJavaType().isEnum()){
                            filter.value = Enum.valueOf(expression.getJavaType(),filter.value.toString());
                        }
                        // logic operator
                        switch (filter.operator) {
                            case EQ:
                                predicates.add(builder.equal(expression, filter.value));
                                break;
                            case NEQ:
                                predicates.add(builder.notEqual(expression, filter.value));
                                break;
                            case LIKE:
                                predicates.add(builder.like(expression, "%" + filter.value + "%"));
                                break;
                            case LLIKE:
                                predicates.add(builder.like(expression, filter.value + "%"));
                                break;
                            case RLIKE:
                                predicates.add(builder.like(expression, "%" + filter.value));
                                break;
                            case GT:
                                predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
                                break;
                            case LT:
                                predicates.add(builder.lessThan(expression, (Comparable) filter.value));
                                break;
                            case GTE:
                                predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                            case LTE:
                                predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                        }
                    }

                    // 将所有条件用 and 联合起来
                    if (!predicates.isEmpty()) {
                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                    }
                }

                return builder.conjunction();
            }
        };
    }
}
