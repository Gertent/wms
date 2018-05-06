package com.rmd.wms.enums;

/**
 * 排序方式
 * Created by wangyf on 2017/7/31.
 */
public enum SortType {

    ASC("ASC"),DESC("DESC");

    public String getValue() {
        return value;
    }

    private final String value;

    private SortType(String value) {
        this.value=value;
    }
}
