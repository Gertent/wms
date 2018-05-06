package com.rmd.wms.bean.vo.web;

import com.rmd.wms.bean.StockOutInfo;

public class PackBill extends StockOutInfo {

	private String locationNo;//库位
	
	private Integer lockedNum;//锁库数量

	public String getLocationNo() {
		return locationNo;
	}

	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}

	public Integer getLockedNum() {
		return lockedNum;
	}

	public void setLockedNum(Integer lockedNum) {
		this.lockedNum = lockedNum;
	}
}
