package com.rmd.wms.bean;

import java.io.Serializable;
import java.util.Date;

public class DeliveryBill implements Serializable {
    public DeliveryBill() {
    }

    public DeliveryBill(Integer deliveryUser, Integer wareId, Integer dodeliveryPrint, Integer logisComId) {
        this.deliveryUser = deliveryUser;
        this.wareId = wareId;
        this.dodeliveryPrint = dodeliveryPrint;
        this.logisComId = logisComId;
    }

    private Integer id;

    private String deliveryNo;

    private Integer orderSum;

    private Integer dodeliveryPrint;

    private Integer logisComId;

    private String logisComName;

    private Date deliveryStartTime;

    private Date deliveryEndTime;

    private Integer deliveryUser;

    private Date createTime;

    private Integer wareId;

    private String wareName;

    private String deliveryUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo == null ? null : deliveryNo.trim();
    }

    public Integer getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(Integer orderSum) {
        this.orderSum = orderSum;
    }

    public Integer getDodeliveryPrint() {
        return dodeliveryPrint;
    }

    public void setDodeliveryPrint(Integer dodeliveryPrint) {
        this.dodeliveryPrint = dodeliveryPrint;
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

    public Date getDeliveryStartTime() {
        return deliveryStartTime;
    }

    public void setDeliveryStartTime(Date deliveryStartTime) {
        this.deliveryStartTime = deliveryStartTime;
    }

    public Date getDeliveryEndTime() {
        return deliveryEndTime;
    }

    public void setDeliveryEndTime(Date deliveryEndTime) {
        this.deliveryEndTime = deliveryEndTime;
    }

    public Integer getDeliveryUser() {
        return deliveryUser;
    }

    public void setDeliveryUser(Integer deliveryUser) {
        this.deliveryUser = deliveryUser;
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

    public String getDeliveryUserName() {
        return deliveryUserName;
    }

    public void setDeliveryUserName(String deliveryUserName) {
        this.deliveryUserName = deliveryUserName;
    }
}