package com.rmd.wms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PurchaseBill implements Serializable {
    private Integer id;

    private String purchaseNo;

    private String barCodeUrl;

    private String supplierName;
    
    private String supplierCode;

	private Integer wareId;

    private String wareName;

    private Integer goodsAmount;

    private BigDecimal goodsSum;

    private Integer inType;

    private Integer status;

    private Integer ouserId;

    private String ouserName;

    private Date inDbData;

    private Date createDate;
    
    private String financeCode;
    
	private String department;

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

    public String getBarCodeUrl() {
        return barCodeUrl;
    }

    public void setBarCodeUrl(String barCodeUrl) {
        this.barCodeUrl = barCodeUrl == null ? null : barCodeUrl.trim();
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }
    
    public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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

    public Integer getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Integer goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getGoodsSum() {
        return goodsSum;
    }

    public void setGoodsSum(BigDecimal goodsSum) {
        this.goodsSum = goodsSum;
    }

    public Integer getInType() {
        return inType;
    }

    public void setInType(Integer inType) {
        this.inType = inType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Date getInDbData() {
        return inDbData;
    }

    public void setInDbData(Date inDbData) {
        this.inDbData = inDbData;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public String getFinanceCode() {
		return financeCode;
	}

	public void setFinanceCode(String financeCode) {
		this.financeCode = financeCode;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}