package com.rmd.wms.enums;

/**
 * 打印标志
 * Created by wangyf on 2017/5/19.
 */
public enum DoPrintFlag {

    A000(0,"未打印"),A001(1,"已打印");

    public Integer getValue() {
        return value;
    }

    public String getInfo() {
        return info;
    }

    private final Integer value;
    private final String info;

    private DoPrintFlag(Integer value, String info) {
        this.value=value;
        this.info=info;
    }
}
