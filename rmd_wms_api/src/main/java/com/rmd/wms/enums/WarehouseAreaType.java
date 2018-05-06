package com.rmd.wms.enums;

/**
 * 库区性质
 * Created by wangyf on 2017/7/13.
 */
public enum WarehouseAreaType {

    A000(0,"不可卖"),A001(1,"可卖");

    public Integer getValue() {
        return value;
    }

    public String getInfo() {
        return info;
    }

    private final Integer value;
    private final String info;

    private WarehouseAreaType(Integer value, String info) {
        this.value=value;
        this.info=info;
    }
}
