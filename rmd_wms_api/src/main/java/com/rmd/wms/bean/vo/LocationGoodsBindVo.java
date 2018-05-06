package com.rmd.wms.bean.vo;

import com.rmd.wms.bean.LocationGoodsBind;

import java.io.Serializable;

/**
 * Created by wyf on 2017/6/22.
 */
public class LocationGoodsBindVo extends LocationGoodsBind implements Serializable {


    private String goodsName;   //商品名称
    private String spec;        //规格型号
    private String packageNum;//包装数量
    private String unit;        //单位

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(String packageNum) {
        this.packageNum = packageNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
