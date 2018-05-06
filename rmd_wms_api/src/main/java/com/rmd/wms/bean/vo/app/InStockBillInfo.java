package com.rmd.wms.bean.vo.app;

import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.LocationGoodsBindIn;
import com.rmd.wms.bean.PurchaseInInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/2/21.
 */
public class InStockBillInfo extends InStockBill implements Serializable {

    private Integer flag;
    private List<PurchaseInInfoAndUnBinds> infos;
    private List<LocationGoodsBindIn> bindIns;
    private Integer skuNum;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public List<PurchaseInInfoAndUnBinds> getInfos() {
        return infos;
    }

    public void setInfos(List<PurchaseInInfoAndUnBinds> infos) {
        this.infos = infos;
    }

    public List<LocationGoodsBindIn> getBindIns() {
        return bindIns;
    }

    public void setBindIns(List<LocationGoodsBindIn> bindIns) {
        this.bindIns = bindIns;
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }
}
