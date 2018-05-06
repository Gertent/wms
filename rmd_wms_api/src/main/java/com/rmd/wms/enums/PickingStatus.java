package com.rmd.wms.enums;

/**
 * 出库单拣货状态
 * Created by wangyf on 2017/5/19.
 */
public enum PickingStatus {

    A000(0,"异常"),A001(1,"待拣货"),A002(2,"拣货中"),A003(3,"已完成"),A004(4,"缺货");

    public String getInfo() {
        return info;
    }
    public Integer getValue(){
        return value;
    }


    private final String info;
    private final Integer value;

    private PickingStatus(Integer value, String info){
        this.value=value;
        this.info=info;
    }
}
