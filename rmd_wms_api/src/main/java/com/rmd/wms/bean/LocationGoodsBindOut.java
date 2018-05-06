package com.rmd.wms.bean;

import java.io.Serializable;
import java.util.Date;

public class LocationGoodsBindOut implements Serializable {

    public LocationGoodsBindOut() {
    }

    /**
     * 查询有效的拣货库位条件
     * @param orderNo
     * @param goodsCode
     * @param id
     */
    public LocationGoodsBindOut(String orderNo, String goodsCode, Integer id) {
        this.orderNo = orderNo;
        this.goodsCode = goodsCode;
        this.id = id;
    }

    private Integer id;

    private Integer ginfoId;

    private Integer locationId;

    private String locationNo;

    private String orderNo;

    private Integer groundingNum;

    private Integer pickedNum;

    private Integer lockedNum;

    private String goodsCode;

    private String goodsBarCode;

    private Date validityTime;

    private Integer sortNum;

    private Integer wareId;

    private String wareName;

    private Integer areaId;

    private String areaName;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGinfoId() {
        return ginfoId;
    }

    public void setGinfoId(Integer ginfoId) {
        this.ginfoId = ginfoId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo == null ? null : locationNo.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getGroundingNum() {
        return groundingNum;
    }

    public void setGroundingNum(Integer groundingNum) {
        this.groundingNum = groundingNum;
    }

    public Integer getPickedNum() {
        return pickedNum;
    }

    public void setPickedNum(Integer pickedNum) {
        this.pickedNum = pickedNum;
    }

    public Integer getLockedNum() {
        return lockedNum;
    }

    public void setLockedNum(Integer lockedNum) {
        this.lockedNum = lockedNum;
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

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
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

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}