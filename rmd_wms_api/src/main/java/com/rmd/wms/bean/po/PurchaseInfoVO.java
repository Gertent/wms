package com.rmd.wms.bean.po;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购单详情消息对象
 * @author : liu
 * @Date : 2017/4/27
 */
public class PurchaseInfoVO implements Serializable {

    private static final long serialVersionUID = -4265394940398169864L;
    // 采购单号
    private String purchaseNo;
    // 商品编码
    private String goodsCode;
    // 商品编码
    private String goodsBarCode;
    // 商品名称
    private String goodsName;
    // 规格型号
    private String spec;
    // 包装数量
    private String packageNum;
    // 包装单位
    private String unit;
    // 采购数量
    private Integer purchaseNum;
    // 采购单价
    private BigDecimal purchasePrice;
    // 采购总价
    private BigDecimal goodspriceSum;
    // 仓库id
    private Integer wareId;
    // 仓库名称
    private String wareName;

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsBarCode() {
        return goodsBarCode;
    }

    public void setGoodsBarCode(String goodsBarCode) {
        this.goodsBarCode = goodsBarCode;
    }

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
}
