package com.rmd.wms.enums;

/**
 * 出库单订单类型
 * Created by wangyf on 2017/5/19.
 */
public enum StockOutOrderType {

   A001(1,"普通订单"),A003(3,"换货单"),A004(4,"补货单");

    public String getInfo() {
        return info;
    }
    public Integer getValue(){
        return value;
    }


    private final String info;
    private final Integer value;

    private StockOutOrderType(Integer value, String info){
        this.value=value;
        this.info=info;
    }
}
