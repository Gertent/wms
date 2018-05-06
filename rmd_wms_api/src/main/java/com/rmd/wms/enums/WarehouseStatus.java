package com.rmd.wms.enums;

/**
 * 仓库状态
 * Created by wangyf on 2017/7/13.
 */
public enum WarehouseStatus {

    A000(0,"禁用"),A001(1,"启用"),A002(2,"大盘中");

    public Integer getValue() {
        return value;
    }

    public String getInfo() {
        return info;
    }

    private final Integer value;
    private final String info;

    private WarehouseStatus(Integer value, String info) {
        this.value=value;
        this.info=info;
    }
}
