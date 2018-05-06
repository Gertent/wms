package com.rmd.wms.enums;

/**
 * 打包复检状态
 * Created by wangyf on 2017/5/19.
 */
public enum RecheckStatus {

    A000(0,"复检异常"),A001(1,"等待复检"),A002(2,"复检中"),A003(3,"已完成");

    public String getInfo() {
        return info;
    }
    public Integer getValue(){
        return value;
    }


    private final String info;
    private final Integer value;

    private RecheckStatus(Integer value, String info){
        this.value=value;
        this.info=info;
    }
}
