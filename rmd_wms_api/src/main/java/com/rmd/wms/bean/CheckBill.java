package com.rmd.wms.bean;

import java.io.Serializable;
import java.util.Date;

public class CheckBill implements Serializable {
	
	private static final long serialVersionUID = 3100161024528263824L;

    public CheckBill() {
    }

    public CheckBill(String checkNo, Integer type, Integer status, Integer checkTimes, Date firstStartTime, Date firstEndTime, Date secondStartTime, Date secondEndTime, Integer lastChecker, Integer createrId, String createrName, Integer approverId, String approverName, Integer wareId, String wareName, Date createTime) {
        this.checkNo = checkNo;
        this.type = type;
        this.status = status;
        this.checkTimes = checkTimes;
        this.firstStartTime = firstStartTime;
        this.firstEndTime = firstEndTime;
        this.secondStartTime = secondStartTime;
        this.secondEndTime = secondEndTime;
        this.lastChecker = lastChecker;
        this.createrId = createrId;
        this.createrName = createrName;
        this.approverId = approverId;
        this.approverName = approverName;
        this.wareId = wareId;
        this.wareName = wareName;
        this.createTime = createTime;
    }

    private Integer id;

    private String checkNo;

    private Integer type;

    private Integer status;

    private Integer checkTimes;

    private Date firstStartTime;

    private Date firstEndTime;

    private Date secondStartTime;

    private Date secondEndTime;

    private Integer lastChecker;

    private Integer createrId;

    private String createrName;

    private Integer approverId;

    private String approverName;

    private Integer wareId;

    private String wareName;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCheckTimes() {
        return checkTimes;
    }

    public void setCheckTimes(Integer checkTimes) {
        this.checkTimes = checkTimes;
    }

    public Date getFirstStartTime() {
        return firstStartTime;
    }

    public void setFirstStartTime(Date firstStartTime) {
        this.firstStartTime = firstStartTime;
    }

    public Date getFirstEndTime() {
        return firstEndTime;
    }

    public void setFirstEndTime(Date firstEndTime) {
        this.firstEndTime = firstEndTime;
    }

    public Date getSecondStartTime() {
        return secondStartTime;
    }

    public void setSecondStartTime(Date secondStartTime) {
        this.secondStartTime = secondStartTime;
    }

    public Date getSecondEndTime() {
        return secondEndTime;
    }

    public void setSecondEndTime(Date secondEndTime) {
        this.secondEndTime = secondEndTime;
    }

    public Integer getLastChecker() {
        return lastChecker;
    }

    public void setLastChecker(Integer lastChecker) {
        this.lastChecker = lastChecker;
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
        this.createrName = createrName == null ? null : createrName.trim();
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName == null ? null : approverName.trim();
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