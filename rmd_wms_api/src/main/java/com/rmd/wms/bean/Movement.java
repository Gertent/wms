package com.rmd.wms.bean;

import java.io.Serializable;
import java.util.Date;

public class Movement implements Serializable {
    private Integer id;

    private String orderNo;

    private Integer inUser;
    
    private String inUserName;

	private Integer outUser;
    
    private String outUserName;

    private Integer type;

    private Integer breakType;

    private Integer status;

    private Integer moveAmount;

    private Date moveOutTime;

    private Date moveInTime;

    private Integer wareId;

    private String wareName;

    private Date createTime;

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
        this.orderNo = orderNo;
    }

    public Integer getInUser() {
        return inUser;
    }

    public void setInUser(Integer inUser) {
        this.inUser = inUser;
    }
    
    public String getInUserName() {
		return inUserName;
	}

	public void setInUserName(String inUserName) {
		this.inUserName = inUserName;
	}

    public Integer getOutUser() {
        return outUser;
    }

    public void setOutUser(Integer outUser) {
        this.outUser = outUser;
    }

    public String getOutUserName() {
		return outUserName;
	}

	public void setOutUserName(String outUserName) {
		this.outUserName = outUserName;
	}
	
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBreakType() {
        return breakType;
    }

    public void setBreakType(Integer breakType) {
        this.breakType = breakType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMoveAmount() {
        return moveAmount;
    }

    public void setMoveAmount(Integer moveAmount) {
        this.moveAmount = moveAmount;
    }

    public Date getMoveOutTime() {
        return moveOutTime;
    }

    public void setMoveOutTime(Date moveOutTime) {
        this.moveOutTime = moveOutTime;
    }

    public Date getMoveInTime() {
        return moveInTime;
    }

    public void setMoveInTime(Date moveInTime) {
        this.moveInTime = moveInTime;
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