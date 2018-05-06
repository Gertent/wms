package com.rmd.wms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class InStockBill implements Serializable {
    public InStockBill() {
    }

    public InStockBill(String purchaseNo, String orderNo, Integer type, Integer status, Integer ouserId, String ouserName) {
        this.purchaseNo = purchaseNo;
        this.orderNo = orderNo;
        this.type = type;
        this.status = status;
        this.setOuserId(ouserId);
        this.setOuserName(ouserName);
    }

    private Integer id;

    private String inStockNo;

    private String purchaseNo;

    private Integer wareId;

    private String wareName;

    private Integer doPrint;

    private Date inStockTime;

    private Integer ouserId;

    private String ouserName;

    private Integer inGoodsAmount;

    private BigDecimal inGoodsSum;

    private Integer type;

    private String orderNo;

    private String serverNo;

    private Integer status;

    private String remark;

    private Date createTime;
    
    private String supplierName;

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInStockNo() {
        return inStockNo;
    }

    public void setInStockNo(String inStockNo) {
        this.inStockNo = inStockNo == null ? null : inStockNo.trim();
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo == null ? null : purchaseNo.trim();
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

    public Integer getDoPrint() {
        return doPrint;
    }

    public void setDoPrint(Integer doPrint) {
        this.doPrint = doPrint;
    }

    public Date getInStockTime() {
        return inStockTime;
    }

    public void setInStockTime(Date inStockTime) {
        this.inStockTime = inStockTime;
    }

    public Integer getOuserId() {
        return ouserId;
    }

    public void setOuserId(Integer ouserId) {
        this.ouserId = ouserId;
    }

    public String getOuserName() {
        return ouserName;
    }

    public void setOuserName(String ouserName) {
        this.ouserName = ouserName == null ? null : ouserName.trim();
    }

    public Integer getInGoodsAmount() {
        return inGoodsAmount;
    }

    public void setInGoodsAmount(Integer inGoodsAmount) {
        this.inGoodsAmount = inGoodsAmount;
    }

    public BigDecimal getInGoodsSum() {
        return inGoodsSum;
    }

    public void setInGoodsSum(BigDecimal inGoodsSum) {
        this.inGoodsSum = inGoodsSum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getServerNo() {
        return serverNo;
    }

    public void setServerNo(String serverNo) {
        this.serverNo = serverNo == null ? null : serverNo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}