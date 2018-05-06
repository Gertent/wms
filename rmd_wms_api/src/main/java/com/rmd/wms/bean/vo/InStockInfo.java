package com.rmd.wms.bean.vo;

import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.PurchaseInInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 入库详情
 * @author : liu
 * Date : 2017/3/24
 */
public class InStockInfo extends InStockBill implements Serializable{

    private List<PurchaseInInfo> inInfos;

    public List<PurchaseInInfo> getInInfos() {
        return inInfos;
    }

    public void setInInfos(List<PurchaseInInfo> inInfos) {
        this.inInfos = inInfos;
    }
}
