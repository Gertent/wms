package com.rmd.wms.enums;

/**
 * 货位状态
 * Created by wangyf on 2017/5/12.
 */
public enum LocationStatus {

    A000(0,"禁用"),A001(1,"启用");

    public Integer getValue() {
        return value;
    }

    public String getInfo() {
        return info;
    }

    private final Integer value;
    private final String info;

    private LocationStatus(Integer value, String info) {
        this.value=value;
        this.info=info;
    }
}
