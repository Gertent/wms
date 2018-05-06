package com.rmd.wms.bean;

import java.io.Serializable;
import java.util.Date;

public class CheckInfo implements Serializable {

    private static final long serialVersionUID = 5540481533905067062L;

    private Integer id;

    private Integer bindId;

    private String checkNo;
    
    private Integer type;

	private String goodsCode;

    private String goodsBarCode;

    private String goodsName;

    private String spec;

    private String packageNum;

    private String unit;

    private Date validityTime;

    private Integer validityNum;

    private String locationNo;

    private Integer locationNum;

    private Integer wareId;

    private String wareName;

    private Integer saleType;

    private Integer submitStatus;

    private Integer doChecked;

    private Integer doAudit;

    private Integer firstCheckNum;

    private Integer firstCheckLockDiff;

    private Integer firstCheckValidDiff;

    private Integer secondCheckNum;

    private Integer secondCheckLockDiff;

    private Integer secondCheckValidDiff;

    private Date createTime;

    private Integer createrId;

    private String createrName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBindId() {
        return bindId;
    }

    public void setBindId(Integer bindId) {
        this.bindId = bindId;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo == null ? null : checkNo.trim();
    }
    
    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
    }

    public String getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(String packageNum) {
        this.packageNum = packageNum == null ? null : packageNum.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
    }

    public Integer getValidityNum() {
        return validityNum;
    }

    public void setValidityNum(Integer validityNum) {
        this.validityNum = validityNum;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo == null ? null : locationNo.trim();
    }

    public Integer getLocationNum() {
        return locationNum;
    }

    public void setLocationNum(Integer locationNum) {
        this.locationNum = locationNum;
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

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
    }

    public Integer getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(Integer submitStatus) {
        this.submitStatus = submitStatus;
    }

    public Integer getDoChecked() {
        return doChecked;
    }

    public void setDoChecked(Integer doChecked) {
        this.doChecked = doChecked;
    }

    public Integer getDoAudit() {
        return doAudit;
    }

    public void setDoAudit(Integer doAudit) {
        this.doAudit = doAudit;
    }

    public Integer getFirstCheckNum() {
        return firstCheckNum;
    }

    public void setFirstCheckNum(Integer firstCheckNum) {
        this.firstCheckNum = firstCheckNum;
    }

    public Integer getFirstCheckLockDiff() {
        return firstCheckLockDiff;
    }

    public void setFirstCheckLockDiff(Integer firstCheckLockDiff) {
        this.firstCheckLockDiff = firstCheckLockDiff;
    }

    public Integer getFirstCheckValidDiff() {
        return firstCheckValidDiff;
    }

    public void setFirstCheckValidDiff(Integer firstCheckValidDiff) {
        this.firstCheckValidDiff = firstCheckValidDiff;
    }

    public Integer getSecondCheckNum() {
        return secondCheckNum;
    }

    public void setSecondCheckNum(Integer secondCheckNum) {
        this.secondCheckNum = secondCheckNum;
    }

    public Integer getSecondCheckLockDiff() {
        return secondCheckLockDiff;
    }

    public void setSecondCheckLockDiff(Integer secondCheckLockDiff) {
        this.secondCheckLockDiff = secondCheckLockDiff;
    }

    public Integer getSecondCheckValidDiff() {
        return secondCheckValidDiff;
    }

    public void setSecondCheckValidDiff(Integer secondCheckValidDiff) {
        this.secondCheckValidDiff = secondCheckValidDiff;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreaterId() {
        return createrId;
    }

    public void setCreaterId(Integer createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }
}