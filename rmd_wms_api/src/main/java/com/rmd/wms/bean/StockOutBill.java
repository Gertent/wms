package com.rmd.wms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockOutBill implements Serializable {

    public StockOutBill() {
    }

    public StockOutBill(Integer logisComId, Integer status, Integer wareId) {
        this.logisComId = logisComId;
        this.status = status;
        this.wareId = wareId;
    }

    private Integer id;

    private String orderNo;

    private Integer orderType;

    private Date orderdate;

    private Double weight;

    private Integer inLogisticsUser;

    private Integer goodsAmount;

    private Integer wareId;

    private String wareName;

    private Integer logisComId;

    private String logisComName;

    private String code;

    private Integer dobinningPrint;

    private Integer dopickingPrint;

    private Integer dowaybillPrint;

    private Integer pickingStatus;

    private Integer pickingUser;

    private Integer recheckStatus;

    private Date recheckStartTime;

    private Date recheckEndTime;

    private Double parcelWeight;

    private String weightUnit;

    private Integer recheckUser;

    private String logisticsNo;

    private Date inLogisticsTime;

    private BigDecimal goodsSum;

    private String deliveryNo;

    private Integer status;

    private Integer freeze;

    private String pickingUserName;

    private String recheckUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getInLogisticsUser() {
        return inLogisticsUser;
    }

    public void setInLogisticsUser(Integer inLogisticsUser) {
        this.inLogisticsUser = inLogisticsUser;
    }

    public Integer getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Integer goodsAmount) {
        this.goodsAmount = goodsAmount;
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

    public Integer getDobinningPrint() {
        return dobinningPrint;
    }

    public void setDobinningPrint(Integer dobinningPrint) {
        this.dobinningPrint = dobinningPrint;
    }

    public Integer getDopickingPrint() {
        return dopickingPrint;
    }

    public void setDopickingPrint(Integer dopickingPrint) {
        this.dopickingPrint = dopickingPrint;
    }

    public Integer getDowaybillPrint() {
        return dowaybillPrint;
    }

    public void setDowaybillPrint(Integer dowaybillPrint) {
        this.dowaybillPrint = dowaybillPrint;
    }

    public Integer getPickingStatus() {
        return pickingStatus;
    }

    public void setPickingStatus(Integer pickingStatus) {
        this.pickingStatus = pickingStatus;
    }

    public Integer getPickingUser() {
        return pickingUser;
    }

    public void setPickingUser(Integer pickingUser) {
        this.pickingUser = pickingUser;
    }

    public Integer getRecheckStatus() {
        return recheckStatus;
    }

    public void setRecheckStatus(Integer recheckStatus) {
        this.recheckStatus = recheckStatus;
    }

    public Date getRecheckStartTime() {
        return recheckStartTime;
    }

    public void setRecheckStartTime(Date recheckStartTime) {
        this.recheckStartTime = recheckStartTime;
    }

    public Date getRecheckEndTime() {
        return recheckEndTime;
    }

    public void setRecheckEndTime(Date recheckEndTime) {
        this.recheckEndTime = recheckEndTime;
    }

    public Double getParcelWeight() {
        return parcelWeight;
    }

    public void setParcelWeight(Double parcelWeight) {
        this.parcelWeight = parcelWeight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Integer getRecheckUser() {
        return recheckUser;
    }

    public void setRecheckUser(Integer recheckUser) {
        this.recheckUser = recheckUser;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo == null ? null : logisticsNo.trim();
    }

    public Date getInLogisticsTime() {
        return inLogisticsTime;
    }

    public void setInLogisticsTime(Date inLogisticsTime) {
        this.inLogisticsTime = inLogisticsTime;
    }

    public BigDecimal getGoodsSum() {
        return goodsSum;
    }

    public void setGoodsSum(BigDecimal goodsSum) {
        this.goodsSum = goodsSum;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo == null ? null : deliveryNo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFreeze() {
        return freeze;
    }

    public void setFreeze(Integer freeze) {
        this.freeze = freeze;
    }

    public String getPickingUserName() {
        return pickingUserName;
    }

    public void setPickingUserName(String pickingUserName) {
        this.pickingUserName = pickingUserName;
    }

    public String getRecheckUserName() {
        return recheckUserName;
    }

    public void setRecheckUserName(String recheckUserName) {
        this.recheckUserName = recheckUserName;
    }
}