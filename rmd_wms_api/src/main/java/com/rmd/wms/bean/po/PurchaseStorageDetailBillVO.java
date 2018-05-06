package com.rmd.wms.bean.po;

import java.math.BigDecimal;

/**
 * Created by Guodj on 2017/4/19.
 * 采购入库详情单
 */
public class PurchaseStorageDetailBillVO {


    //商品编码
    private String goodsCode;
    //商品条码
    private String goodsBarcode;
    //商品名称
    private String goodsName;
    //规格
    private String specifications;
    //单位
    private String unit;
    //商品入库数量
    private String number;
    //单价（含税）
    private BigDecimal unitPrice;
    //商品入库含税金额
    private BigDecimal taxAmount;

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsBarcode() {
        return goodsBarcode;
    }

    public void setGoodsBarcode(String goodsBarcode) {
        this.goodsBarcode = goodsBarcode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}
