package com.rmd.wms.bean;

import java.io.Serializable;
import java.util.Date;

public class LocationGoodsBind implements Serializable {

    public LocationGoodsBind() {
    }

    /**
     * 单个商品的货位库存信息查询条件
     * @param goodsCode
     * @param wareId
     * @param saleType
     */
    public LocationGoodsBind(String goodsCode, Integer wareId, Integer saleType) {
        this.goodsCode = goodsCode;
        this.wareId = wareId;
        this.saleType = saleType;
    }

    /**
     * 单条货位库存信息查询条件
     * @param locationNo
     * @param goodsCode
     * @param wareId
     * @param areaId
     * @param saleType
     */
    public LocationGoodsBind(String locationNo, String goodsCode, Integer wareId, Integer areaId, Integer saleType) {
        this.locationNo = locationNo;
        this.goodsCode = goodsCode;
        this.wareId = wareId;
        this.areaId = areaId;
        this.saleType = saleType;
    }

    private Integer id;

    private Integer locationId;

    private String locationNo;

    private String goodsCode;

    private String goodsBarCode;

    private Integer locationNum;

    private Integer validityNum;

    private Date validityTime;

    private Integer sortNum;

    private Integer wareId;

    private String wareName;

    private Integer areaId;

    private String areaName;

    private Date createTime;

    private Integer saleType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getLocationNum() {
        return locationNum;
    }

    public void setLocationNum(Integer locationNum) {
        this.locationNum = locationNum;
    }

    public Integer getValidityNum() {
        return validityNum;
    }

    public void setValidityNum(Integer validityNum) {
        this.validityNum = validityNum;
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

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
    }
}