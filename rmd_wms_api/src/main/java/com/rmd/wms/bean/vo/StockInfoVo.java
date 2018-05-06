package com.rmd.wms.bean.vo;

import java.io.Serializable;

/**
 * @author : liu
 * @Date : 2017/4/27
 */
public class StockInfoVo implements Serializable {

    private static final long serialVersionUID = 696368536525614623L;

    private String goodsCode;

    private Integer stockNum;

    private Integer validityNum;

    private Integer wareId;

    private String wareName;

    private String wareCode;

    private Integer saleType;

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Integer getValidityNum() {
        return validityNum;
    }

    public void setValidityNum(Integer validityNum) {
        this.validityNum = validityNum;
    }

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getWareCode() {
        return wareCode;
    }

    public void setWareCode(String wareCode) {
        this.wareCode = wareCode;
    }

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
    }
}
