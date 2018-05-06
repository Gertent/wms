package com.rmd.wms.enums;

/**
 * 盘点单类型
 * Created by wangyf on 2017/5/19.
 */
public enum CheckBillType {

    A001(1,"日常"),A002(2,"大盘");

    public Integer getValue() {
        return value;
    }

    public String getInfo() {
        return info;
    }

    private final Integer value;
    private final String info;

    private CheckBillType(Integer value, String info) {
        this.value=value;
        this.info=info;
    }
}
