package com.rmd.wms.enums;

/**
 * 是否标志
 * Created by wangyf on 2017/5/19.
 */
public enum YesAdNoFlag {

    A000(0,"否"),A001(1,"是");

    public Integer getValue() {
        return value;
    }

    public String getInfo() {
        return info;
    }

    private final Integer value;
    private final String info;

    private YesAdNoFlag(Integer value, String info) {
        this.value=value;
        this.info=info;
    }
}
