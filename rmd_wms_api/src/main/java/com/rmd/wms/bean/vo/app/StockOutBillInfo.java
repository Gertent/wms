package com.rmd.wms.bean.vo.app;

import com.rmd.wms.bean.LocationGoodsBindOut;
import com.rmd.wms.bean.StockOutBill;
import com.rmd.wms.bean.StockOutInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/2/23.
 */
public class StockOutBillInfo extends StockOutBill implements Serializable {

    private Integer skuNum;
    private List<StockOutInfo> soInfos;
    private List<LocationGoodsBindOut> bindOutInfos;

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }

    public List<StockOutInfo> getSoInfos() {
        return soInfos;
    }

    public void setSoInfos(List<StockOutInfo> soInfos) {
        this.soInfos = soInfos;
    }

    public List<LocationGoodsBindOut> getBindOutInfos() {
        return bindOutInfos;
    }

    public void setBindOutInfos(List<LocationGoodsBindOut> bindOutInfos) {
        this.bindOutInfos = bindOutInfos;
    }
}
