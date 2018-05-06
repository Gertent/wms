package com.rmd.wms.bean;

import java.io.Serializable;
import java.util.Date;

public class GroundingBill implements Serializable {
    private Integer id;

    private String groundingNo;

    private String inStockNo;

    private Integer status;

    private Integer type;

    private Date startTime;

    private Date endTime;

    private Integer groundingAmount;

    private Integer ouserId;

    private String ouserName;

    private Integer wareId;

    private String wareName;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroundingNo() {
        return groundingNo;
    }

    public void setGroundingNo(String groundingNo) {
        this.groundingNo = groundingNo == null ? null : groundingNo.trim();
    }

    public String getInStockNo() {
        return inStockNo;
    }

    public void setInStockNo(String inStockNo) {
        this.inStockNo = inStockNo == null ? null : inStockNo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getGroundingAmount() {
        return groundingAmount;
    }

    public void setGroundingAmount(Integer groundingAmount) {
        this.groundingAmount = groundingAmount;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}