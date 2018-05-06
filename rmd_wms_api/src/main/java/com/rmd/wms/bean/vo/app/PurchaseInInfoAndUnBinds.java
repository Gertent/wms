package com.rmd.wms.bean.vo.app;

import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.PurchaseInInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/3/6.
 */
public class PurchaseInInfoAndUnBinds extends PurchaseInInfo implements Serializable {

    private List<LocationGoodsBind> unBinds;

    public List<LocationGoodsBind> getUnBinds() {
        return unBinds;
    }

    public void setUnBinds(List<LocationGoodsBind> unBinds) {
        this.unBinds = unBinds;
    }
}
