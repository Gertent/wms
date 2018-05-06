package com.rmd.wms.bean.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购单消息对象
 * @author : liu
 * @Date : 2017/4/27
 */
public class PurchaseBillVO implements Serializable {
    private static final long serialVersionUID = 4753630345741884235L;
    // 采购单号
    private String purchaseNo;
    // 供应商编码
    private String supplierCode;
    // 供应商名称
    private String supplierName;
    // 收货仓库id
    private Integer wareId;
    // 收货仓库名称
    private String wareName;
    // 商品总数
    private Integer goodsAmount;
    // 商品总额
    private BigDecimal goodsSum;
    // 操作人id
    private Integer ouserId;
    // 操作人
    private String ouserName;
    // 创建时间
    private Date createDate;
    // 财务组织编号
    private String financeCode;
    // 操作人部门
    private String department;

    List<PurchaseInfoVO> purchaseInfoVOs;

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
        this.ouserName = ouserName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<PurchaseInfoVO> getPurchaseInfoVOs() {
        return purchaseInfoVOs;
    }

    public void setPurchaseInfoVOs(List<PurchaseInfoVO> purchaseInfoVOs) {
        this.purchaseInfoVOs = purchaseInfoVOs;
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
