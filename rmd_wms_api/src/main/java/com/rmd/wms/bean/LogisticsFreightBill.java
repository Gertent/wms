package com.rmd.wms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LogisticsFreightBill implements Serializable {

    private Integer id;

    private String logisticsNo;

    private String orderNo;

    private Integer logisComId;

    private String logisComName;

    private String code;

    private String receiveAddress;

    private BigDecimal basePrice;

    private BigDecimal extraCharges;

    private Integer doChange;

    private String remark;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;

    private Date createTime;

    private Integer wareId;

    private String wareName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo == null ? null : logisticsNo.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getLogisComId() {
        return logisComId;
    }

    public void setLogisComId(Integer logisComId) {
        this.logisComId = logisComId;
    }

    public String getLogisComName() {
        return logisComName;
    }

    public void setLogisComName(String logisComName) {
        this.logisComName = logisComName == null ? null : logisComName.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress == null ? null : receiveAddress.trim();
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(BigDecimal extraCharges) {
        this.extraCharges = extraCharges;
    }

    public Integer getDoChange() {
        return doChange;
    }

    public void setDoChange(Integer doChange) {
        this.doChange = doChange;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName == null ? null : updateUserName.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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