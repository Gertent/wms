package com.rmd.wms.enums;

/**
 * 出库单状态
 * Created by wangyf on 2017/5/10.
 */
public enum StockOutBillStatus {

    A12101(12101,"拣货"),A12102(12102,"打包复检"),A12103(12103,"录入运单号"),A12104(12104,"交接发货");

    public String getInfo() {
        return info;
    }
    public Integer getValue(){
        return value;
    }


    private final String info;
    private final Integer value;

    private  StockOutBillStatus(Integer value,String info){
        this.value=value;
        this.info=info;
    }
}
