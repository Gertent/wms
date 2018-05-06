package com.rmd.wms.bean.vo.web;

import java.util.List;

import com.rmd.wms.bean.LocationGoodsBindOut;
import com.rmd.wms.bean.StockOutInfo;

public class PackBillPrint {

	private String orderNo;

	private String wareName;

	private Integer billNum;

	private List<PackBill> stockOutInfos;
	

	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public Integer getBillNum() {
		return billNum;
	}

	public void setBillNum(Integer billNum) {
		this.billNum = billNum;
	}

	public List<PackBill> getStockOutInfos() {
		return stockOutInfos;
	}

	public void setStockOutInfos(List<PackBill> stockOutInfos) {
		this.stockOutInfos = stockOutInfos;
	}
	



}
