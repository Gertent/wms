package com.rmd.wms.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liu on 2017/2/28.
 */
public class WmsUtil {
    public static Object copyBean(Object target, Object source) throws InvocationTargetException, IllegalAccessException {
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        //如果没有下面几行，则在转换null时会抛异常，例如：org.apache.commons.beanutils.ConversionException: No value specified for 'BigDecimal'
        //在org.apache.commons.beanutils.converters这个包下面有很多的Converter，可以按需要使用
        beanUtilsBean.getConvertUtils().register(new BigDecimalConverter(null), BigDecimal.class);
        beanUtilsBean.getConvertUtils().register(new DateConverter(null), java.util.Date.class);
        beanUtilsBean.getConvertUtils().register(new SqlDateConverter(null), java.sql.Date.class);
        beanUtilsBean.copyProperties(target, source);
        return target;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
}
