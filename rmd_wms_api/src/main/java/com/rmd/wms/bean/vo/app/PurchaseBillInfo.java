package com.rmd.wms.bean.vo.app;

import com.rmd.wms.bean.PurchaseBill;
import com.rmd.wms.bean.PurchaseInInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/2/21.
 */
public class PurchaseBillInfo extends PurchaseBill implements Serializable {

    private Integer flag;
    private List<PurchaseInInfo> infos;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public List<PurchaseInInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<PurchaseInInfo> infos) {
        this.infos = infos;
    }
}
