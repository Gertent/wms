package com.rmd.wms.bean.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Guodj on 2017/4/19.
 * 采购入库单
 */
public class PurchaseStorageBillVO implements Serializable {

    private static final long serialVersionUID = 8556805213206641573L;
    private Integer wareId;// 仓库ID
    //仓库名称
    private String wareName;
    //仓库编号
    private String wareCode;
    //入库时间
    private Date date;
    //入库单号
    private String oddNumbers;
    //采购订单号
    private String purchaseOrderNumber;
    //入库类别
    private String category;
    //供应商名称
    private String supplierName;
    //供应商编号
    private String supplierNumber;
    //订单入库数量
    private int number;
    //订单入库含税金额
    private BigDecimal taxAmount;
    
    private String salesman;// 业务员，工号+姓名

    private String department;// 部门

    private String financeCode; //财务组织编码

    //采购入库订单详情
    private List<PurchaseStorageDetailBillVO> purchaseStorageDetailBillVOs;

    public List<PurchaseStorageDetailBillVO> getPurchaseStorageDetailBillVOs() {
        if(this.purchaseStorageDetailBillVOs == null){
            return new ArrayList<PurchaseStorageDetailBillVO>();
        }
        return purchaseStorageDetailBillVOs;
    }

    public void setPurchaseStorageDetailBillVOs(List<PurchaseStorageDetailBillVO> purchaseStorageDetailBillVOs) {
        this.purchaseStorageDetailBillVOs = purchaseStorageDetailBillVOs;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOddNumbers() {
        return oddNumbers;
    }

    public void setOddNumbers(String oddNumbers) {
        this.oddNumbers = oddNumbers;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierNumber() {
        return supplierNumber;
    }

    public void setSupplierNumber(String supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getFinanceCode() {
        return financeCode;
    }

    public void setFinanceCode(String financeCode) {
        this.financeCode = financeCode;
    }
}
