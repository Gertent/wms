package com.rmd.wms.bean;

import java.io.Serializable;
import java.util.Date;

public class Stock implements Serializable {

    public Stock(){}

    public Stock(String goodsCode, Integer wareId, Integer saleType) {
        this.goodsCode = goodsCode;
        this.wareId = wareId;
        this.saleType = saleType;
    }

    private Integer id;

    private String goodsCode;

    private Integer stockNum;

    private Integer validityNum;

    private Integer lockedNum;

    private Integer wareId;

    private String wareName;

    private Integer saleType;

    private Date alterTime;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
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

    public Integer getLockedNum() {
        return lockedNum;
    }

    public void setLockedNum(Integer lockedNum) {
        this.lockedNum = lockedNum;
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
        this.wareName = wareName == null ? null : wareName.trim();
    }

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
    }

    public Date getAlterTime() {
        return alterTime;
    }

    public void setAlterTime(Date alterTime) {
        this.alterTime = alterTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}