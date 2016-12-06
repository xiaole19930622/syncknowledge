package com.xuebang.o2o.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xuwen on 2015-5-13.
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 忽略空值拷贝属性
     * @param source
     * @param taget
     * @throws BeansException
     */
    public static void copyPropertiesIgnoreNull(Object source, Object taget) throws BeansException {
        copyProperties(source,taget,getNullPropertyNames(source));
    }

    /**
     * 获取值为空的属性名数组
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
