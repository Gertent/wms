package com.rmd.wms.bean;

import java.io.Serializable;
import java.util.Date;

public class MovementInfo implements Serializable {
    private Integer id;

    private Integer moveId;

    private String goodsCode;

    private String goodsBarCode;

    private Date inStockTime;

    private String locationNoOut;

    private Integer outNum;

    private Integer inNum;

    private Date validityTime;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMoveId() {
        return moveId;
    }

    public void setMoveId(Integer moveId) {
        this.moveId = moveId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getGoodsBarCode() {
        return goodsBarCode;
    }

    public void setGoodsBarCode(String goodsBarCode) {
        this.goodsBarCode = goodsBarCode == null ? null : goodsBarCode.trim();
    }

    public Date getInStockTime() {
        return inStockTime;
    }

    public void setInStockTime(Date inStockTime) {
        this.inStockTime = inStockTime;
    }

    public String getLocationNoOut() {
        return locationNoOut;
    }

    public void setLocationNoOut(String locationNoOut) {
        this.locationNoOut = locationNoOut == null ? null : locationNoOut.trim();
    }

    public Integer getOutNum() {
        return outNum;
    }

    public void setOutNum(Integer outNum) {
        this.outNum = outNum;
    }

    public Integer getInNum() {
        return inNum;
    }

    public void setInNum(Integer inNum) {
        this.inNum = inNum;
    }

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}