package com.rmd.wms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PurchaseInInfo implements Serializable {
    private Integer id;

    private String purchaseNo;

    private String inStockNo;

    private String groundingNo;

    private String goodsCode;

    private String goodsBarCode;

    private String goodsName;

    private String spec;

    private String packageNum;

    private String unit;

    private Integer purchaseNum;

    private Integer purchaseWaitNum;

    private BigDecimal purchasePrice;

    private BigDecimal goodspriceSum;

    private Date validityTime;

    private Integer inStockNum;

    private Integer inStockBeNum;

    private BigDecimal inStockSum;

    private Integer wareId;

    private String wareName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo == null ? null : purchaseNo.trim();
    }

    public String getInStockNo() {
        return inStockNo;
    }

    public void setInStockNo(String inStockNo) {
        this.inStockNo = inStockNo == null ? null : inStockNo.trim();
    }

    public String getGroundingNo() {
        return groundingNo;
    }

    public void setGroundingNo(String groundingNo) {
        this.groundingNo = groundingNo == null ? null : groundingNo.trim();
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
    }

    public String getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(String packageNum) {
        this.packageNum = packageNum == null ? null : packageNum.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Integer getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(Integer purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getGoodspriceSum() {
        return goodspriceSum;
    }

    public void setGoodspriceSum(BigDecimal goodspriceSum) {
        this.goodspriceSum = goodspriceSum;
    }

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
    }

    public Integer getInStockNum() {
        return inStockNum;
    }

    public void setInStockNum(Integer inStockNum) {
        this.inStockNum = inStockNum;
    }

    public BigDecimal getInStockSum() {
        return inStockSum;
    }

    public void setInStockSum(BigDecimal inStockSum) {
        this.inStockSum = inStockSum;
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

    public Integer getPurchaseWaitNum() {
        return purchaseWaitNum;
    }

    public void setPurchaseWaitNum(Integer purchaseWaitNum) {
        this.purchaseWaitNum = purchaseWaitNum;
    }

    public Integer getInStockBeNum() {
        return inStockBeNum;
    }

    public void setInStockBeNum(Integer inStockBeNum) {
        this.inStockBeNum = inStockBeNum;
    }
}