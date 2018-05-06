package com.rmd.wms.bean.po;

import java.io.Serializable;
import java.util.Date;

public class GoodsGroundingNumVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//商品编码
	private String goodsCode;
	
	//入库数量
	private Integer storageCount;
	
	//操作人
	private Integer userId;
	
	//操作时间
	private Date operatingTime;

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public Integer getStorageCount() {
		return storageCount;
	}

	public void setStorageCount(Integer storageCount) {
		this.storageCount = storageCount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getOperatingTime() {
		return operatingTime;
	}

	public void setOperatingTime(Date operatingTime) {
		this.operatingTime = operatingTime;
	}
}
