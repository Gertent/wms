package com.rmd.wms.enums;

/**
 * 上架单状态
 * Created by wangyf on 2017/5/3.
 */
public enum  GroundingBillStatus {

    A001(1,"等待"),A002(2,"上架中"),A003(3,"已完成");

    public Integer getValue() {
        return value;
    }

    public String getInfo() {
        return info;
    }

    private final Integer value;
    private final String info;

    private GroundingBillStatus(Integer value,String info) {
        this.value=value;
        this.info=info;
    }
}
