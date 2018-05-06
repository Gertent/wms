package com.rmd.wms.app.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 设置当前用户注解
 */
@Target({ElementType.METHOD})
public @interface CurrentUserAnno {
    
}
