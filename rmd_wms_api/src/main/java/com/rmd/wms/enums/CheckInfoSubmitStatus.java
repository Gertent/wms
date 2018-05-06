package com.rmd.wms.enums;

/**
 * 盘点单提报状态
 * Created by wangyf on 2017/5/19.
 */
public enum CheckInfoSubmitStatus {

    A000(0,"未提交"),A001(1,"已提交"),B001(-1,"不可提交");

    public Integer getValue() {
        return value;
    }

    public String getInfo() {
        return info;
    }

    private final Integer value;
    private final String info;

    private CheckInfoSubmitStatus(Integer value, String info) {
        this.value=value;
        this.info=info;
    }
}
